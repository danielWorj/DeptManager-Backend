package com.example.DeptManager.REPOSITORY.Horaire;

import com.example.DeptManager.ENTITY.Horaire.Horaire;
import com.example.DeptManager.ENTITY.Horaire.Jour;
import com.example.DeptManager.ENTITY.Structure.Filiere;
import com.example.DeptManager.ENTITY.Structure.Niveau;
import com.example.DeptManager.ENTITY.Utilisateur.Enseignant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HoraireRepository extends JpaRepository<Horaire,Integer> {
    List<Horaire> findByEnseignant(Enseignant enseignant);
    List<Horaire> findByFiliereAndNiveau(Filiere filiere, Niveau niveau);
    List<Horaire> findByFiliereAndNiveauAndJour(Filiere filiere, Niveau niveau , Jour jour);
}
