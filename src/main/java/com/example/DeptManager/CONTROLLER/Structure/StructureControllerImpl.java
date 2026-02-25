package com.example.DeptManager.CONTROLLER.Structure;

import com.example.DeptManager.DTO.*;
import com.example.DeptManager.ENTITY.Scolarite.AnneeAcademique;
import com.example.DeptManager.ENTITY.Scolarite.Documentation.TypeDocument;
import com.example.DeptManager.ENTITY.Server.ServerReponse;
import com.example.DeptManager.ENTITY.Structure.*;
import com.example.DeptManager.ENTITY.Utilisateur.Poste;
import com.example.DeptManager.REPOSITORY.Scolarite.AnneeAcademiqueRepository;
import com.example.DeptManager.REPOSITORY.Scolarite.TypeDocumentRepository;
import com.example.DeptManager.REPOSITORY.Structure.*;
import com.example.DeptManager.REPOSITORY.Utilisateur.EtudiantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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
public class StructureControllerImpl implements StructureControllerInt{
    @Autowired
    private DepartementRepository departementRepository;
    @Autowired
    private FiliereRepository filiereRepository;
    @Autowired
    private NiveauRepository niveauRepository;
    @Autowired
    private SalleRepository salleRepository;
    @Autowired
    private PosteRepository posteRepository;
    @Autowired
    private AnneeAcademiqueRepository anneeAcademiqueRepository;
    @Autowired
    private TypeDocumentRepository typeDocumentRepository;
    @Autowired
    private DeboucheRepository deboucheRepository;
    @Autowired
    private MediaRepository mediaRepository;
    @Autowired
    private RequeteRepository requeteRepository;
    @Autowired
    private MotifRequeteRepository motifRequeteRepository;
    @Autowired
    private EtudiantRepository etudiantRepository;
    @Autowired
    private PieceJointeRequeteRepository pieceJointeRequeteRepository;
    @Autowired
    private TypePieceJointeRequeteRepository typePieceJointeRequeteRepository;
    @Autowired
    private StatutRequeteRepository statutRequeteRepository;
    private static String folderFile = System.getProperty("user.dir")+"/src/main/resources/templates/deptwebapp/public/assets/file"; //chemin a déinir


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

    @Override
    public ResponseEntity<List<Poste>> findAllPoste() {
        return ResponseEntity.ok(
                this.posteRepository.findAll()
        );
    }

    @Override
    public ResponseEntity<ServerReponse> createPoste(String poste) {
        return null;
    }

    @Override
    public ResponseEntity<List<AnneeAcademique>> findAllAnneeAcademique() {
        return ResponseEntity.ok(
                this.anneeAcademiqueRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
        );
    }

    @Override
    public ResponseEntity<List<TypeDocument>> findAllTypeDocument() {
        return ResponseEntity.ok(
                this.typeDocumentRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
        );
    }

    @Override
    public ResponseEntity<List<Debouche>> findAllDeboucheByFiliere(Integer id) {
        return ResponseEntity.ok(
                this.deboucheRepository.findByFiliere(
                        this.filiereRepository.findById(id).orElse(null)
                )
        );
    }

    @Override
    public ResponseEntity<ServerReponse> createDebouche(String debouche) {
        DeboucheDTO deboucheDTO = new ObjectMapper().readValue(debouche, DeboucheDTO.class);

        Debouche deboucheDB = new Debouche();

        deboucheDB.setIntitule(deboucheDTO.getIntitule());
        deboucheDB.setDescription(deboucheDTO.getDescription());
        deboucheDB.setFiliere(this.filiereRepository.findById(deboucheDTO.getFiliere()).orElse(null));

        this.deboucheRepository.save(deboucheDB);

        return ResponseEntity.ok(new ServerReponse("Debouche cree avec success", true));
    }

    @Override
    public ResponseEntity<ServerReponse> updateDebouche(String debouche) {
        DeboucheDTO deboucheDTO = new ObjectMapper().readValue(debouche, DeboucheDTO.class);
        Debouche deboucheSaved = this.deboucheRepository.findById(deboucheDTO.getId()).orElse(null);

        if (Objects.nonNull(deboucheSaved)){

            Debouche deboucheDB = new Debouche();
            deboucheDB.setId(deboucheSaved.getId());
            deboucheDB.setIntitule(deboucheDTO.getIntitule());
            deboucheDB.setDescription(deboucheDTO.getDescription());
            deboucheDB.setFiliere(this.filiereRepository.findById(deboucheDTO.getFiliere()).orElse(null));
            this.deboucheRepository.save(deboucheDB);

            return ResponseEntity.ok(new ServerReponse("Debouche mis a jour avec success", true));

        }else{
            return ResponseEntity.ok(new ServerReponse("Erreur de mise a jour des Debouche", true));

        }

    }

    @Override
    public ResponseEntity<ServerReponse> deleteDebouche(Integer id) {
        this.deboucheRepository.deleteById(id);
        return ResponseEntity.ok(new ServerReponse("Debouche supprimé avec success", true));
    }

    @Override
    public ResponseEntity<ServerReponse> createMedia(String media, MultipartFile fichier) throws IOException {
        MediaDTO mediaDTO = new ObjectMapper().readValue(media, MediaDTO.class);

        Media mediaDB = new Media();

        mediaDB.setDepartement(this.departementRepository.findById(mediaDTO.getDepartement()).orElse(null));

        String fileName ;
        if (!fichier.isEmpty()){
            //S'il n'y a pas de fichier
            fileName = fichier.getOriginalFilename(); // le fichier prend le nom du client

            mediaDB.setUrl(fileName);

            System.out.println("le nom du fichier "+ fileName);

            Path path = Paths.get(folderFile,fileName);

            fichier.transferTo(path);

            System.out.println("media enregistre en base de donnee");
        }

        this.mediaRepository.save(mediaDB);

        return ResponseEntity.ok(new ServerReponse("Media enregistré", true));
    }

    @Override
    public ResponseEntity<List<Media>> findAllMediaByDepartement(Integer id) {
        return ResponseEntity.ok(
                this.mediaRepository.findByDepartement(
                        this.departementRepository.findById(id).orElse(null)
                )
        );
    }

    @Override
    public ResponseEntity<ServerReponse> deleteMedia(Integer id) {
        this.mediaRepository.deleteById(id);
        return ResponseEntity.ok(new ServerReponse("Media supprime avec success", true));
    }

    @Override
    public ResponseEntity<List<Requete>> findAllRequete() {
        return ResponseEntity.ok(this.requeteRepository.findAll(Sort.by(Sort.Direction.DESC, "id")));
    }

    @Override
    public ResponseEntity<List<Requete>> findAllRequeteByMotif(Integer id) {
        return ResponseEntity.ok(
                this.requeteRepository.findByMotifRequete(
                        this.motifRequeteRepository.findById(id).orElse(null)
                )
        );
    }

    @Override
    public ResponseEntity<ServerReponse> creationRequete(String requete) {
        RequeteDTO requeteDTO = new ObjectMapper().readValue(requete, RequeteDTO.class);

        Requete requeteDB = new Requete();


        requeteDB.setDateCreation(LocalDate.now());
        requeteDB.setStatutRequete(this.statutRequeteRepository.findById(1).orElse(null)); //1- En attente 2- Traite 3- Rejete
        requeteDB.setDescription(requeteDTO.getDescription());
        requeteDB.setMotifRequete(
                this.motifRequeteRepository.findById(requeteDTO.getMotifRequete()).orElse(null)
        );
        requeteDB.setEtudiant(
                this.etudiantRepository.findById(requeteDTO.getEtudiant()).orElse(null)
        );

        this.requeteRepository.save(requeteDB);
        return ResponseEntity.ok(new ServerReponse("Creation de requete avec success", true));
    }

    @Override
    public ResponseEntity<ServerReponse> updateRequete(String requete) {

        RequeteDTO requeteDTO = new ObjectMapper().readValue(requete, RequeteDTO.class);
        Requete requeteSaved = this.requeteRepository.findById(requeteDTO.getId()).orElse(null);

        if (Objects.nonNull(requeteSaved)){
            Requete requeteDB = new Requete();

            requeteDB.setId(requeteSaved.getId());
            requeteDB.setDescription(requeteDTO.getDescription());
            requeteDB.setMotifRequete(
                    this.motifRequeteRepository.findById(requeteDTO.getMotifRequete()).orElse(null)
            );
            requeteDB.setEtudiant(
                    this.etudiantRepository.findById(requeteDTO.getEtudiant()).orElse(null)
            );

            this.requeteRepository.save(requeteDB);
            return ResponseEntity.ok(new ServerReponse("Mise a jour de requete avec success", true));
        }else{
            return ResponseEntity.ok(new ServerReponse("Echec de Mise a jour de requete", true));

        }
    }

    @Override
    public ResponseEntity<ServerReponse> deleteRequete(Integer id) {
        this.requeteRepository.deleteById(id);
        return ResponseEntity.ok(new ServerReponse("Suppression de requete avec success", true));
    }

    @Override
    public ResponseEntity<ServerReponse> changeRequeteStatut(Integer idR, Integer idS) {
        Requete requeteDB = this.requeteRepository.findById(idR).orElse(null);
        requeteDB.setStatutRequete(this.statutRequeteRepository.findById(idS).orElse(null));
        this.requeteRepository.save(requeteDB);
        return ResponseEntity.ok(new ServerReponse("Changement du statut reussi", true));
    }

    @Override
    public ResponseEntity<List<MotifRequete>> findAllMotifRequete() {
        return ResponseEntity.ok(this.motifRequeteRepository.findAll(Sort.by(Sort.Direction.DESC, "id")));
    }

    @Override
    public ResponseEntity<List<PieceJointeRequete>> findAllPieceByRequete(Integer id) {
        return ResponseEntity.ok(
                this.pieceJointeRequeteRepository.findByRequete(
                        this.requeteRepository.findById(id).orElse(null)
                )
        );
    }

    @Override
    public ResponseEntity<ServerReponse> createPieceJointeRequete(String piece, MultipartFile file) throws IOException {
        PieceJointeRequeteDTO pieceJointeRequeteDTO = new ObjectMapper().readValue(piece, PieceJointeRequeteDTO.class);

        PieceJointeRequete pieceJointeRequete =new PieceJointeRequete();

        pieceJointeRequete.setRequete(this.requeteRepository.findById(pieceJointeRequeteDTO.getRequete()).orElse(null));


        String fileName ;
        if (!file.isEmpty()){
            //S'il n'y a pas de fichier
            fileName = file.getOriginalFilename(); // le fichier prend le nom du client

            pieceJointeRequete.setUrl(fileName);

            System.out.println("le nom du fichier "+ fileName);

            Path path = Paths.get(folderFile,fileName);

            file.transferTo(path);

            System.out.println("piece jointe enregistre en base de donnee");
        }
        return ResponseEntity.ok(new ServerReponse("Piece jointe cree avec success", true));
    }
}
