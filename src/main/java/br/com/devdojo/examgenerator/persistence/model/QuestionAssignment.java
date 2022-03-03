package br.com.devdojo.examgenerator.persistence.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionAssignment extends AbstractEntity{
	@ManyToOne
	private Assignment assignment;
	@ManyToOne
	private Question question;
	@ManyToOne
	private Professor professor;
	private double grade;
	
	public Assignment getAssignment() {
		return assignment;
	}
	
	public void setAssignment(Assignment assignment) {
		this.assignment = assignment;
	}
	
	public Question getQuestion() {
		return question;
	}
	
	public void setQuestion(Question question) {
		this.question = question;
	}
	
	public Professor getProfessor() {
		return professor;
	}
	
	public void setProfessor(Professor professor) {
		this.professor = professor;
	}
	
	public double getGrade() {
		return grade;
	}
	
	public void setGrade(double grade) {
		this.grade = grade;
	} 
}
