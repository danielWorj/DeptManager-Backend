package com.example.DeptManager.ENTITY.Structure;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table
@Data
public class StatutRequete {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id ;
    private String intitule ;
}
