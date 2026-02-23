package com.example.DeptManager.REPOSITORY.Scolarite;

import com.example.DeptManager.ENTITY.Scolarite.Matiere;
import com.example.DeptManager.ENTITY.Structure.Departement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatiereRepository extends JpaRepository<Matiere,Integer> {
List<Matiere> findByDepartement(Departement departement);
}
