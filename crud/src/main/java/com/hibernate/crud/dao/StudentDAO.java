package com.hibernate.crud.dao;

import com.hibernate.crud.entity.Student;

import java.util.List;

public interface StudentDAO {

    void save(Student student);
    Student findById(Integer id);
    List<Student> findAll();
    List<Student> findByLastName(String lastName);
    void update(Student student);
    int updateMultiple(String newEmail, String email);
    void deleteStudent(Integer id);
    int deleteAll();
}
