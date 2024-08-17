package ma.emsi.minicrm.web;

import ma.emsi.minicrm.dao.entities.Interaction;
import ma.emsi.minicrm.services.InteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

@Controller
@RequestMapping("api/interactions")
public class InteractionRestController {

    @Autowired
    private InteractionService interactionService;

    @GetMapping
    public ResponseEntity<Page<Interaction>> listInteractions(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size,
                                                              @RequestParam(required = false) String keyword) {
        Page<Interaction> interactions = interactionService.findPaginated(page, size, keyword);
        return new ResponseEntity<>(interactions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Interaction> getInteractionById(@PathVariable Integer id) {
        Interaction interaction = interactionService.getInteractionById(id);
        if (interaction != null) {
            return new ResponseEntity<>(interaction, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Interaction> createInteraction(@RequestBody Interaction interaction) {
        Interaction savedInteraction = interactionService.saveInteraction(interaction);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/interactions/" + savedInteraction.getId());
        return new ResponseEntity<>(savedInteraction, headers, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Interaction> updateInteraction(@PathVariable Integer id, @RequestBody Interaction interaction) {
        if (!interactionService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        interaction.setId(id);
        Interaction updatedInteraction = interactionService.saveInteraction(interaction);
        return new ResponseEntity<>(updatedInteraction, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInteraction(@PathVariable Integer id) {
        if (!interactionService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        interactionService.deleteInteraction(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
