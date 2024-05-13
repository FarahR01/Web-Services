package com.example.project.service;

import java.util.List;
import java.util.Optional;

import com.example.project.model.Matiere;

public interface MatiereService {

    List<Matiere> getAllMatieres();

    Optional<Matiere> getMatiereById(Long id);
    
    List<Matiere> getMatieresBySemestre(Integer semestre);

    Matiere createMatiere(Matiere matiere);

    Matiere updateMatiere(Long id, Matiere matiere);

    void deleteMatiere(Long id);
}
