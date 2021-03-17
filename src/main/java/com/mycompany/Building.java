package com.mycompany;


import java.util.ArrayList;
import java.util.List;


public class Building {
    private String buildingID;
    private String name;
    private String location;
    private List<Elevator> elevators = new ArrayList<Elevator>();

    public Building(String buildingID, String name, String location, List<Elevator> elevators) {
        this.buildingID = buildingID;
        this.name = name;
        this.location = location;
        this.elevators = elevators;
    }

    public String getBuildingID() {
        return buildingID;
    }

    public void setBuildingID(String buildingID) {
        this.buildingID = buildingID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Elevator> getElevators() {
        return elevators;
    }

    public void setElevators(List<Elevator> elevators) {
        this.elevators = elevators;
    }
    
}

