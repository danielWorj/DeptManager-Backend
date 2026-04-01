package com.example.DeptManager.DTO;

import com.example.DeptManager.ENTITY.Scolarite.AnneeAcademique;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class SemestreDTO {
    private Integer id;
    private String intitule ;
    private Integer anneeAcademique ;
}
