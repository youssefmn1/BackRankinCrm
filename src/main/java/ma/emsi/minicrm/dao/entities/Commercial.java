package ma.emsi.minicrm.dao.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Commercial extends Utilisateur {

    @OneToMany(mappedBy = "commercial", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"commercial", "rendezVous", "interactions", "otherFields"})
    private List<Lead> leads;

    @OneToMany(mappedBy = "commercial", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"commercial", "lead", "otherFields"})
    private List<RendezVous> rendezVousList;
}
