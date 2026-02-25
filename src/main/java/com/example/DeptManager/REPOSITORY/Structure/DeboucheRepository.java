package com.example.DeptManager.REPOSITORY.Structure;

import com.example.DeptManager.ENTITY.Structure.Debouche;
import com.example.DeptManager.ENTITY.Structure.Filiere;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeboucheRepository extends JpaRepository<Debouche,Integer> {

    List<Debouche> findByFiliere(Filiere filiere);
}
