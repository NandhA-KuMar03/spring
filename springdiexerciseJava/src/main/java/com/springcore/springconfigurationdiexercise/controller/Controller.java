package com.springcore.springconfigurationdiexercise.controller;


import com.springcore.springconfigurationdiexercise.common.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class Controller {

    private Service service;

    //Constructor injection
//    public Controller(@Qualifier("car") Service service){
//        this.service = service;
//    }

    //Setter injection
    @Autowired
    public void setService(@Qualifier("bike") Service service){
        this.service = service;
    }

    @GetMapping("/MajorService")
    public String GetMajorService(){
        return service.MajorService();
    }
    @GetMapping("/MinorService")
    public String GetMinorService(){
        return service.MinorService();
    }
}
