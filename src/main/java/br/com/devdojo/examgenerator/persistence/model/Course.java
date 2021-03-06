package br.com.devdojo.examgenerator.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
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
	@Override
	public String toString() {
		return "Course [name=" + name + ", professor=" + professor + ", id=" + id + ", enabled=" + enabled + "]";
	}
}
