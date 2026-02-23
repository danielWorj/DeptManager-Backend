package com.example.DeptManager.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UtilisateurDTO {
    private Integer id ;
    private String nom ;
    private String prenom ;
    private LocalDate dateCreation;
    private String telephone ;

    private String email;
    private String password;
    private Integer role ;
    private Boolean status ;
    private String photo ;

}
