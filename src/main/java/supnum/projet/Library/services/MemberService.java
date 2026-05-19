package supnum.projet.Library.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import supnum.projet.Library.data.entities.Member;
import supnum.projet.Library.data.repositories.MemberRepository;
import supnum.projet.Library.dto.MemberDTO;
import supnum.projet.Library.exceptions.ResourceNotFoundException;
import supnum.projet.Library.exceptions.DuplicateResourceException;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    
    private final MemberRepository memberRepository;
    
    public MemberDTO createMember(MemberDTO memberDTO) {
        // Vérifier l'unicité de l'email
        if (memberRepository.findByEmailActive(memberDTO.getEmail()) != null) {
            throw new DuplicateResourceException("Member with email '" + memberDTO.getEmail() + "' already exists");
        }
        
        Member member = Member.builder()
                .email(memberDTO.getEmail())
                .memberType(memberDTO.getMemberType())
                .maxBorrows(memberDTO.getMaxBorrows())
                .deleted(false)
                .build();
        
        Member saved = memberRepository.save(member);
        return mapToDTO(saved);
    }
    
    @Transactional(readOnly = true)
    public Page<MemberDTO> getAllMembers(Pageable pageable) {
        return memberRepository.findAllActive(pageable)
                .map(this::mapToDTO);
    }
    
    @Transactional(readOnly = true)
    public MemberDTO getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .filter(m -> !m.getDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));
        return mapToDTO(member);
    }
    
    @Transactional(readOnly = true)
    public MemberDTO getMemberByEmail(String email) {
        Member member = memberRepository.findByEmailActive(email);
        if (member == null) {
            throw new ResourceNotFoundException("Member not found with email: " + email);
        }
        return mapToDTO(member);
    }
    
    public MemberDTO updateMember(Long id, MemberDTO memberDTO) {
        Member member = memberRepository.findById(id)
                .filter(m -> !m.getDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));
        
        // Vérifier l'email si modifié
        if (!member.getEmail().equals(memberDTO.getEmail()) && 
            memberRepository.findByEmailActive(memberDTO.getEmail()) != null) {
            throw new DuplicateResourceException("Member with email '" + memberDTO.getEmail() + "' already exists");
        }
        
        member.setEmail(memberDTO.getEmail());
        member.setMemberType(memberDTO.getMemberType());
        member.setMaxBorrows(memberDTO.getMaxBorrows());
        
        Member updated = memberRepository.save(member);
        return mapToDTO(updated);
    }
    
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id)
                .filter(m -> !m.getDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));
        
        member.setDeleted(true);
        memberRepository.save(member);
    }
    
    private MemberDTO mapToDTO(Member member) {
        return MemberDTO.builder()
                .id(member.getId())
                .email(member.getEmail())
                .memberType(member.getMemberType())
                .maxBorrows(member.getMaxBorrows())
                .build();
    }
}
