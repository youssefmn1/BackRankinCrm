package ma.emsi.minicrm.services;

import ma.emsi.minicrm.dao.entities.Lead;
import ma.emsi.minicrm.dao.repositories.LeadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LeadService {

    @Autowired
    private LeadRepository leadRepository;

    // Create a new lead
    public Lead createLead(Lead lead) {
        return leadRepository.save(lead);
    }

    // Retrieve a lead by its ID
    public Lead getLeadById(Integer id) {
        Optional<Lead> optionalLead = leadRepository.findById(id);
        return optionalLead.orElse(null);
    }

    // Retrieve all leads
    public List<Lead> getAllLeads() {
        return leadRepository.findAll();
    }

    // Update an existing lead
    public Lead updateLead(Integer id, Lead leadDetails) {
        Lead lead = getLeadById(id);
        if (lead != null) {
            lead.setNom(leadDetails.getNom());
            lead.setPrenom(leadDetails.getPrenom());
            lead.setEmail(leadDetails.getEmail());
            lead.setAdresse(leadDetails.getAdresse());
            lead.setTelephone(leadDetails.getTelephone());
            lead.setSource(leadDetails.getSource());
            lead.setNote(leadDetails.getNote());
            return leadRepository.save(lead);
        } else {
            return null;
        }
    }

    // Delete a lead by its ID
    public boolean deleteLead(Integer id) {
        Lead lead = getLeadById(id);
        if (lead != null) {
            leadRepository.delete(lead);
            return true;
        } else {
            return false;
        }
    }
}

