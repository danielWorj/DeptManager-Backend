package com.example.DeptManager.DTO;

import com.example.DeptManager.ENTITY.Structure.Requete;
import com.example.DeptManager.ENTITY.Structure.TypePieceJointeRequete;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class PieceJointeRequeteDTO {
    private Integer id ;
    private String intitule ;
    private String url ;
    private Integer typePieceJointeRequete ;
    private Integer requete ;
}
