package ma.emsi.minicrm.dao.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public enum Statut {

    NOUVEAU("Nouveau"),
    EN_COURS("En cours"),
    CONVERTI("Converti"),
    ABANDONNE("Abandonné");

    private String statut;

//    @Override
//    public String toString() {
//        return statut;
//    }

}
