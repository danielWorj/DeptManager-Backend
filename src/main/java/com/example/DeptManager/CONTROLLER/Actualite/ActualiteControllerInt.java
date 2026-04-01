package com.example.DeptManager.CONTROLLER.Actualite;

import com.example.DeptManager.ENTITY.Actualite.Actualite;
import com.example.DeptManager.ENTITY.Actualite.CategorieActualite;
import com.example.DeptManager.ENTITY.Server.ServerReponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequestMapping("/deptmanager/api/actualite/")
@CrossOrigin("*")
public interface ActualiteControllerInt {
    @GetMapping("/all")
    ResponseEntity<List<Actualite>> getAllActualite();
    @GetMapping("/last03")
    ResponseEntity<List<Actualite>> getLast03Actualite();
    @PostMapping("create")
    ResponseEntity<ServerReponse> createActualite(@RequestParam("actualite") String actualite, @RequestParam("file")MultipartFile file) throws IOException;
    @PostMapping("update")
    ResponseEntity<ServerReponse> updateActualite(@RequestParam("actualite") String actualite);
    @GetMapping("delete/{id}")
    ResponseEntity<ServerReponse> deleteActualite(@PathVariable Integer id);

    //CATEGORIE ACTUALITE

    @GetMapping("/categorie/all")
    ResponseEntity<List<CategorieActualite>> getAllCategorieActualite();
    @PostMapping("/categorie/create")
    ResponseEntity<ServerReponse> createCategorieActualite(@RequestParam("categorie") String categorie);
    @PostMapping("/categorie/update")
    ResponseEntity<ServerReponse> updateCategorieActualite(@RequestParam("categorie") String categorie);
    @GetMapping("/categorie/delete/{id}")
    ResponseEntity<ServerReponse> deleteCategorieActualite(@PathVariable Integer id);

}
