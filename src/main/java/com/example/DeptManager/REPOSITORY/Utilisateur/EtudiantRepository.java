package com.example.DeptManager.REPOSITORY.Utilisateur;

import com.example.DeptManager.ENTITY.Structure.Departement;
import com.example.DeptManager.ENTITY.Structure.Filiere;
import com.example.DeptManager.ENTITY.Structure.Niveau;
import com.example.DeptManager.ENTITY.Utilisateur.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EtudiantRepository extends JpaRepository<Etudiant,Integer> {

    @Query(value = "SELECT e FROM Etudiant e JOIN e.filiere f JOIN f.departement d WHERE d.id=:id")
    List<Etudiant> findAllEtudiantByDepartement(@Param("id") Integer id);

    List<Etudiant> findByNiveau(Niveau niveau);
    List<Etudiant> findByFiliere(Filiere filiere);
    List<Etudiant> findByNiveauAndFiliere(Niveau niveau, Filiere filiere);
    List<Etudiant> findByFiliereAndNiveau(Filiere filiere, Niveau niveau);
}
