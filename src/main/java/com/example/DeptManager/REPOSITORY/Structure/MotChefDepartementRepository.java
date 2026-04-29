package com.example.DeptManager.REPOSITORY.Structure;

import com.example.DeptManager.ENTITY.Structure.Departement;
import com.example.DeptManager.ENTITY.Structure.MotChefDepartement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.relational.core.sql.In;

import java.util.Optional;

public interface MotChefDepartementRepository extends JpaRepository<MotChefDepartement, Integer> {
    Optional<MotChefDepartement> findByDepartement(Departement departement);
}
