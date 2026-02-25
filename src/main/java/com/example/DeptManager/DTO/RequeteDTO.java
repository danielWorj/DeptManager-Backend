package com.example.DeptManager.DTO;

import com.example.DeptManager.ENTITY.Structure.MotifRequete;
import com.example.DeptManager.ENTITY.Structure.StatutRequete;
import com.example.DeptManager.ENTITY.Utilisateur.Etudiant;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RequeteDTO {

    private Integer id;
    private Integer motifRequete ;
    private String dateCreation;
    private Integer statutRequete ;
    private Integer etudiant ;
    @Lob
    private String description;

}
