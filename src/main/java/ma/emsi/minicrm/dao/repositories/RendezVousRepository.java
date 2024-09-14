package ma.emsi.minicrm.dao.repositories;

import ma.emsi.minicrm.dao.entities.RendezVous;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RendezVousRepository extends JpaRepository<RendezVous, Integer> {
    Page<RendezVous> findByLieuContains(String kw, PageRequest of);

    List<RendezVous> findByCommercialId(Integer commercialId);

    List<RendezVous> findByLeadId(Integer leadId);
}