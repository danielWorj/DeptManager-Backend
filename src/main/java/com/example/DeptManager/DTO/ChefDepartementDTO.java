package com.example.DeptManager.DTO;

import com.example.DeptManager.ENTITY.Structure.Departement;
import com.example.DeptManager.ENTITY.Utilisateur.Enseignant;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ChefDepartementDTO {
    private Integer id;
    private Integer enseignant;
    private Integer departement;
    private String diplome ;
    private Integer anneeexperience ;
    private String bureau ;
    private LocalDate dateDebut;
    private LocalDate dateTime ;
}
