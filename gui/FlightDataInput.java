package gui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import airports.Airport;
import flights.Flight;

public class FlightDataInput extends Dialog {
	private static final long serialVersionUID = 1L;
	private TextField startAerodrom = new TextField(10);
	private TextField endAerodrom = new TextField(10);
	private TextField flightStartH = new TextField(2);
	private TextField flightStartMin = new TextField(2);
	private TextField flightDuration = new TextField(4);
	private Button submit = new Button("Add");
	
	private boolean checkDataEmpty() {
		return (startAerodrom.getText().isEmpty() || endAerodrom.getText().isEmpty() || flightStartH.getText().isEmpty() ||
				flightStartMin.getText().isEmpty() || flightDuration.getText().isEmpty());
	}
	
	private void readData() {
		if(!checkDataEmpty()) {
			boolean polazni = Airport.checkExistingAirport(startAerodrom.getText());
			boolean dolazni = Airport.checkExistingAirport(endAerodrom.getText());
			String kodPolazni = startAerodrom.getText();
			String kodDolazni = endAerodrom.getText();
			int startH = Integer.parseInt(flightStartH.getText());
			int startMin = Integer.parseInt(flightStartMin.getText());
			int dur = Integer.parseInt(flightDuration.getText());
			Flight let = new Flight(kodPolazni, kodDolazni, startH, startMin, dur);
			String msg;
			
			if(!polazni && !dolazni) {
				msg = "ERROR: flight " + let + " is not possible because departure and arrival airports, " + let.getDepartureCode() + " and " + let.getArrivalCode() + ", do not exist!";
				System.out.println(msg);
				new PopupMsg(this, msg);
				Flight.allFlights.remove(let);
				return;
			} else if(!polazni) {
				msg = "ERROR: flight " + let + " is not possible because departure airport " + let.getDepartureCode() + " does not exist!";
				System.out.println(msg);
				new PopupMsg(this, msg);
				Flight.allFlights.remove(let);
				return;
			} else if(!dolazni) {
				msg = "ERROR: flight " + let + " is not possible because arrival airport " + let.getArrivalCode() + " does not exist!";
				System.out.println(msg);
				Flight.allFlights.remove(let);
				new PopupMsg(this, msg);
				return;
			}
			
    		System.out.println(let);
    		
    		startAerodrom.setText("");
    		endAerodrom.setText("");
    		flightStartH.setText("");
    		flightStartMin.setText("");
    		flightDuration.setText("");
    	} else {
    		PopupMsg.showMsg(this, "Please fill in all the data first");
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
	
	public FlightDataInput(Frame owner) {
		super(owner, "Flight Data Input", true);
		setLayout(new BorderLayout());
		setBounds(700, 300, 500, 500);
		setResizable(false);
		
		startAerodrom.addKeyListener(enterListener);
		endAerodrom.addKeyListener(enterListener);
		flightStartH.addKeyListener(enterListener);
		flightStartMin.addKeyListener(enterListener);
		flightDuration.addKeyListener(enterListener);
		submit.addKeyListener(enterListener);
		
		submit.addActionListener((ae) -> {
			readData();
			dispose();
		});
		
		Panel letPanel = new Panel(new GridLayout(5, 2, 5, 5));
        letPanel.add(new Label("Departure airport:"));
        letPanel.add(startAerodrom);
        letPanel.add(new Label("Arrival airport:"));
        letPanel.add(endAerodrom);
        letPanel.add(new Label("Hour:"));
        letPanel.add(flightStartH);
        letPanel.add(new Label("Minute:"));
        letPanel.add(flightStartMin);
        letPanel.add(new Label("Duration (min):"));
        letPanel.add(flightDuration);
        
        add(letPanel, BorderLayout.CENTER);

        Panel submitPanel = new Panel();
        submitPanel.add(submit);
        add(submitPanel, BorderLayout.SOUTH);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		
		pack();
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new FlightDataInput(new Frame());
	}
	
}