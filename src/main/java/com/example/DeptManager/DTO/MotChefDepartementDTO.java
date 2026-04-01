package com.example.DeptManager.DTO;

import com.example.DeptManager.ENTITY.Structure.Departement;
import com.example.DeptManager.ENTITY.Utilisateur.ChefDepartement;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class MotChefDepartementDTO {
    private Integer id ;
    private Integer chef ;
    private Integer departement ;
    @Lob
    private String enonce ;

}
