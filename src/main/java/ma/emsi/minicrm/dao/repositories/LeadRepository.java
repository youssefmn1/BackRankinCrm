package ma.emsi.minicrm.dao.repositories;

import jakarta.transaction.Transactional;
import ma.emsi.minicrm.dao.entities.Lead;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

@Transactional
public interface LeadRepository extends JpaRepository<Lead,Integer> {
    Page<Lead> findByNomContains(String keyword, Pageable pageable); // pageable sert pour faire la pagination

}