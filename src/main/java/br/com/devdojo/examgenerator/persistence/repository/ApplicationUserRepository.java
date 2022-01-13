package br.com.devdojo.examgenerator.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.devdojo.examgenerator.persistence.model.ApplicationUser;

public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {
	ApplicationUser findByUsername(String username);
}
