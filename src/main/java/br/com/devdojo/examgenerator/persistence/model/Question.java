package br.com.devdojo.examgenerator.persistence.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;

import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
public class Question extends AbstractEntity{
	@NotEmpty(message = "The field title cannot be empty")
	private String title;
	@ManyToOne
	private Course course;
	@ManyToOne
	private Professor professor;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Course getCourse() {
		return course;
	}
	public void setCourse(Course course) {
		this.course = course;
	}
	public Professor getProfessor() {
		return professor;
	}
	public void setProfessor(Professor professor) {
		this.professor = professor;
	}

	
}
