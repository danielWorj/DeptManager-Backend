package com.example.DeptManager.DTO;

import com.example.DeptManager.ENTITY.Scolarite.AnneeAcademique;
import com.example.DeptManager.ENTITY.Scolarite.Documentation.TypeDocument;
import com.example.DeptManager.ENTITY.Scolarite.Matiere;
import com.example.DeptManager.ENTITY.Structure.Departement;
import com.example.DeptManager.ENTITY.Structure.Filiere;
import com.example.DeptManager.ENTITY.Structure.Niveau;
import com.example.DeptManager.ENTITY.Utilisateur.Enseignant;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DocumentDTO {
    private Integer id ;
    private String url ;
    private LocalDate dateC;

    private Integer typeDocument ;
    private Integer matiere ;
    private Integer departement ;
    private Integer filiere ;
    private Integer niveau ;
    private Integer enseignant ;
    private Integer anneeAcademique ;
}
