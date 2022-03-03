package br.com.devdojo.examgenerator.endpoint.v1.questionassignment;

import java.util.List;
import java.util.stream.Collectors;

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
import br.com.devdojo.examgenerator.persistence.model.Choice;
import br.com.devdojo.examgenerator.persistence.model.Course;
import br.com.devdojo.examgenerator.persistence.model.Professor;
import br.com.devdojo.examgenerator.persistence.model.Question;
import br.com.devdojo.examgenerator.persistence.model.QuestionAssignment;
import br.com.devdojo.examgenerator.persistence.repository.AssignmentRepository;
import br.com.devdojo.examgenerator.persistence.repository.CourseRepository;
import br.com.devdojo.examgenerator.persistence.repository.QuestionAssignmentRepository;
import br.com.devdojo.examgenerator.persistence.repository.QuestionRepository;
import br.com.devdojo.examgenerator.util.EndpointUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("v1/professor/course/assignment/questionassignment")
@Api(description = "Operations to associate questions to an assignment")
public class QuestionAssignmentEndpoint {
	private final QuestionRepository questionRepository;
	private final QuestionAssignmentRepository questionAssignmentRepository;
	private final AssignmentRepository assignmentRepository;
	private final EndpointUtil endpointUtil;
	private final CascadeDeleteService deleteService;
	private final GenericService genericService;
	
	public QuestionAssignmentEndpoint(QuestionRepository questionRepository, QuestionAssignmentRepository questionAssignmentRepository, AssignmentRepository assignmentRepository, EndpointUtil endpointUtil, GenericService questionService, CascadeDeleteService deleteService) {
		this.questionRepository = questionRepository;
		this.questionAssignmentRepository = questionAssignmentRepository;
		this.assignmentRepository = assignmentRepository;
		this.endpointUtil = endpointUtil;
		this.genericService = questionService;
		this.deleteService = deleteService;
	}
	
	@GetMapping(path = "/{courseId}/{assignmentId}")
	@ApiOperation(value = "Return valid questions for that course (valid questions are questions with at least two choices" +
            " and one of the choices is correct and it is not already associated with that assignment)", response = Question[].class)
	public ResponseEntity<?> listValidQuestionsForAnAssignment(@PathVariable long courseId, @PathVariable long assignmentId) {
		List<Question> questions = questionRepository.listQuestionsByCourseNotAssociatedWithAnAssignment(courseId, assignmentId);
		List<Question> validQuestions = questions
				.stream()
				.filter(question -> hasMoreThanOneChoice(question) && hasOnlyOneCorrectAnswer(question))
				.collect(Collectors.toList());
		return new ResponseEntity<>(validQuestions, HttpStatus.OK);
	}

	private boolean hasOnlyOneCorrectAnswer(Question question) {
		return question.getChoices().stream().filter(Choice::isCorrectAnswer).count() == 1;
	}

	private boolean hasMoreThanOneChoice(Question question) {
		return question.getChoices() != null && question.getChoices().size() > 1;
	}
	
	@PostMapping
    @ApiOperation(value = "Associate a question to an assignment and return the QuestionAssignment created", response = QuestionAssignment[].class)
	public ResponseEntity<?> create(@Valid @RequestBody QuestionAssignment questionAssignment) {
		validateQuestionAndAssignmentExistance(questionAssignment);
		if(isQuestionAlreadyAssociatedWithAssignment(questionAssignment)) {
			return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
		}
		questionAssignment.setProfessor(endpointUtil.extractProfessorFromToken());
		return new ResponseEntity<>(questionAssignmentRepository.save(questionAssignment), HttpStatus.OK);
	}

	private void validateQuestionAndAssignmentExistance(QuestionAssignment questionAssignment) {
		genericService.throwResourceNotFoundIfDoesNotExist(questionAssignment.getQuestion(), questionRepository, "Question not found");
		genericService.throwResourceNotFoundIfDoesNotExist(questionAssignment.getAssignment(), assignmentRepository, "Assignment not found");
	}
	
	private boolean isQuestionAlreadyAssociatedWithAssignment(QuestionAssignment questionAssignment) {
		long questionId = questionAssignment.getQuestion().getId();
		long assignmentId = questionAssignment.getAssignment().getId();
		List<QuestionAssignment> questionAssignments = questionAssignmentRepository.listQuestionAssignmentByQuestionAndAssignment(questionId, assignmentId);
		return !questionAssignments.isEmpty();
	}
	
	@DeleteMapping(path = "/{questionAssignment}")
	@Transactional
    @ApiOperation(value = "Delete a specific question assigned to an assignment and return 200 Ok with no body")
	public ResponseEntity<?> delete(@PathVariable long questionAssignment){
		validateQuestionAssignmentOnDB(questionAssignment);
		deleteService.deleteQuestionAssignmentAndAllRelatedEntities(questionAssignment);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PutMapping
    @ApiOperation(value = "Update QuestionAssignment and return 200 Ok with no body")
	public ResponseEntity<?> update(@Valid @RequestBody QuestionAssignment questionAssignment) {
		validateQuestionAssignmentOnDB(questionAssignment.getId());
		questionAssignmentRepository.save(questionAssignment);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	private void validateQuestionAssignmentOnDB(Long questionAssignmentId) {
		genericService.throwResourceNotFoundIfDoesNotExist(questionAssignmentId, questionAssignmentRepository, "QuestionAssignment not found");
	}
	
	@GetMapping(path = "{assignmentId}")
    @ApiOperation(value = "List all QuestionAssignment associated with assignmentId", response = QuestionAssignment[].class)
	public ResponseEntity<?> list(@PathVariable long assignmentId) {
		return new ResponseEntity<>(questionAssignmentRepository.listQuestionAssignmentByAssignmentId(assignmentId), HttpStatus.OK);
	}
}










