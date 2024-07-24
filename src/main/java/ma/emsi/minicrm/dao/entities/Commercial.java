package ma.emsi.minicrm.dao.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
@Entity

public class Commercial extends Utilisateur {

    @OneToMany (mappedBy = "commercial", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lead> leads;

    @OneToMany(mappedBy = "commercial", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RendezVous> rendezVousList;
}