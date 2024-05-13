package com.example.project.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project.model.Etudiant;
import com.example.project.model.Matiere;

@Repository
public interface MatiereRepository extends JpaRepository<Matiere, Long> {
	Optional<Matiere> findByNom(String matiereNom);
	List<Matiere> findByNomIn(List<String> matieresEtudiees);
    List<Matiere> findBySemestre(Integer semestre);
}
