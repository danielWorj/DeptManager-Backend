package com.example.DeptManager.CONTROLLER.Horaire;

import com.example.DeptManager.ENTITY.Horaire.Horaire;
import com.example.DeptManager.ENTITY.Horaire.Jour;
import com.example.DeptManager.ENTITY.Horaire.Periode;
import com.example.DeptManager.ENTITY.Server.ServerReponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequestMapping("/deptmanager/api/horaire/")
@CrossOrigin("*")
public interface HoraireControllerInt {
    //Jour
    @GetMapping("/jour/all")
    ResponseEntity<List<Jour>> getAllJour();

    //Periode
    @GetMapping("/periode/all")
    ResponseEntity<List<Periode>> getAllPeriode();
    //Horaire
    @GetMapping("/all")
    ResponseEntity<List<Horaire>> getAllHoraire();
    @PostMapping("/create")
    ResponseEntity<ServerReponse> creationHoraire(@RequestParam("horaire") String horaire);
    @PostMapping("/update")
    ResponseEntity<ServerReponse> updateHoraire(@RequestParam("horaire") String horaire);
    @GetMapping("/all/byrepartition/{idR}")
    ResponseEntity<Horaire> findAllHoraireByRepartition(@PathVariable Integer idR);
    @GetMapping("/all/bysalle/jour/periode/{idS}/{idJ}/{idP}")
    ResponseEntity<Horaire> findAllHoraireBySalleJourAndPeriode(@PathVariable Integer idS , @PathVariable Integer idJ, @PathVariable Integer idP);
    @GetMapping("/all/byfiliere/andniveau/{idF}/{idN}")
    ResponseEntity<List<Horaire>> findAllHoraireByFiliereAndNiveau(@PathVariable Integer idF , @PathVariable Integer idN);
    @GetMapping("/all/byenseignant/{id}")
    ResponseEntity<List<Horaire>> findAllHoraireByEnseignant(@PathVariable Integer id);

    //Generation
    @GetMapping("/impression/{idF}/{idN}")
    ResponseEntity<?> impression(@PathVariable Integer idF, @PathVariable Integer idN) throws IOException;


}
