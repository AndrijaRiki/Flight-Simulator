package test;

import java.awt.Frame;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;

import airports.Airport;
import flights.Flight;
import gui.AirportDataInput;
import gui.FileInput;
import gui.FlightDataInput;

public class Test {

	public static void main(String[] args) {
		new FileInput(new Frame());
		Vector<Airport> aerodromi = new Vector<Airport>();
		Vector<Flight> letovi = new Vector<Flight>();
		
		aerodromi = Airport.allAirports;
		for(Airport a : aerodromi) {
			System.out.println(a);
		}
		
		letovi = Flight.allFlights;
		for(Flight l : letovi) {
			System.out.println(l);
		}
		
		System.exit(0);
	}
}
