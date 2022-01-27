package br.com.devdojo.examgenerator.endpoint.v1.course;

import java.util.Collections;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import br.com.devdojo.examgenerator.endpoint.v1.ProfessorEndpointTest;
import br.com.devdojo.examgenerator.persistence.model.Course;
import br.com.devdojo.examgenerator.persistence.model.Professor;
import br.com.devdojo.examgenerator.persistence.repository.CourseRepository;
import br.com.devdojo.examgenerator.persistence.repository.ProfessorRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CourseEndpointTest {
	@Mock
	private CourseRepository courseRepository;
	@Mock
	private ProfessorRepository professorRepository;
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	private HttpEntity<Void> professorHeader;
	private HttpEntity<Void> wrongHeader;
	private Course course = mockCourse();
	private Professor professor = ProfessorEndpointTest.mockProfessor();
	
	private Course mockCourse() {
		return Course.builder()
				.id(1L)
				.name("Java")
				.professor(ProfessorEndpointTest.mockProfessor())
				.build();
	}
	
	@BeforeEach
	void configProfessorHeader() {
		String body = "{\"username\":\"william\",\"password\":\"devdojo\"}";
		HttpHeaders headers  = testRestTemplate.postForEntity("/login", body, String.class).getHeaders();
		this.professorHeader = new HttpEntity<>(headers);
	}
	
	@BeforeEach
	void configWrongHeader() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "1111");
		this.wrongHeader = new HttpEntity<>(headers); 
	}
	
	@BeforeEach
	void setUp() {
		BDDMockito.when(courseRepository.findByIdAndProfessor(course.getId(), professor)).thenReturn(course);
		BDDMockito.when(courseRepository.findAllByProfessor(professor)).thenReturn(Collections.singletonList(course));
		BDDMockito.when(courseRepository.findByNameAndProfessor("java", professor)).thenReturn(course);
		BDDMockito.doNothing().when(courseRepository).deleteByIdAndProfessor(1L, professor);
		BDDMockito.when(courseRepository.save(course)).thenReturn(course);
	}
	
	@Test
	void getCourseById_Returns403_WhenTokenIsWrong() {
		ResponseEntity<Course> exchange = testRestTemplate.exchange("/v1/professor/course/1", HttpMethod.GET, wrongHeader, Course.class);
		
		Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(403);
	}
	
	@Test
	void listCourses_Returns403_WhenTokenIsWrong() {
		ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/list", HttpMethod.GET, wrongHeader, String.class);
		
		Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(403);
	}
	
	@Test
	void getCourseByName_Returns404_WhenCourseDoesNotExist() {
		ResponseEntity<Course> exchange = testRestTemplate.exchange("/v1/professor/course/list/xaxa", HttpMethod.GET, professorHeader, Course.class);
		
		Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(404);
	}
	
	@Test
	void getCourseByName_Returns200_WhenSuccessful() {
		ResponseEntity<Course> exchange = testRestTemplate.exchange("/v1/professor/course/list/java", HttpMethod.GET, professorHeader, Course.class);
		
		Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
	}
	
	@Test
	void getCourseById_Returns200_WhenSucessful() {
		ResponseEntity<Course> exchange = testRestTemplate.exchange("/v1/professor/course/1", HttpMethod.GET, professorHeader, Course.class);
		
		Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
	}
	
	@Test
	void delete_Returns200_WhenSuccessful() {
		Long id = 1L;
		ResponseEntity<Void> exchange = testRestTemplate.exchange("/v1/professor/course/{id}", HttpMethod.GET, professorHeader, Void.class, id);
		
		Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
	}
	
	@Test
	void delete_Returns404_WhenNotFound() {
		Long id = -1L;
		ResponseEntity<Void> exchange = testRestTemplate.exchange("/v1/professor/course/{id}", HttpMethod.GET, professorHeader, Void.class, id);
		
		Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(404);
	}
	
	@Test
	void create_Returns400_WhenNameIsNull() {
		Course course = courseRepository.findByIdAndProfessor(1L, professor);
		course.setName(null);
		
		ResponseEntity<String> exchange = testRestTemplate
				.exchange("/v1/professor/course/", HttpMethod.POST, new HttpEntity<>(course, professorHeader.getHeaders()), String.class);
		System.out.println(exchange);
		
		Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(400);
	}
	
	@Test
	void create_Returns200_WhenSuccessful() {
		ResponseEntity<String> exchange = testRestTemplate
				.exchange("/v1/professor/course/", HttpMethod.POST, new HttpEntity<>(course, professorHeader.getHeaders()), String.class);
		
		Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
	}
	
}


















