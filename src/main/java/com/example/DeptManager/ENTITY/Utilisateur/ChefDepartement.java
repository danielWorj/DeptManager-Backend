package com.example.DeptManager.ENTITY.Utilisateur;

import com.example.DeptManager.ENTITY.Structure.Departement;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data

public class ChefDepartement{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    private Enseignant enseignant;

    @OneToOne(fetch = FetchType.LAZY)
    private Departement departement;

    private String diplome ;
    private Integer anneeexperience ;
    private String bureau ;
    private LocalDate dateDebut;
    private LocalDate dateTime ;
}
