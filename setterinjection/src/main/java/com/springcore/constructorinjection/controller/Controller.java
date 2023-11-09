package com.springcore.constructorinjection.controller;


import com.springcore.constructorinjection.common.Coach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    //defining for dependency
    private Coach myCoach;


    //Setter method for the Coach class - setter injection
    @Autowired
    public void setCoach(Coach myCoach){
        this.myCoach = myCoach;
    }

    @GetMapping("/dailyworkout")
    public String DailyWorkout(){
        return myCoach.getDailyWorkout();
    }

}
