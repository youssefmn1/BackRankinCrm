package ma.emsi.minicrm.services;

import jakarta.persistence.EntityNotFoundException;
import ma.emsi.minicrm.dao.entities.Commercial;
import ma.emsi.minicrm.dao.entities.Lead;
import ma.emsi.minicrm.dao.entities.RendezVous;
import ma.emsi.minicrm.dao.repositories.RendezVousRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RendezVousService {

    @Autowired
    private RendezVousRepository rendezVousRepository;

    public Page<RendezVous> findPaginated(int page, int size, String kw) {
        return rendezVousRepository.findByLieuContains(kw, PageRequest.of(page, size));
    }

    public RendezVous createRendezVous(RendezVous rendezVous) {
        return rendezVousRepository.save(rendezVous);
    }

    public RendezVous getRendezVousById(Integer id) {
        Optional<RendezVous> optionalRendezVous = rendezVousRepository.findById(id);
        return optionalRendezVous.orElse(null);
    }

    public List<RendezVous> getAllRendezVous() {
        return rendezVousRepository.findAll();
    }

    public RendezVous updateRendezVous(Integer id, RendezVous rendezVousDetails) {
        RendezVous rendezVous = getRendezVousById(id);
        if (rendezVous != null) {
            rendezVous.setDate(rendezVousDetails.getDate());
            rendezVous.setHeure(rendezVousDetails.getHeure());
            rendezVous.setLieu(rendezVousDetails.getLieu());
            rendezVous.setLead(rendezVousDetails.getLead());
            rendezVous.setCommercial(rendezVousDetails.getCommercial());

            return rendezVousRepository.save(rendezVous);
        } else {
            return null;
        }
    }

   public void deleteRendezVous(Integer id) {
    RendezVous rendezVous = getRendezVousById(id);
    if (rendezVous != null) {
        rendezVousRepository.delete(rendezVous);
        System.out.println("RendezVous deleted successfully.");
    } else {
        throw new EntityNotFoundException("RendezVous with ID " + id + " not found");
    }
}

}
