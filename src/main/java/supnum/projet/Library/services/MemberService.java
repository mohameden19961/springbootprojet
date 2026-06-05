package supnum.projet.Library.services;

import supnum.projet.Library.data.entities.Member;
import supnum.projet.Library.dto.MemberDTO;
import supnum.projet.Library.dto.response.MemberResponse;
import supnum.projet.Library.data.repositories.MemberRepository;
import supnum.projet.Library.exceptions.DuplicateResourceException;
import supnum.projet.Library.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberService {

    private final MemberRepository repository;

    public MemberService(MemberRepository repository) {
        this.repository = repository;
    }

    public Page<MemberResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toResponse);
    }

    public MemberResponse findById(Long id) {
        return repository.findById(id)
            .map(this::toResponse)
            .orElseThrow(() -> new ResourceNotFoundException("Membre non trouvé avec l'id : " + id));
    }

    public MemberResponse create(MemberDTO dto) {
        if (repository.findByEmail(dto.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Un membre avec cet email existe déjà");
        }
        Member member = Member.builder()
            .email(dto.getEmail())
            .memberType(dto.getMemberType())
            .maxBorrows(dto.getMaxBorrows())
            .build();
        return toResponse(repository.save(member));
    }

    public MemberResponse update(Long id, MemberDTO dto) {
        Member member = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Membre non trouvé avec l'id : " + id));
        if (!member.getEmail().equals(dto.getEmail()) && repository.findByEmail(dto.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Un membre avec cet email existe déjà");
        }
        member.setEmail(dto.getEmail());
        member.setMemberType(dto.getMemberType());
        member.setMaxBorrows(dto.getMaxBorrows());
        return toResponse(repository.save(member));
    }

    public void delete(Long id) {
        Member member = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Membre non trouvé avec l'id : " + id));
        member.setDeleted(true);
        repository.save(member);
    }

    private MemberResponse toResponse(Member member) {
        MemberResponse r = new MemberResponse();
        r.setId(member.getId());
        r.setEmail(member.getEmail());
        r.setMemberType(member.getMemberType());
        r.setMaxBorrows(member.getMaxBorrows());
        return r;
    }
}
