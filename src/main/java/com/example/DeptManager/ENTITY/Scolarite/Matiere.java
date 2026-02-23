package com.example.DeptManager.ENTITY.Scolarite;

import com.example.DeptManager.ENTITY.Structure.Departement;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table
@Data
public class Matiere {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String intitule ;
    private Integer semestre ;
    private Integer credit ;
    private Integer seance ;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    private Departement departement ;


}
