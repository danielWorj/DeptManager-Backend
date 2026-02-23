package com.example.DeptManager.DTO;

import lombok.Data;

@Data
public class EtudiantDTO extends UtilisateurDTO{
    private String matricule ;
    private Integer filiere ;
    private Integer niveau ;
}
