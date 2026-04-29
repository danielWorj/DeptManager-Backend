package com.example.DeptManager.DTO;

import com.example.DeptManager.ENTITY.Structure.Departement;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class FiliereDTO {
    private Integer id ;
    private String intitule ;
    private String abreviation;
    @Lob
    private String description ;
    private Integer departement ;
}
