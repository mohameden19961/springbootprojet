package supnum.projet.Library.dao;

import supnum.projet.Library.data.entities.Borrow;
import supnum.projet.Library.data.entities.Member;
import supnum.projet.Library.data.entities.enums.BorrowStatus;
import supnum.projet.Library.data.repositories.BorrowRepository;
import supnum.projet.Library.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BorrowDao {

    private final BorrowRepository repository;

    public BorrowDao(BorrowRepository repository) {
        this.repository = repository;
    }

    public List<Borrow> findAll() {
        return repository.findAll();
    }

    public Borrow findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Emprunt non trouvé avec l'id : " + id));
    }

    public Borrow save(Borrow borrow) {
        return repository.save(borrow);
    }

    public long countByMemberAndStatus(Member member, BorrowStatus status) {
        return repository.countByMemberAndStatus(member, status);
    }
}
