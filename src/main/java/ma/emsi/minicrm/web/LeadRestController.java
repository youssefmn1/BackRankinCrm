package ma.emsi.minicrm.web;

import ma.emsi.minicrm.dao.entities.Interaction;
import ma.emsi.minicrm.dao.entities.Lead;
import ma.emsi.minicrm.dao.entities.FileMetadata;
import ma.emsi.minicrm.dao.entities.Statut;
import ma.emsi.minicrm.services.InteractionService;
import ma.emsi.minicrm.services.LeadService;
import ma.emsi.minicrm.services.FileService;
import ma.emsi.minicrm.services.CommercialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/leads")
public class LeadRestController {

    @Autowired
    private LeadService leadService;

    @Autowired
    private CommercialService commercialService;

    @Autowired
    private FileService fileService;

    @Autowired
    private InteractionService interactionService;

    // Get all leads (GET /api/v1/leads)
    @GetMapping
    public ResponseEntity<List<Lead>> getAllLeads() {
        List<Lead> leads = leadService.getAllLeads();
        return ResponseEntity.ok(leads);
    }

    // Get a lead by its ID (GET /api/v1/leads/{id})
    @GetMapping("/{id}")
    public ResponseEntity<Lead> getLeadById(@PathVariable Integer id) {
        Lead lead = leadService.getLeadById(id);
        if (lead == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(lead);
    }

    // Create a new lead (POST /api/v1/leads)
    @PostMapping
    public ResponseEntity<Lead> createLead(@RequestBody Lead lead) {
        lead.setStatut(Statut.NOUVEAU);
        Lead createdLead = leadService.createLead(lead);
        return ResponseEntity.ok(createdLead);
    }

    // Assign a lead to a commercial (PUT /api/v1/leads/assignCommercial)
    @PutMapping("/assignCommercial")
    public ResponseEntity<Void> assignCommercial(
            @RequestParam Integer leadId,
            @RequestParam Integer commercialId) {
        leadService.assignCommercial(leadId, commercialId);
        return ResponseEntity.ok().build();
    }

    // Update an existing lead (PUT /api/v1/leads/{id})
    @PutMapping("/{id}")
    public ResponseEntity<Lead> updateLead(@PathVariable Integer id, @RequestBody Lead lead) {
        if (!id.equals(lead.getId())) {
            return ResponseEntity.badRequest().build();
        }
        Lead updatedLead = leadService.updateLead(id, lead);
        return ResponseEntity.ok(updatedLead);
    }

    // Delete a lead by its ID (DELETE /api/v1/leads/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLead(@PathVariable Integer id) {
        leadService.deleteLead(id);
        return ResponseEntity.ok().build();
    }

    // Delete multiple leads by their IDs (POST /api/v1/leads/deleteSelected)
    @PostMapping("/deleteSelected")
    public ResponseEntity<Void> deleteSelectedLeads(@RequestBody List<Integer> leadIds) {
        try {
            leadService.deleteLeads(leadIds);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Upload a file and attach it to a lead (POST /api/v1/leads/{leadId}/files/upload)
    @PostMapping("/{leadId}/files/upload")
    public ResponseEntity<FileMetadata> uploadFile(@PathVariable Integer leadId, @RequestParam("file") MultipartFile file) {
        try {
            FileMetadata fileMetadata = fileService.saveFile(file, leadId);
            return ResponseEntity.ok(fileMetadata);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get all files attached to a specific lead (GET /api/v1/leads/{leadId}/files)
    @GetMapping("/{leadId}/files")
    public ResponseEntity<List<FileMetadata>> getFilesByLeadId(@PathVariable Integer leadId) {
        List<FileMetadata> files = fileService.getFilesByLeadId(leadId);
        return ResponseEntity.ok(files);
    }
    @GetMapping("/{leadId}/interactions")
    public ResponseEntity<List<Interaction>> getInteractionsByLeadId(@PathVariable Integer leadId) {
        List<Interaction> interactions = interactionService.getInteractionsByLeadId(leadId);
        if (interactions == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(interactions);
    }


    // Create an interaction for a lead (POST /api/v1/leads/{leadId}/interactions)
    @PostMapping("/{leadId}/interactions")
    public ResponseEntity<Interaction> createInteractionForLead(@PathVariable Integer leadId, @RequestBody Interaction interaction) {
        Lead lead = leadService.getLeadById(leadId);
        if (lead == null) {
            return ResponseEntity.notFound().build();
        }
        interaction.setLead(lead);
        Interaction savedInteraction = interactionService.saveInteraction(interaction);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/leads/" + leadId + "/interactions/" + savedInteraction.getId());
        return new ResponseEntity<>(savedInteraction, headers, HttpStatus.CREATED);
    }

}
