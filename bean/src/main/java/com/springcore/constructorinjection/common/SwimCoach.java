package com.springcore.constructorinjection.common;

public class SwimCoach implements Coach{

    public SwimCoach(){
        System.out.println("Class name" + getClass().getName());
    }

    @Override
    public String getDailyWorkout() {
        return "Swim daily";
    }
}
