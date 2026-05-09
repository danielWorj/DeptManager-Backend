package com.example.DeptManager.REPOSITORY.Note;

import com.example.DeptManager.ENTITY.Evaluation.Note;
import com.example.DeptManager.ENTITY.Evaluation.TypeEvaluation;
import com.example.DeptManager.ENTITY.Scolarite.Repartition;
import com.example.DeptManager.ENTITY.Utilisateur.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note,Integer> {
    List<Note> findByRepartition(Repartition repartition);
    List<Note> findByRepartitionAndTypeEvaluation(Repartition repartition, TypeEvaluation typeEvaluation);
    @Query(value = "SELECT n FROM Note n JOIN n.repartition r  JOIN r.filiere f JOIN r.niveau ni WHERE f.id=:idF AND ni.id=:idN")
    List<Note> findAllNoteByFiliereAndNiveau(@Param("idF") Integer idF, @Param("idN") Integer idN);
    @Query(value = "SELECT n FROM Note n JOIN n.etudiant e JOIN n.repartition r JOIN r.semestre s WHERE e.id=:idE AND s.id=:idS")
    List<Note> findAllNoteByEtudiantAndSemestre(@Param("idE") Integer idE, @Param("idS") Integer idS);

    //Etudiant
    List<Note> findByEtudiant(Etudiant etudiant);
}
