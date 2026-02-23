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
}
