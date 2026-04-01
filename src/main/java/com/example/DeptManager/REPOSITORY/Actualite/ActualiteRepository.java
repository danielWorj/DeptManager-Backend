package com.example.DeptManager.REPOSITORY.Actualite;

import com.example.DeptManager.ENTITY.Actualite.Actualite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ActualiteRepository extends JpaRepository<Actualite,Integer> {
    @Query(value = "SELECT a FROM Actualite a ORDER BY a.id DESC LIMIT 3 ")
    List<Actualite> findTheLastNews();

    @Query(value = "SELECT a FROM Actualite a ORDER BY a.id DESC")
    List<Actualite> findAllActualiteOrderByIdDesc();
}
