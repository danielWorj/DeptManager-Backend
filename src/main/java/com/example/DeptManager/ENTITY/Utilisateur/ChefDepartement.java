package com.example.DeptManager.ENTITY.Utilisateur;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
@DiscriminatorValue(value = "etudiant")
public class ChefDepartement extends Enseignant{

    String diplome ;
    Integer anneeexperience ;
    String labo ;
    String bureau ;
}
