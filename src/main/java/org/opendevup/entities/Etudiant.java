package org.opendevup.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Etudiant implements Serializable{
	@Id
	@GeneratedValue
	private Long id;
	@Column(name = "NOM", length = 30)
	@NotEmpty
	@Size(min = 5,max = 30,message = "La taille de champs doit être entre 5 et 30 caractères!")
	private String nom;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateNaissance;
	@javax.validation.constraints.NotEmpty
	@Email
	private String email;
 	
	private String picture;
	
	

	public Etudiant() {
		super();
 	}

	public Etudiant(String nom, Date dateNaissance, String email, String picture) {
		super();
		this.nom = nom;
		this.dateNaissance = dateNaissance;
		this.email = email;
 		this.picture = picture;
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

	public Date getDateNaissance() {
		return dateNaissance;
	}

	public void setDateNaissance(Date dateNaissance) {
		this.dateNaissance = dateNaissance;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
 	 
	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

}
