package ma.emsi.minicrm.services;

import jakarta.transaction.Transactional;
import ma.emsi.minicrm.dao.entities.Commercial;
import ma.emsi.minicrm.dao.entities.Lead;
import ma.emsi.minicrm.dao.repositories.CommercialRepository;
import ma.emsi.minicrm.dao.repositories.LeadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Service
public class LeadService {

    @Autowired
    private LeadRepository leadRepository;

    @Autowired
    private CommercialRepository commercialRepository;

    public Page<Lead> findPaginated(@RequestParam(name = "page",defaultValue = "0") int page,
                                    @RequestParam(name = "size",defaultValue = "5") int size,
                                    @RequestParam(name = "keyword",defaultValue = "") String kw) {

        return leadRepository.findByNomContains(kw, PageRequest.of(page,size));
    }

    public Lead createLead(Lead lead) {
        return leadRepository.save(lead);
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

        if (lead.getCommercial() != null) {
            lead.getCommercial().getLeads().remove(lead);
            System.out.println("Removed lead from old commercial");
        }

        if (leadDetails.getCommercial() != null) {
            Commercial commercial = commercialRepository.findById(leadDetails.getCommercial().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid commercial Id: " + leadDetails.getCommercial().getId()));
            lead.setCommercial(commercial);
            commercial.getLeads().add(lead);
            System.out.println("Added lead to new commercial");
        } else {
            lead.setCommercial(null);
        }

        lead.setNom(leadDetails.getNom());
        lead.setPrenom(leadDetails.getPrenom());
        lead.setEmail(leadDetails.getEmail());
        lead.setAdresse(leadDetails.getAdresse());
        lead.setTelephone(leadDetails.getTelephone());
        lead.setSource(leadDetails.getSource());
        lead.setNote(leadDetails.getNote());
        lead.setStatut(leadDetails.getStatut());
        //lead.setInteractions(leadDetails.getInteractions());

        return leadRepository.save(lead);
    }


    public boolean deleteLead(Integer id) {
        Lead lead = getLeadById(id);
        if (lead != null) {
            if (lead.getCommercial() != null) {
                lead.getCommercial().getLeads().remove(lead);
            }
            leadRepository.delete(lead);
            return true;
        } else {
            return false;
        }
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

    public boolean existsById(Integer id) {
        return leadRepository.existsById(id);
    }
}