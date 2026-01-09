package com.example.DeptManager.CONTROLLER.Scolarite;

import com.example.DeptManager.ENTITY.Scolarite.Matiere;
import com.example.DeptManager.ENTITY.Server.ServerReponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/deptmanager/api/scolarite/")
@CrossOrigin("*")
public interface ScolariteControllerInt {
    //Matiere
    @GetMapping("/matiere/all")
    ResponseEntity<List<Matiere>> findAllMatiere();
    @PostMapping("/matiere/create")
    ResponseEntity<ServerReponse> createMatiere(@RequestParam("matiere") String matiere);
    @PostMapping("/matiere/update")
    ResponseEntity<ServerReponse> updateMatiere(@RequestParam("matiere") String matiere);
    @GetMapping("/matiere/delete/{id}")
    ResponseEntity<ServerReponse> deleteMatiere(@PathVariable Integer id);



}
