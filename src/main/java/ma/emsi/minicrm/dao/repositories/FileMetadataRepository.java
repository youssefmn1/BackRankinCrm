package ma.emsi.minicrm.dao.repositories;

import ma.emsi.minicrm.dao.entities.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileMetadataRepository extends JpaRepository<FileMetadata, Long> {
    List<FileMetadata> findByLeadId(Integer leadId);
}
