package ma.emsi.minicrm.web;

import ma.emsi.minicrm.dao.entities.Lead;
import ma.emsi.minicrm.dao.entities.Statut;
import ma.emsi.minicrm.services.LeadService;
import ma.emsi.minicrm.services.CommercialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/leads")
public class LeadRestController {

    @Autowired
    private LeadService leadService;

    @Autowired
    private CommercialService commercialService;

    // Get all leads (GET /api/v1/leads)
    @GetMapping
    public ResponseEntity<List<Lead>> getAllLeads() {
        List<Lead> leads = leadService.getAllLeads(); // Assuming you have a method in LeadService that fetches all leads
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
}
