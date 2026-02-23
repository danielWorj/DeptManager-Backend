package com.example.DeptManager.ENTITY.Scolarite.Documentation;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table
@Data
public class TypeDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id ;
    private String intitule ;
    private String description ;
}
