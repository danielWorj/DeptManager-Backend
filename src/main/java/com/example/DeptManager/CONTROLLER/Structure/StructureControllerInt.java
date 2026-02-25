package com.example.DeptManager.CONTROLLER.Structure;

import com.example.DeptManager.ENTITY.Scolarite.AnneeAcademique;
import com.example.DeptManager.ENTITY.Scolarite.Documentation.TypeDocument;
import com.example.DeptManager.ENTITY.Server.ServerReponse;
import com.example.DeptManager.ENTITY.Structure.*;
import com.example.DeptManager.ENTITY.Utilisateur.Poste;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    //Annee Academique
    @GetMapping("/anneeacademique/all")
    ResponseEntity<List<AnneeAcademique>> findAllAnneeAcademique();

    //Type Document
    @GetMapping("/typedocument/all")
    ResponseEntity<List<TypeDocument>> findAllTypeDocument();



    //Debouche

    @GetMapping("/debouche/byfiliere/{id}")
    ResponseEntity<List<Debouche>> findAllDeboucheByFiliere(@PathVariable Integer id);
    @PostMapping("/debouche/create")
    ResponseEntity<ServerReponse> createDebouche(@RequestParam("debouche") String debouche);
    @PostMapping("/debouche/update")
    ResponseEntity<ServerReponse> updateDebouche(@RequestParam("debouche") String debouche);
    @GetMapping("/debouche/delete/{id}")
    ResponseEntity<ServerReponse> deleteDebouche(@PathVariable Integer id);


    //Media
    @PostMapping("/media/create")
    ResponseEntity<ServerReponse> createMedia(@RequestParam("media") String media , @RequestParam("file")MultipartFile fichier) throws IOException;
    @GetMapping("/media/all/bydepartement/{id}")
    ResponseEntity<List<Media>> findAllMediaByDepartement(@PathVariable Integer id);
    @GetMapping("/media/delete/{id}")
    ResponseEntity<ServerReponse> deleteMedia(@PathVariable Integer id);

    //Requete
    @GetMapping("/requete/all")
    ResponseEntity<List<Requete>> findAllRequete();
    @GetMapping("/requete/allbymotif/{id}")
    ResponseEntity<List<Requete>> findAllRequeteByMotif(@PathVariable Integer id);
    @PostMapping("/requete/creation")
    ResponseEntity<ServerReponse> creationRequete(@RequestParam("requete") String requete);
    @PostMapping("/requete/update")
    ResponseEntity<ServerReponse> updateRequete(@RequestParam("requete") String requete);
    @GetMapping("/requete/delete/{id}")
    ResponseEntity<ServerReponse> deleteRequete(@PathVariable Integer id);
    @GetMapping("/requete/change/{idR}/{idS}")
    ResponseEntity<ServerReponse> changeRequeteStatut(@PathVariable Integer idR, @PathVariable Integer idS);
    @GetMapping("/requete/motif/all")
    ResponseEntity<List<MotifRequete>> findAllMotifRequete();


    //Piece jointe
    @GetMapping("/requete/all/piecejointe/{id}")
    ResponseEntity<List<PieceJointeRequete>> findAllPieceByRequete(@PathVariable Integer id);
    @PostMapping("/requete/piecejointe/create")
    ResponseEntity<ServerReponse> createPieceJointeRequete(@RequestParam("piece") String piece , @RequestParam("file") MultipartFile file) throws IOException;

}
