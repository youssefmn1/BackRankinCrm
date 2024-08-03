package ma.emsi.minicrm.dao.repositories;

import jakarta.transaction.Transactional;
import ma.emsi.minicrm.dao.entities.Lead;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Transactional
public interface LeadRepository extends JpaRepository<Lead,Integer> {
     Page<Lead> findByNomContains(String keyword, Pageable pageable); // pageable sert pour faire la pagination

     // Méthode pour trouver les leads par l'ID du commercial
     List<Lead> findByCommercialId(Integer commercialId);

     Page<Lead> findByCommercialId(Integer commercialId, Pageable pageable);

     @Query("SELECT l FROM Lead l WHERE l.commercial.id = :commercialId AND (l.nom LIKE %:keyword% OR l.prenom LIKE %:keyword%)")
     Page<Lead> findByCommercialIdAndKeyword(@Param("commercialId") Integer commercialId, @Param("keyword") String keyword, Pageable pageable);
}