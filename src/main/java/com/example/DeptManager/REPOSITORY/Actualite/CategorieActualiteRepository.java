package com.example.DeptManager.REPOSITORY.Actualite;

import com.example.DeptManager.ENTITY.Actualite.Actualite;
import com.example.DeptManager.ENTITY.Actualite.CategorieActualite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategorieActualiteRepository extends JpaRepository<CategorieActualite,Integer> {

}
