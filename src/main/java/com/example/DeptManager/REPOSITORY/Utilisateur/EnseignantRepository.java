package com.example.DeptManager.REPOSITORY.Utilisateur;

import com.example.DeptManager.ENTITY.Horaire.Horaire;
import com.example.DeptManager.ENTITY.Structure.Departement;
import com.example.DeptManager.ENTITY.Utilisateur.Enseignant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnseignantRepository extends JpaRepository<Enseignant,Integer> {

    List<Enseignant> findByDepartement(Departement departement);

}
