package br.com.devdojo.examgenerator.endpoint.v1.course;

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
import br.com.devdojo.examgenerator.persistence.repository.CourseRepository;
import br.com.devdojo.examgenerator.util.EndpointUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("v1/professor/course")
@Api(description = "Operations related to professors' course")
public class CourseEndpoint {
	private final CourseRepository courseRepository;
	private final EndpointUtil endpointUtil;
	private final CascadeDeleteService deleteService;
	private final GenericService genericService;
	
	public CourseEndpoint(CourseRepository courseRepository, EndpointUtil endpointUtil, GenericService genericService, CascadeDeleteService deleteService) {
		this.courseRepository = courseRepository;
		this.endpointUtil = endpointUtil;
		this.genericService = genericService;
		this.deleteService = deleteService;
	}
	
	@GetMapping(path = "/{id}")
    @ApiOperation(value = "Return a course based on it's id", response = Course.class)
	public ResponseEntity<?> getCourseById(@PathVariable Long id) {
		return endpointUtil
				.returnObjectOrNotFound(courseRepository.findOne(id));
	}
	
	@GetMapping(path = "/list")
    @ApiOperation(value = "Return a list of courses related to professor", response = Course.class)
	public ResponseEntity<?> listCourses(@RequestParam(value="name", defaultValue="") String name){
		return new ResponseEntity<>(courseRepository.listCoursesByName(name), HttpStatus.OK);
	}
	
	@DeleteMapping(path = "/{id}")
	@Transactional
    @ApiOperation(value = "Delete a specific course and all related questions and choices and return 200 Ok with no body")
	public ResponseEntity<?> delete(@PathVariable Long id){
		validateCourseExistenceOnDB(id);
		deleteService.deleteCourseAndAllRelatedEntities(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PutMapping
    @ApiOperation(value = "Update course and return 200 Ok with no body")
	public ResponseEntity<?> update(@Valid @RequestBody Course course) {
		validateCourseExistenceOnDB(course.getId());
		courseRepository.save(course);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	@PostMapping
    @ApiOperation(value = "Create course and return the course created")
	public ResponseEntity<?> create(@Valid @RequestBody Course course) {
		course.setProfessor(endpointUtil.extractProfessorFromToken());
		return new ResponseEntity<>(courseRepository.save(course), HttpStatus.OK);
	}

	private void validateCourseExistenceOnDB(Long id) {
		genericService.throwResourceNotFoundIfDoesNotExist(id, courseRepository, "Course not found");
	}
}










