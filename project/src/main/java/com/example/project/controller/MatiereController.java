package com.example.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.project.model.Matiere;
import com.example.project.service.MatiereService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/matieres")
public class MatiereController {

    @Autowired
    private MatiereService matiereService;

    @GetMapping
    public ResponseEntity<List<Matiere>> getAllMatieres() {
        List<Matiere> matieres = matiereService.getAllMatieres();
        return new ResponseEntity<>(matieres, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Matiere> getMatiereById(@PathVariable Long id) {
        Optional<Matiere> matiere = matiereService.getMatiereById(id);
        return matiere.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    
    @GetMapping("/bySemestre/{semestre}")
    public ResponseEntity<List<Matiere>> getMatieresBySemestre(@PathVariable Integer semestre) {
        List<Matiere> matieres = matiereService.getMatieresBySemestre(semestre);

        if (matieres.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }

        return ResponseEntity.ok(matieres);
    }

    @PostMapping
    public ResponseEntity<Matiere> createMatiere(@RequestBody Matiere matiere) {
        Matiere createdMatiere = matiereService.createMatiere(matiere);
        return new ResponseEntity<>(createdMatiere, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Matiere> updateMatiere(@PathVariable Long id, @RequestBody Matiere matiere) {
        Matiere updatedMatiere = matiereService.updateMatiere(id, matiere);
        return updatedMatiere != null ?
                new ResponseEntity<>(updatedMatiere, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatiere(@PathVariable Long id) {
        matiereService.deleteMatiere(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

