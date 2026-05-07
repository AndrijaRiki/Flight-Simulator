package flights;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;

import airports.Airport;
import checkings.CheckInputData;
import gui.PopupMsg;

public class Flight implements CheckInputData {
	private String departureCode;
	private String arrivalCode;
	private int startHour;
	private int startMinute;
	private int flightDuration;
	private boolean isValid;
	
	public static Vector<Flight> allFlights = new Vector<Flight>();
	
	public Flight(String filePath) throws FileNotFoundException, IndexOutOfBoundsException {
		readFlightFile(filePath);
	}
	
	public Flight(String departureCode, String arrivalCode, int startHour, int startMinute, int flightDuration) {
		this.isValid = checkCode(departureCode) && checkCode(arrivalCode) && 
				       checkStartTime(startHour, startMinute) && checkDuration(flightDuration);
		
		if(isValid) {
			this.departureCode = departureCode;
			this.arrivalCode = arrivalCode;
			this.startHour = startHour;
			this.startMinute = startMinute;
			this.flightDuration = flightDuration;
			
			allFlights.add(this);
			
			if (Airport.waitingQueue.containsKey(departureCode) && Airport.checkExistingAirport(arrivalCode)) {
	            Airport.waitingQueue.get(departureCode).add(this);
	        }
		}
	}

	@Override
	public boolean checkCoords(double coord) {
		// Not implemented for Flight
		return false;
	}

	@Override
    public boolean checkCode(String code) {
		String msg = "";
    	if(code == null || code.isEmpty()) {
    		msg = "Airport code cannot be empty.\nPlease enter a 3-letter code.";
    		PopupMsg.showMsg(msg);
    		return false;
    	}
    	if(code.length() != 3) {
    		msg = "Airport code must contain exactly 3 letters.";
    		PopupMsg.showMsg(msg);
    		return false;
    	}
    	if(!code.matches("[A-Z]{3}")) {
    		msg = "Airport code must consist only of uppercase letters.";
    		PopupMsg.showMsg(msg);
    		return false;
    	}
    	return true;
    }

	@Override
	public boolean checkStartTime(int h, int m) {
		if(h < 0 || h > 23) {
			PopupMsg.showMsg("Hours must be in range 00-23.");
			return false;
		}
		if(m < 0 || m > 59) {
			PopupMsg.showMsg("Minutes must be in range 00-59.");
			return false;
		}
		return true;
	}

	@Override
	public boolean checkDuration(int m) {
		if(m <= 0) {
			PopupMsg.showMsg("Flight duration must be a positive number.");
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return isValid ? "(" + departureCode + ", " + arrivalCode + ", " + 
	           String.format("%02d:%02d", startHour, startMinute) + ", " + flightDuration + " min)" : "Invalid flight";
	}

	public String getDepartureCode() {
		return departureCode;
	}
	
	public String getArrivalCode() {
		return arrivalCode;
	}
	
	public int getStartHour() {
		return startHour;
	}
	
	public int getStartMinute() {
		return startMinute;
	}
	
	public int getDuration() {
		return flightDuration;
	}
	
	public void readFlightFile(String filePath) throws FileNotFoundException, IndexOutOfBoundsException {
		File file = new File(filePath);
		Scanner scanner = new Scanner(file);
		String line;
		while(scanner.hasNextLine()) {
			line = scanner.nextLine();
			String[] data = line.split(",");
			// Basic cleanup of UTF-8 BOM and whitespace
			new Flight(data[0].replace("\uFEFF", "").trim(), data[1].trim(), 
					   Integer.parseInt(data[2].trim()), Integer.parseInt(data[3].trim()), 
					   Integer.parseInt(data[4].trim()));
		}
		scanner.close();
	}

	public static void drawFlights(Graphics2D g2d, int width, int height, int sqSize) {
		double scaleX = (width / 2.0) / 90.0;
		double scaleY = (height / 2.0) / 90.0;
		
		for(Flight f : allFlights) {
			Airport a = Airport.getAirport(f.getDepartureCode());
			if (a != null) {
				int x = (int)(a.getX() * scaleX);
				int y = (int)(a.getY() * scaleY);
				
				g2d.setColor(Color.BLUE);
				g2d.fillOval(x - sqSize/2, y - sqSize/2, sqSize, sqSize);
			}
		}
	}
}