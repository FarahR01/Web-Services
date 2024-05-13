package com.example.project.service;

//ReleveDeNotesServiceImpl.java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project.exception.DuplicateNoteException;
import com.example.project.exception.MatiereNotStudiedException;
import com.example.project.exception.ValidationException;
import com.example.project.model.Etudiant;
import com.example.project.model.Matiere;
import com.example.project.model.ReleveDeNotes;
import com.example.project.repository.EtudiantRepository;
import com.example.project.repository.MatiereRepository;
import com.example.project.repository.ReleveDeNotesRepository;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class ReleveDeNotesServiceImpl implements ReleveDeNotesService {

 @Autowired
 private ReleveDeNotesRepository releveDeNotesRepository;
 
 @Autowired
 private EtudiantRepository etudiantRepository;

 @Autowired
 private MatiereRepository matiereRepository;

 @Override
 public List<ReleveDeNotes> getAllRelevesDeNotes() {
     return releveDeNotesRepository.findAll();
 }

 @Override
 public Optional<ReleveDeNotes> getReleveDeNotesById(Long id) {
     return releveDeNotesRepository.findById(id);
 }
 
 @Override
 public ReleveDeNotes createReleveDeNotes(ReleveDeNotes releveDeNotes) throws MatiereNotStudiedException, DuplicateNoteException {
     try {
         // Check if the specified Etudiant and Matiere exist
         Etudiant etudiant = releveDeNotes.getEtudiant();
         Matiere matiere = releveDeNotes.getMatiere();

         if (etudiant == null || matiere == null) {
             throw new IllegalArgumentException("Etudiant and Matiere must be specified.");
         }

         Optional<Etudiant> existingEtudiant = etudiantRepository.findById(etudiant.getId());
         Optional<Matiere> existingMatiere = matiereRepository.findById(matiere.getId());

         if (existingEtudiant.isEmpty() || existingMatiere.isEmpty()) {
             throw new IllegalArgumentException("Invalid Etudiant or Matiere specified. Cannot create ReleveDeNotes.");
         }

         // Set the Etudiant and Matiere for the ReleveDeNotes
         releveDeNotes.setEtudiant(existingEtudiant.get());
         releveDeNotes.setMatiere(existingMatiere.get());

         // Check if the Etudiant studies the specified Matiere
         if (!existingEtudiant.get().getMatieresEtudiees().contains(existingMatiere.get().getNom())) {
             throw new MatiereNotStudiedException("Etudiant doesn't study the specified Matiere.");
         }

         // Check if the same Etudiant has already taken notes in the same Matiere
         if (releveDeNotesRepository.existsByEtudiantAndMatiere(existingEtudiant.get(), existingMatiere.get())) {
             throw new DuplicateNoteException("Etudiant cannot take notes in the same Matiere twice.");
         }

         // If Etudiant and Matiere are valid, and no duplicate note, proceed with creating ReleveDeNotes
         return releveDeNotesRepository.save(releveDeNotes);
     } catch (MatiereNotStudiedException | DuplicateNoteException e) {
         throw e; // Re-throw the specific exception to handle it separately
     } catch (Exception e) {
         // Handle other exceptions with a generic error message
         throw new RuntimeException("Internal Server Error", e);
     }
 }



 @Override
 public ReleveDeNotes updateReleveDeNotes(Long id, ReleveDeNotes updatedReleveDeNotesData) {
     try {
         // Find the existing ReleveDeNotes by ID
         ReleveDeNotes existingReleveDeNotes = releveDeNotesRepository.findById(id)
                 .orElseThrow(() -> new EntityNotFoundException("ReleveDeNotes not found with ID: " + id));

         // Check if the updated Etudiant exists
         Etudiant updatedEtudiant = updatedReleveDeNotesData.getEtudiant();
         if (updatedEtudiant != null) {
             Etudiant existingEtudiant = etudiantRepository.findById(updatedEtudiant.getId())
                     .orElseThrow(() -> new EntityNotFoundException("Etudiant not found with ID: " + updatedEtudiant.getId()));
             existingReleveDeNotes.setEtudiant(existingEtudiant);
         }

         // Check if the updated Matiere exists
         Matiere updatedMatiere = updatedReleveDeNotesData.getMatiere();
         if (updatedMatiere != null) {
             Matiere existingMatiere = matiereRepository.findById(updatedMatiere.getId())
                     .orElseThrow(() -> new EntityNotFoundException("Matiere not found with ID: " + updatedMatiere.getId()));
             existingReleveDeNotes.setMatiere(existingMatiere);
         }

         // Save the updated ReleveDeNotes
         return releveDeNotesRepository.save(existingReleveDeNotes);
     } catch (EntityNotFoundException e) {
         // Handle the EntityNotFoundException (e.g., return an error response)
         // You can log the exception or take appropriate actions based on your application's needs
         e.printStackTrace(); // Logging the exception (replace with your logging mechanism)
         throw new RuntimeException("Error updating ReleveDeNotes: " + e.getMessage());
     }
 }


 @Override
 public void deleteReleveDeNotes(Long id) {
     releveDeNotesRepository.deleteById(id);
 }
}

