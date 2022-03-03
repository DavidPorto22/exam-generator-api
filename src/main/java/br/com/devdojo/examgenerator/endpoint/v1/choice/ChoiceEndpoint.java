package br.com.devdojo.examgenerator.endpoint.v1.choice;

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
import org.springframework.web.bind.annotation.RestController;

import br.com.devdojo.examgenerator.endpoint.v1.genericservice.GenericService;
import br.com.devdojo.examgenerator.exception.ConflictException;
import br.com.devdojo.examgenerator.persistence.model.Choice;
import br.com.devdojo.examgenerator.persistence.model.QuestionAssignment;
import br.com.devdojo.examgenerator.persistence.repository.ChoiceRepository;
import br.com.devdojo.examgenerator.persistence.repository.QuestionAssignmentRepository;
import br.com.devdojo.examgenerator.persistence.repository.QuestionRepository;
import br.com.devdojo.examgenerator.util.EndpointUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("v1/professor/course/question/choice")
@Api(description = "Operations related to questions' choice")
public class ChoiceEndpoint {
	private final ChoiceRepository choiceRepository;
	private final QuestionRepository questionRepository;
	private final QuestionAssignmentRepository questionAssignmentRepository;
	private final EndpointUtil endpointUtil;
	private final GenericService genericService;
	
	public ChoiceEndpoint(ChoiceRepository choiceRepository, QuestionRepository questionRepository, QuestionAssignmentRepository questionAssignmentRepository,EndpointUtil endpointUtil, GenericService questionService) {
		this.choiceRepository = choiceRepository;
		this.questionRepository = questionRepository;
		this.questionAssignmentRepository = questionAssignmentRepository;
		this.endpointUtil = endpointUtil;
		this.genericService = questionService;
	}
	
	@ApiOperation(value = "Return a choice based on it's id", response = Choice.class)
    @GetMapping(path = "{id}")
    public ResponseEntity<?> getChoiceById(@PathVariable long id) {
        return endpointUtil.returnObjectOrNotFound(choiceRepository.findOne(id));
    }
	
	@ApiOperation(value = "Return a list of choices related to the questionId", response = Choice[].class)
    @GetMapping(path = "list/{questionId}/")
    public ResponseEntity<?> listChoicesByQuestionId(@PathVariable long questionId) {
        return new ResponseEntity<>(choiceRepository.listChoicesByQuestionId(questionId), HttpStatus.OK);
    }

	@ApiOperation(value = "Create choice and return the choice created",
            notes = "If this choice's correctAnswer is true all other choices' correctAnswer related to this question will be updated to false")
    @PostMapping
    @Transactional
    public ResponseEntity<?> create(@Valid @RequestBody Choice choice) {
		throwResourceNotFoundExceptionIfQuestionDoesNotExist(choice);
        choice.setProfessor(endpointUtil.extractProfessorFromToken());
        Choice savedChoice = choiceRepository.save(choice);
        updateChangingOtherChoicesCorrectAnswerToFalse(choice);
        return new ResponseEntity<>(savedChoice, HttpStatus.OK);
    }
	
	@ApiOperation(value = "Update choice and return 200 Ok with no body",
            notes = "If this choice's correctAnswer is true all other choices' correctAnswer related to this question will be updated to false")
    @PutMapping
    @Transactional
	private ResponseEntity<?> update(@Valid @RequestBody Choice choice) {
		throwResourceNotFoundExceptionIfQuestionDoesNotExist(choice);
		updateChangingOtherChoicesCorrectAnswerToFalse(choice);
		choiceRepository.save(choice);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@ApiOperation(value = "Delete a specific choice and return 200 Ok with no body")
    @DeleteMapping(path = "{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
		genericService.throwResourceNotFoundIfDoesNotExist(id, choiceRepository, "Choice not found");
		Choice choice = choiceRepository.findOne(id);
		throwConflictExceptionIfQuestionIsBeingUsedInAnyAssignment(choice.getQuestion().getId());
		choiceRepository.delete(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
    	
    private void throwConflictExceptionIfQuestionIsBeingUsedInAnyAssignment(long questionId) {
    	List<QuestionAssignment> questionAssignments = questionAssignmentRepository.listQuestionAssignmentByQuestionId(questionId);
    	if(questionAssignments.isEmpty()) return;
    	String assignments = questionAssignments
    			.stream()
    			.map(qa -> qa.getAssignment().getTitle())
    			.collect(Collectors.joining(", "));
    	throw new ConflictException("This choice cannot be deleted because this question is being used in the following assignments: " + assignments);
    }
	
	private void throwResourceNotFoundExceptionIfQuestionDoesNotExist(@Valid @RequestBody Choice choice) {
		genericService.throwResourceNotFoundIfDoesNotExist(choice.getQuestion(), questionRepository, "The question related to this course was not found");
	}
	
	private void updateChangingOtherChoicesCorrectAnswerToFalse(Choice choice) {
		if(choice.isCorrectAnswer()) {
			choiceRepository.updateAllOtherChoicesCorrectAnswerToFalse(choice, choice.getQuestion());
		}
	}
}










