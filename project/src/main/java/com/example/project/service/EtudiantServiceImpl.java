package com.example.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.exception.MatiereNotFoundException;
import com.example.project.exception.MatiereNotStudiedException;
import com.example.project.exception.ValidationException;
import com.example.project.model.Etudiant;
import com.example.project.model.Matiere;
import com.example.project.repository.EtudiantRepository;
import com.example.project.repository.MatiereRepository;
import com.example.project.repository.ReleveDeNotesRepository;

import jakarta.persistence.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EtudiantServiceImpl implements EtudiantService {

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private MatiereRepository matiereRepository;
    
    @Autowired
    private ReleveDeNotesRepository releveDeNotesRepository;

    @Override
    public List<Etudiant> getAllEtudiants() {
        return etudiantRepository.findAll();
    }

    @Override
    public Optional<Etudiant> getEtudiantById(Long id) {
        return etudiantRepository.findById(id);
    }

    @Override
    public Etudiant addMatiereToEtudiant(Long etudiantId, String matiereNom) {
        // Find the Etudiant by ID
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new EntityNotFoundException("Etudiant not found with ID: " + etudiantId));

        // Find the Matiere by name
        Matiere matiere = matiereRepository.findByNom(matiereNom)
                .orElseThrow(() -> new EntityNotFoundException("Matiere not found with name: " + matiereNom));

        // Add Matiere name to the matieresEtudiees list if not already present
        List<String> matieresEtudiees = etudiant.getMatieresEtudiees();
        if (matieresEtudiees == null || !matieresEtudiees.contains(matiereNom)) {
            if (matieresEtudiees == null) {
                matieresEtudiees = new ArrayList<>();
            }
            matieresEtudiees.add(matiereNom);
            etudiant.setMatieresEtudiees(matieresEtudiees);

            // Save the updated Etudiant
            return etudiantRepository.save(etudiant);
        }

        // If Matiere name is already in the list, return the unchanged Etudiant
        return etudiant;
    }

    @Override
    public Etudiant createEtudiant(Etudiant etudiant) {
        // Perform validation logic
        if (etudiant == null || etudiant.getNom() == null || etudiant.getPrenom() == null) {
            throw new ValidationException("Invalid Etudiant data. Please provide a valid name and surname.");
        }

        // Check if an Etudiant with the same nom and prenom already exists
        String nom = etudiant.getNom();
        String prenom = etudiant.getPrenom();

        if (etudiantRepository.existsByNomAndPrenom(nom, prenom)) {
            throw new ValidationException("An Etudiant with the same nom and prenom already exists.");
        }

        // Validate matieresEtudiees if provided
        List<String> matieresEtudiees = etudiant.getMatieresEtudiees();
        if (matieresEtudiees != null && !matieresEtudiees.isEmpty()) {
            List<Matiere> matieres = matiereRepository.findByNomIn(matieresEtudiees);
            if (matieres.size() != matieresEtudiees.size()) {
                // Identify the missing matieres
                List<String> missingMatieres = matieresEtudiees.stream()
                        .filter(matiere -> matieres.stream().noneMatch(existingMatiere -> existingMatiere.getNom().equals(matiere)))
                        .collect(Collectors.toList());

                throw new ValidationException("Invalid Matiere(s) specified for Etudiant: " + missingMatieres);
            }
        }

        // If validation passes, proceed with creating Etudiant
        return etudiantRepository.save(etudiant);
    }


    @Override
    public Etudiant updateEtudiant(Long etudiantId, Etudiant updatedEtudiantData) throws MatiereNotFoundException {
        try {
            // Find the existing Etudiant by ID
            Etudiant existingEtudiant = etudiantRepository.findById(etudiantId)
                    .orElseThrow(() -> new EntityNotFoundException("Etudiant not found with ID: " + etudiantId));

            // Update only the fields that are not null in the provided data
            if (updatedEtudiantData.getNom() != null) {
                existingEtudiant.setNom(updatedEtudiantData.getNom());
            }

            if (updatedEtudiantData.getPrenom() != null) {
                existingEtudiant.setPrenom(updatedEtudiantData.getPrenom());
            }

            if (updatedEtudiantData.getDateNaissance() != null) {
                existingEtudiant.setDateNaissance(updatedEtudiantData.getDateNaissance());
            }

            // Update matieresEtudiees if provided
            List<String> updatedMatieresEtudiees = updatedEtudiantData.getMatieresEtudiees();
            if (updatedMatieresEtudiees != null && !updatedMatieresEtudiees.isEmpty()) {
                List<Matiere> existingMatieres = matiereRepository.findByNomIn(updatedMatieresEtudiees);
                if (existingMatieres.size() != updatedMatieresEtudiees.size()) {
                    // Handle the case where not all matieresEtudiees exist in the Matiere table
                    throw new MatiereNotFoundException("Invalid Matiere(s) specified for Etudiant. Please provide valid Matiere(s).");
                }

                existingEtudiant.setMatieresEtudiees(updatedMatieresEtudiees);
            }

            // Save the updated Etudiant
            return etudiantRepository.save(existingEtudiant);
        } catch (EntityNotFoundException e) {
            // Handle the EntityNotFoundException (e.g., return an error response)
            e.printStackTrace(); // Logging the exception (replace with your logging mechanism)
            throw new RuntimeException("Error updating Etudiant: " + e.getMessage());
        }
    }




    
    @Override
    public Etudiant deleteMatiereFromEtudiant(Long etudiantId, String matiereNom) {
        // Find the Etudiant by ID
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new EntityNotFoundException("Etudiant not found with ID: " + etudiantId));

        // Check if the Etudiant studies the specified Matiere
        if (!etudiant.getMatieresEtudiees().contains(matiereNom)) {
            throw new MatiereNotStudiedException("Etudiant doesn't study the specified Matiere.");
        }

        // Remove Matiere from the matieresEtudiees list
        etudiant.getMatieresEtudiees().remove(matiereNom);
        etudiantRepository.save(etudiant);

        // Delete ReleveDeNotes with the specified Etudiant and Matiere
        releveDeNotesRepository.deleteByEtudiantAndMatiere(etudiant, matiereRepository.findByNom(matiereNom)
                .orElseThrow(() -> new EntityNotFoundException("Matiere not found with name: " + matiereNom)));

        return etudiant;
    }

    @Override
    public void deleteEtudiant(Long etudiantId) {
        // Find the Etudiant by ID
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new EntityNotFoundException("Etudiant not found with ID: " + etudiantId));

        // Delete ReleveDeNotes associated with the Etudiant
        deleteReleveDeNotesByEtudiant(etudiant);

        // Delete the Etudiant
        etudiantRepository.delete(etudiant);
    }

    @Transactional // Add the Transactional annotation
    private void deleteReleveDeNotesByEtudiant(Etudiant etudiant) {
        releveDeNotesRepository.deleteByEtudiant(etudiant);
    }
}
