package ma.emsi.minicrm.web;

import ma.emsi.minicrm.dao.entities.*;
import ma.emsi.minicrm.services.InteractionService;
import ma.emsi.minicrm.services.LeadService;
import ma.emsi.minicrm.services.FileService;
import ma.emsi.minicrm.services.CommercialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
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

    @GetMapping
    public ResponseEntity<List<Lead>> getAllLeads() {
        List<Lead> leads = leadService.getAllLeads();
        return ResponseEntity.ok(leads);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Lead> getLeadById(@PathVariable Integer id) {
        Lead lead = leadService.getLeadById(id);
        if (lead == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(lead);
    }

    @PostMapping
    public ResponseEntity<Lead> createLead(@RequestBody Lead lead) {
        lead.setStatut(Statut.NOUVEAU);
        Lead createdLead = leadService.createLead(lead);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLead);
    }

    @PutMapping("/{leadId}/assign-commercial/{commercialId}")
    public ResponseEntity<Void> assignCommercial(
            @PathVariable Integer leadId,
            @PathVariable Integer commercialId) {
        leadService.assignCommercial(leadId, commercialId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Lead> updateLead(@PathVariable Integer id, @RequestBody Lead lead) {
        if (!id.equals(lead.getId())) {
            return ResponseEntity.badRequest().build();
        }
        Lead updatedLead = leadService.updateLead(id, lead);
        return ResponseEntity.ok(updatedLead);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLead(@PathVariable Integer id) {
        boolean deleted = leadService.deleteLead(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/deleteSelected")
    public ResponseEntity<Void> deleteSelectedLeads(@RequestBody List<Integer> leadIds) {
        try {
            leadService.deleteLeads(leadIds);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{leadId}/files/upload")
    public ResponseEntity<FileMetadata> uploadFile(@PathVariable Integer leadId, @RequestParam("file") MultipartFile file) {
        try {
            FileMetadata fileMetadata = fileService.saveFile(file, leadId);
            return ResponseEntity.ok(fileMetadata);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/files/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) {
        try {
            FileMetadata fileMetadata = fileService.getFileById(fileId);
            Path filePath = Paths.get(fileMetadata.getFilePath());
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileMetadata.getFileName() + "\"")
                        .body(resource);
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

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

    @GetMapping("/{id}/history")
    public ResponseEntity<List<LeadHistory>> getLeadHistory(@PathVariable Integer id) {
        List<LeadHistory> history = leadService.getLeadHistory(id);
        return ResponseEntity.ok(history);
    }

    @DeleteMapping("/{leadId}/files/{fileId}")
    public ResponseEntity<Void> deleteFile(@PathVariable Integer leadId, @PathVariable Long fileId) {
        try {
            fileService.deleteFile(fileId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}