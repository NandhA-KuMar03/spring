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

    //this will call track coach as it has primary
    @Autowired
    public Controller(Coach theCoach){
        myCoach = theCoach;
    }

//    this will call the qualifier bean id
//    @Autowired
//    public Controller(@Qualifier("cricketCoach") Coach theCoach){
//        myCoach = theCoach;
//    }



    @GetMapping("/dailyworkout")
    public String DailyWorkout(){
        return myCoach.getDailyWorkout();
    }

}
