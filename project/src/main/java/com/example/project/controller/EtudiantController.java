package com.example.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.project.exception.MatiereNotFoundException;
import com.example.project.exception.MatiereNotStudiedException;
import com.example.project.exception.ValidationException;
import com.example.project.model.Etudiant;
import com.example.project.service.EtudiantService;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/etudiants")
public class EtudiantController {

    @Autowired
    private EtudiantService etudiantService;

    @GetMapping
    public ResponseEntity<List<Etudiant>> getAllEtudiants() {
        List<Etudiant> etudiants = etudiantService.getAllEtudiants();
        return new ResponseEntity<>(etudiants, HttpStatus.OK);
    }
    
    @GetMapping("/etudiants")
    public String etudiantList(Model model) {
        model.addAttribute("etudiants", etudiantService.getAllEtudiants());
        return "etudiantList";
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEtudiantById(@PathVariable Long id) {
        try {
            Optional<Etudiant> etudiant = etudiantService.getEtudiantById(id);
            
            if (etudiant.isPresent()) {
                return new ResponseEntity<>(etudiant.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Etudiant not found with ID: " + id, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while processing your request.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping
    public ResponseEntity<?> createEtudiant(@RequestBody Etudiant etudiant) {
        try {
            Etudiant createdEtudiant = etudiantService.createEtudiant(etudiant);

            if (createdEtudiant != null) {
                return new ResponseEntity<>(createdEtudiant, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Unable to create Etudiant. Please check your input.", HttpStatus.BAD_REQUEST);
            }
        } catch (ValidationException e) {
            // Handle validation errors and return specific error messages
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while processing your request.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/{etudiantId}/addMatiere")
    public ResponseEntity<Object> addMatiereToEtudiant(
            @PathVariable Long etudiantId,
            @RequestBody Map<String, String> requestMap) {
        String matiereNom = requestMap.get("matiereNom");

        if (matiereNom == null) {
            return ResponseEntity.badRequest().body("Matiere Name must be provided in the request body");
        }

        try {
            Etudiant updatedEtudiant = etudiantService.addMatiereToEtudiant(etudiantId, matiereNom);
            return ResponseEntity.ok(updatedEtudiant);
        } catch (IllegalArgumentException e) {
            // Handle the case where matiere Name does not exist in the table Matiere
            return ResponseEntity.badRequest().body("Matiere not found: " + matiereNom);
        } catch (Exception e) {
            // Handle other exceptions (e.g., database errors) with a generic error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Matiere not found: " + matiereNom);
        }
    }
    
    @DeleteMapping("/{etudiantId}/deleteMatiere")
    public ResponseEntity<Object> deleteMatiereFromEtudiant(
            @PathVariable Long etudiantId,
            @RequestParam String matiereNom) {
        try {
            Etudiant updatedEtudiant = etudiantService.deleteMatiereFromEtudiant(etudiantId, matiereNom);
            return ResponseEntity.ok(updatedEtudiant);
        } catch (EntityNotFoundException e) {
            // Handle the case where Etudiant is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Etudiant not found with ID: " + etudiantId);
        } catch (MatiereNotStudiedException e) {
            // Handle the case where Matiere is not studied by the Etudiant
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Etudiant doesn't study the specified Matiere.");
        } catch (Exception e) {
            // Handle other exceptions with a generic error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }


    @PutMapping("/{etudiantId}")
    public ResponseEntity<Object> updateEtudiant(
            @PathVariable Long etudiantId,
            @RequestBody Etudiant updatedEtudiantData) {
        try {
            Etudiant updatedEtudiant = etudiantService.updateEtudiant(etudiantId, updatedEtudiantData);
            return ResponseEntity.ok(updatedEtudiant);
        } catch (MatiereNotFoundException e) {
            // Handle the MatiereNotFoundException
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // Handle other exceptions with a generic error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEtudiant(@PathVariable Long id) {
        etudiantService.deleteEtudiant(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
