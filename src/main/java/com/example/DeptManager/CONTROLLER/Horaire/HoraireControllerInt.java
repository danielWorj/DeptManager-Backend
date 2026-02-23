package com.example.DeptManager.CONTROLLER.Horaire;

import com.example.DeptManager.ENTITY.Horaire.Horaire;
import com.example.DeptManager.ENTITY.Horaire.Jour;
import com.example.DeptManager.ENTITY.Horaire.Periode;
import com.example.DeptManager.ENTITY.Server.ServerReponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/create")
    ResponseEntity<ServerReponse> creationHoraire(@RequestParam("horaire") String horaire);
    @PostMapping("/update")
    ResponseEntity<ServerReponse> updateHoraire(@RequestParam("horaire") String horaire);
    @GetMapping("/all/byfiliere/andniveau/{idF}/{idN}")
    ResponseEntity<List<Horaire>> findAllHoraireByFiliereAndNiveau(@PathVariable Integer idF , @PathVariable Integer idN);
    @GetMapping("/all/byenseignant/{id}")
    ResponseEntity<List<Horaire>> findAllHoraireByEnseignant(@PathVariable Integer id);


}
