package br.com.devdojo.examgenerator.persistence.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;

@Entity
public class Course extends AbstractEntity{
	@NotEmpty(message = "The field cannot be empty")
	private String name;
	@ManyToOne(optional = false)
	private Professor professor;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Professor getProfessor() {
		return professor;
	}
	public void setProfessor(Professor professor) {
		this.professor = professor;
	}
	
	
}
