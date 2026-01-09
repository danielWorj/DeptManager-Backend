package com.example.DeptManager.ENTITY.Horaire;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;

@Entity
@Table
@Data
public class Periode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id ;
    private LocalTime debut ;
    private LocalTime fin ;
}
