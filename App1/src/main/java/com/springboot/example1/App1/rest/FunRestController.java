package com.springboot.example1.App1.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FunRestController {

//   Defined in application.properties file
    @Value("${player.name}")
    private String playerName;

    @Value("${team.name}")
    private String teamName;

    @GetMapping("/TeamInfo")
    public String TeamInfo(){
        return "Player Name: " + playerName + "\n Team Name: " + teamName;
    }

    @GetMapping("/")
    public String Hello() {
        return "Hello World";
    }

    @GetMapping("/workout")
    public String DailyWorkout(){
        return "Run daily";
    }


}
