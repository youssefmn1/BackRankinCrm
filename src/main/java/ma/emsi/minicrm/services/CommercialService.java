package ma.emsi.minicrm.services;

import ma.emsi.minicrm.dao.entities.Commercial;
import ma.emsi.minicrm.dao.repositories.CommercialRepository;
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

    public Page<Commercial> findPaginated(@RequestParam(name = "page", defaultValue = "0") int page,
                                          @RequestParam(name = "size", defaultValue = "5") int size,
                                          @RequestParam(name = "keyword", defaultValue = "") String kw) {
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
            commercialRepository.delete(commercial);
            return true;
        } else {
            return false;
        }
    }
}
