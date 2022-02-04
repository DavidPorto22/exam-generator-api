package br.com.devdojo.examgenerator.endpoint.v1.course;

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
import br.com.devdojo.examgenerator.persistence.repository.CourseRepository;
import br.com.devdojo.examgenerator.util.EndpointUtil;

@RestController
@RequestMapping("v1/professor/course")
public class CourseEndpoint {
	private final CourseRepository courseRepository;
	private final EndpointUtil endpointUtil;
	private final GenericService genericService;
	
	public CourseEndpoint(CourseRepository courseRepository, EndpointUtil endpointUtil, GenericService genericService) {
		this.courseRepository = courseRepository;
		this.endpointUtil = endpointUtil;
		this.genericService = genericService;
	}
	
	@GetMapping(path = "/{id}")
	public ResponseEntity<?> getCourseById(@PathVariable Long id) {
		return endpointUtil
				.returnObjectOrNotFound(courseRepository.getById(id));
	}
	
	@GetMapping(path = "/list")
	public ResponseEntity<?> listCourses(@RequestParam(value="name", defaultValue="") String name){
		return new ResponseEntity<>(courseRepository.listCoursesByName(name), HttpStatus.OK);
	}
	
	@DeleteMapping(path = "/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id){
		genericService.throwResourceNotFoundIfDoesNotExist(id, courseRepository, "Course not found");
		courseRepository.deleteById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PutMapping
	public ResponseEntity<?> update(@Valid @RequestBody Course course) {
		genericService.throwResourceNotFoundIfDoesNotExist(course, courseRepository, "Course not found");
		courseRepository.save(course);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody Course course) {
		course.setProfessor(endpointUtil.extractProfessorFromToken());
		return new ResponseEntity<>(courseRepository.save(course), HttpStatus.OK);
	}

}










