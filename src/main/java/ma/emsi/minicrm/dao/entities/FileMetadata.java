package ma.emsi.minicrm.dao.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Setter@Getter
@Entity
public class FileMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String fileType;
    private Long fileSize;
    private String filePath;

    @ManyToOne
    @JoinColumn(name = "lead_id")
    private Lead lead;

    // Getters and setters
}
