package com.example.DeptManager.REPOSITORY.Horaire;

import com.example.DeptManager.ENTITY.Horaire.Periode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Period;

public interface PeriodeRepository extends JpaRepository<Periode,Integer> {
}
