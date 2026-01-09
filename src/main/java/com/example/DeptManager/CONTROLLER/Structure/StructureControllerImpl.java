package com.example.DeptManager.CONTROLLER.Structure;

import com.example.DeptManager.DTO.FiliereDTO;
import com.example.DeptManager.ENTITY.Server.ServerReponse;
import com.example.DeptManager.ENTITY.Structure.Departement;
import com.example.DeptManager.ENTITY.Structure.Filiere;
import com.example.DeptManager.ENTITY.Structure.Niveau;
import com.example.DeptManager.ENTITY.Structure.Salle;
import com.example.DeptManager.REPOSITORY.Structure.DepartementRepository;
import com.example.DeptManager.REPOSITORY.Structure.FiliereRepository;
import com.example.DeptManager.REPOSITORY.Structure.NiveauRepository;
import com.example.DeptManager.REPOSITORY.Structure.SalleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Objects;

@Controller
public class StructureControllerImpl implements StructureControllerInt{
    @Autowired
    private DepartementRepository departementRepository;
    @Autowired
    private FiliereRepository filiereRepository;
    @Autowired
    private NiveauRepository niveauRepository;
    @Autowired
    private SalleRepository salleRepository;

    @Override
    public ResponseEntity<List<Departement>> findAllDepartement() {
        return ResponseEntity.ok(this.departementRepository.findAll());
    }

    @Override
    public ResponseEntity<ServerReponse> createDepartement(String departement) {
        Departement departementToSave = new ObjectMapper().readValue(departement,Departement.class);
        this.departementRepository.save(departementToSave);
        return ResponseEntity.ok(new ServerReponse("Un nouveau departement a ete cree",true));
    }

    @Override
    public ResponseEntity<ServerReponse> updateDepartement(String departement) {
        Departement departementD = new ObjectMapper().readValue(departement,Departement.class);
        Departement departementToU = this.departementRepository.findById(departementD.getId()).orElse(null);

        if (Objects.nonNull(departementToU)){

            departementD.setId(departementToU.getId());

            System.out.println("objet departement up: "+ departementD.toString());

            return ResponseEntity.ok(new ServerReponse("Un nouveau departement a ete cree",true));

        }else{

            System.out.println("aucun departement trouve pour cet id");
            return ResponseEntity.ok(new ServerReponse("Erreur de mise a jour ",false));

        }

    }

    @Override
    public ResponseEntity<ServerReponse> deleteDepartement(Integer id) {
        return null;
    }

    @Override
    public ResponseEntity<List<Filiere>> findAllFiliere() {
        return ResponseEntity.ok(this.filiereRepository.findAll());
    }

    @Override
    public ResponseEntity<List<Filiere>> findAllFiliereByDepartement(Integer id) {
        return ResponseEntity.ok(
                this.filiereRepository.findAllByDepartement(
                        this.departementRepository.findById(id).orElse(null)
                )
        );
    }

    @Override
    public ResponseEntity<ServerReponse> createFiliere(String filiere) {
        FiliereDTO filiereDTO = new ObjectMapper().readValue(filiere, FiliereDTO.class);
        Filiere filiereDB = new Filiere();

        filiereDB.setIntitule(filiereDTO.getIntitule());
        filiereDB.setAbreviation(filiereDTO.getAbreviation());
        filiereDB.setDescription(filiereDTO.getDescription());
        filiereDB.setDepartement(this.departementRepository.findById(filiereDTO.getDepartement()).orElse(null));

        this.filiereRepository.save(filiereDB);

        return ResponseEntity.ok(new ServerReponse("Creation de la filiere", true));
    }

    @Override
    public ResponseEntity<ServerReponse> updateFiliere(String filiere) {
        FiliereDTO filiereDTO = new ObjectMapper().readValue(filiere, FiliereDTO.class);
        Filiere filiereDB = new Filiere();
        Filiere filiereU = this.filiereRepository.findById(filiereDTO.getId()).orElse(null);

        if (Objects.nonNull(filiereU)){

            filiereDB.setId(filiereU.getId());
            filiereDB.setIntitule(filiereDTO.getIntitule());
            filiereDB.setDescription(filiereDTO.getDescription());
            filiereDB.setDepartement(this.departementRepository.findById(filiereDTO.getDepartement()).orElse(null));

            this.filiereRepository.save(filiereDB);

            return ResponseEntity.ok(new ServerReponse("Creation de la filiere", true));

        }else{
            System.out.println("Aucune filire avec cet id");

            return ResponseEntity.ok(new ServerReponse("Erreur de mise a jour de la filiere", true));

        }


    }

    @Override
    public ResponseEntity<ServerReponse> deleteFiliere(Integer id) {
        return null;
    }

    @Override
    public ResponseEntity<List<Niveau>> findAllNiveau() {
        return ResponseEntity.ok(this.niveauRepository.findAll());
    }

    @Override
    public ResponseEntity<ServerReponse> createNiveau(String niveau) {
        Niveau niveauDB = new ObjectMapper().readValue(niveau , Niveau.class);
        this.niveauRepository.save(niveauDB);
        return ResponseEntity.ok(new ServerReponse("Niveau creee",true));
    }

    @Override
    public ResponseEntity<ServerReponse> updateNiveau(String niveau) {
        return null;
    }

    @Override
    public ResponseEntity<ServerReponse> deleteNiveau(Integer id) {
        return null;
    }

    @Override
    public ResponseEntity<List<Salle>> findAllSalle() {
        return ResponseEntity.ok(
                this.salleRepository.findAll(Sort.by(Sort.Direction.DESC,"id"))
        );
    }

    @Override
    public ResponseEntity<ServerReponse> createSallle(String salle) {
        Salle salleDB = new ObjectMapper().readValue(salle, Salle.class);
        this.salleRepository.save(salleDB);
        return ResponseEntity.ok(new ServerReponse("Creation salle ", true));
    }

    @Override
    public ResponseEntity<ServerReponse> updateSalle(String salle) {
        return null;
    }

    @Override
    public ResponseEntity<ServerReponse> deleteSalle(Integer id) {
        this.salleRepository.deleteById(id);
        return ResponseEntity.ok(new ServerReponse("Salle supprime", true));
    }
}
