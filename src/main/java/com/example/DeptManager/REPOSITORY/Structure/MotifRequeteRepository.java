package com.example.DeptManager.REPOSITORY.Structure;

import com.example.DeptManager.ENTITY.Structure.MotifRequete;
import com.example.DeptManager.ENTITY.Structure.Requete;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MotifRequeteRepository extends JpaRepository<MotifRequete,Integer> {
}
