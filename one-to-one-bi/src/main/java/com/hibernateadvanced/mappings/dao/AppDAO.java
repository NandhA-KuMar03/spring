package com.hibernateadvanced.mappings.dao;

import com.hibernateadvanced.mappings.entity.Instructor;
import com.hibernateadvanced.mappings.entity.InstructorDetail;

public interface AppDAO {

    void save(Instructor instructor);
    Instructor findInstructorById(int id);
    void deleteById(int id);
    InstructorDetail findInstructorDetailById(int id);
    void saveInstructorDetail(InstructorDetail instructorDetail);
    void deleteInstructorDetailById(int id);
}
