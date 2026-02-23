package com.example.DeptManager.REPOSITORY.Scolarite;

import com.example.DeptManager.ENTITY.Scolarite.AnneeAcademique;
import com.example.DeptManager.ENTITY.Scolarite.Documentation.Document;
import com.example.DeptManager.ENTITY.Scolarite.Documentation.TypeDocument;
import com.example.DeptManager.ENTITY.Scolarite.Matiere;
import com.example.DeptManager.ENTITY.Structure.Departement;
import com.example.DeptManager.ENTITY.Structure.Filiere;
import com.example.DeptManager.ENTITY.Structure.Niveau;
import com.example.DeptManager.ENTITY.Utilisateur.Enseignant;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface DocumentRepository extends JpaRepository<Document, Integer> {

    List<Document> findByDepartement(Departement departement);
    List<Document> findByFiliere(Filiere filiere);
    List<Document> findByEnseignant(Enseignant enseignant);
    List<Document> findByNiveau(Niveau niveau);
    List<Document> findByAnneeAcademique(AnneeAcademique anneeAcademique);
    List<Document> findByMatiere(Matiere matiere);
    List<Document> findByTypeDocument(TypeDocument typeDocument);


}
