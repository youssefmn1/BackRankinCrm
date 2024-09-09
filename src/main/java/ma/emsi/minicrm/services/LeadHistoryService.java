package ma.emsi.minicrm.services;

import ma.emsi.minicrm.dao.entities.LeadHistory;
import ma.emsi.minicrm.dao.repositories.LeadHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LeadHistoryService {

    @Autowired
    private LeadHistoryRepository leadHistoryRepository;

    // Méthode pour enregistrer un historique de lead
    public LeadHistory createLeadHistory(LeadHistory leadHistory) {
        leadHistory.setModificationDate(LocalDateTime.now());
        return leadHistoryRepository.save(leadHistory);
    }

    // Méthode pour obtenir l'historique d'un lead par son ID
    public List<LeadHistory> getHistoryByLeadId(Integer leadId) {
        return leadHistoryRepository.findByLeadId(leadId);
    }

    // Méthode pour obtenir tout l'historique
    public List<LeadHistory> getAllHistories() {
        return leadHistoryRepository.findAll();
    }
}
