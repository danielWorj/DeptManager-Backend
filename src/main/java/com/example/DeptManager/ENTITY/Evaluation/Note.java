package com.example.DeptManager.ENTITY.Evaluation;

import com.example.DeptManager.ENTITY.Scolarite.Matiere;
import com.example.DeptManager.ENTITY.Scolarite.Repartition;
import com.example.DeptManager.ENTITY.Utilisateur.Etudiant;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table
@Data
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer note ;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    private Repartition repartition ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    private TypeEvaluation typeEvaluation ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    private Etudiant etudiant ;

}
