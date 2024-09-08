package ma.emsi.minicrm.web;

import ma.emsi.minicrm.dao.entities.Interaction;
import ma.emsi.minicrm.services.InteractionService;
import ma.emsi.minicrm.services.LeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Controller
@RequestMapping("api/interactions")
public class InteractionRestController {

    @Autowired
    private InteractionService interactionService;

    @Autowired
    private LeadService leadService;

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
    public ResponseEntity<Interaction> createInteractionForLead(@PathVariable Integer leadId, @RequestBody Interaction interaction) {
        // Check if the lead exists
        if (!leadService.existsById(leadId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // Optionally, set the lead to the interaction
        //interaction.setLeadId(leadId); // Assuming you have a method to set the leadId
        Interaction savedInteraction = interactionService.saveInteraction(interaction);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/leads/" + leadId + "/interactions/" + savedInteraction.getId());
        return new ResponseEntity<>(savedInteraction, headers, HttpStatus.CREATED);
    }
    @GetMapping("/{leadId}/interactions")
    public ResponseEntity<List<Interaction>> getInteractionsByLeadId(@PathVariable Integer leadId) {
        List<Interaction> interactions = interactionService.getInteractionsByLeadId(leadId);
        if (interactions == null || interactions.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(interactions);
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
