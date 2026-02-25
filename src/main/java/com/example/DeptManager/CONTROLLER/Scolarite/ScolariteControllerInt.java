package com.example.DeptManager.CONTROLLER.Scolarite;

import com.example.DeptManager.ENTITY.Scolarite.Documentation.Document;
import com.example.DeptManager.ENTITY.Scolarite.Matiere;
import com.example.DeptManager.ENTITY.Scolarite.Repartition;
import com.example.DeptManager.ENTITY.Server.ServerReponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequestMapping("/deptmanager/api/scolarite/")
@CrossOrigin("*")
public interface ScolariteControllerInt {
    //Matiere
    @GetMapping("/matiere/all")
    ResponseEntity<List<Matiere>> findAllMatiere();
    @GetMapping("/matiere/all/bydepartement/{id}")
    ResponseEntity<List<Matiere>> findAllMatiereByDepartement(@PathVariable Integer id);
    @GetMapping("/matiere/all/byfiliere/{id}")
    ResponseEntity<List<Matiere>> findAllMatiereByFiliere(@PathVariable Integer id);
    @PostMapping("/matiere/create")
    ResponseEntity<ServerReponse> createMatiere(@RequestParam("matiere") String matiere);
    @PostMapping("/matiere/update")
    ResponseEntity<ServerReponse> updateMatiere(@RequestParam("matiere") String matiere);
    @GetMapping("/matiere/delete/{id}")
    ResponseEntity<ServerReponse> deleteMatiere(@PathVariable Integer id);

    ///Documention

    @GetMapping("/document/all")
    ResponseEntity<List<Document>> findAllDocument();
    @GetMapping("/document/all/bymatiere/{id}")
    ResponseEntity<List<Document>> findAllDocumentByMatiere(@PathVariable Integer id);
    @GetMapping("/document/all/bytype/{id}")
    ResponseEntity<List<Document>> findAllDocumentByTypeDoc(@PathVariable Integer id);
    @GetMapping("/document/all/bydepartement/{id}")
    ResponseEntity<List<Document>> findAllDocumentByDepartement(@PathVariable Integer id);
    @GetMapping("/document/all/byfiliere/{id}")
    ResponseEntity<List<Document>> findAllDocumentByFiliere(@PathVariable Integer id);
    @GetMapping("/document/all/byniveau/{id}")
    ResponseEntity<List<Document>> findAllDocumentByNiveau(@PathVariable Integer id);
    @GetMapping("/document/all/byenseignant/{id}")
    ResponseEntity<List<Document>> findAllDocumentByEnseignant(@PathVariable Integer id);
    @GetMapping("/document/all/byannee/{id}")
    ResponseEntity<List<Document>> findAllDocumentByAnnee(@PathVariable Integer id);
    @PostMapping("/document/create")
    ResponseEntity<ServerReponse> createDocumentD(@RequestParam("document") String document, @RequestParam("file")MultipartFile file) throws IOException;
    @PostMapping("/document/update")
    ResponseEntity<ServerReponse> updateDocumentD(@RequestParam("document") String document, @RequestParam("file") MultipartFile file) throws IOException;
    @GetMapping("/document/delete/{id}")
    ResponseEntity<ServerReponse> deleteDocument(@PathVariable Integer id);


    //Repartition
    @PostMapping("/repartition/create")
    ResponseEntity<ServerReponse> createRepartition(@RequestParam("repartition") String repartition) ;
    @PostMapping("/repartition/update")
    ResponseEntity<ServerReponse> updateRepartition(@RequestParam("repartition") String repartition) ;
    @GetMapping("/repartition/delete/{id}")
    ResponseEntity<ServerReponse> deleteRepartition(@PathVariable Integer id);
    @GetMapping("/repartition/all/byenseignant/{id}")
    ResponseEntity<List<Repartition>> findAllRepartitionByEnseignant(@PathVariable Integer id);
    @GetMapping("/repartition/all/byfiliere/{id}")
    ResponseEntity<List<Repartition>> findAllRepartitionByFiliere(@PathVariable Integer id);
    @GetMapping("/repartition/all/bymatiere/{id}")
    ResponseEntity<List<Repartition>> findAllRepartitionByMatiere(@PathVariable Integer id);
    @GetMapping("/repartition/all/bysemestre/{id}")
    ResponseEntity<List<Repartition>> findAllRepartitionBySemestre(@PathVariable Integer id);
    @GetMapping("/repartition/all/by/filiere/niveau/{idF}/{idN}")
    ResponseEntity<List<Repartition>> findAllRepartitionByFiliereAndNiveau(@PathVariable Integer idF, @PathVariable Integer idN);
    @GetMapping("/repartition/all/by/filiere/niveau/semestre/{idF}/{idN}/{idS}")
    ResponseEntity<List<Repartition>> findAllRepartitionByFiliereAndNiveauAndSemestre(@PathVariable Integer idF, @PathVariable Integer idN, @PathVariable Integer idS);





}
