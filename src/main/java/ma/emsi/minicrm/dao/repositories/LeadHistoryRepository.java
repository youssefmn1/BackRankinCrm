package ma.emsi.minicrm.dao.repositories;

import ma.emsi.minicrm.dao.entities.LeadHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LeadHistoryRepository extends JpaRepository<LeadHistory, Integer> {

    List<LeadHistory> findByLeadId(Integer leadId);

    List<LeadHistory> findByModificationDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
