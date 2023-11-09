package com.springcore.springconfigurationdiexercise.common;

public class BikeService implements Service{

    public BikeService(){
        System.out.println("Bike bean created");
    }

    @Override
    public String MajorService() {
        return "Bike Major Service";
    }

    @Override
    public String MinorService() {
        return "Bike Minor Service";
    }
}
