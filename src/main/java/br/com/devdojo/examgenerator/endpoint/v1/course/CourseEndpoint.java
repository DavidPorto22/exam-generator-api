package br.com.devdojo.examgenerator.endpoint.v1.course;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.devdojo.examgenerator.persistence.model.ApplicationUser;
import br.com.devdojo.examgenerator.persistence.model.Course;
import br.com.devdojo.examgenerator.persistence.model.Professor;
import br.com.devdojo.examgenerator.persistence.repository.CourseRepository;
import br.com.devdojo.examgenerator.util.EndpointUtil;

@RestController
@RequestMapping("v1/professor/course")
public class CourseEndpoint {
	private final CourseRepository courseRepository;
	private final EndpointUtil endpointUtil;
	private final CourseService courseService;
	
	public CourseEndpoint(CourseRepository courseRepository, EndpointUtil endpointUtil, CourseService courseService) {
		this.courseRepository = courseRepository;
		this.endpointUtil = endpointUtil;
		this.courseService = courseService;
	}
	
	@GetMapping(path = "/{id}")
	public ResponseEntity<?> getCourseById(@PathVariable Long id) {
		return endpointUtil
				.returnObjectOrNotFound(courseRepository.findByIdAndProfessor(id, endpointUtil.extractProfessorFromToken()));
	}
	
	@GetMapping(path = "/list")
	public ResponseEntity<?> listCourses(){
		return endpointUtil
				.returnObjectOrNotFound(courseRepository.findAllByProfessor(endpointUtil.extractProfessorFromToken()));
	}
	
	@GetMapping(path = "/list/{name}")
	public ResponseEntity<?> getCourseByName(@PathVariable String name){
		return endpointUtil
				.returnObjectOrNotFound(courseRepository.findByNameAndProfessor(name, endpointUtil.extractProfessorFromToken()));
	}
	
	@DeleteMapping(path = "/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id){
		Professor professor = endpointUtil.extractProfessorFromToken();
		courseService.throwResourceNotFoundIfCourseDoesNotExist(courseRepository.findByIdAndProfessor(id, professor));
		courseRepository.deleteByIdAndProfessor(id, professor);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PutMapping
	public ResponseEntity<?> update(@Valid @RequestBody Course course) {
		courseService.throwResourceNotFoundIfCourseDoesNotExist(courseRepository.findByIdAndProfessor(course.getId(), endpointUtil.extractProfessorFromToken()));
		courseRepository.save(course);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody Course course) {
		course.setProfessor(endpointUtil.extractProfessorFromToken());
		return new ResponseEntity<>(courseRepository.save(course), HttpStatus.OK);
	}
	
}










