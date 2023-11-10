package com.hibernateadvanced.mappings;

import com.hibernateadvanced.mappings.dao.AppDAO;
import com.hibernateadvanced.mappings.entity.Course;
import com.hibernateadvanced.mappings.entity.Instructor;
import com.hibernateadvanced.mappings.entity.InstructorDetail;
import com.hibernateadvanced.mappings.entity.Review;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class MappingsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MappingsApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(AppDAO appDAO){
		return runner -> {
//			createInstructor(appDAO);
//			findByID(appDAO);
//			deleteById(appDAO);
//			findInstructorDetailById(appDAO);
//			createInstructorDetail(appDAO);
//			deleteInstructorDetailById(appDAO);
//			createInstructorWithCourses(appDAO);
//			findInstructorsWithCourses(appDAO);
//			findInstructorsWithCoursesJoinFetch(appDAO);
//			updateInstructor(appDAO);
//			updateCourse(appDAO);
			deleteCourseById(appDAO);
//			addCourseAndReviews(appDAO);
//			findCourseAndReviews(appDAO);
		};
	}

	private void findCourseAndReviews(AppDAO appDAO) {
		Course course = appDAO.findCourseAndReviews(10);
		System.out.println(course);
		System.out.println(course.getReviews());
	}

	private void addCourseAndReviews(AppDAO appDAO) {
		Review review1 = new Review("Nice");
		Review review2 = new Review("Worst");
		Course course = new Course("Course1");
		course.addReview(review1);
		course.addReview(review2);
		appDAO.saveCourse(course);
		System.out.println("Done");
	}

	private void deleteCourseById(AppDAO appDAO) {
		appDAO.deleteCourseById(10);
		System.out.println("done");
	}

	private void updateCourse(AppDAO appDAO) {
		Course course = appDAO.findCourseById(12);
		course.setTitle("Heelo");
		appDAO.updateCourse(course);
		System.out.println("Done");
	}

	private void updateInstructor(AppDAO appDAO) {
		Instructor instructor = appDAO.findInstructorById(2);
		instructor.setEmail("abc@gmail.com");
		appDAO.updateInstructor(instructor);
		System.out.println("done");
	}

	private void findInstructorsWithCoursesJoinFetch(AppDAO appDAO) {
		Instructor instructor = appDAO.getInstructorWithCoursesJoinFetch(2);
		System.out.println(instructor);
		System.out.println(instructor.getCourses());
	}

	private void findInstructorsWithCourses(AppDAO appDAO) {
		Instructor instructor = appDAO.findInstructorById(2);
		List<Course> courses = appDAO.getCoursesForInstructor(instructor.getId());
		instructor.setCourses(courses);
		System.out.println(instructor);
		System.out.println(instructor.getCourses());
	}

	private void createInstructorWithCourses(AppDAO appDAO) {
		Instructor tempinstructor = new Instructor("Nandha","Kumar","abcd@gmal.com");
		InstructorDetail tempinstructorDetail = new InstructorDetail("yt1", "guitar");
		tempinstructor.setInstructorDetail(tempinstructorDetail);
		Course course1 = new Course("Course1");
		Course course2 = new Course("Course2");
		tempinstructor.add(course1);
		tempinstructor.add(course2);
		appDAO.save(tempinstructor);
		System.out.println("Done");
	}

	private void deleteInstructorDetailById(AppDAO appDAO) {
		appDAO.deleteInstructorDetailById(7);
	}

	private void createInstructorDetail(AppDAO appDAO) {
		Instructor tempinstructor = new Instructor("instructor","name1","abc1@gmal.com");
		InstructorDetail tempinstructorDetail = new InstructorDetail("yt1", "play");
		tempinstructor.setInstructorDetail(tempinstructorDetail);
		tempinstructorDetail.setInstructor(tempinstructor);
		appDAO.saveInstructorDetail(tempinstructorDetail);
	}

	//	InstructorDetail find by id bi directional
	private void findInstructorDetailById(AppDAO appDAO) {
		InstructorDetail instructorDetail =  appDAO.findInstructorDetailById(2);
		System.out.println(instructorDetail);
		System.out.println(instructorDetail.getInstructor());
	}

	private void deleteById(AppDAO appDAO) {
		appDAO.deleteById(2);
	}

	//	Retrieve data by id - instructor
	private void findByID(AppDAO appDAO) {
		Instructor instructor = appDAO.findInstructorById(2);
		System.out.println(instructor);
		System.out.println(instructor.getInstructorDetail());
	}

	//	Create both instructor detail and instructor and persist them in db - instructor
	private void createInstructor(AppDAO appDAO) {
//		Instructor tempinstructor = new Instructor("name","kumar","abc@gmal.com");
//		InstructorDetail tempinstructorDetail = new InstructorDetail("yt1", "draw");
		Instructor tempinstructor = new Instructor("instructor","name2","abcd@gmal.com");
		InstructorDetail tempinstructorDetail = new InstructorDetail("yt2", "guitar");
		tempinstructor.setInstructorDetail(tempinstructorDetail);
		appDAO.save(tempinstructor);
	}
}
