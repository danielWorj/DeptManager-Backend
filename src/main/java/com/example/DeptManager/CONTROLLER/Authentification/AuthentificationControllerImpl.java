package com.example.DeptManager.CONTROLLER.Authentification;

import com.example.DeptManager.DTO.BasicAuthDTO;
import com.example.DeptManager.DTO.BasicAuthData;
import com.example.DeptManager.ENTITY.Utilisateur.Utilisateur;
import com.example.DeptManager.REPOSITORY.Utilisateur.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.databind.ObjectMapper;

@RestController
public class AuthentificationControllerImpl implements AuthentificationControllerInt{
    @Autowired
    private UtilisateurRepository utilisateurRepository;


    @Override
    public ResponseEntity<BasicAuthData> basicLoginSysteme(String authdata) {
        BasicAuthDTO basicAuthDTO = new ObjectMapper().readValue(authdata, BasicAuthDTO.class);

        Utilisateur utilisateur = this.utilisateurRepository.findByEmailAndPassword(basicAuthDTO.getEmail(), basicAuthDTO.getPassword()).orElse(null);

        System.out.println("Le nom de l'utilisateur est : " + utilisateur.getNom());

        return ResponseEntity.ok(new BasicAuthData(utilisateur.getId(), utilisateur.getRole()));
    }
}
