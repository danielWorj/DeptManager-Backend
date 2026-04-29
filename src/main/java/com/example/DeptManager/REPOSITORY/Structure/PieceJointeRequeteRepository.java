package com.example.DeptManager.REPOSITORY.Structure;

import com.example.DeptManager.ENTITY.Structure.PieceJointeRequete;
import com.example.DeptManager.ENTITY.Structure.Requete;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PieceJointeRequeteRepository extends JpaRepository<PieceJointeRequete,Integer> {
    List<PieceJointeRequete> findByRequete(Requete requete);
}
