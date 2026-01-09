package com.example.DeptManager.DTO;

import com.example.DeptManager.ENTITY.Horaire.Jour;
import com.example.DeptManager.ENTITY.Horaire.Periode;
import com.example.DeptManager.ENTITY.Scolarite.Matiere;
import com.example.DeptManager.ENTITY.Structure.Filiere;
import com.example.DeptManager.ENTITY.Structure.Niveau;
import com.example.DeptManager.ENTITY.Structure.Salle;
import com.example.DeptManager.ENTITY.Utilisateur.Enseignant;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class HoraireDTO {
    private Integer id ;
    private Integer enseignant ;
    private Integer filiere ;
    private Integer niveau ;
    private Integer matiere ;
    private Integer salle ;
    private Integer jour ;
    private Integer periode ;
}
