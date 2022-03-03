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
public class Choice extends AbstractEntity{
	@NotEmpty(message = "The field title cannot be empty")
	private String title;
	@NotNull(message = "The field correctAnswer must be true or false")
	@Column(columnDefinition = "boolean default false")
	private boolean correctAnswer = false;
	@ManyToOne(optional = false)
	private Question question;
	@ManyToOne(optional = false)
	private Professor professor;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public boolean isCorrectAnswer() {
		return correctAnswer;
	}
	public void setCorrectAnswer(boolean correctAnswer) {
		this.correctAnswer = correctAnswer;
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
	@Override
	public String toString() {
		return "Choice [title=" + title + ", correctAnswer=" + correctAnswer + ", question=" + question + ", professor="
				+ professor + ", id=" + id + ", enabled=" + enabled + "]";
	}
	
	
}
