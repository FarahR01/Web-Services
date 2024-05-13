package com.example.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.model.Etudiant;
import com.example.project.model.Matiere;
import com.example.project.repository.EtudiantRepository;
import com.example.project.repository.MatiereRepository;
import com.example.project.repository.ReleveDeNotesRepository;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class MatiereServiceImpl implements MatiereService {

    @Autowired
    private MatiereRepository matiereRepository;
    
    @Autowired
    private EtudiantRepository etudiantRepository;
    
    @Autowired
    private ReleveDeNotesRepository releveDeNotesRepository;

    @Override
    public List<Matiere> getAllMatieres() {
        return matiereRepository.findAll();
    }

    @Override
    public Optional<Matiere> getMatiereById(Long id) {
        return matiereRepository.findById(id);
    }
    
    @Override
    public List<Matiere> getMatieresBySemestre(Integer semestre) {
        return matiereRepository.findBySemestre(semestre);
    }

    @Override
    public Matiere createMatiere(Matiere matiere) {
        return matiereRepository.save(matiere);
    }

    @Override
    public Matiere updateMatiere(Long id, Matiere updatedMatiereData) {
        // Find the existing Matiere by ID
        Matiere existingMatiere = matiereRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Matiere not found with ID: " + id));

        // Update only the fields that are not null in the provided data
        if (updatedMatiereData.getNom() != null) {
            existingMatiere.setNom(updatedMatiereData.getNom());
        }

        if (updatedMatiereData.getCoefficient() != 0) {
            existingMatiere.setCoefficient(updatedMatiereData.getCoefficient());
        }

        if (updatedMatiereData.getEnseignant() != null) {
            existingMatiere.setEnseignant(updatedMatiereData.getEnseignant());
        }

        if (updatedMatiereData.getDescription() != null) {
            existingMatiere.setDescription(updatedMatiereData.getDescription());
        }

        if (updatedMatiereData.getSemestre() != 0) {
            existingMatiere.setSemestre(updatedMatiereData.getSemestre());
        }

        // Save the updated Matiere
        return matiereRepository.save(existingMatiere);
    }


    @Override
    @Transactional // Add the Transactional annotation
    public void deleteMatiere(Long matiereId) {
        // Find the Matiere by ID
        Matiere matiere = matiereRepository.findById(matiereId)
                .orElseThrow(() -> new EntityNotFoundException("Matiere not found with ID: " + matiereId));

        // Update related entities in Etudiant and ReleveDeNotes tables
        updateEtudiantsAndRelevesForDeletedMatiere(matiere);

        // Delete the Matiere
        matiereRepository.delete(matiere);
    }

    private void updateEtudiantsAndRelevesForDeletedMatiere(Matiere matiere) {
        // Update Etudiants
        List<Etudiant> etudiants = etudiantRepository.findByMatieresEtudieesContaining(matiere.getNom());
        etudiants.forEach(etudiant -> {
            etudiant.getMatieresEtudiees().remove(matiere.getNom());
            etudiantRepository.save(etudiant);
        });

        // Delete ReleveDeNotes with the specified Matiere
        releveDeNotesRepository.deleteByMatiere(matiere);
    }
}

