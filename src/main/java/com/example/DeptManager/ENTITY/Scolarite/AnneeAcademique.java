package com.example.DeptManager.ENTITY.Scolarite;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table
@Data
public class AnneeAcademique {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String intitule ;
    private Boolean status ; //Active 1 : Inactive 0
}
