package supnum.projet.Library.services;

import supnum.projet.Library.data.entities.Member;
import supnum.projet.Library.dto.MemberDTO;
import supnum.projet.Library.data.repositories.MemberRepository;
import supnum.projet.Library.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MemberService {

    private final MemberRepository repository;

    public MemberService(MemberRepository repository) {
        this.repository = repository;
    }

    public List<Member> findAll() {
        return repository.findAll();
    }

    public Member findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Membre non trouvé avec l'id : " + id));
    }

    public Member create(MemberDTO dto) {
        if (repository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Un membre avec cet email existe déjà");
        }
        Member member = new Member();
        member.setEmail(dto.getEmail());
        member.setMemberType(dto.getMemberType());
        member.setMaxBorrows(dto.getMaxBorrows());
        return repository.save(member);
    }

    public Member update(Long id, MemberDTO dto) {
        Member member = findById(id);
        if (!member.getEmail().equals(dto.getEmail()) && repository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Un membre avec cet email existe déjà");
        }
        member.setEmail(dto.getEmail());
        member.setMemberType(dto.getMemberType());
        member.setMaxBorrows(dto.getMaxBorrows());
        return repository.save(member);
    }

    public void delete(Long id) {
        Member member = findById(id);
        member.setDeleted(true);
        repository.save(member);
    }
}
