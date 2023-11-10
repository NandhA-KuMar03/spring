package com.hibernateadvanced.mappings.dao;

import com.hibernateadvanced.mappings.entity.Course;
import com.hibernateadvanced.mappings.entity.Instructor;
import com.hibernateadvanced.mappings.entity.InstructorDetail;
import com.hibernateadvanced.mappings.entity.Review;
import com.hibernateadvanced.mappings.entity.Student;

import java.util.List;

public interface AppDAO {

    void save(Instructor instructor);
    Instructor findInstructorById(int id);
    void deleteById(int id);
    InstructorDetail findInstructorDetailById(int id);
    void saveInstructorDetail(InstructorDetail instructorDetail);
    void deleteInstructorDetailById(int id);
    List<Course> getCoursesForInstructor(int id);
    Instructor getInstructorWithCoursesJoinFetch(int id);
    void updateInstructor(Instructor instructor);
    void updateCourse(Course course);
    Course findCourseById(int id);
    void deleteCourseById(int id);
    void saveCourse(Course course);
    Course findCourseAndReviews(int id);
    Course findCourseAndStudentByCourseId(int id);
    Student findStudentAndCoursesByStudentId(int id);
}
