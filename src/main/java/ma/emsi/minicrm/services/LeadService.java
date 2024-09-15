package ma.emsi.minicrm.services;

import jakarta.transaction.Transactional;
import ma.emsi.minicrm.dao.entities.Commercial;
import ma.emsi.minicrm.dao.entities.FileMetadata;
import ma.emsi.minicrm.dao.entities.Lead;
import ma.emsi.minicrm.dao.entities.LeadHistory;
import ma.emsi.minicrm.dao.repositories.CommercialRepository;
import ma.emsi.minicrm.dao.repositories.LeadHistoryRepository;
import ma.emsi.minicrm.dao.repositories.LeadRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LeadService {

    private static final Logger log = LoggerFactory.getLogger(LeadService.class);

    @Autowired
    private LeadRepository leadRepository;

    @Autowired
    private CommercialRepository commercialRepository;

    @Autowired
    private LeadHistoryRepository leadHistoryRepository;

    @Autowired
    private FileService fileService;

    public Page<Lead> findPaginated(@RequestParam(name = "page",defaultValue = "0") int page,
                                    @RequestParam(name = "size",defaultValue = "5") int size,
                                    @RequestParam(name = "keyword",defaultValue = "") String kw) {

        return leadRepository.findByNomContains(kw, PageRequest.of(page,size));
    }

    public Lead createLead(Lead lead) {
        Lead createdLead = leadRepository.save(lead);
        recordHistory(createdLead, "CREATE"); // Enregistrez l'historique avec le type "CREATE"
        return createdLead;
    }

    private void recordHistory(Lead lead, String modificationType) {
        LeadHistory leadHistory = new LeadHistory();
        leadHistory.setLead(lead);
        leadHistory.setModificationDate(LocalDateTime.now());
        leadHistory.setModificationType(modificationType);
        leadHistory.setNom(lead.getNom());
        leadHistory.setPrenom(lead.getPrenom());
        leadHistory.setEmail(lead.getEmail());
        leadHistory.setAdresse(lead.getAdresse());
        leadHistory.setTelephone(lead.getTelephone());
        leadHistory.setSource(lead.getSource());
        leadHistory.setStatut(lead.getStatut());
        leadHistory.setNote(lead.getNote());

        leadHistoryRepository.save(leadHistory);
    }

    @Transactional
    public boolean deleteLead(Integer id) {
        try {
            // Récupérer le Lead
            Lead lead = getLeadById(id);
            if (lead != null) {
                // Supprimer les historiques associés
                leadHistoryRepository.deleteByLeadId(id);

                // Supprimer le lead
                if (lead.getCommercial() != null) {
                    lead.getCommercial().getLeads().remove(lead);
                }
                leadRepository.delete(lead);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            // Log de l'exception
            log.error("Error while deleting lead: " + e.getMessage(), e);
            return false;
        }
    }

    public void assignCommercial(Integer leadId, Integer commercialId) {
        Lead lead = leadRepository.findById(leadId).orElseThrow(() -> new IllegalArgumentException("Invalid lead Id:" + leadId));
        Commercial commercial = commercialRepository.findById(commercialId).orElseThrow(() -> new IllegalArgumentException("Invalid commercial Id:" + commercialId));
        lead.setCommercial(commercial);
        leadRepository.save(lead);
    }

    public Lead getLeadById(Integer id) {
        Optional<Lead> optionalLead = leadRepository.findById(id);
        return optionalLead.orElse(null);
    }

    public List<Lead> getAllLeads() {
        return leadRepository.findAll();
    }

    @Transactional
    public Lead updateLead(Integer id, Lead leadDetails) {
        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid lead Id: " + id));

        // Update lead details
        lead.setNom(leadDetails.getNom());
        lead.setPrenom(leadDetails.getPrenom());
        lead.setEmail(leadDetails.getEmail());
        lead.setAdresse(leadDetails.getAdresse());
        lead.setTelephone(leadDetails.getTelephone());
        lead.setSource(leadDetails.getSource());
        lead.setNote(leadDetails.getNote());
        lead.setStatut(leadDetails.getStatut());

        // Handle commercial assignment
        if (leadDetails.getCommercial() != null) {
            Commercial commercial = commercialRepository.findById(leadDetails.getCommercial().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid commercial Id: " + leadDetails.getCommercial().getId()));
            lead.setCommercial(commercial);
            commercial.getLeads().add(lead);
        } else {
            if (lead.getCommercial() != null) {
                lead.getCommercial().getLeads().remove(lead);
            }
            lead.setCommercial(null);
        }

        // Save and record history
        Lead updatedLead = leadRepository.save(lead);
        recordHistory(updatedLead, "UPDATE"); // Enregistrez l'historique avec le type "UPDATE"
        return updatedLead;
    }

    public List<Lead> findAll() {
        return leadRepository.findAll();
    }

    public Page<Lead> getLeadsByCommercialId(Integer commercialId, String keyword, Pageable pageable) {
        if (keyword != null && !keyword.isEmpty()) {
            return leadRepository.findByCommercialIdAndKeyword(commercialId, keyword, pageable);
        } else {
            return leadRepository.findByCommercialId(commercialId, pageable);
        }
    }

    public List<Lead> getLeadsByCommercialId(Integer commercialId) {
        return leadRepository.findByCommercialId(commercialId);
    }

    public LeadService(LeadRepository leadRepository) {
        this.leadRepository = leadRepository;
    }

    public void deleteLeads(List<Integer> leadIds) {
        leadRepository.deleteAllByIdIn(leadIds);
    }

    //pour RDV
    public Lead getLeadId(Integer id) {
        return leadRepository.findById(id).orElse(null);
    }

    public List<LeadHistory> getLeadHistory(Integer leadId) {
        return leadHistoryRepository.findByLeadId(leadId);
    }

    public boolean existsById(Integer id) {
        return leadRepository.existsById(id);
    }
}