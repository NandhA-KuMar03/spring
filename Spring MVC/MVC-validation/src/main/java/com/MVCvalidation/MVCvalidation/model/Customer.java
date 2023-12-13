package com.MVCvalidation.MVCvalidation.model;

import com.MVCvalidation.MVCvalidation.Validation.CourseCode;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class Customer {

    @NotNull(message = "is required")
    @Size(min=1, message = "is required")
    private String firstName;

    private String lastName;

    @Min(value = 18, message = "Greater than 18")
    @Max(value = 50, message = "Less than 50")
    private Integer age;

    @Pattern(regexp="^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = "Enter a valid mail ID")
    private String email;

    @CourseCode(value = "ABC", message = "Start with ABC")
    private String courseCode;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
}
