package com.example.DeptManager.REPOSITORY.Scolarite;

import com.example.DeptManager.ENTITY.Scolarite.Matiere;
import com.example.DeptManager.ENTITY.Scolarite.Repartition;
import com.example.DeptManager.ENTITY.Structure.Filiere;
import com.example.DeptManager.ENTITY.Structure.Niveau;
import com.example.DeptManager.ENTITY.Structure.Semestre;
import com.example.DeptManager.ENTITY.Utilisateur.Enseignant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepartitionRepository extends JpaRepository<Repartition,Integer> {

    List<Repartition> findByEnseignant(Enseignant enseignant);
    List<Repartition> findByFiliere(Filiere filiere);
    List<Repartition> findByMatiere(Matiere matiere);
    List<Repartition> findBySemestre(Semestre semestre);
    List<Repartition> findByFiliereAndNiveau(Filiere filiere, Niveau niveau);
    List<Repartition> findByFiliereAndNiveauAndSemestre(Filiere filiere, Niveau niveau, Semestre semestre);
}
