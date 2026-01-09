package com.example.DeptManager.ENTITY.Utilisateur;

import com.example.DeptManager.ENTITY.Structure.Niveau;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
@DiscriminatorValue(value = "enseignant")
public class Enseignant extends Utilisateur{
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    private Poste poste ;

}
