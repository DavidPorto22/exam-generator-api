package br.com.devdojo.examgenerator.persistence.repository;

import br.com.devdojo.examgenerator.persistence.model.Assignment;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AssignmentRepository extends CustomPagingAndSortRepository<Assignment, Long> {
    @Query("select a from Assignment a where a.course.id = ?1 and a.title like %?2% and a.professor = ?#{principal.professor} and a.enabled = true")
    List<Assignment> listAssignmentsByCourseAndTitle(long courseId, String title);

    @Query("update Assignment a set a.enabled = false where a.course.id = ?1 and a.professor = ?#{principal.professor} and a.enabled = true")
    @Modifying
    void deleteAllAssignmentsRelatedToCourse(long courseId);
    
    @Query("select a from Assignment a where a.course.id = ?1 and a.accessCode = ?2 and a.professor = ?#{principal.professor} and a.enabled = true")
    Assignment accessCodeExistsForCourse(long accessCode, long courseId);

//    @Query("select q from Question q where q.course.id = ?1 and q.id not in " +
//            "(select qa.question.id from QuestionAssignment qa where qa.assignment.id = ?2 and qa.professor = ?#{principal.professor} and qa.enabled = true) " +
//            "and q.professor = ?#{principal.professor} and q.enabled = true")
//    @Transactional
//    List<Question> listQuestionsByCourseNotAssociatedWithAnAssignment(long courseId, long assigmentId);
}