package com.mycompany;


import java.util.ArrayList;
import java.util.List;

public class Elevator {
    private String elevatorID;
    private String name;
    private List<String> floors;
    private String currentFloor;
    private String[] states = {"Up","Down","Stopped","Out of Service"};
    private String currentState;

    public Elevator(String elevatorID, String name, String currentFloor, String currentState, List<String> floors) {
        this.elevatorID = elevatorID;
        this.name = name;
        this.currentFloor = currentFloor;
        this.currentState = currentState;
        this.floors = floors;
    }

    public String getElevatorID() {
        return elevatorID;
    }

    public void setElevatorID(String elevatorID) {
        this.elevatorID = elevatorID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getFloors() {
        return floors;
    }

    public void setFloors(List<String> floors) {
        this.floors = floors;
    }

    public String getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(String currentFloor) {
        this.currentFloor = currentFloor;
    }

    public String[] getStates() {
        return states;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }
    
    
}

