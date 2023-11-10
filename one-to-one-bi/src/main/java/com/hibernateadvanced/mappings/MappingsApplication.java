package com.hibernateadvanced.mappings;

import com.hibernateadvanced.mappings.dao.AppDAO;
import com.hibernateadvanced.mappings.entity.Instructor;
import com.hibernateadvanced.mappings.entity.InstructorDetail;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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
			deleteInstructorDetailById(appDAO);
		};
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
		appDAO.deleteById(1);
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
