package com.example.project.service;

import java.util.List;
import java.util.Optional;

import com.example.project.exception.MatiereNotFoundException;
import com.example.project.model.Etudiant;

public interface EtudiantService {

    List<Etudiant> getAllEtudiants();

    Optional<Etudiant> getEtudiantById(Long id);

    Etudiant createEtudiant(Etudiant etudiant);

    Etudiant updateEtudiant(Long id, Etudiant etudiant) throws MatiereNotFoundException;
    
    Etudiant addMatiereToEtudiant(Long etudiantId, String matiereNom);

    void deleteEtudiant(Long id);

	Etudiant deleteMatiereFromEtudiant(Long etudiantId, String matiereNom);
}
