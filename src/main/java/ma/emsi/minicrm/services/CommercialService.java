package ma.emsi.minicrm.services;

import ma.emsi.minicrm.dao.entities.Commercial;
import ma.emsi.minicrm.dao.entities.Lead;
import ma.emsi.minicrm.dao.entities.Utilisateur;
import ma.emsi.minicrm.dao.repositories.CommercialRepository;
import ma.emsi.minicrm.dao.repositories.LeadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
@Service
public class CommercialService {

    @Autowired
    private CommercialRepository commercialRepository;

    @Autowired
    private LeadRepository leadRepository;  // Ensure to inject LeadRepository to handle leads

    public Page<Commercial> findPaginated(int page, int size, String kw) {
        return commercialRepository.findByNomContains(kw, PageRequest.of(page, size));
    }

    // Create a new commercial
    public Commercial createCommercial(Commercial commercial) {
        return commercialRepository.save(commercial);
    }

    // Retrieve a commercial by its ID
    public Commercial getCommercialById(Integer id) {
        Optional<Commercial> optionalCommercial = commercialRepository.findById(id);
        return optionalCommercial.orElse(null);
    }

    // Retrieve all commercials
    public List<Commercial> getAllCommercials() {
        return commercialRepository.findAll();
    }

    // Update an existing commercial
    public Commercial updateCommercial(Integer id, Commercial commercialDetails) {
        Commercial commercial = getCommercialById(id);
        if (commercial != null) {
            commercial.setNom(commercialDetails.getNom());
            commercial.setPrenom(commercialDetails.getPrenom());
            commercial.setEmail(commercialDetails.getEmail());

            return commercialRepository.save(commercial);
        } else {
            return null;
        }
    }

    // Delete a commercial by its ID
    public boolean deleteCommercial(Integer id) {
        Commercial commercial = getCommercialById(id);
        if (commercial != null) {
            // Detach leads from this commercial
            for (Lead lead : commercial.getLeads()) {
                lead.setCommercial(null);
                leadRepository.save(lead);  // Update the lead to remove the association
            }
            commercialRepository.delete(commercial);
            return true;
        } else {
            return false;
        }
    }

    public List<Commercial> findAll() {
        return commercialRepository.findAll();
    }

    public String getCommercialNameById(Integer commercialId) {
        return commercialRepository.findById(commercialId)
                .map(Utilisateur::getNom) // Assuming the field is 'name'
                .orElse("Unknown Commercial");
    }
    //pour RDV
    public Commercial getCommercialId(Integer id) {
        return commercialRepository.findById(id).orElse(null);
    }
}
