package br.com.devdojo.examgenerator.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.devdojo.examgenerator.persistence.model.Professor;

public interface ProfessorRepository extends JpaRepository<Professor, Long>{
	Professor findByEmail(String email);
}
