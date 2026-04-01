package com.example.DeptManager.CONTROLLER.Horaire;

import com.example.DeptManager.DTO.HoraireDTO;
import com.example.DeptManager.ENTITY.Horaire.Horaire;
import com.example.DeptManager.ENTITY.Horaire.Jour;
import com.example.DeptManager.ENTITY.Horaire.Periode;
import com.example.DeptManager.ENTITY.Server.ServerReponse;
import com.example.DeptManager.ENTITY.Structure.Filiere;
import com.example.DeptManager.ENTITY.Structure.Niveau;
import com.example.DeptManager.REPOSITORY.Horaire.HoraireRepository;
import com.example.DeptManager.REPOSITORY.Horaire.JourRepository;
import com.example.DeptManager.REPOSITORY.Horaire.PeriodeRepository;
import com.example.DeptManager.REPOSITORY.Scolarite.MatiereRepository;
import com.example.DeptManager.REPOSITORY.Scolarite.RepartitionRepository;
import com.example.DeptManager.REPOSITORY.Structure.FiliereRepository;
import com.example.DeptManager.REPOSITORY.Structure.NiveauRepository;
import com.example.DeptManager.REPOSITORY.Structure.SalleRepository;
import com.example.DeptManager.REPOSITORY.Utilisateur.EnseignantRepository;
import com.example.DeptManager.UTILS.PlanningPdfGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    @Autowired
    private RepartitionRepository repartitionRepository;

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

        horaireDB.setRepartition(this.repartitionRepository.findById(horaireDTO.getRepartition()).orElse(null));
        horaireDB.setJour(this.jourRepository.findById(horaireDTO.getJour()).orElse(null));
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
            horaireDB.setRepartition(this.repartitionRepository.findById(horaireDTO.getRepartition()).orElse(null));
            horaireDB.setJour(this.jourRepository.findById(horaireDTO.getJour()).orElse(null));
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
    public ResponseEntity<Horaire> findAllHoraireByRepartition(Integer idR) {
        return ResponseEntity.ok(
                this.horaireRepository.findByRepartition(
                        this.repartitionRepository.findById(idR).orElse(null)
                )
        );
    }

    @Override
    public ResponseEntity<Horaire> findAllHoraireBySalleJourAndPeriode(Integer idS, Integer idJ, Integer idP) {
        return ResponseEntity.ok(
                this.horaireRepository.findHoraireBySalleJourAndPeriode(idS, idJ, idP).orElse(null)
        );
    }

    @Override
    public ResponseEntity<List<Horaire>> findAllHoraireByFiliereAndNiveau(Integer idF, Integer idN) {
        return ResponseEntity.ok(
                this.horaireRepository.findByNiveauAndFiliere(
                        this.niveauRepository.findById(idN).orElse(null),
                        this.filiereRepository.findById(idF).orElse(null)
                )
        );
    }


    @Override
    public ResponseEntity<List<Horaire>> findAllHoraireByEnseignant(Integer id) {
        return ResponseEntity.ok(this.horaireRepository.findByEnseignant(
                this.enseignantRepository.findById(id).orElse(null)
        ));
    }

    @Override
    public ResponseEntity<?> impression(Integer idF, Integer idN) throws IOException {

        Niveau niveau = this.niveauRepository.findById(idN).orElse(null);
        Filiere filiere = this.filiereRepository.findById(idF).orElse(null);

        List<Horaire> horaires = this.horaireRepository.findByNiveauAndFiliere(niveau,filiere);

        PlanningPdfGenerator planningPdfGenerator = new PlanningPdfGenerator();
        String nomFichier = planningPdfGenerator.genererPlanning(
                horaires,
                "PLANNING DES COURS DU PREMIER SEMESTRE",
                "Planning des cours du premier semestre pour la filière " + filiere.getIntitule() + " et le niveau " + niveau.getIntitule(),
                "GINFO",
                "G2I 5",
                27
        );
        // Lecture du fichier depuis static/notes/
        Path cheminPdf = Paths.get("src/main/resources/static/planning/" + nomFichier);

        if (!Files.exists(cheminPdf)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ServerReponse("Fichier PDF introuvable après génération", false));
        }

        byte[] contenu = Files.readAllBytes(cheminPdf);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + nomFichier + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(contenu.length)
                .body(contenu);

    }
}

