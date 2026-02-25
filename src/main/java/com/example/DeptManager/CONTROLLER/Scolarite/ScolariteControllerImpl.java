package com.example.DeptManager.CONTROLLER.Scolarite;

import com.example.DeptManager.DTO.DocumentDTO;
import com.example.DeptManager.DTO.MatiereDTO;
import com.example.DeptManager.ENTITY.Scolarite.AnneeAcademique;
import com.example.DeptManager.ENTITY.Scolarite.Documentation.Document;
import com.example.DeptManager.ENTITY.Scolarite.Matiere;
import com.example.DeptManager.ENTITY.Scolarite.Repartition;
import com.example.DeptManager.ENTITY.Server.ServerReponse;
import com.example.DeptManager.ENTITY.Structure.Filiere;
import com.example.DeptManager.REPOSITORY.Scolarite.*;
import com.example.DeptManager.REPOSITORY.Structure.DepartementRepository;
import com.example.DeptManager.REPOSITORY.Structure.FiliereRepository;
import com.example.DeptManager.REPOSITORY.Structure.NiveauRepository;
import com.example.DeptManager.REPOSITORY.Utilisateur.EnseignantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Controller
public class ScolariteControllerImpl implements ScolariteControllerInt {
    @Autowired
    private MatiereRepository matiereRepository;
    @Autowired
    private DepartementRepository departementRepository;
    @Autowired
    private FiliereRepository filiereRepository;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private TypeDocumentRepository typeDocumentRepository;
    @Autowired
    private AnneeAcademiqueRepository anneeAcademiqueRepository;
    @Autowired
    private NiveauRepository niveauRepository;
    @Autowired
    private EnseignantRepository enseignantRepository;
    @Autowired
    private RepartitionRepository repartitionRepository;
    @Autowired
    private SemestreRepository semestreRepository;


    private static String folderFile = System.getProperty("user.dir")+"/src/main/resources/templates/deptwebapp/public/assets/file"; //chemin a déinir

    @Override
    public ResponseEntity<List<Matiere>> findAllMatiere() {
        return ResponseEntity.ok(this.matiereRepository.findAll());
    }

    @Override
    public ResponseEntity<List<Matiere>> findAllMatiereByDepartement(Integer id) {
        return ResponseEntity.ok(
                this.matiereRepository.findByDepartement(
                        this.departementRepository.findById(id).orElse(null)
                )
        );
    }

    @Override
    public ResponseEntity<List<Matiere>> findAllMatiereByFiliere(Integer id) {
        Filiere filiere = this.filiereRepository.findById(id).orElse(null);
        return ResponseEntity.ok(
                this.matiereRepository.findByDepartement(
                        filiere.getDepartement()
                )
        );
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

    @Override
    public ResponseEntity<List<Document>> findAllDocument() {
        return ResponseEntity.ok(this.documentRepository.findAll(Sort.by(Sort.Direction.DESC, "id")));
    }

    @Override
    public ResponseEntity<List<Document>> findAllDocumentByMatiere(Integer id) {
        return ResponseEntity.ok(
                this.documentRepository.findByMatiere(
                        this.matiereRepository.findById(id).orElse(null)
                )
        );
    }

    @Override
    public ResponseEntity<List<Document>> findAllDocumentByTypeDoc(Integer id) {
        return ResponseEntity.ok(
                this.documentRepository.findByTypeDocument(
                        this.typeDocumentRepository.findById(id).orElse(null)
                )
        );
    }

    @Override
    public ResponseEntity<List<Document>> findAllDocumentByDepartement(Integer id) {
        return ResponseEntity.ok(
                this.documentRepository.findByDepartement(
                        this.departementRepository.findById(id).orElse(null)
                )
        );
    }

    @Override
    public ResponseEntity<List<Document>> findAllDocumentByFiliere(Integer id) {
        return ResponseEntity.ok(
                this.documentRepository.findByFiliere(
                        this.filiereRepository.findById(id).orElse(null)
                )
        );
    }

    @Override
    public ResponseEntity<List<Document>> findAllDocumentByNiveau(Integer id) {
        return ResponseEntity.ok(
                this.documentRepository.findByNiveau(
                        this.niveauRepository.findById(id).orElse(null)
                )
        );
    }

    @Override
    public ResponseEntity<List<Document>> findAllDocumentByEnseignant(Integer id) {
        return ResponseEntity.ok(
                this.documentRepository.findByEnseignant(
                        this.enseignantRepository.findById(id).orElse(null)
                )
        );
    }

    @Override
    public ResponseEntity<List<Document>> findAllDocumentByAnnee(Integer id) {
        return ResponseEntity.ok(
                this.documentRepository.findByAnneeAcademique(
                        this.anneeAcademiqueRepository.findById(id).orElse(null)
                )
        );
    }

    @Override
    public ResponseEntity<ServerReponse> createDocumentD(String document, MultipartFile file) throws IOException {
        DocumentDTO documentDTO = new ObjectMapper().readValue(document, DocumentDTO.class);

        Document documentDB = new Document();

        documentDB.setDateC(LocalDate.now());

        documentDB.setTypeDocument(
                this.typeDocumentRepository.findById(documentDTO.getTypeDocument()).orElse(null)
        );

        documentDB.setDepartement(
                this.departementRepository.findById(documentDTO.getDepartement()).orElse(null)
        );

        documentDB.setFiliere(
                this.filiereRepository.findById(documentDTO.getFiliere()).orElse(null)
        );

        documentDB.setNiveau(
                this.niveauRepository.findById(documentDTO.getNiveau()).orElse(null)
        );

        documentDB.setEnseignant(
                this.enseignantRepository.findById(documentDTO.getEnseignant()).orElse(null)
        );

        documentDB.setAnneeAcademique(
                this.anneeAcademiqueRepository.findById(documentDTO.getAnneeAcademique()).orElse(null)
        );

        documentDB.setMatiere(
                this.matiereRepository.findById(documentDTO.getMatiere()).orElse(null)
        );


        String fileName = "";
        if (!file.isEmpty()){
            //S'il n'y a pas de fichier
            fileName = file.getOriginalFilename(); // le fichier prend le nom du client

            documentDB.setUrl(fileName);

            System.out.println("le nom du fichier "+ fileName);

            Path path = Paths.get(folderFile,fileName);

            file.transferTo(path);

            System.out.println("fichier enregistre en base de donnee");
        }

        this.documentRepository.save(documentDB);

        return ResponseEntity.ok(new ServerReponse("Document ajouté avec success",true));
    }

    @Override
    public ResponseEntity<ServerReponse> updateDocumentD(String document , MultipartFile file) throws IOException {
        DocumentDTO documentDTO = new ObjectMapper().readValue(document, DocumentDTO.class);
        Document documentUpdating = this.documentRepository.findById(documentDTO.getId()).orElse(null);

        Document documentDB = new Document();
        if (Objects.nonNull(documentUpdating)){

            documentDB.setId(documentUpdating.getId());
            documentDB.setDateC(documentUpdating.getDateC());
            documentDB.setTypeDocument(
                    this.typeDocumentRepository.findById(documentDTO.getTypeDocument()).orElse(null)
            );

            documentDB.setDepartement(
                    this.departementRepository.findById(documentDTO.getDepartement()).orElse(null)
            );

            documentDB.setFiliere(
                    this.filiereRepository.findById(documentDTO.getFiliere()).orElse(null)
            );

            documentDB.setNiveau(
                    this.niveauRepository.findById(documentDTO.getNiveau()).orElse(null)
            );

            documentDB.setEnseignant(
                    this.enseignantRepository.findById(documentDTO.getEnseignant()).orElse(null)
            );

            documentDB.setAnneeAcademique(
                    this.anneeAcademiqueRepository.findById(documentDTO.getAnneeAcademique()).orElse(null)
            );

            documentDB.setMatiere(
                    this.matiereRepository.findById(documentDTO.getMatiere()).orElse(null)
            );


            String fileName = "";
            if (!file.isEmpty()){
                //S'il n'y a pas de fichier
                fileName = file.getOriginalFilename(); // le fichier prend le nom du client

                documentDB.setUrl(fileName);

                System.out.println("le nom du fichier "+ fileName);

                Path path = Paths.get(folderFile,fileName);

                file.transferTo(path);

                System.out.println("fichier enregistre en base de donnee");
            }

            this.documentRepository.save(documentDB);

            return ResponseEntity.ok(new ServerReponse("Document mis a jour  avec success",true));
        }else {
            return ResponseEntity.ok(new ServerReponse("Erreur de mise a jour du Document",true));

        }
    }

    @Override
    public ResponseEntity<ServerReponse> deleteDocument(Integer id) {
        this.documentRepository.deleteById(id);
        return ResponseEntity.ok(new ServerReponse("Document supprime avec success", true));
    }

    @Override
    public ResponseEntity<List<Repartition>> findAllRepartitionByEnseignant(Integer id) {
        return ResponseEntity.ok(
                this.repartitionRepository.findByEnseignant(
                        this.enseignantRepository.findById(id).orElse(null)
                )
        );
    }

    @Override
    public ResponseEntity<List<Repartition>> findAllRepartitionByFiliere(Integer id) {
        return ResponseEntity.ok(
                this.repartitionRepository.findByFiliere(
                        this.filiereRepository.findById(id).orElse(null)
                )
        );
    }

    @Override
    public ResponseEntity<List<Repartition>> findAllRepartitionByMatiere(Integer id) {
        return ResponseEntity.ok(
                this.repartitionRepository.findByMatiere(
                        this.matiereRepository.findById(id).orElse(null)
                )
        );
    }

    @Override
    public ResponseEntity<List<Repartition>> findAllRepartitionBySemestre(Integer id) {
        return ResponseEntity.ok(
                this.repartitionRepository.findBySemestre(
                        this.semestreRepository.findById(id).orElse(null)
                )
        );
    }

    @Override
    public ResponseEntity<List<Repartition>> findAllRepartitionByFiliereAndNiveau(Integer idF, Integer idN) {
        return ResponseEntity.ok(
                this.repartitionRepository.findByFiliereAndNiveau(
                        this.filiereRepository.findById(idF).orElse(null),
                        this.niveauRepository.findById(idN).orElse(null)
                )
        );
    }

    @Override
    public ResponseEntity<List<Repartition>> findAllRepartitionByFiliereAndNiveauAndSemestre(Integer idF, Integer idN, Integer idS) {
        return ResponseEntity.ok(
                this.repartitionRepository.findByFiliereAndNiveauAndSemestre(
                        this.filiereRepository.findById(idF).orElse(null),
                        this.niveauRepository.findById(idN).orElse(null),
                        this.semestreRepository.findById(idS).orElse(null)
                )
        );
    }


}
