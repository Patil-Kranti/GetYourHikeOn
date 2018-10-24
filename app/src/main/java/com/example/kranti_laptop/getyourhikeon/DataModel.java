package com.example.kranti_laptop.getyourhikeon;

public class DataModel {

    String name,location;
    int id_;
    String image;


    public DataModel(String name,String location, int id_, String image) {
        this.name = name;
        this.location = location;
        this.id_ = id_;
        this.image=image;
    }

    public String getName() {
        return name;
    }
    public String getLocation() {
        return location;
    }
    public String getImage() {
        return image;
    }
    public int getId() {
        return id_;
    }
}