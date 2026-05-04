package com.example.DeptManager.DTO;

import com.example.DeptManager.ENTITY.Actualite.CategorieActualite;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ActualiteDTO {
    private Integer id;
    private String titre ;
    private Boolean vedette ; //Il s'agit de l'info en vedette

    @Lob
    private String description ;
    private LocalDate datePublication ;
    private Integer categorieActualite ;
}
