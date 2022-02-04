package br.com.devdojo.examgenerator.endpoint.v1.course;

import java.util.Collections;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import br.com.devdojo.examgenerator.endpoint.v1.ProfessorEndpointTest;
import br.com.devdojo.examgenerator.persistence.model.Course;
import br.com.devdojo.examgenerator.persistence.model.Professor;
import br.com.devdojo.examgenerator.persistence.repository.CourseRepository;
import br.com.devdojo.examgenerator.persistence.repository.ProfessorRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CourseEndpointTest {
	@MockBean
	private CourseRepository courseRepository;
	@MockBean
	private ProfessorRepository professorRepository;
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	private HttpEntity<Void> professorHeader;
	private HttpEntity<Void> wrongHeader;
	private Course course = mockCourse();
	
	public static Course mockCourse() {
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
		BDDMockito.when(courseRepository.getById(course.getId())).thenReturn(course);
		BDDMockito.when(courseRepository.listCoursesByName("")).thenReturn(Collections.singletonList(course));
		BDDMockito.when(courseRepository.listCoursesByName("Java")).thenReturn(Collections.singletonList(course));
		BDDMockito.doNothing().when(courseRepository).deleteById(ArgumentMatchers.anyLong());
		BDDMockito.when(courseRepository.save(course)).thenReturn(course);
	}
	
	@Test
	void getCourseById_Returns403_WhenTokenIsWrong() {
		ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/1", HttpMethod.GET, wrongHeader, String.class);
		
		Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(403);
	}
	
	@Test
	void listCourses_Returns403_WhenTokenIsWrong() {
		ResponseEntity<String> exchange =  testRestTemplate.exchange("/v1/professor/course/list", HttpMethod.GET, wrongHeader, String.class);
		
		Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(403);
	}
	
	@Test
	void listCourses_ReturnsEmptyList_WhenCourseDoesNotExist() {
		ResponseEntity<List<Course>> exchange = testRestTemplate.exchange("/v1/professor/course/list?name=xaxa", HttpMethod.GET,
				professorHeader, new ParameterizedTypeReference<List<Course>>() {});
	
		Assertions.assertThat(exchange.getBody()).isEmpty();
	}
	
	@Test
	void listCourses_Returns200_WhenSuccessful() {
		ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/list?name=Java", HttpMethod.GET, professorHeader, String.class);
		
		Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
	}
	
	@Test
	void getCourseById_Returns200_WhenSucessful() {
		ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/1", HttpMethod.GET, professorHeader, String.class);
	
		Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
	}
	
	@Test
	void delete_Returns200_WhenSuccessful() {
		Long id = 1L;
		
		ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/{id}", HttpMethod.DELETE, professorHeader, String.class, id);
	
		Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
	}
	
	@Test
	void delete_Returns404_WhenNotFound() {
		long id = -1L;
        ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/{id}", HttpMethod.DELETE, professorHeader, String.class, id);
        Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(404);
	}	
	
//	@Test
//	void create_Returns400_WhenNameIsNull() {
//		Course course = courseRepository.getById(1L);
//		course.setName(null);
//			
//		ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course", HttpMethod.POST, new HttpEntity<>(course, professorHeader.getHeaders()), String.class);	
//	
//		Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(400);
//	}
//	
//	@Test
//	void create_Returns200_WhenSuccessful() {
//		ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course", HttpMethod.POST, new HttpEntity<>(course, professorHeader.getHeaders()), String.class);
//	
//		Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
//	}
	
}


















