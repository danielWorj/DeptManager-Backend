package com.example.DeptManager.REPOSITORY.Horaire;

import com.example.DeptManager.ENTITY.Horaire.Horaire;
import com.example.DeptManager.ENTITY.Horaire.Jour;
import com.example.DeptManager.ENTITY.Scolarite.Repartition;
import com.example.DeptManager.ENTITY.Structure.Filiere;
import com.example.DeptManager.ENTITY.Structure.Niveau;
import com.example.DeptManager.ENTITY.Utilisateur.Enseignant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HoraireRepository extends JpaRepository<Horaire,Integer> {
    Horaire findByRepartition(Repartition repartition);

    @Query(value = "SELECT h FROM Horaire h JOIN h.repartition r JOIN r.enseignant e WHERE e=:ens")
    List<Horaire> findByEnseignant(@Param("ens") Enseignant enseignant);

    @Query(value = "SELECT h FROM Horaire h JOIN h.salle s JOIN h.jour j JOIN h.periode p WHERE s.id=:sa AND j.id=:jo AND p.id=:pe")
    Optional<Horaire> findHoraireBySalleJourAndPeriode(@Param("sa") Integer idS, @Param("jo") Integer idJ, @Param("pe") Integer pe);
    @Query(value = "SELECT h FROM Horaire h JOIN h.repartition r JOIN r.niveau n JOIN r.filiere f WHERE n=:niv AND f=:fil")
    List<Horaire> findByNiveauAndFiliere(@Param("niv") Niveau niveau, @Param("fil") Filiere filiere);
}
