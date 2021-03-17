package com.mycompany;

import java.util.UUID;

public class User {
    private String userID;
    private String userName;
    private Building building = null;

    public User(String userName, Building building) {
        this.userID = UUID.randomUUID().toString();
        this.userName = userName;
        this.building = building;
    }
    
    public User(String userID, String userName, Building building) {
    	this.userID = userID;
    	this.userName = userName;
    	this.building = building;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuildings(Building building) {
        this.building = building;
    }
    
    // Method to create json string for post request
    @Override
    public String toString() {
    	String buildingID = this.building.getBuildingID();
    	String output = "";
    	output += "{\r\n" + 
    			"    \"TableName\":\"Users\",\r\n" + 
    			"    \"Item\":{\r\n" + 
    			"        \"userID\":{\r\n" + 
    			"            \"S\":\""+userID+"\"\r\n" + 
    			"        },\r\n" + 
    			"        \"userName\":{\r\n" + 
    			"            \"S\":\""+ userName +"\"\r\n" + 
    			"        },\r\n" + 
    			"        \"building\":{\r\n" + 
    			"            \"S\":\""+ buildingID+"\"\r\n" + 
    			"        }\r\n" + 
    			"    }\r\n" + 
    			"}";
    	return output;
    }
    
}