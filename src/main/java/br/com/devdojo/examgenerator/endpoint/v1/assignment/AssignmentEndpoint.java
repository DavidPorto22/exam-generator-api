package br.com.devdojo.examgenerator.endpoint.v1.assignment;

import java.util.concurrent.ThreadLocalRandom;

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
import br.com.devdojo.examgenerator.persistence.model.Assignment;
import br.com.devdojo.examgenerator.persistence.model.Course;
import br.com.devdojo.examgenerator.persistence.model.Professor;
import br.com.devdojo.examgenerator.persistence.model.Question;
import br.com.devdojo.examgenerator.persistence.repository.AssignmentRepository;
import br.com.devdojo.examgenerator.persistence.repository.CourseRepository;
import br.com.devdojo.examgenerator.persistence.repository.QuestionRepository;
import br.com.devdojo.examgenerator.util.EndpointUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("v1/professor/course/assignment")
@Api(description = "Operations related to courses' assignment")
public class AssignmentEndpoint {
	private final AssignmentRepository assignmentRepository;
	private final CourseRepository courseRepository;
	private final EndpointUtil endpointUtil;
	private final GenericService genericService;
	private final CascadeDeleteService deleteService;
	
	public AssignmentEndpoint(AssignmentRepository assignmentRepository, CourseRepository courseRepository, EndpointUtil endpointUtil, GenericService questionService, CascadeDeleteService deleteService) {
		this.assignmentRepository = assignmentRepository;
		this.courseRepository = courseRepository;
		this.endpointUtil = endpointUtil;
		this.genericService = questionService;
		this.deleteService = deleteService;
	}
	
	@GetMapping(path = "/{id}")
	@ApiOperation(value = "Return an assignment based on it's id", response = Assignment.class)
	public ResponseEntity<?> getAssignmentById(@PathVariable Long id) {
		return endpointUtil
				.returnObjectOrNotFound(assignmentRepository.findOne(id));
	}
	
	@GetMapping(path = "/list/{courseId}")
	@ApiOperation(value = "Return a list of assignments related to course", response = Assignment[].class)
	public ResponseEntity<?> listAssignments(@PathVariable long courseId,
			@RequestParam(value="title", defaultValue="") String title){
		return new ResponseEntity<>(assignmentRepository.listAssignmentsByCourseAndTitle(courseId, title), HttpStatus.OK);
	}
	
	@DeleteMapping(path = "/{id}")
	@Transactional
	@ApiOperation(value = "Delete a specific assignment return 200 Ok with no body")
	public ResponseEntity<?> delete(@PathVariable Long id){
		validateAssignmentExistenceOnDB(id);
		deleteService.deleteAssignmentAndAllRelatedEntities(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PutMapping
    @ApiOperation(value = "Update assignment and return 200 Ok with no body")
	public ResponseEntity<?> update(@Valid @RequestBody Assignment assignment) {
		validateAssignmentExistenceOnDB(assignment.getId());
		assignmentRepository.save(assignment);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping
    @ApiOperation(value = "Create assignment and return the assignment created")
	public ResponseEntity<?> create(@Valid @RequestBody Assignment assignment) {
		genericService.throwResourceNotFoundIfDoesNotExist(assignment.getCourse(), courseRepository, "Course not found");
		assignment.setProfessor(endpointUtil.extractProfessorFromToken());
		assignment.setAccessCode(generateAccessCode(assignment.getCourse().getId()));
		return new ResponseEntity<>(assignmentRepository.save(assignment), HttpStatus.OK);
	}

	private void validateAssignmentExistenceOnDB(Long id) {
		genericService.throwResourceNotFoundIfDoesNotExist(id, assignmentRepository, "Assignment not found");
	}
	
	private String generateAccessCode(long courseId) {
		long accessCode = ThreadLocalRandom.current().nextLong(1000, 10000);
		while(assignmentRepository.accessCodeExistsForCourse(accessCode, courseId) != null) {
			generateAccessCode(courseId);
		} 
		return String.valueOf(accessCode);
	}
	
}










