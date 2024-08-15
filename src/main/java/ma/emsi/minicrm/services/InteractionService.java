package ma.emsi.minicrm.services;

import ma.emsi.minicrm.dao.entities.Interaction;
import ma.emsi.minicrm.dao.entities.Lead;
import ma.emsi.minicrm.dao.repositories.InteractionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InteractionService {
    @Autowired
    private InteractionRepository interactionRepository;

    public Page<Interaction> findPaginated(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page, size);
        if (keyword != null && !keyword.isEmpty()) {
            return interactionRepository.findByDetailsContains(keyword, pageable);
        } else {
            return interactionRepository.findAll(pageable);
        }
    }

    public Interaction getInteractionById(Integer id) {
        return interactionRepository.findById(id).orElse(null);
    }
    public List<Interaction> getAllInteractions() {
        return interactionRepository.findAll();
    }
    public Interaction saveInteraction(Interaction interaction) {
        return interactionRepository.save(interaction);
    }

    public void deleteInteraction(Integer id) {
        interactionRepository.deleteById(id);
    }
    public boolean existsById(Integer id) {
        return interactionRepository.existsById(id);
    }
}
