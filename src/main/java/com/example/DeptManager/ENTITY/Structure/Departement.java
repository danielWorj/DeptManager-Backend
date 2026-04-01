package com.example.DeptManager.ENTITY.Structure;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table
@Data
public class Departement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id ;
    private String abreviation ;
    private String intitule ;
    @Lob
    private String description ; //Description du departement
    @Lob
    private String motChef ;
    private String nomChef ;
    private String admission ;
}
