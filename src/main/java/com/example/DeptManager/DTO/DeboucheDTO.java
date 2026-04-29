package com.example.DeptManager.DTO;

import com.example.DeptManager.ENTITY.Structure.Filiere;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class DeboucheDTO {
    private Integer id ;
    private String intitule ;
    @Lob
    private String description ;
    private Integer filiere ;
}
