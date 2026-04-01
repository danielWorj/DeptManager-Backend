package com.example.DeptManager.REPOSITORY.Scolarite;

import com.example.DeptManager.ENTITY.Scolarite.AnneeAcademique;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnneeAcademiqueRepository extends JpaRepository<AnneeAcademique,Integer> {
    Optional<AnneeAcademique> findByStatusIsTrue();
}
