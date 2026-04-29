package com.example.DeptManager.REPOSITORY.Structure;

import com.example.DeptManager.ENTITY.Structure.Departement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartementRepository extends JpaRepository<Departement,Integer> {
}
