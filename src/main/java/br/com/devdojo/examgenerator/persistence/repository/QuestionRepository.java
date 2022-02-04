package br.com.devdojo.examgenerator.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import br.com.devdojo.examgenerator.persistence.model.Course;
import br.com.devdojo.examgenerator.persistence.model.Question;

public interface QuestionRepository extends CustomJpaRepository<Question, Long>{
	@Query("select q from Question q where q = ?1 and q.professor = ?#{principal.professor} and q.enabled = true")
	Question findOne(Question question);
	
	@Query("select q from Question q where q.course.id = ?1 and q.title like %?2% and professor = ?#{principal.professor} and q.enabled = true")
	List<Question> listQuestionsByCourseAndTitle(long courseId, String title);
}
