package com.example.DeptManager.CONTROLLER.Scolarite;

import com.example.DeptManager.DTO.MatiereDTO;
import com.example.DeptManager.ENTITY.Scolarite.Matiere;
import com.example.DeptManager.ENTITY.Server.ServerReponse;
import com.example.DeptManager.REPOSITORY.Scolarite.MatiereRepository;
import com.example.DeptManager.REPOSITORY.Structure.DepartementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@CrossOrigin("*")
public class ScolariteControllerImpl implements ScolariteControllerInt {
    @Autowired
    private MatiereRepository matiereRepository;
    @Autowired
    private DepartementRepository departementRepository;

    @Override
    public ResponseEntity<List<Matiere>> findAllMatiere() {
        return ResponseEntity.ok(this.matiereRepository.findAll());
    }

    @Override
    public ResponseEntity<ServerReponse> createMatiere(String matiere) {
        MatiereDTO matiereDTO = new ObjectMapper().readValue(matiere, MatiereDTO.class);

        Matiere matiereDB = new Matiere();

        matiereDB.setDepartement(this.departementRepository.findById(matiereDTO.getDepartement()).orElse(null));
        matiereDB.setIntitule(matiereDTO.getIntitule());

        return ResponseEntity.ok(new ServerReponse("Creation de la matiere", true));
    }

    @Override
    public ResponseEntity<ServerReponse> updateMatiere(String matiere) {
        return null;
    }

    @Override
    public ResponseEntity<ServerReponse> deleteMatiere(Integer id) {
        this.matiereRepository.deleteById(id);
        return ResponseEntity.ok(new ServerReponse("suppression de la matiere", true));
    }
}
