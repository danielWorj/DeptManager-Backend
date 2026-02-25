package com.example.DeptManager.ENTITY.Structure;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table
public class MotifRequete {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id ;
    private String intitule ;
    @Lob
    private String description ;
}
