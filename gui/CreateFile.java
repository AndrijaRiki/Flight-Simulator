package gui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

import airports.Airport;
import flights.Flight;

public class CreateFile extends Dialog {
	private static final long serialVersionUID = 1L;
	private TextField filenameField;
	private CheckboxGroup selectionGroup;
	private Checkbox airportsCheck;
	private Checkbox flightsCheck;
	private Button confirmButton = new Button("CONFIRM");
	private Button cancelButton = new Button("CANCEL");
	private String selectedDataType = "airports";

	public CreateFile(Frame owner) {
		super(owner, "Save to CSV", true);
		setBounds(500, 300, 300, 300);
		
		setLayout(new BorderLayout(10, 10));
		
		// North Panel - Filename input
		Panel northPanel = new Panel(new FlowLayout(FlowLayout.LEFT));
        filenameField = new TextField(20);
        filenameField.setText("data.csv");
        northPanel.add(new Label("Filename:"));
        northPanel.add(filenameField);
        
        // Center Panel - Checkboxes for data type
        Panel centerPanel = new Panel(new FlowLayout(FlowLayout.LEFT));
        selectionGroup = new CheckboxGroup();
        airportsCheck = new Checkbox("Airports", selectionGroup, true);
        flightsCheck = new Checkbox("Flights", selectionGroup, false);
        
        confirmButton.addActionListener(e -> {
        	selectedDataType = airportsCheck.getState() ? "airports" : "flights";
        	String filename = filenameField.getText();
        	
        	try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
				if(selectedDataType.equals("airports")) {
					Vector<Airport> data = Airport.allAirports; 
					for(Airport a : data) {
						writer.print(a.getCode() + "," + a.getX() + "," + a.getY() + "\n");
					}
				} else if(selectedDataType.equals("flights")) {
					Vector<Flight> data = Flight.allFlights;
					for(Flight f : data) {
						writer.print(f.getDepartureCode() + "," + f.getArrivalCode() + "," + 
								     f.getStartHour() + "," + f.getStartMinute() + "," + 
								     f.getDuration() + "\n");
					}
				}
				writer.flush();
			} catch (IOException ex) {
				PopupMsg.showMsg("Error while saving file: " + ex.getMessage());
				ex.printStackTrace();
			}
        	        	
        	dispose();
        });

        cancelButton.addActionListener(e -> {
        	dispose();
        });
        
        centerPanel.add(airportsCheck);
        centerPanel.add(flightsCheck);
        
        add(northPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        
        // South Panel - Action buttons
        Panel southPanel = new Panel(new FlowLayout(FlowLayout.RIGHT));
        southPanel.add(cancelButton);
        southPanel.add(confirmButton);
        add(southPanel, BorderLayout.SOUTH);
        
        addWindowListener(new WindowAdapter() {
        	@Override
        	public void windowClosing(WindowEvent e) {
        		dispose();
        	}
 		});
        
        pack();
        setLocationRelativeTo(owner);
        setVisible(true);
	}
	
	public static void main(String[] args) {
		new CreateFile(new Frame());
	}
}