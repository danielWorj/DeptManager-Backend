package com.example.DeptManager.ENTITY.Actualite;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table
@Data
public class CategorieActualite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String intitule ;

}
