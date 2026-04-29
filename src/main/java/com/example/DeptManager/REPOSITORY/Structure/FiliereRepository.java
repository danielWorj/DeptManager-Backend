package com.example.DeptManager.REPOSITORY.Structure;

import com.example.DeptManager.ENTITY.Structure.Departement;
import com.example.DeptManager.ENTITY.Structure.Filiere;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FiliereRepository extends JpaRepository<Filiere,Integer> {
    List<Filiere> findAllByDepartement(Departement departement);
}
