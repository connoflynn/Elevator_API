package com.mycompany;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Main {
	private static HttpURLConnection connection;
	private static String baseURL = "https://uzdekua4c8.execute-api.eu-west-1.amazonaws.com/Test/";
	private static List<Building> buildings = new ArrayList<>();
	private static List<Elevator> elevators = new ArrayList<>();

	public static void main(String[] args) {
			getElevators();
			getBuildings();
			
			String userID;
			String username;
			String buildingID;
			Building userBuilding = null;
			User user = null;
			List<Elevator> buildingElevators = new ArrayList<>();;
			switch(args[0]) {
			case "-cu":
				username = args[1];
				buildingID = args[2];
				for(Building building : buildings) {
					if(building.getBuildingID().equals(buildingID)) {
						userBuilding = building;
					}
				}
				user = new User(username, userBuilding);
				postUser(user);
				System.out.println("User created! User ID: " + user.getUserID());
				break;
			case "-mu":
				userID = args[1];
				username = args[2];
				buildingID = args[3];
				for(Building building : buildings) {
					if(building.getBuildingID().equals(buildingID)) {
						userBuilding = building;
					}
				}
				postUser(new User(userID, username, userBuilding));
				
				break;
			case "-es":
				userID = args[1];
				user = getUser(userID);
				userBuilding = user.getBuilding();
				buildingElevators = userBuilding.getElevators();
				
				System.out.println("Building " + userBuilding.getBuildingID());
				System.out.println("Current Elevator Status:");
				String out = "";
				for (Elevator elevator : buildingElevators) {
					out += "Elevator: " + elevator.getElevatorID() + " Current Floor: " + elevator.getCurrentFloor() +"\n";
				}
				System.out.println(out);
				
				break;
			case "-se":
				userID = args[1];
				user = getUser(userID);
				userBuilding = user.getBuilding();
				buildingElevators = userBuilding.getElevators();
				Elevator nearestElevator = buildingElevators.get(0);
				for(int i = 1; i < buildingElevators.size(); i++) {
					if((Integer.parseInt(nearestElevator.getCurrentFloor()) - 0) > (Integer.parseInt(buildingElevators.get(i).getCurrentFloor())-0)) {
						nearestElevator = buildingElevators.get(i);
					}
				}
				System.out.println("Elevator " +nearestElevator.getElevatorID()+ " is arriving!");
				System.out.println("Available floors in this building are:");
				String output = "";
				for(int i = 0; i < nearestElevator.getFloors().size(); i++) {
					output += nearestElevator.getFloors().get(i) + " ";
				}
				System.out.println(output);
				
				break;
			case "-sf":
				userID = args[1];
				user = getUser(userID);
				String elevatorID = args[2];
				String floor = args[3];
				Elevator userElevator = null;
				for(Elevator elevator : elevators) {
					if(elevator.getElevatorID().equals(elevatorID)) {
						userElevator = elevator;
					}
				}
				
				System.out.println("Elevator " +userElevator.getElevatorID()+" is taking you to floor " + floor);
				userElevator.setCurrentFloor(floor);
				changeFloor(userElevator);
				System.out.println("You have arrived!");
				break;
			case "-commands":
				System.out.println("Commands:");
				System.out.println("-cu (create user) <username> <building>");
				System.out.println("-mu (modify user) <userID> <username> <building>");
				System.out.println("-es (elevator status) <userID>");
				System.out.println("-se (summon elevator) <userID>");
				System.out.println("-sf (select floor) <userID> <elevatorID> <floorNumber>");
				System.out.println();
				break;
			default:
				System.out.println("Invalid Arguements!");
			}
			
			connection.disconnect();
	}
	
	//Method to retrieve the elevator information from the database using a Http request
	public static void getElevators() {
		String request = "{\r\n" + 
				"    \"RequestItems\": {\r\n" + 
				"        \"Elevators\": {\r\n" + 
				"            \"Keys\": [\r\n" + 
				"                {\r\n" + 
				"                    \"elevatorID\":{\"S\":\"1A\"}\r\n" + 
				"                },\r\n" + 
				"                {\r\n" + 
				"                    \"elevatorID\":{\"S\":\"2A\"}\r\n" + 
				"                },\r\n" + 
				"                {\r\n" + 
				"                    \"elevatorID\":{\"S\":\"3A\"}\r\n" + 
				"                },\r\n" + 
				"                {\r\n" + 
				"                    \"elevatorID\":{\"S\":\"1B\"}\r\n" + 
				"                },\r\n" + 
				"                {\r\n" + 
				"                    \"elevatorID\":{\"S\":\"2B\"}\r\n" + 
				"                },\r\n" + 
				"                {\r\n" + 
				"                    \"elevatorID\":{\"S\":\"1C\"}\r\n" + 
				"                },\r\n" + 
				"                {\r\n" + 
				"                    \"elevatorID\":{\"S\":\"2C\"}\r\n" + 
				"                },\r\n" + 
				"                {\r\n" + 
				"                    \"elevatorID\":{\"S\":\"3C\"}\r\n" + 
				"                }\r\n" + 
				"            ]\r\n" + 
				"        }\r\n" + 
				"    }\r\n" + 
				"}";
		try {
			URL url = new URL(baseURL + "buildings");
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json; utf-8");
			connection.setRequestProperty("Accept", "application/json");
			connection.setDoOutput(true);
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try(OutputStream os = connection.getOutputStream()) {
		    byte[] input = request.getBytes("utf-8");
		    os.write(input, 0, input.length);			
		} catch (IOException e) {
			e.printStackTrace();
		}
		try(BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
		    StringBuilder response = new StringBuilder();
		    String responseLine = null;
		    while ((responseLine = br.readLine()) != null) {
		        response.append(responseLine.trim());
		    }
		    parseElevators(response.toString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Method to parse the elevator response and create elevator objects
	public static void parseElevators(String response) {
		JSONArray items = new JSONObject(response).getJSONObject("Responses").getJSONArray("Elevators");
		for (int i =0; i<items.length(); i++) {
			JSONObject item = items.getJSONObject(i);
			String elevatorID = new String(item.getJSONObject("elevatorID").getString("S"));
			String currentFloor = new String(item.getJSONObject("currentFloor").getString("S"));
			String state = new String(item.getJSONObject("state").getString("S"));
			String name = new String(item.getJSONObject("name").getString("S"));
			List<String> floors = new ArrayList<>();
			JSONArray floorsArray = item.getJSONObject("floors").getJSONArray("L");
			for(int j = 0; j < floorsArray.length(); j++) {
				String floor = floorsArray.getJSONObject(j).getString("S");
				floors.add(floor);
			}
			elevators.add(new Elevator(elevatorID, name, currentFloor, state, floors));
		}
	}
	
	public static void changeFloor(Elevator elevator) {
		String elevatorID = elevator.getElevatorID();
		String floor = elevator.getCurrentFloor();
		String json = "{\r\n" + 
				"    \"TableName\": \"Elevators\",\r\n" + 
				"    \"Key\": {\r\n" + 
				"        \"elevatorID\": {\r\n" + 
				"            \"S\": \""+elevatorID+"\"\r\n" + 
				"        }\r\n" + 
				"    },\r\n" + 
				"    \"UpdateExpression\": \"set currentFloor = :val1\",\r\n" + 
				"    \"ExpressionAttributeValues\": {\r\n" + 
				"        \":val1\": {\"S\": \""+floor+"\"}\r\n" + 
				"    },\r\n" + 
				"    \"ReturnValues\": \"ALL_NEW\"\r\n" + 
				"}";
		try {
			URL url = new URL(baseURL + "elevators/changefloor");
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json; utf-8");
			connection.setRequestProperty("Accept", "application/json");
			connection.setDoOutput(true);
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try(OutputStream os = connection.getOutputStream()) {
		    byte[] input = json.getBytes("utf-8");
		    os.write(input, 0, input.length);			
		} catch (IOException e) {
			e.printStackTrace();
		}
		try(BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
		    StringBuilder response = new StringBuilder();
		    String responseLine = null;
		    while ((responseLine = br.readLine()) != null) {
		        response.append(responseLine.trim());
		    }
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	//Method to retrieve the buildings information from the database using a Http request
	public static void getBuildings() {
		String request = "{\r\n" + 
				"    \"RequestItems\": {\r\n" + 
				"        \"Buildings\": {\r\n" + 
				"            \"Keys\": [\r\n" + 
				"                {\r\n" + 
				"                    \"buildingID\":{\"S\":\"A\"}\r\n" + 
				"                },\r\n" + 
				"                {\r\n" + 
				"                    \"buildingID\":{\"S\":\"B\"}\r\n" + 
				"                },\r\n" + 
				"                {\r\n" + 
				"                    \"buildingID\":{\"S\":\"C\"}\r\n" + 
				"                }\r\n" + 
				"            ]\r\n" + 
				"        }\r\n" + 
				"    }\r\n" + 
				"}";
		try {
			URL url = new URL(baseURL + "elevators");
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json; utf-8");
			connection.setRequestProperty("Accept", "application/json");
			connection.setDoOutput(true);
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try(OutputStream os = connection.getOutputStream()) {
		    byte[] input = request.getBytes("utf-8");
		    os.write(input, 0, input.length);			
		} catch (IOException e) {
			e.printStackTrace();
		}
		try(BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
		    StringBuilder response = new StringBuilder();
		    String responseLine = null;
		    while ((responseLine = br.readLine()) != null) {
		        response.append(responseLine.trim());
		    }
		    parseBuildings(response.toString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Method to parse the building response and create building objects
	public static void parseBuildings(String response) {
		JSONArray items = new JSONObject(response).getJSONObject("Responses").getJSONArray("Buildings");
		for (int i =0; i<items.length(); i++) {
			JSONObject item = items.getJSONObject(i);
			String buildingID = new String(item.getJSONObject("buildingID").getString("S"));
			String location = new String(item.getJSONObject("location").getString("S"));
			String name = new String(item.getJSONObject("name").getString("S"));
			List<Elevator> buildingElevators = new ArrayList<>();
			JSONArray elevatorsArray = item.getJSONObject("elevators").getJSONArray("L");
			for(int j = 0; j < elevatorsArray.length(); j++) {
				String elevatorID = elevatorsArray.getJSONObject(j).getString("S");
				for(Elevator elevator : elevators) {
					if(elevator.getElevatorID().equals(elevatorID)) {
						buildingElevators.add(elevator);
					}
				}
				
			}
			buildings.add(new Building(buildingID, name, location, buildingElevators));
		}
	}
	
	//method used to create new user or update existing user in dynamodb database
	public static void postUser(User user) {
		String json = user.toString();
		try {
			URL url = new URL(baseURL + "users");
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json; utf-8");
			connection.setRequestProperty("Accept", "application/json");
			connection.setDoOutput(true);
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try(OutputStream os = connection.getOutputStream()) {
		    byte[] input = json.getBytes("utf-8");
		    os.write(input, 0, input.length);			
		} catch (IOException e) {
			e.printStackTrace();
		}
		try(BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
		    StringBuilder response = new StringBuilder();
		    String responseLine = null;
		    while ((responseLine = br.readLine()) != null) {
		        response.append(responseLine.trim());
		    }
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Method to retrieve user's information from the database using a http request
	public static User getUser(String userID) {
		String request = "{\r\n" + 
				"    \"TableName\": \"Users\",\r\n" + 
				"    \"Key\": {\r\n" + 
				"        \"userID\": {\r\n" + 
				"            \"S\": \""+ userID+"\"\r\n" + 
				"        }\r\n" + 
				"    }\r\n" + 
				"}";
		try {
			URL url = new URL(baseURL + "users/retrieve");
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json; utf-8");
			connection.setRequestProperty("Accept", "application/json");
			connection.setDoOutput(true);
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try(OutputStream os = connection.getOutputStream()) {
		    byte[] input = request.getBytes("utf-8");
		    os.write(input, 0, input.length);			
		} catch (IOException e) {
			e.printStackTrace();
		}
		try(BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
		    StringBuilder response = new StringBuilder();
		    String responseLine = null;
		    while ((responseLine = br.readLine()) != null) {
		        response.append(responseLine.trim());
		    }
		    User user = parseUser(response.toString());
		    return user;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
		
	// Method to parse the user response and create a user object
	public static User parseUser(String response) {
		JSONObject item = new JSONObject(response).getJSONObject("Item");
		String userID = item.getJSONObject("userID").getString("S");
		String userName = item.getJSONObject("userName").getString("S");
		String buildingID = item.getJSONObject("building").getString("S");
		Building building = null;
		for(Building buildingi : buildings) {
			if(buildingi.getBuildingID().equals(buildingID)) {
				building = buildingi;
			}
		}
		User user = new User(userID, userName, building);
		return user;
	}

}
