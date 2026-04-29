package com.example.DeptManager.CONTROLLER.Utilisateur;

import com.example.DeptManager.DTO.ChefDeptDTO;
import com.example.DeptManager.DTO.EnseignantDTO;
import com.example.DeptManager.DTO.EtudiantDTO;
import com.example.DeptManager.ENTITY.Server.ServerReponse;
import com.example.DeptManager.ENTITY.Structure.Departement;
import com.example.DeptManager.ENTITY.Utilisateur.ChefDepartement;
import com.example.DeptManager.ENTITY.Utilisateur.Enseignant;
import com.example.DeptManager.ENTITY.Utilisateur.Etudiant;
import com.example.DeptManager.REPOSITORY.Scolarite.AnneeAcademiqueRepository;
import com.example.DeptManager.REPOSITORY.Structure.*;
import com.example.DeptManager.REPOSITORY.Utilisateur.EnseignantRepository;
import com.example.DeptManager.REPOSITORY.Utilisateur.EtudiantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Controller
public class UtilisateurControllerImpl implements UtilisateurControllerInt{
    @Autowired
    private EnseignantRepository enseignantRepository;
    @Autowired
    private EtudiantRepository etudiantRepository;
    @Autowired
    private DepartementRepository departementRepository;
    @Autowired
    private FiliereRepository filiereRepository;
    @Autowired
    private NiveauRepository niveauRepository;
    @Autowired
    private PosteRepository posteRepository;
    @Autowired
    private AnneeAcademiqueRepository anneeAcademiqueRepository;
    @Autowired
    private ChefDepartementRepository chefDepartementRepository;


    @Override
    public ResponseEntity<List<Enseignant>> findAllEnseignant() {
        return ResponseEntity.ok(
                this.enseignantRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
        );
    }

    @Override
    public ResponseEntity<Enseignant> findEnseignantById(Integer id) {
        return ResponseEntity.ok(
                this.enseignantRepository.findById(id).orElse(null)
        );
    }

    @Override
    public ResponseEntity<Long> countAllEnseignant() {
        return ResponseEntity.ok(
                this.enseignantRepository.count()
        );
    }

    @Override
    public ResponseEntity<Integer> countAllEnseignantByDepartement(Integer id) {
        return ResponseEntity.ok(
                this.enseignantRepository.findByDepartement(
                        this.departementRepository.findById(id).orElse(null)
                ).size()
        );
    }



    @Override
    public ResponseEntity<ServerReponse> createEnseignant(String enseignant) {
        EnseignantDTO enseignantDTO = new ObjectMapper().readValue(enseignant, EnseignantDTO.class);

        Enseignant enseignantDB = new Enseignant();

        enseignantDB.setDepartement(this.departementRepository.findById(enseignantDTO.getDepartement()).orElse(null));
        enseignantDB.setPoste(this.posteRepository.findById(enseignantDTO.getPoste()).orElse(null));

        enseignantDB.setNom(enseignantDTO.getNom());
        enseignantDB.setPrenom(enseignantDTO.getPrenom());
        enseignantDB.setEmail(enseignantDTO.getEmail());
        enseignantDB.setTelephone(enseignantDTO.getTelephone());
        enseignantDB.setRole(1);
        enseignantDB.setStatus(false);
        enseignantDB.setDateCreation(LocalDate.now());

        this.enseignantRepository.save(enseignantDB);
        return ResponseEntity.ok(new ServerReponse("Enseignant cree avec success", true));
    }

    @Override
    public ResponseEntity<ServerReponse> updateEnseignant(String enseignant) {
        EnseignantDTO enseignantDTO = new ObjectMapper().readValue(enseignant, EnseignantDTO.class);
        Enseignant enseignantDB1 = this.enseignantRepository.findById(enseignantDTO.getId()).orElse(null);

        if (Objects.nonNull(enseignantDB1)){
            Enseignant enseignantDB = new Enseignant();

            enseignantDB.setDepartement(this.departementRepository.findById(enseignantDTO.getDepartement()).orElse(null));
            enseignantDB.setPoste(this.posteRepository.findById(enseignantDTO.getPoste()).orElse(null));

            enseignantDB.setId(enseignantDB1.getId());
            enseignantDB.setNom(enseignantDTO.getNom());
            enseignantDB.setPrenom(enseignantDTO.getPrenom());
            enseignantDB.setTelephone(enseignantDTO.getTelephone());
            enseignantDB.setEmail(enseignantDTO.getEmail());
            enseignantDB.setRole(1);
            enseignantDB.setStatus(false);
            enseignantDB.setDateCreation(LocalDate.now());

            this.enseignantRepository.save(enseignantDB);

            return ResponseEntity.ok(new ServerReponse("Enseignant mis a jour avec success", true));

        }else {
            System.out.println("Erreur de mise a jour");
            return ResponseEntity.ok(new ServerReponse("Erreur de mise a jour de l'enseignant", true));

        }


    }

    @Override
    public ResponseEntity<List<Enseignant>> findAllEnseignantByDepartement(Integer id) {
        return ResponseEntity.ok(
                this.enseignantRepository.findByDepartement(
                        this.departementRepository.findById(id).orElse(null)
                )
        );
    }

    @Override
    public ResponseEntity<List<Etudiant>> findAllEtudiant() {
        return ResponseEntity.ok(
                this.etudiantRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
        );
    }

    @Override
    public ResponseEntity<Etudiant> findEtudiantById(Integer id) {
        return ResponseEntity.ok(
                this.etudiantRepository.findById(id).orElse(null)
        );
    }

    @Override
    public ResponseEntity<Long> countAllEtudiant() {
        return ResponseEntity.ok(
                this.etudiantRepository.count()
        );
    }

    @Override
    public ResponseEntity<Integer> countAllEtudiantByDepartement(Integer id) {
        return ResponseEntity.ok(
                this.etudiantRepository.findAllEtudiantByDepartement(id).size()
        );
    }

    @Override
    public ResponseEntity<Integer> countAllEtudiantByFiliere(Integer id) {
        return ResponseEntity.ok(
                this.etudiantRepository.findByFiliere(
                        this.filiereRepository.findById(id).orElse(null)
                ).size()
        );
    }

    @Override
    public ResponseEntity<ServerReponse> createEtudiant(String etudiant) {
        EtudiantDTO etudiantDTO = new ObjectMapper().readValue(etudiant, EtudiantDTO.class);

        Etudiant etudiantDB = new Etudiant();

        etudiantDB.setNom(etudiantDTO.getNom());
        etudiantDB.setPrenom(etudiantDTO.getPrenom());
        etudiantDB.setEmail(etudiantDTO.getEmail());
        etudiantDB.setTelephone(etudiantDTO.getTelephone());
        etudiantDB.setMatricule(etudiantDTO.getMatricule());
        etudiantDB.setAnneeAcademique(this.anneeAcademiqueRepository.findByStatusIsTrue().orElse(null));
        etudiantDB.setFiliere(this.filiereRepository.findById(etudiantDTO.getFiliere()).orElse(null));
        etudiantDB.setNiveau(this.niveauRepository.findById(etudiantDTO.getNiveau()).orElse(null));
        etudiantDB.setDateCreation(LocalDate.now());
        etudiantDB.setRole(2);
        etudiantDB.setStatus(false);

        this.etudiantRepository.save(etudiantDB);

        return ResponseEntity.ok(new ServerReponse("Enseignant cree avec success", true));

    }

    @Override
    public ResponseEntity<ServerReponse> updateEtudiant(String etudiant) {
        EtudiantDTO etudiantDTO = new ObjectMapper().readValue(etudiant, EtudiantDTO.class);
        Etudiant etudiantDB = this.etudiantRepository.findById(etudiantDTO.getId()).orElse(null);

        if (Objects.nonNull(etudiantDB)){
            etudiantDB.setNom(etudiantDTO.getNom());
            etudiantDB.setPrenom(etudiantDTO.getPrenom());
            etudiantDB.setEmail(etudiantDTO.getEmail());
            etudiantDB.setMatricule(etudiantDTO.getMatricule());
            etudiantDB.setFiliere(this.filiereRepository.findById(etudiantDTO.getFiliere()).orElse(null));
            etudiantDB.setNiveau(this.niveauRepository.findById(etudiantDTO.getNiveau()).orElse(null));
            etudiantDB.setDateCreation(LocalDate.now());
            etudiantDB.setRole(2);
            etudiantDB.setStatus(false);

            this.etudiantRepository.save(etudiantDB);

            return ResponseEntity.ok(new ServerReponse("Enseignant cree avec success", true));

        }else {

            System.out.println("Erreur de mise a jour");

            return ResponseEntity.ok(new ServerReponse("Erreur de mise a jour de l'etudiant", true));

        }

    }

    @Override
    public ResponseEntity<List<Etudiant>> findAllEtudiantByDepartement(Integer id) {
        return ResponseEntity.ok(
                this.etudiantRepository.findAllEtudiantByDepartement(id)
        );
    }

    @Override
    public ResponseEntity<List<Etudiant>> findAllEtudiantByNiveau(Integer id) {
        return ResponseEntity.ok(
                this.etudiantRepository.findByNiveau(
                        this.niveauRepository.findById(id).orElse(null)
                )
        );
    }


    @Override
    public ResponseEntity<List<Etudiant>> findAllEtudiantByFiliereAndNiveau(Integer idA, Integer idF, Integer idN) {
        return ResponseEntity.ok(
                this.etudiantRepository.findByAnneeAcademiqueAndFiliereAndNiveau(
                        this.anneeAcademiqueRepository.findById(idA).orElse(null),
                        this.filiereRepository.findById(idF).orElse(null),
                        this.niveauRepository.findById(idN).orElse(null)
                )
        );
    }

    @Override
    public ResponseEntity<List<ChefDepartement>> findAllChefDepartement() {
        return ResponseEntity.ok(
                this.chefDepartementRepository.findAll()
        );
    }

    @Override
    public ResponseEntity<ChefDepartement> findChefDepartementById(Integer id) {
        return ResponseEntity.ok(this.chefDepartementRepository.findByDepartement(
                this.departementRepository.findById(id).orElse(null)
        ).orElse(null));
    }

    @Override
    public ResponseEntity<ServerReponse> createChefDepartement(String chef) {
        ChefDeptDTO chefDeptDTO = new ObjectMapper().readValue(chef,ChefDeptDTO.class);
        Enseignant enseignant = this.enseignantRepository.findById(chefDeptDTO.getEnseignant()).orElse(null);
        Departement departement = this.departementRepository.findById(chefDeptDTO.getDepartement()).orElse(null);


        ChefDepartement chefDepartement= new ChefDepartement();

        chefDepartement.setEnseignant(enseignant);
        chefDepartement.setDepartement(departement);

        chefDepartement.setAnneeexperience(chefDeptDTO.getAnneeexperience());
        chefDepartement.setDiplome(chefDeptDTO.getDiplome());
        chefDepartement.setBureau(chefDeptDTO.getBureau());

        this.chefDepartementRepository.save(chefDepartement);
        return ResponseEntity.ok(new ServerReponse("Chef de departement cree", true));
    }

    @Override
    public ResponseEntity<ServerReponse> updateChefDepartement(String chef) {
        ChefDeptDTO chefDeptDTO = new ObjectMapper().readValue(chef,ChefDeptDTO.class);
        Enseignant enseignant = this.enseignantRepository.findById(chefDeptDTO.getEnseignant()).orElse(null);
        ChefDepartement chefDepartementUpdating = this.chefDepartementRepository.findById(enseignant.getId()).orElse(null);
        Departement departement = this.departementRepository.findById(chefDeptDTO.getDepartement()).orElse(null);
        ChefDepartement chefDepartement= new ChefDepartement();

        chefDepartement.setId(chefDeptDTO.getId());
        chefDepartement.setEnseignant(enseignant);
        chefDepartement.setDepartement(departement);

        chefDepartement.setAnneeexperience(chefDeptDTO.getAnneeexperience());
        chefDepartement.setDiplome(chefDeptDTO.getDiplome());
        chefDepartement.setBureau(chefDeptDTO.getBureau());

        this.chefDepartementRepository.save(chefDepartement);
        return ResponseEntity.ok(new ServerReponse("Mise a jour chef departement", true));

    }

    @Override
    public ResponseEntity<List<Etudiant>> findAllEtudiantByFiliereAndNiveau(Integer idF, Integer idN) {
        return ResponseEntity.ok(
                this.etudiantRepository.findByFiliereAndNiveau(
                        this.filiereRepository.findById(idF).orElse(null),
                        this.niveauRepository.findById(idN).orElse(null)
                )
        );
    }


}
