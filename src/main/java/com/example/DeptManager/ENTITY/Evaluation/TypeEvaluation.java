package com.example.DeptManager.ENTITY.Evaluation;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table
public class TypeEvaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id ;
    private String intitule ;
}
