package com.springcore.constructorinjection.controller;


import com.springcore.constructorinjection.common.Coach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    //defining for dependency
    private Coach myCoach;

    //Since Coach has only one implementation it can autowire automatically
    @Autowired
    public Controller(@Qualifier("swimCoach") Coach theCoach){
        myCoach = theCoach;
    }

    @GetMapping("/dailyworkout")
    public String DailyWorkout(){
        return myCoach.getDailyWorkout();
    }

}
