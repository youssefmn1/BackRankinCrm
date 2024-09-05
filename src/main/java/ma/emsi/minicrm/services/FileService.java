package ma.emsi.minicrm.services;

import ma.emsi.minicrm.dao.entities.FileMetadata;
import ma.emsi.minicrm.dao.entities.Lead;
import ma.emsi.minicrm.dao.repositories.FileMetadataRepository;
import ma.emsi.minicrm.dao.repositories.LeadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
@Service
public class FileService {

    @Autowired
    private FileMetadataRepository fileMetadataRepository;

    @Autowired
    private LeadRepository leadRepository;

    private final Path rootLocation = Paths.get("uploaded-files"); // Directory for file storage

    public FileService() throws IOException {
        // Create directory if it doesn't exist
        if (!Files.exists(rootLocation)) {
            Files.createDirectories(rootLocation);
        }
    }

    public FileMetadata saveFile(MultipartFile file, Integer leadId) throws IOException {
        // Sanitize the filename
        String sanitizedFileName = Paths.get(file.getOriginalFilename()).getFileName().toString();
        String filePath = rootLocation.resolve(sanitizedFileName).toString();

        // Save file to the filesystem
        Files.copy(file.getInputStream(), Paths.get(filePath));

        // Create and save file metadata
        FileMetadata fileMetadata = new FileMetadata();
        fileMetadata.setFileName(file.getOriginalFilename());
        fileMetadata.setFileType(file.getContentType());
        fileMetadata.setFileSize(file.getSize());
        fileMetadata.setFilePath(filePath);

        // Find lead and associate the file with it
        Lead lead = leadRepository.findById(leadId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid lead Id: " + leadId));
        fileMetadata.setLead(lead);

        return fileMetadataRepository.save(fileMetadata);
    }

    public List<FileMetadata> getFilesByLeadId(Integer leadId) {
        return fileMetadataRepository.findByLeadId(leadId);
    }
}
