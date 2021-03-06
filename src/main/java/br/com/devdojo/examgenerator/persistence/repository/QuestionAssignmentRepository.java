package br.com.devdojo.examgenerator.persistence.repository;

import br.com.devdojo.examgenerator.persistence.model.Question;
import br.com.devdojo.examgenerator.persistence.model.QuestionAssignment;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface QuestionAssignmentRepository extends CustomPagingAndSortRepository<QuestionAssignment, Long> {
	@Query("update QuestionAssignment qa set qa.enabled = false where qa.assignment.id in (select a.id from Assignment a where a.course.id = ?1) and qa.professor = ?#{principal.professor} and qa.enabled = true")
    @Modifying
    void deleteAllQuestionAssignmentsRelatedToCourse(long courseId);
	
	@Query("update QuestionAssignment qa set qa.enabled = false where qa.assignment.id = ?1 and qa.professor = ?#{principal.professor} and qa.enabled = true")
    @Modifying
    void deleteAllQuestionAssignmentsRelatedToAssignment(long assignmentId);
	
	@Query("update QuestionAssignment qa set qa.enabled = false where qa.question.id = ?1 and qa.professor = ?#{principal.professor} and qa.enabled = true")
    @Modifying
    void deleteAllQuestionAssignmentsRelatedToQuestion(long questionId);
	
	@Query("select qa from QuestionAssignment qa where qa.question.id = ?1 and qa.assignment.id = ?2 and qa.professor = ?#{principal.professor} and qa.enabled = true")
	List<QuestionAssignment> listQuestionAssignmentByQuestionAndAssignment(long questionId, long assignmentId);

	@Query("select qa from QuestionAssignment qa where qa.assignment.id = ?1 and qa.professor = ?#{principal.professor} and qa.enabled = true")
	List<QuestionAssignment> listQuestionAssignmentByAssignmentId(long assignmentId);
	
	@Query("select qa from QuestionAssignment qa where qa.question.id = ?1 and qa.professor = ?#{principal.professor} and qa.enabled = true")
	List<QuestionAssignment> listQuestionAssignmentByQuestionId(long questionId);
	
	@Query("select qa.question from QuestionAssignment qa where qa.assignment.accessCode = ?1 and qa.enabled = true")
	List<Question> listQuestionsFromQuestionAssignmentByAssignmentAccessCode(String accessCode);
}