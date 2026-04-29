package com.example.DeptManager.ENTITY.Actualite;

import com.example.DeptManager.ENTITY.Evaluation.TypeEvaluation;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table
@Data
public class Actualite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String titre ;
    @Lob
    private String description ;
    private String url ;
    private LocalDate datePublication ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    private CategorieActualite categorieActualite ;
}
