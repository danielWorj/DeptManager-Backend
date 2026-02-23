package com.example.DeptManager.CONTROLLER.Utilisateur;

import com.example.DeptManager.ENTITY.Server.ServerReponse;
import com.example.DeptManager.ENTITY.Utilisateur.Enseignant;
import com.example.DeptManager.ENTITY.Utilisateur.Etudiant;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/deptmanager/api/utilisateur/")
@CrossOrigin("*")
public interface UtilisateurControllerInt {

    //Enseignant
    @GetMapping("/enseignant/all")
    ResponseEntity<List<Enseignant>> findAllEnseignant();
    @GetMapping("/enseignant/count")
    ResponseEntity<Long> countAllEnseignant();

    @PostMapping("/enseignant/create")
    ResponseEntity<ServerReponse> createEnseignant(@RequestParam("enseignant") String enseignant);

    @PostMapping("/enseignant/update")
    ResponseEntity<ServerReponse> updateEnseignant(@RequestParam("enseignant") String enseignant);
    @GetMapping("/enseignant/bydepartement/{id}")
    ResponseEntity<List<Enseignant>> findAllEnseignantByDepartement(@PathVariable Integer id);

    //Etudiant
    @GetMapping("/etudiant/all")
    ResponseEntity<List<Etudiant>> findAllEtudiant();
    @GetMapping("/etudiant/count")
    ResponseEntity<Long> countAllEtudiant();
    @PostMapping("/etudiant/create")
    ResponseEntity<ServerReponse> createEtudiant(@RequestParam("etudiant") String enseignant);
    @PostMapping("/etudiant/update")
    ResponseEntity<ServerReponse> updateEtudiant(@RequestParam("etudiant") String enseignant);
    @GetMapping("/etudiant/bydepartement/{id}")
    ResponseEntity<List<Etudiant>> findAllEtudiantByDepartement(@PathVariable Integer id);
    @GetMapping("/etudiant/byniveau/{id}")
    ResponseEntity<List<Etudiant>> findAllEtudiantByNiveau(@PathVariable Integer id);
    @GetMapping("/etudiant/byfiliere/byniveau/{idF}/{idN}")
    ResponseEntity<List<Etudiant>> findAllEtudiantByFiliereAndNiveau(@PathVariable Integer id);

}
