package com.example.DeptManager.REPOSITORY.Scolarite;

import com.example.DeptManager.ENTITY.Scolarite.AnneeAcademique;
import com.example.DeptManager.ENTITY.Structure.Semestre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SemestreRepository extends JpaRepository<Semestre,Integer> {
    List<Semestre> findByAnneeAcademique(AnneeAcademique anneeAcademique);
}
