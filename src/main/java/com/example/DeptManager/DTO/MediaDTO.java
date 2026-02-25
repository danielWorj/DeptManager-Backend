package com.example.DeptManager.DTO;

import com.example.DeptManager.ENTITY.Structure.Departement;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class MediaDTO {
    private Integer id ;
    private String url ;
    private Integer departement ;
}
