package ma.emsi.minicrm.dao.repositories;

import jakarta.transaction.Transactional;
import ma.emsi.minicrm.dao.entities.Commercial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Transactional
public interface CommercialRepository extends JpaRepository<Commercial,Integer> {
    Page<Commercial> findByNomContains(String keyword, Pageable pageable); // pageable sert pour faire la pagination

}