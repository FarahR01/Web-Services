package com.example.project.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table
public class Matiere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "coefficient")
    private int coefficient;

    @Column(name = "enseignant")
    private String enseignant;

    @Column(name = "description")
    private String description;

    @Column(name = "semestre")
    private int semestre;
    
    @OneToMany(mappedBy = "matiere")
    private List<ReleveDeNotes> releveDeNotes;
    
    
    public Matiere() {
        // Default constructor with no arguments
    }
    
    
	public Matiere(Long id, String nom, int coefficient, String enseignant, String description, int semestre) {
		super();
		this.id = id;
		this.nom = nom;
		this.coefficient = coefficient;
		this.enseignant = enseignant;
		this.description = description;
		this.semestre = semestre;
	}
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public int getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(int coefficient) {
		this.coefficient = coefficient;
	}

	public String getEnseignant() {
		return enseignant;
	}

	public void setEnseignant(String enseignant) {
		this.enseignant = enseignant;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getSemestre() {
		return semestre;
	}

	public void setSemestre(int semestre) {
		this.semestre = semestre;
	}



	
}
