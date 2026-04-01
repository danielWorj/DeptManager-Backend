package com.example.DeptManager.DTO;


import lombok.Data;

import java.time.LocalDate;

@Data
public class ChefDeptDTO {
    private Integer id;
    private Integer enseignant;
    private Integer departement;
    private String diplome ;
    private Integer anneeexperience ;
    private String bureau ;
    private LocalDate dateDebut;
    private LocalDate dateTime ;
}
