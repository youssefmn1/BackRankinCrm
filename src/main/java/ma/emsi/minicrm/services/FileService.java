package ma.emsi.minicrm.services;

import jakarta.transaction.Transactional;
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
import java.util.UUID;

@Service
public class FileService {

    @Autowired
    private FileMetadataRepository fileMetadataRepository;

    @Autowired
    private LeadRepository leadRepository;

    private final Path rootLocation = Paths.get("uploaded-files"); // Directory for file storage
    private final String uploadDir = "uploaded-files"; // Define upload directory

    public FileService() throws IOException {
        // Create directory if it doesn't exist
        if (!Files.exists(rootLocation)) {
            Files.createDirectories(rootLocation);
        }
    }

    public FileMetadata getFileById(Long fileId) {
        return fileMetadataRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));
    }

    public FileMetadata saveFile(MultipartFile file, Integer leadId) throws IOException {
        Lead lead = leadRepository.findById(leadId)
                .orElseThrow(() -> new RuntimeException("Lead not found"));

        String originalFileName = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFileName);
        String uniqueFileName = generateUniqueFileName(fileExtension);

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(uniqueFileName);
        Files.copy(file.getInputStream(), filePath);

        FileMetadata fileMetadata = new FileMetadata();
        fileMetadata.setFileName(originalFileName);
        fileMetadata.setUniqueFileName(uniqueFileName);
        fileMetadata.setFilePath(filePath.toString());
        fileMetadata.setFileType(file.getContentType());
        fileMetadata.setFileSize(file.getSize());
        fileMetadata.setLead(lead);

        return fileMetadataRepository.save(fileMetadata);
    }

    private String generateUniqueFileName(String fileExtension) {
        return UUID.randomUUID().toString() + fileExtension;
    }

    private String getFileExtension(String fileName) {
        int lastIndexOf = fileName.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return fileName.substring(lastIndexOf);
    }

    @Transactional
    public void deleteFile(Long fileId) throws Exception {
        FileMetadata fileMetadata = fileMetadataRepository.findById(fileId)
                .orElseThrow(() -> new Exception("File not found"));

        // Delete the physical file
        Path filePath = Paths.get(fileMetadata.getFilePath());
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new Exception("Error deleting physical file", e);
        }

        // Delete the file metadata from the database
        fileMetadataRepository.delete(fileMetadata);
    }

    public List<FileMetadata> getFilesByLeadId(Integer leadId) {
        return fileMetadataRepository.findByLeadId(leadId);
    }
}