package com.example.project.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Etudiant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;
    
    @ElementCollection
    @CollectionTable(name = "etudiant_matieres", joinColumns = @JoinColumn(name = "etudiant_id"))
    @Column(name = "matiere")
    private List<String> matieresEtudiees;
    
    @JsonBackReference
    @OneToMany(mappedBy = "etudiant")
    private List<ReleveDeNotes> relevesDeNotes;




    
    public Etudiant() {
        // Default constructor with no arguments
    }

	
	

	public Etudiant(Long id, String nom, String prenom, LocalDate dateNaissance, List<String> matieresEtudiees) {
		super();
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
		this.dateNaissance = dateNaissance;
		this.matieresEtudiees = matieresEtudiees;
	}

	

	public List<String> getMatieresEtudiees() {
		return matieresEtudiees;
	}




	public void setMatieresEtudiees(List<String> matieresEtudiees) {
		this.matieresEtudiees = matieresEtudiees;
	}






	public List<ReleveDeNotes> getRelevesDeNotes() {
		return relevesDeNotes;
	}




	public void setRelevesDeNotes(List<ReleveDeNotes> relevesDeNotes) {
		this.relevesDeNotes = relevesDeNotes;
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

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public LocalDate getDateNaissance() {
		return dateNaissance;
	}

	public void setDateNaissance(LocalDate dateNaissance) {
		this.dateNaissance = dateNaissance;
	}




    
}

