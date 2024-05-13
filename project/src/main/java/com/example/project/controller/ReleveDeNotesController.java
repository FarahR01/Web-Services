package com.example.project.controller;

//ReleveDeNotesController.java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.project.exception.DuplicateNoteException;
import com.example.project.exception.ValidationException;
import com.example.project.model.ReleveDeNotes;
import com.example.project.service.ReleveDeNotesService;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/relevesdenotes")
public class ReleveDeNotesController {

 @Autowired
 private ReleveDeNotesService releveDeNotesService;

 @GetMapping
 public List<ReleveDeNotes> getAllRelevesDeNotes() {
     return releveDeNotesService.getAllRelevesDeNotes();
 }

 @GetMapping("/{id}")
 public ResponseEntity<ReleveDeNotes> getReleveDeNotesById(@PathVariable Long id) {
     return releveDeNotesService.getReleveDeNotesById(id)
             .map(ResponseEntity::ok)
             .orElse(ResponseEntity.notFound().build());
 }

 @PostMapping
 public ResponseEntity<Object> createReleveDeNotes(@RequestBody ReleveDeNotes releveDeNotes) {
     try {
         ReleveDeNotes createdReleveDeNotes = releveDeNotesService.createReleveDeNotes(releveDeNotes);
         return ResponseEntity.status(HttpStatus.CREATED).body(createdReleveDeNotes);
     } catch (DuplicateNoteException e) {
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
     } catch (Exception e) {
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
     }
 }


 @PutMapping("/{id}")
 public ResponseEntity<Object> updateReleveDeNotes(
         @PathVariable Long id,
         @RequestBody ReleveDeNotes updatedReleveDeNotesData) {
     try {
         ReleveDeNotes updatedReleveDeNotes = releveDeNotesService.updateReleveDeNotes(id, updatedReleveDeNotesData);
         return ResponseEntity.ok(updatedReleveDeNotes);
     } catch (EntityNotFoundException e) {
         // Handle the EntityNotFoundException
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
     } catch (RuntimeException e) {
         // Handle other exceptions with a generic error message
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
     }
 }


 @DeleteMapping("/{id}")
 public ResponseEntity<Void> deleteReleveDeNotes(@PathVariable Long id) {
     releveDeNotesService.deleteReleveDeNotes(id);
     return ResponseEntity.noContent().build();
 }
}
