package com.springcore.springconfigurationdiexercise.common;

public class CarService implements Service{

    public CarService(){
        System.out.println("Car bean created");
    }

    @Override
    public String MajorService() {
        return "Car Major Service";
    }

    @Override
    public String MinorService() {
        return "Car Minor Service";
    }
}
