package br.com.devdojo.examgenerator.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import br.com.devdojo.examgenerator.persistence.model.Course;

public interface CourseRepository extends CustomJpaRepository<Course, Long>{
	@Query("select c from Course c where c = ?1 and c.professor = ?#{principal.professor} and c.enabled = true")
	Course findOne(Course course);
	
	@Query("select c from Course c where c.name like %?1% and professor = ?#{principal.professor} and c.enabled = true")
	List<Course> listCoursesByName(String name);
}
