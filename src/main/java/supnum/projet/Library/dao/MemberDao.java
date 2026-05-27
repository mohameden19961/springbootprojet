package supnum.projet.Library.dao;

import supnum.projet.Library.data.entities.Member;
import supnum.projet.Library.data.repositories.MemberRepository;
import supnum.projet.Library.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MemberDao {

    private final MemberRepository repository;

    public MemberDao(MemberRepository repository) {
        this.repository = repository;
    }

    public List<Member> findAll() {
        return repository.findAll();
    }

    public Member findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Membre non trouvé avec l'id : " + id));
    }

    public Optional<Member> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public Member save(Member member) {
        return repository.save(member);
    }
}
