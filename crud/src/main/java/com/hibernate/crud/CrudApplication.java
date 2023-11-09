package com.hibernate.crud;

import com.hibernate.crud.dao.StudentDAO;
import com.hibernate.crud.entity.Student;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.security.auth.kerberos.DelegationPermission;
import java.util.List;

@SpringBootApplication
public class CrudApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrudApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(StudentDAO studentDAO){
		return runner -> {
			createStudent(studentDAO);
//			getStudent(studentDAO);
//			queryForStudents(studentDAO);
//			queryForStudentsByLastName(studentDAO);
//			updateStudent(studentDAO);
//			updateMultiple(studentDAO);
//			deleteStudent(studentDAO);
//			deleteAll(studentDAO);
		};
	}

	private void deleteAll(StudentDAO studentDAO) {
		int num = studentDAO.deleteAll();
		System.out.println(num);
	}

	//	Delete student by ID
	private void deleteStudent(StudentDAO studentDAO) {
		studentDAO.deleteStudent(1);
	}

	//	Replace old email by new Email
	private void updateMultiple(StudentDAO studentDAO) {
		String oldEmail = "abcd@yopmail.com";
		String newEmail = "abcd@gmail.com";
		int n = studentDAO.updateMultiple(newEmail, oldEmail);
		System.out.println(n);
	}

	//	get a student update it by setter and persist it
	private void updateStudent(StudentDAO studentDAO) {
		Student student = studentDAO.findById(1);
		student.setEmail("abcd@gmail.com");
		studentDAO.update(student);
		System.out.println(student);
	}

//	Get Students By last name
	private void queryForStudentsByLastName(StudentDAO studentDAO) {
		List<Student> studentList = studentDAO.findByLastName("1");
		studentList.stream()
				.forEach(student -> System.out.println(student));
	}

	//	Find All Students
	private void queryForStudents(StudentDAO studentDAO) {
		List<Student> studentList = studentDAO.findAll();
		studentList.stream()
				.forEach(student -> System.out.println(student));
	}

//	Get a student by ID
	private void getStudent(StudentDAO studentDAO) {
		Student tempStudent = new Student("Name", "4", "abcg@gmail.com");
		studentDAO.save(tempStudent);
		Student student = studentDAO.findById(tempStudent.getId());
		System.out.println(student);
	}

//	Add a student to DB
	private void createStudent(StudentDAO studentDAO){
		Student tempStudent1 = new Student("Name", "1", "abcd@gmail.com");
		Student tempStudent2 = new Student("Name", "2", "abce@gmail.com");
		Student tempStudent3 = new Student("Name", "3", "abcf@gmail.com");

		studentDAO.save(tempStudent1);
		studentDAO.save(tempStudent2);
		studentDAO.save(tempStudent3);
		System.out.println("Saved student");
	}
}
