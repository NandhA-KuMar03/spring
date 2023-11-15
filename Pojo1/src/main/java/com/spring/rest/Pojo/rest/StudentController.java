package com.spring.rest.Pojo.rest;


import com.spring.rest.Pojo.entity.Student;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class StudentController {

    private List<Student> theStudents;

    @PostConstruct
    public void loadStudents(){
        theStudents = new ArrayList<>();
        theStudents.add(new Student("Nandha", "Kumar"));
        theStudents.add(new Student("Name", "Example2"));
        theStudents.add(new Student("Name", "Example3"));
    }

    @GetMapping("/students")
    public List<Student> getStudents(){
        return theStudents;
    }

    @GetMapping("/students/{index}")
    public Student getStudent(@PathVariable int index){
        if(index>=theStudents.size() || index<0)
            throw new StudentNotFoundException("Student not found for index" + index);
        return theStudents.get(index);
    }
}
