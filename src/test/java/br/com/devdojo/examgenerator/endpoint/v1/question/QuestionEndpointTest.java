package br.com.devdojo.examgenerator.endpoint.v1.question;

import java.util.Collections;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import br.com.devdojo.examgenerator.endpoint.v1.ProfessorEndpointTest;
import br.com.devdojo.examgenerator.endpoint.v1.course.CourseEndpointTest;
import br.com.devdojo.examgenerator.persistence.model.Question;
import br.com.devdojo.examgenerator.persistence.repository.QuestionRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class QuestionEndpointTest {
	@MockBean
	QuestionRepository questionRepository;
	@Autowired
	private TestRestTemplate testRestTemplate;
	private HttpEntity<Void> professorHeader;
	private HttpEntity<Void> wrongHeader;
	private Question question = mockQuestion();
	
	private static Question mockQuestion() {
		return Question.builder()
				.id(1L)
				.title("What is class?")
				.course(CourseEndpointTest.mockCourse())
				.professor(ProfessorEndpointTest.mockProfessor())
				.build();
	}
	
	@BeforeEach
	private void ConfigProfessorHeader() {
		String body = "{\"username\":\"william\",\"password\":\"devdojo\"}";
		HttpHeaders headers = testRestTemplate.postForEntity("/login", body, String.class).getHeaders();
		this.professorHeader = new HttpEntity<>(headers);
	}
	
	@BeforeEach
	private void ConfigWrongHeader() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "1111");
		this.wrongHeader = new HttpEntity<>(headers);
	}
	
	@BeforeEach
	private void setUp() {
		BDDMockito.when(questionRepository.getById(1L)).thenReturn(question);
		BDDMockito.when(questionRepository.listQuestionsByCourseAndTitle(1L, "")).thenReturn(Collections.singletonList(question));
		BDDMockito.when(questionRepository.listQuestionsByCourseAndTitle(1L, question.getTitle())).thenReturn(Collections.singletonList(question));
		BDDMockito.doNothing().when(questionRepository).deleteById(ArgumentMatchers.anyLong());
	}
	
	@Test
	void getQuestionById_Returns403_WhenTokenIsWrong() {
		ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/question/1", HttpMethod.GET, wrongHeader, String.class);
		
		Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(403);
	}
	
	@Test
	void listQuestions_Returns403_WhenTokenIsWrong() {
		ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/question/1?title=", HttpMethod.GET, wrongHeader, String.class);
	
		Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(403);
	}

	@Test
	void listQuestions_ReturnsEmptyList_WhenQuestionDoesNotExit() {
		ResponseEntity<List<Question>> exchange = testRestTemplate.exchange("/v1/professor/course/question/list/1?title=xaxa", HttpMethod.GET, professorHeader, new ParameterizedTypeReference<List<Question>>() {});

		Assertions.assertThat(exchange.getBody()).isEmpty();
	}
	
	@Test
	void listQuestions_Returns200_WhenSuccessful() {
		ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/question/list/1?title=what", HttpMethod.GET, professorHeader, String.class);
		
		Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
	}
	
	@Test
	void getQuestionById_Returns200_WhenSucessful() {
		ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/question/1", HttpMethod.GET, professorHeader, String.class);

		Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
	}
	
	@Test
	void delete_Returns200_WhenSuccessful() {
		ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/question/1", HttpMethod.DELETE, professorHeader, String.class);
	
		Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
	}
	
	@Test
	void delete_Returns404_WhenNotFound() {
		ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/question/-1", HttpMethod.DELETE, professorHeader, String.class);

		Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(404);
	}
	
}


















