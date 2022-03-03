package br.com.devdojo.examgenerator.endpoint.v1.question;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.devdojo.examgenerator.endpoint.v1.deleteservice.CascadeDeleteService;
import br.com.devdojo.examgenerator.endpoint.v1.genericservice.GenericService;
import br.com.devdojo.examgenerator.persistence.model.Course;
import br.com.devdojo.examgenerator.persistence.model.Professor;
import br.com.devdojo.examgenerator.persistence.model.Question;
import br.com.devdojo.examgenerator.persistence.repository.CourseRepository;
import br.com.devdojo.examgenerator.persistence.repository.QuestionRepository;
import br.com.devdojo.examgenerator.util.EndpointUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("v1/professor/course/question")
@Api(description = "Operations related to courses' question")
public class QuestionEndpoint {
	private final QuestionRepository questionRepository;
	private final CourseRepository courseRepository;
	private final EndpointUtil endpointUtil;
	private final CascadeDeleteService deleteService;
	private final GenericService genericService;
	
	public QuestionEndpoint(QuestionRepository questionRepository, CourseRepository courseRepository, EndpointUtil endpointUtil, GenericService questionService, CascadeDeleteService deleteService) {
		this.questionRepository = questionRepository;
		this.courseRepository = courseRepository;
		this.endpointUtil = endpointUtil;
		this.genericService = questionService;
		this.deleteService = deleteService;
	}
	
	@GetMapping(path = "/{id}")
    @ApiOperation(value = "Return a question based on it's id", response = Question.class)
	public ResponseEntity<?> getQuestionById(@PathVariable Long id) {
		return endpointUtil
				.returnObjectOrNotFound(questionRepository.findOne(id));
	}
	
	@GetMapping(path = "/list/{courseId}")
    @ApiOperation(value = "Return a list of question related to course", response = Question[].class)
	public ResponseEntity<?> listQuestions(@PathVariable long courseId,
			@RequestParam(value="title", defaultValue="") String title){
		return new ResponseEntity<>(questionRepository.listQuestionsByCourseAndTitle(courseId, title), HttpStatus.OK);
	}
	
	@DeleteMapping(path = "/{id}")
	@Transactional
    @ApiOperation(value = "Delete a specific question and all related choices and return 200 Ok with no body")
	public ResponseEntity<?> delete(@PathVariable Long id){
		validateQuestionExistenceOnDB(id);
		deleteService.deleteQuestionAndAllRelatedEntities(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PutMapping
    @ApiOperation(value = "Update question and return 200 Ok with no body")
	public ResponseEntity<?> update(@Valid @RequestBody Question question) {
		validateQuestionExistenceOnDB(question.getId());
		questionRepository.save(question);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	@PostMapping
    @ApiOperation(value = "Create question and return the question created", response = Question.class)
	public ResponseEntity<?> create(@Valid @RequestBody Question question) {
		genericService.throwResourceNotFoundIfDoesNotExist(question.getCourse(), courseRepository, "Course not found");
		question.setProfessor(endpointUtil.extractProfessorFromToken());
		return new ResponseEntity<>(questionRepository.save(question), HttpStatus.OK);
	}

	private void validateQuestionExistenceOnDB(Long id) {
		genericService.throwResourceNotFoundIfDoesNotExist(id, questionRepository, "Question not found");
	}
}










