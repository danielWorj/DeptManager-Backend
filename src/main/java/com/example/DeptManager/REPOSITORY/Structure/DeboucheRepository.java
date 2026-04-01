package com.example.DeptManager.REPOSITORY.Structure;

import com.example.DeptManager.ENTITY.Structure.Debouche;
import com.example.DeptManager.ENTITY.Structure.Filiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DeboucheRepository extends JpaRepository<Debouche,Integer> {

    @Query(value = "SELECT d FROM Debouche d JOIN d.filiere f JOIN f.departement dp WHERE dp.id=:id")
    List<Debouche> findByDepartement(@Param("id") Integer id);
    List<Debouche> findByFiliere(Filiere filiere);
}
