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

public class AirportDataInput extends Dialog {
	private static final long serialVersionUID = 1L;
	private TextField kodField = new TextField(10);
	private TextField xField = new TextField(10);
	private TextField yField = new TextField(10);
	private Button submit = new Button("Dodaj");
	
	public boolean checkDataEmpty() {
		if(kodField.getText().isEmpty() || xField.getText().isEmpty() || yField.getText().isEmpty()) {
			return false;
		}
		return true;
	}
	
	KeyAdapter enterListener = new KeyAdapter() {
		@Override
    	public void keyTyped(KeyEvent e) {
    		if(Character.toUpperCase(e.getKeyChar()) == KeyEvent.VK_ENTER) {
    			readData();
    			if(checkDataEmpty())
    				dispose();
    		}
    	}
	};
	
	private void readData() {
		if(checkDataEmpty()) {
    		double xData = Double.parseDouble(xField.getText());
    		double yData = Double.parseDouble(yField.getText());
    		String kodData = kodField.getText();
    		
    		Airport a = new Airport(kodData, xData, yData);
    		System.out.println(a);
    		kodField.setText("");
    		xField.setText("");
    		yField.setText("");
    	} else {
    		PopupMsg.showMsg(this, "Popunite podatke prvo");
    	}
	}
	
	public AirportDataInput(Frame owner) {
		super(owner, "Unos podataka o aerodromu", true);
		setLayout(new BorderLayout());
		setBounds(700, 300, 500, 500);
		setResizable(false);
		
		kodField.addKeyListener(enterListener);
		xField.addKeyListener(enterListener);
		yField.addKeyListener(enterListener);
		submit.addKeyListener(enterListener);
		
		submit.addActionListener((ae) -> {
			readData();
			dispose();
		});

		Panel form = new Panel(new GridLayout(3, 2, 5, 5));
        form.add(new Label("X:"));
        form.add(xField);
        form.add(new Label("Y:"));
        form.add(yField);
        form.add(new Label("Kod:"));
        form.add(kodField);

        add(form, BorderLayout.CENTER);

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
		new AirportDataInput(new Frame());
	}
}
