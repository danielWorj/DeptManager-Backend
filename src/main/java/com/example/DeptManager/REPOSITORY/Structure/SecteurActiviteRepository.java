package com.example.DeptManager.REPOSITORY.Structure;

import com.example.DeptManager.ENTITY.Structure.Departement;
import com.example.DeptManager.ENTITY.Structure.SecteurActivite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SecteurActiviteRepository extends JpaRepository<SecteurActivite,Integer> {
    List<SecteurActivite> findByDepartement(Departement departement);
}
