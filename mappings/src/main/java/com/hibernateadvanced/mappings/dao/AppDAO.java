package com.hibernateadvanced.mappings.dao;

import com.hibernateadvanced.mappings.entity.Instructor;

public interface AppDAO {

    void save(Instructor instructor);
    Instructor findInstructorById(int id);
    void deleteById(int id);
}
