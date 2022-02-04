package br.com.devdojo.examgenerator.endpoint.v1.question;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.devdojo.examgenerator.endpoint.v1.genericservice.GenericService;
import br.com.devdojo.examgenerator.persistence.model.Course;
import br.com.devdojo.examgenerator.persistence.model.Professor;
import br.com.devdojo.examgenerator.persistence.model.Question;
import br.com.devdojo.examgenerator.persistence.repository.CourseRepository;
import br.com.devdojo.examgenerator.persistence.repository.QuestionRepository;
import br.com.devdojo.examgenerator.util.EndpointUtil;

@RestController
@RequestMapping("v1/professor/course/question")
public class QuestionEndpoint {
	private final QuestionRepository questionRepository;
	private final EndpointUtil endpointUtil;
	private final GenericService genericService;
	
	public QuestionEndpoint(QuestionRepository questionRepository, EndpointUtil endpointUtil, GenericService questionService) {
		this.questionRepository = questionRepository;
		this.endpointUtil = endpointUtil;
		this.genericService = questionService;
	}
	
	@GetMapping(path = "/{id}")
	public ResponseEntity<?> getQuestionById(@PathVariable Long id) {
		return endpointUtil
				.returnObjectOrNotFound(questionRepository.getById(id));
	}
	
	@GetMapping(path = "/list/{courseId}")
	public ResponseEntity<?> listQuestions(@PathVariable long courseId,
			@RequestParam(value="title", defaultValue="") String title){
		return new ResponseEntity<>(questionRepository.listQuestionsByCourseAndTitle(courseId, title), HttpStatus.OK);
	}
	
	@DeleteMapping(path = "/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id){
		genericService.throwResourceNotFoundIfDoesNotExist(id, questionRepository, "Question not found");
		questionRepository.deleteById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PutMapping
	public ResponseEntity<?> update(@Valid @RequestBody Question question) {
		genericService.throwResourceNotFoundIfDoesNotExist(question, questionRepository, "Question not found");
		questionRepository.save(question);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody Question question) {
		question.setProfessor(endpointUtil.extractProfessorFromToken());
		return new ResponseEntity<>(questionRepository.save(question), HttpStatus.OK);
	}

}










