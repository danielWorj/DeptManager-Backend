package com.example.DeptManager.ENTITY.Utilisateur;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;


@Entity
@Data
@Inheritance(strategy = InheritanceType.JOINED) //Heritage
@DiscriminatorColumn(name = "quality_user")
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id ;
    private String nom ;
    private String prenom ;
    private LocalDate dateCreation;
    private String email;
    private String password;
    private Integer role ;
    private Boolean status ;
    private String photo ;

}
