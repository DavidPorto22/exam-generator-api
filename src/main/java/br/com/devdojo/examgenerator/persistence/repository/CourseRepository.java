package br.com.devdojo.examgenerator.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.devdojo.examgenerator.persistence.model.Course;
import br.com.devdojo.examgenerator.persistence.model.Professor;

public interface CourseRepository extends JpaRepository<Course, Long>{
	Course findByIdAndProfessor(Long id, Professor professor);

	Course findByNameAndProfessor(String name, Professor professor);

	List<Course> findAllByProfessor(Professor professor);
	
	void deleteByIdAndProfessor(Long id, Professor professor);
}
