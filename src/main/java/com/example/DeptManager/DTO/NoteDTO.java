package com.example.DeptManager.DTO;


import com.example.DeptManager.ENTITY.Evaluation.TypeEvaluation;
import lombok.Data;

@Data
public class NoteDTO {
    private Integer id;
    private Integer note ;
    private Integer repartition ;
    private Integer typeEvaluation ;
    private Integer etudiant ;
}
