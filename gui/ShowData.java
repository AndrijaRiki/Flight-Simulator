package gui;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Checkbox;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import airports.Airport;
import flights.Flight;
import timeHandling.Timer;

public class ShowData extends Frame {
	private static final long serialVersionUID = 1L;
	private GridLayout airportsGrid = new GridLayout(0, 4, 15, 10);
	public Panel airportsPanel = new Panel(airportsGrid);
	private Map<String, Checkbox> checkboxMap = new HashMap<>();
	private Canvas drawingCanvas;
	
	public ShowData(Canvas canvas) {
		this.drawingCanvas = canvas;
	}
	
	public Checkbox getCheckboxByCode(String code) {
        return checkboxMap.get(code);
    }

	public void showAirports(Timer timer) {
		Vector<Airport> airports = Airport.allAirports;
		
		setTitle("Airport List");
		setBounds(250, 50, 300, 700);
		
		removeAll();
		airportsPanel.removeAll();
		checkboxMap.clear();
		
		// Header row
		airportsPanel.add(new Label("Code", Label.CENTER));
		airportsPanel.add(new Label("X", Label.CENTER));
        airportsPanel.add(new Label("Y", Label.CENTER));
        airportsPanel.add(new Label("Activate", Label.CENTER));

        // Data rows
        for (Airport a : airports) {
        	Checkbox airportCheckbox = new Checkbox("", true);
        	airportCheckbox.setName(a.getCode());
        	
        	airportsPanel.add(new Label(a.getCode(), Label.CENTER));
        	airportsPanel.add(new Label(String.valueOf(a.getX()), Label.CENTER));
            airportsPanel.add(new Label(String.valueOf(a.getY()), Label.CENTER));
            airportsPanel.add(airportCheckbox);
            
            airportCheckbox.addItemListener(e -> {
            	drawingCanvas.repaint();
            	a.setVisibleState(!a.getVisibleState());
            	
            	if(a.isBlinkingActive() && !airportCheckbox.getState()) {
            		a.stopBlinking();
            		timer.go();
            	}
            	timer.reset();
            });
            
            checkboxMap.put(a.getCode(), airportCheckbox);
        }

        add(airportsPanel, BorderLayout.CENTER);
        
        pack();
        setVisible(true);
	}
	
	public void refreshAirports(Timer timer) {
		Vector<Airport> airports = Airport.allAirports;
		
		for(int i = 0; i < airports.size(); i++) {
			Airport a = airports.get(i);
			if(checkboxMap.containsKey(a.getCode())) continue;
			
			Checkbox airportCheckbox = new Checkbox("", a.getVisibleState());
        	airportCheckbox.setName(a.getCode());
        	airportCheckbox.setState(a.getVisibleState());
        	
        	airportsPanel.add(new Label(a.getCode(), Label.CENTER));
        	airportsPanel.add(new Label(String.valueOf(a.getX()), Label.CENTER));
            airportsPanel.add(new Label(String.valueOf(a.getY()), Label.CENTER));
            airportsPanel.add(airportCheckbox);

            airportCheckbox.addItemListener(e -> {
            	drawingCanvas.repaint();
            	a.setVisibleState(airportCheckbox.getState());
            	
            	if(a.isBlinkingActive() && !airportCheckbox.getState()) {
            		a.stopBlinking();
            		timer.go();
            	}
            	timer.reset();
            });
            checkboxMap.put(a.getCode(), airportCheckbox);
        }
		airportsPanel.validate();
		airportsPanel.repaint();
		pack();
	}
	
	public void showFlights(Timer timer) {
		Vector<Flight> flights = Flight.allFlights;
		
		if(flights.size() == 0) {
			PopupMsg.showMsg("No flights loaded. Please import flights first.");
			return;
		}
		
		setTitle("Flight List");
		setBounds(250, 50, 300, 700);
        
        Panel grid = new Panel(new GridLayout(flights.size() + 1, 5, 15, 10));

        // Header
        grid.add(new Label("From", Label.CENTER));
        grid.add(new Label("To", Label.CENTER));
        grid.add(new Label("Hour", Label.CENTER));
        grid.add(new Label("Min", Label.CENTER));
        grid.add(new Label("Duration", Label.CENTER));

        for (Flight f : flights) {
            grid.add(new Label(f.getDepartureCode(), Label.CENTER));
            grid.add(new Label(f.getArrivalCode(), Label.CENTER));
            grid.add(new Label(String.valueOf(f.getStartHour()), Label.CENTER));
            grid.add(new Label(String.valueOf(f.getStartMinute()), Label.CENTER));
            grid.add(new Label(String.valueOf(f.getDuration()), Label.CENTER));
        }

        removeAll(); // Ensure the frame is clear before adding new grid
        add(grid, BorderLayout.CENTER);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                dispose();
            }
        });
        
        timer.reset();
        pack();
        setVisible(true);
	}
}