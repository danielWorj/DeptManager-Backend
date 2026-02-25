package com.example.DeptManager.REPOSITORY.Note;

import com.example.DeptManager.ENTITY.Evaluation.Note;
import com.example.DeptManager.ENTITY.Evaluation.TypeEvaluation;
import com.example.DeptManager.ENTITY.Scolarite.Repartition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note,Integer> {
    List<Note> findByRepartition(Repartition repartition);
    List<Note> findByRepartitionAndTypeEvaluation(Repartition repartition, TypeEvaluation typeEvaluation);
}
