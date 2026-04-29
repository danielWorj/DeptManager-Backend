package com.example.DeptManager.ENTITY.Horaire;

import com.example.DeptManager.ENTITY.Scolarite.Matiere;
import com.example.DeptManager.ENTITY.Scolarite.Repartition;
import com.example.DeptManager.ENTITY.Structure.Departement;
import com.example.DeptManager.ENTITY.Structure.Filiere;
import com.example.DeptManager.ENTITY.Structure.Niveau;
import com.example.DeptManager.ENTITY.Structure.Salle;
import com.example.DeptManager.ENTITY.Utilisateur.Enseignant;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table
@Data
public class Horaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    private Repartition repartition ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    private Salle salle ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    private Jour jour ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    private Periode periode ;




}
