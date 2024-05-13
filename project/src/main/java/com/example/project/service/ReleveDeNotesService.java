package com.example.project.service;

//ReleveDeNotesService.java
import java.util.List;
import java.util.Optional;

import com.example.project.exception.DuplicateNoteException;
import com.example.project.exception.MatiereNotStudiedException;
import com.example.project.model.ReleveDeNotes;

public interface ReleveDeNotesService {
 List<ReleveDeNotes> getAllRelevesDeNotes();
 Optional<ReleveDeNotes> getReleveDeNotesById(Long id);
 ReleveDeNotes createReleveDeNotes(ReleveDeNotes releveDeNotes) throws MatiereNotStudiedException, DuplicateNoteException;
 ReleveDeNotes updateReleveDeNotes(Long id, ReleveDeNotes releveDeNotes);
 void deleteReleveDeNotes(Long id);
}
