package br.com.devdojo.examgenerator.endpoint.v1.course;

import java.io.Serializable;

import org.springframework.stereotype.Service;

import br.com.devdojo.examgenerator.exception.ResourceNotFoundException;
import br.com.devdojo.examgenerator.persistence.model.Course;
import br.com.devdojo.examgenerator.persistence.repository.CourseRepository;

@Service
public class CourseService implements Serializable{
	CourseRepository courseRepository;
	
	public CourseService(CourseRepository courseRepository) {
		this.courseRepository = courseRepository;
	}
	
	public void throwResourceNotFoundIfCourseDoesNotExist(Course course) {
		if(course == null || course.getId() == null) {
			throw new ResourceNotFoundException("Course not found");
		}
	}
}
