package ma.emsi.minicrm.dao.repositories;

import jakarta.transaction.Transactional;
import ma.emsi.minicrm.dao.entities.Interaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

@Transactional
public interface InteractionRepository extends JpaRepository<Interaction, Integer> {
    Page<Interaction> findByDetailsContains(String keyword, Pageable pageable); // pageable for pagination
}
