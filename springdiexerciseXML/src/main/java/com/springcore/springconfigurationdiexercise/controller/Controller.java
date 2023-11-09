package com.springcore.springconfigurationdiexercise.controller;


import com.springcore.springconfigurationdiexercise.common.Service;


public class Controller {

    public Controller(){
        System.out.println("Controller bean created");
    }

    private Service service;

    // Constructor injection
//    public Controller(Service service){
//        this.service = service;
//    }

    // Setter injection
    public void setService(Service service){
        this.service = service;
    }

    public String GetMajorService(){
        return service.MajorService();
    }

    public String GetMinorService(){
        return service.MinorService();
    }
}
