package com.example.DeptManager.CONTROLLER.Horaire;

import com.example.DeptManager.DTO.HoraireDTO;
import com.example.DeptManager.ENTITY.Horaire.Horaire;
import com.example.DeptManager.ENTITY.Horaire.Jour;
import com.example.DeptManager.ENTITY.Horaire.Periode;
import com.example.DeptManager.ENTITY.Server.ServerReponse;
import com.example.DeptManager.ENTITY.Structure.Filiere;
import com.example.DeptManager.REPOSITORY.Horaire.HoraireRepository;
import com.example.DeptManager.REPOSITORY.Horaire.JourRepository;
import com.example.DeptManager.REPOSITORY.Horaire.PeriodeRepository;
import com.example.DeptManager.REPOSITORY.Scolarite.MatiereRepository;
import com.example.DeptManager.REPOSITORY.Structure.FiliereRepository;
import com.example.DeptManager.REPOSITORY.Structure.NiveauRepository;
import com.example.DeptManager.REPOSITORY.Structure.SalleRepository;
import com.example.DeptManager.REPOSITORY.Utilisateur.EnseignantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Objects;

@Controller
public class HoraireControllerImpl implements HoraireControllerInt {
    @Autowired
    private HoraireRepository horaireRepository;
    @Autowired
    private EnseignantRepository enseignantRepository;
    @Autowired
    private MatiereRepository matiereRepository;
    @Autowired
    private FiliereRepository filiereRepository;
    @Autowired
    private NiveauRepository niveauRepository;
    @Autowired
    private SalleRepository salleRepository;
    @Autowired
    private JourRepository jourRepository;
    @Autowired
    private PeriodeRepository periodeRepository;

    @Override
    public ResponseEntity<List<Jour>> getAllJour() {
        return ResponseEntity.ok(
                this.jourRepository.findAll()
        );
    }

    @Override
    public ResponseEntity<List<Periode>> getAllPeriode() {
        return ResponseEntity.ok(
                this.periodeRepository.findAll()
        );
    }

    @Override
    public ResponseEntity<ServerReponse> creationHoraire(String horaire) {
        HoraireDTO horaireDTO = new ObjectMapper().readValue(horaire, HoraireDTO.class);

        Horaire horaireDB = new Horaire();

        horaireDB.setFiliere(this.filiereRepository.findById(horaireDTO.getFiliere()).orElse(null));
        horaireDB.setNiveau(this.niveauRepository.findById(horaireDTO.getNiveau()).orElse(null));
        horaireDB.setEnseignant(this.enseignantRepository.findById(horaireDTO.getEnseignant()).orElse(null));
        horaireDB.setJour(this.jourRepository.findById(horaireDTO.getJour()).orElse(null));
        horaireDB.setMatiere(this.matiereRepository.findById(horaireDTO.getMatiere()).orElse(null));
        horaireDB.setPeriode(this.periodeRepository.findById(horaireDTO.getPeriode()).orElse(null));
        horaireDB.setSalle(this.salleRepository.findById(horaireDTO.getSalle()).orElse(null));

        this.horaireRepository.save(horaireDB);

        return ResponseEntity.ok(new ServerReponse("Creation horaire creee",true));
    }

    @Override
    public ResponseEntity<ServerReponse> updateHoraire(String horaire) {
        HoraireDTO horaireDTO = new ObjectMapper().readValue(horaire, HoraireDTO.class);
        Horaire horaireToU = this.horaireRepository.findById(horaireDTO.getId()).orElse(null);

        if (Objects.nonNull(horaireToU)){
            Horaire horaireDB = new Horaire();

            horaireDB.setId(horaireToU.getId());
            horaireDB.setFiliere(this.filiereRepository.findById(horaireDTO.getFiliere()).orElse(null));
            horaireDB.setNiveau(this.niveauRepository.findById(horaireDTO.getId()).orElse(null));
            horaireDB.setJour(this.jourRepository.findById(horaireDTO.getJour()).orElse(null));
            horaireDB.setEnseignant(this.enseignantRepository.findById(horaireDTO.getEnseignant()).orElse(null));
            horaireDB.setMatiere(this.matiereRepository.findById(horaireDTO.getMatiere()).orElse(null));
            horaireDB.setPeriode(this.periodeRepository.findById(horaireDTO.getPeriode()).orElse(null));
            horaireDB.setSalle(this.salleRepository.findById(horaireDTO.getSalle()).orElse(null));

            this.horaireRepository.save(horaireDB);

            return ResponseEntity.ok(new ServerReponse("Creation horaire creee",true));

        }else{
            System.out.println("erreur de mise a jour");
        }

        return ResponseEntity.ok(new ServerReponse("Erreur mise de jour ",true));
    }

    @Override
    public ResponseEntity<List<Horaire>> findAllHoraireByFiliereAndNiveau(Integer idF, Integer idN) {
        return ResponseEntity.ok(this.horaireRepository.findByFiliereAndNiveau(
                this.filiereRepository.findById(idF).orElse(null),
                this.niveauRepository.findById(idN).orElse(null)
        ));
    }

    @Override
    public ResponseEntity<List<Horaire>> findAllHoraireByEnseignant(Integer id) {
        return ResponseEntity.ok(this.horaireRepository.findByEnseignant(
                this.enseignantRepository.findById(id).orElse(null)
        ));
    }
}

