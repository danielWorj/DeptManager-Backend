package com.example.DeptManager.REPOSITORY.Utilisateur;

import com.example.DeptManager.ENTITY.Utilisateur.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur,Integer> {
    Optional<Utilisateur> findByEmailAndPassword(String email, String password);
}
