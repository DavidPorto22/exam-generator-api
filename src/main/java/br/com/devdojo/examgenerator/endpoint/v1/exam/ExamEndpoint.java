package br.com.devdojo.examgenerator.endpoint.v1.exam;

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
import br.com.devdojo.examgenerator.exception.ResourceNotFoundException;
import br.com.devdojo.examgenerator.persistence.model.Choice;
import br.com.devdojo.examgenerator.persistence.model.Course;
import br.com.devdojo.examgenerator.persistence.model.Professor;
import br.com.devdojo.examgenerator.persistence.model.Question;
import br.com.devdojo.examgenerator.persistence.model.QuestionAssignment;
import br.com.devdojo.examgenerator.persistence.repository.AssignmentRepository;
import br.com.devdojo.examgenerator.persistence.repository.ChoiceRepository;
import br.com.devdojo.examgenerator.persistence.repository.CourseRepository;
import br.com.devdojo.examgenerator.persistence.repository.QuestionAssignmentRepository;
import br.com.devdojo.examgenerator.persistence.repository.QuestionRepository;
import br.com.devdojo.examgenerator.util.EndpointUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("v1/student/exam")
@Api(description = "Operations to associate questions to an assignment")
public class ExamEndpoint {
	private final QuestionRepository questionRepository;
	private final QuestionAssignmentRepository questionAssignmentRepository;
	private final ChoiceRepository choiceRepository;
	private final AssignmentRepository assignmentRepository;
	private final EndpointUtil endpointUtil;
	private final CascadeDeleteService deleteService;
	private final GenericService genericService;
	
	public ExamEndpoint(QuestionRepository questionRepository, QuestionAssignmentRepository questionAssignmentRepository, ChoiceRepository choiceRepository,AssignmentRepository assignmentRepository, EndpointUtil endpointUtil, GenericService questionService, CascadeDeleteService deleteService) {
		this.questionRepository = questionRepository;
		this.questionAssignmentRepository = questionAssignmentRepository;
		this.choiceRepository = choiceRepository;
		this.assignmentRepository = assignmentRepository;
		this.endpointUtil = endpointUtil;
		this.genericService = questionService;
		this.deleteService = deleteService;
	}
	
	@GetMapping(path = "choice/{accessCode}")
    @ApiOperation(value = "List all Choices based on the Questions by the assignment access code", response = Choice[].class)
	public ResponseEntity<?> listQuestionsFromQuestionAssignmentByAssignmentAccessCode(@PathVariable String accessCode) {
		List<Question> questions = questionAssignmentRepository.listQuestionsFromQuestionAssignmentByAssignmentAccessCode(accessCode);
		if(questions.isEmpty()) throw new ResourceNotFoundException("Invalid access code");
		List<Long> questionsId = questions.stream().map(question -> question.getId()).collect(Collectors.toList());
		List<Choice> choices = choiceRepository.listChoicesByQuestionsIdForStudent(questionsId);
		return new ResponseEntity<>(choices, HttpStatus.OK);
	}
}










