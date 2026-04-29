package com.example.DeptManager.CONTROLLER.Actualite;

import com.example.DeptManager.DTO.ActualiteDTO;
import com.example.DeptManager.ENTITY.Actualite.Actualite;
import com.example.DeptManager.ENTITY.Actualite.CategorieActualite;
import com.example.DeptManager.ENTITY.Server.ServerReponse;
import com.example.DeptManager.REPOSITORY.Actualite.ActualiteRepository;
import com.example.DeptManager.REPOSITORY.Actualite.CategorieActualiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Controller
public class ActualiteControllerImpl implements  ActualiteControllerInt{
    @Autowired
    private ActualiteRepository actualiteRepository;
    @Autowired
    private CategorieActualiteRepository categorieActualiteRepository;

    private static String folderFile = System.getProperty("user.dir")+"/src/main/resources/templates/deptwebapp/public/assets/file"; //chemin a déinir


    @Override
    public ResponseEntity<List<Actualite>> getAllActualite() {
        return ResponseEntity.ok(
                this.actualiteRepository.findAllActualiteOrderByIdDesc()
        );
    }

    @Override
    public ResponseEntity<List<Actualite>> getLast03Actualite() {
        return ResponseEntity.ok(
                this.actualiteRepository.findTheLastNews()
        );
    }

    @Override
    public ResponseEntity<ServerReponse> createActualite(String actualite, MultipartFile file) throws IOException {
        ActualiteDTO actualiteDTO = new ObjectMapper().readValue(actualite, ActualiteDTO.class);

        Actualite actualiteDB = new Actualite();

        actualiteDB.setTitre(actualiteDTO.getTitre());
        actualiteDB.setDescription(actualiteDTO.getDescription());
        actualiteDB.setDatePublication(LocalDate.now());
        actualiteDB.setCategorieActualite(
                this.categorieActualiteRepository.findById(actualiteDTO.getCategorieActualite()).orElse(null)
        );

        String fileName ;
        if (!file.isEmpty()){
            //S'il n'y a pas de fichier
            fileName = file.getOriginalFilename(); // le fichier prend le nom du client

            actualiteDB.setUrl(fileName);

            System.out.println("le nom du fichier "+ fileName);

            Path path = Paths.get(folderFile,fileName);

            file.transferTo(path);

            System.out.println("media enregistre en base de donnee");
        }

        this.actualiteRepository.save(actualiteDB);
        return ResponseEntity.ok(new ServerReponse("Actualite cree avec success", true));
    }

    @Override
    public ResponseEntity<ServerReponse> updateActualite(String actualite) {
        ActualiteDTO actualiteDTO = new ObjectMapper().readValue(actualite, ActualiteDTO.class);
        Actualite actualiteToUpdating = this.actualiteRepository.findById(actualiteDTO.getId()).orElse(null);

        if (Objects.nonNull(actualiteToUpdating)){
            Actualite actualiteDB = new Actualite();

            actualiteDB.setId(actualiteToUpdating.getId());
            actualiteDB.setTitre(actualiteDTO.getTitre());
            actualiteDB.setDescription(actualiteDTO.getDescription());
            actualiteDB.setDatePublication(LocalDate.now());
            actualiteDB.setCategorieActualite(
                    this.categorieActualiteRepository.findById(actualiteDTO.getCategorieActualite()).orElse(null)
            );

            this.actualiteRepository.save(actualiteDB);
            return ResponseEntity.ok(new ServerReponse("Actualite mis a jour avec success", true));
        }else{
            return ResponseEntity.ok(new ServerReponse("Erreur de mise a jour  ", true));

        }

    }

    @Override
    public ResponseEntity<ServerReponse> deleteActualite(Integer id) {
        this.actualiteRepository.deleteById(id);
        return ResponseEntity.ok(new ServerReponse("Actualite a ete supprime", true));
    }

    @Override
    public ResponseEntity<List<CategorieActualite>> getAllCategorieActualite() {
        return ResponseEntity.ok(
                this.categorieActualiteRepository.findAll()
        );
    }

    @Override
    public ResponseEntity<ServerReponse> createCategorieActualite(String categorie) {
        CategorieActualite categorieActualite = new ObjectMapper().readValue(categorie,CategorieActualite.class);
        this.categorieActualiteRepository.save(categorieActualite);
        return ResponseEntity.ok(new ServerReponse("Categorie Actualite creee avec succes", true ));
    }

    @Override
    public ResponseEntity<ServerReponse> updateCategorieActualite(String categorie) {
        CategorieActualite categorieActualite = new ObjectMapper().readValue(categorie,CategorieActualite.class);
        CategorieActualite categorieActualiteUpdating = this.categorieActualiteRepository.findById(categorieActualite.getId()).orElse(null);

        if (Objects.nonNull(categorieActualiteUpdating)){
            this.categorieActualiteRepository.save(categorieActualite);
            return ResponseEntity.ok(new ServerReponse("Categorie Actualite mis a jour avec succes", true ));
        }else{
            return ResponseEntity.ok(new ServerReponse("Erreur  mis a jour ", true ));
        }

    }

    @Override
    public ResponseEntity<ServerReponse> deleteCategorieActualite(Integer id) {
        this.categorieActualiteRepository.deleteById(id);
        return ResponseEntity.ok(new ServerReponse("Categorie Actualite creee avec succes", true ));
    }
}
