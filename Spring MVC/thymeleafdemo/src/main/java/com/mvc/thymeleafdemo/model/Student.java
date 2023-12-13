package com.mvc.thymeleafdemo.model;

import java.util.List;

public class Student {

    private String firstName;

    private String lastName;

    private String country;

    private String favoriteCodingLanguage;

    private List<String> favoriteOS;

    public Student() {
    }

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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFavoriteCodingLanguage() {
        return favoriteCodingLanguage;
    }

    public void setFavoriteCodingLanguage(String favoriteCodingLanguage) {
        this.favoriteCodingLanguage = favoriteCodingLanguage;
    }

    public List<String> getFavoriteOS() {
        return favoriteOS;
    }

    public void setFavoriteOS(List<String> favoriteOS) {
        this.favoriteOS = favoriteOS;
    }
}
