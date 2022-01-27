package br.com.devdojo.examgenerator.endpoint.v1;

import br.com.devdojo.examgenerator.persistence.model.Professor;

public class ProfessorEndpointTest {
	public static Professor mockProfessor() {
		return Professor.builder()
				.id(1L)
				.name("Will")
				.email("will@something.com")
				.build();
	}
}	