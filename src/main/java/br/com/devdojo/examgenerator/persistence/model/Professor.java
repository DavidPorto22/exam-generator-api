package br.com.devdojo.examgenerator.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Entity
public class Professor extends AbstractEntity {
	@NotEmpty(message = "The field name cannot be ampty")
	private String name;
	@Email(message = "This email is not valid")
	@NotEmpty
	@Column(unique = true)
	private String email;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}	
}
