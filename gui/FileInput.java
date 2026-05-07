package gui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.util.Iterator;
import java.util.Vector;

import airports.Airport;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;

import flights.Flight;

public class FileInput extends Dialog {
	private static final long serialVersionUID = 1L;
	private TextField airportsFileField = new TextField(20);
	private TextField flightsFileField = new TextField(20);
	private Button submitButton = new Button("Read Data");
	
	private void readData() {
		String airportsCsv = airportsFileField.getText().trim();
		String flightsCsv = flightsFileField.getText().trim();
		try {
			if(!airportsCsv.isEmpty()) {
				new Airport(airportsCsv, this);
			}
			if(!flightsCsv.isEmpty()) {
				new Flight(flightsCsv);
			}
			validateData();
		} catch(FileNotFoundException ex) {
			String msg = "";
			if(!(new File(airportsCsv).exists())) {
				msg = "File " + airportsCsv + " does not exist!";
			} else if(!(new File(flightsCsv).exists())) {
				msg = "File " + flightsCsv + " does not exist!";
			}
			PopupMsg.showMsg(this, msg);
		} catch(IndexOutOfBoundsException ex) {
			PopupMsg.showMsg(this, "Insufficient data in files.");
		}
	}
	
	KeyAdapter enterListener = new KeyAdapter() {
		@Override
    	public void keyTyped(KeyEvent e) {
    		if(Character.toUpperCase(e.getKeyChar()) == KeyEvent.VK_ENTER) {
    			readData();
    			dispose();
    		}
    	}
	};
	
	public FileInput(Frame owner) {
		super(owner, "File Import", true);
		setLayout(new BorderLayout());
		setBounds(700, 300, 500, 500);
		setResizable(false);
		
		Panel filePanel = new Panel(new GridLayout(2, 2, 5, 5));
		filePanel.add(new Label("Airports file: "));
		filePanel.add(airportsFileField);
		filePanel.add(new Label("Flights file: "));
		filePanel.add(flightsFileField);
		
		add(filePanel, BorderLayout.CENTER);
		
		Panel submitPanel = new Panel();
		submitPanel.add(submitButton);
		add(submitPanel, BorderLayout.SOUTH);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		
		airportsFileField.addKeyListener(enterListener);
		flightsFileField.addKeyListener(enterListener);
		submitButton.addKeyListener(enterListener);
		
		submitButton.addActionListener(e -> {
			readData();
			dispose();
		});
		
		pack();
		setLocationRelativeTo(owner);
		setVisible(true);
	}
	
	public void validateData() {
		if(Airport.allAirports.isEmpty()) {
			PopupMsg.showMsg("Please import airport data first.");
			return;
		}
		
		Vector<Flight> currentFlights = Flight.allFlights;
		Iterator<Flight> it = currentFlights.iterator();
		
		while (it.hasNext()) {
		    Flight f = it.next();

		    boolean departureExists = Airport.checkExistingAirport(f.getDepartureCode());
		    boolean arrivalExists = Airport.checkExistingAirport(f.getArrivalCode());

		    String msg = null;
		    if (!departureExists && !arrivalExists) {
		        msg = "Flight " + f + " is not possible because departure and arrival airports, "
		              + f.getDepartureCode() + " and " + f.getArrivalCode() + ", do not exist!";
		        it.remove();
		    } else if (!departureExists) {
		        msg = "Flight " + f + " is not possible because departure airport " + f.getDepartureCode() + " does not exist!";
		        it.remove();
		    } else if (!arrivalExists) {
		        msg = "Flight " + f + " is not possible because arrival airport " + f.getArrivalCode() + " does not exist!";
		        it.remove();
		    }

		    if (msg != null) {
		        PopupMsg.showMsg(this, msg);
		    }
		}

		Flight.allFlights = currentFlights;
	}
}