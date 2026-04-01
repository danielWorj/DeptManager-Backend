package com.example.DeptManager.REPOSITORY.Structure;

import com.example.DeptManager.ENTITY.Structure.Departement;
import com.example.DeptManager.ENTITY.Utilisateur.ChefDepartement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChefDepartementRepository extends JpaRepository<ChefDepartement,Integer> {
    List<ChefDepartement> findByDepartement(Departement departement);
}
