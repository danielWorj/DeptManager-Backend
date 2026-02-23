package com.example.DeptManager.CONTROLLER.Structure;

import com.example.DeptManager.ENTITY.Server.ServerReponse;
import com.example.DeptManager.ENTITY.Structure.Departement;
import com.example.DeptManager.ENTITY.Structure.Filiere;
import com.example.DeptManager.ENTITY.Structure.Niveau;
import com.example.DeptManager.ENTITY.Structure.Salle;
import com.example.DeptManager.ENTITY.Utilisateur.Poste;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/deptmanager/api/structure/")
@CrossOrigin("*")
public interface StructureControllerInt {
    //Departement
    @GetMapping("/departement/all")
    ResponseEntity<List<Departement>> findAllDepartement();
    @PostMapping("/departement/create")
    ResponseEntity<ServerReponse> createDepartement(@RequestParam("departement") String departement);
    @PostMapping("/departement/update")
    ResponseEntity<ServerReponse> updateDepartement(@RequestParam("departement") String departement);
    @GetMapping("/departement/delete/{id}")
    ResponseEntity<ServerReponse> deleteDepartement(@PathVariable Integer id);

    //Filiere
    @GetMapping("/filiere/all")
    ResponseEntity<List<Filiere>> findAllFiliere();
    @GetMapping("/filiere/all/bydepartement/{id}")
    ResponseEntity<List<Filiere>> findAllFiliereByDepartement(@PathVariable Integer id);
    @PostMapping("/filiere/create")
    ResponseEntity<ServerReponse> createFiliere(@RequestParam("filiere") String filiere);
    @PostMapping("/filiere/update")
    ResponseEntity<ServerReponse> updateFiliere(@RequestParam("filiere") String filiere);
    @GetMapping("/filiere/delete/{id}")
    ResponseEntity<ServerReponse> deleteFiliere(@PathVariable Integer id);

    //Niveau
    @GetMapping("/niveau/all")
    ResponseEntity<List<Niveau>> findAllNiveau();
    @PostMapping("/niveau/create")
    ResponseEntity<ServerReponse> createNiveau(@RequestParam("niveau") String niveau);
    @PostMapping("/niveau/update")
    ResponseEntity<ServerReponse> updateNiveau(@RequestParam("niveau") String niveau);
    @GetMapping("/niveau/delete/{id}")
    ResponseEntity<ServerReponse> deleteNiveau(@PathVariable Integer id);

    //Salle
    @GetMapping("/salle/all")
    ResponseEntity<List<Salle>> findAllSalle();
    @PostMapping("/salle/create")
    ResponseEntity<ServerReponse> createSallle(@RequestParam("salle") String salle);
    @PostMapping("/salle/update")
    ResponseEntity<ServerReponse> updateSalle(@RequestParam("salle") String salle);
    @GetMapping("/salle/delete/{id}")
    ResponseEntity<ServerReponse> deleteSalle(@PathVariable Integer id);

    //Poste
    @GetMapping("/poste/all")
    ResponseEntity<List<Poste>> findAllPoste();
    @PostMapping("/poste/create")
    ResponseEntity<ServerReponse> createPoste(@RequestParam("poste") String poste);


}
