package com.example.DeptManager.DTO;

import lombok.Data;

@Data
public class EnseignantDTO extends UtilisateurDTO{
    private Integer poste ;
    private Integer departement;
}
