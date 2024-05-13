package com.example.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.stereotype.Repository;

import com.example.project.model.Etudiant;
import com.example.project.model.Matiere;
import com.example.project.model.ReleveDeNotes;

@Repository
public interface ReleveDeNotesRepository extends JpaRepository<ReleveDeNotes, Long> {

	boolean existsByEtudiantAndMatiere(Etudiant etudiant, Matiere matiere);
 // You can add custom queries or methods if needed

	void deleteByMatiere(Matiere matiere);

	void deleteByEtudiant(Etudiant etudiant);

	void deleteByEtudiantAndMatiere(Etudiant etudiant, Matiere orElseThrow);
}

