package airports;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.Vector;

import checkings.CheckInputData;
import flights.Flight;
import gui.PopupMsg;
import gui.ShowData;

public class Airport implements CheckInputData {
    private int x, y;
    private String code;
    private boolean isValid;
    private boolean isVisible;
    private boolean isBlinkingColor; // state for current color cycle
    private boolean isBlinkingEnabled; // state for thread loop
    private Thread blinkThread;
    
    public static Vector<Airport> allAirports = new Vector<Airport>();
    public static Map<String, TreeSet<Flight>> waitingQueue = new HashMap<>();
    
    public Airport(String filePath, Dialog owner) throws FileNotFoundException, IndexOutOfBoundsException {
    	readAirportFile(filePath, owner);
    }
    
    public Airport(String code, double x, double y) {
        this.isValid = checkCoords(x) && checkCoords(y) && checkCode(code);
        if (isValid) {
            this.x = (int)x;
            this.y = (int)y;
            this.code = code;
            if(!exists(this)) {
            	allAirports.add(this);
            	setVisibleState(true);
            	setBlinkingState(false);
            	
            	// Using custom comparator for flights in the waiting queue
            	waitingQueue.put(code, new TreeSet<>(new Comparator<Flight>() {
            	    @Override
            	    public int compare(Flight f1, Flight f2) {
            	        int total1 = f1.getStartHour() * 60 + f1.getStartMinute();
            	        int total2 = f2.getStartHour() * 60 + f2.getStartMinute();
            	        
            	        if (total1 != total2)
            	            return Integer.compare(total1, total2);
            	        
            	        // Break tie by destination code if times are identical
            	        return f1.getArrivalCode().compareTo(f2.getArrivalCode());
            	    }
            	}));
            }
        }
    }
    
    public boolean getIsValid() {
    	return isValid;
    }
    
    public String getCode() {
    	return code;
    }

    public int getX() {
    	return x;
    }
    
    public int getY() {
    	return y;
    }
    
    public boolean getVisibleState() {
    	return isVisible;
    }
    
    public void setVisibleState(boolean state) {
    	this.isVisible = state;
    }
    
    public boolean getBlinkingState() {
    	return isBlinkingColor;
    }
    
    public void setBlinkingState(boolean state) {
    	this.isBlinkingColor = state;
    }
    
    public boolean isBlinkingActive() {
    	return isBlinkingEnabled;
    }
    
    public static Airport getAirport(String code) {
    	for(Airport a : allAirports) {
    		if(a.getCode().equals(code)) {
    			return a;
    		}
    	}
    	return null;
    }
    
    @Override
    public boolean checkCoords(double coord) {
    	if(coord >= -90 && coord <= 90)
    		return true;
    	PopupMsg.showMsg("Airport coordinate must be between -90 and 90.");
        return false;
    }

    @Override
    public boolean checkCode(String code) {
    	if(code == null || code.isEmpty()) {
    		PopupMsg.showMsg("Airport code cannot be empty.");
    		return false;
    	}
    	if(code.length() != 3) {
    		PopupMsg.showMsg("Airport code must be exactly 3 characters long.");
    		return false;
    	}
    	if(!code.matches("[A-Z]{3}")) {
    		PopupMsg.showMsg("Airport code must consist of uppercase letters.");
    		return false;
    	}
    	return true;
    }
    
    @Override
    public String toString() {
        return isValid ? code + "(" + x + ", " + y + ")" : "Invalid airport";
    }

	@Override
	public boolean checkStartTime(int h, int m) {
		return false; // Implemented in Flight class
	}

	@Override
	public boolean checkDuration(int m) {
		return false; // Implemented in Flight class
	}
	
	private boolean exists(Airport a) {
	    for (Airport existing : allAirports) {
	        if (existing.code.equals(a.code)) {
	            PopupMsg.showMsg("An airport with the same code already exists!");
	            return true;
	        }

	        if (existing.x == a.x && existing.y == a.y) {
	            PopupMsg.showMsg("Airport coordinates overlap with an existing airport!");
	            return true;
	        }
	    }
	    return false;
	}

	public void readAirportFile(String filePath, Dialog owner) throws FileNotFoundException, IndexOutOfBoundsException {
		File file = new File(filePath);
		try (Scanner scanner = new Scanner(file)) {
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] data = line.split(",");
				new Airport(data[0].replace("\uFEFF", "").trim(), 
						    Double.parseDouble(data[1].trim()), 
						    Double.parseDouble(data[2].trim()));
			}
		} catch (NumberFormatException e) {
			PopupMsg.showMsg(owner, "Error in file format: " + filePath);
		}
	}
	
	public static boolean checkExistingAirport(String code) {
		for(Airport a : allAirports) {
			if(a.code.equals(code))
				return true;
		}
		return false;
	}
	
	public static void drawAirports(Graphics2D g2d, int width, int height, int sqSize, ShowData data, boolean b, ShowData data2) {
		double scaleX = (width / 2.0) / 90.0;
		double scaleY = (height / 2.0) / 90.0;
		
		for (Airport a : allAirports) {
			if(!a.getVisibleState()) continue;
			
			int xPos = (int)(a.getX() * scaleX);
			int yPos = (int)(a.getY() * scaleY);
			
			if (a.getBlinkingState()) {
				g2d.setColor(Color.RED);
			} else {
				g2d.setColor(Color.GRAY);
			}
			
			g2d.fillRect(xPos - sqSize / 2, yPos - sqSize / 2, sqSize, sqSize);
			
			// --- Draw airport code text ---
			AffineTransform originalTransform = g2d.getTransform();
			AffineTransform textTransform = new AffineTransform(originalTransform);
			textTransform.scale(1, -1); // Flip vertically for text
			g2d.setTransform(textTransform);
			
			g2d.setColor(Color.BLACK);
			g2d.drawString(a.getCode(), xPos + 5, -yPos - 5);
			
			g2d.setTransform(originalTransform);
		}
	}

	public void startBlinking(Runnable repaintCallback) {
		stopBlinking();
		isBlinkingEnabled = true;
		isBlinkingColor = true;
		
		blinkThread = new Thread(() -> {
			try {
				while(isBlinkingEnabled) {
					isBlinkingColor = !isBlinkingColor;
					repaintCallback.run();
					Thread.sleep(250);
				}
			} catch (InterruptedException e) {
				// Thread interrupted, exit loop
			} finally {
				repaintCallback.run();
			}
		});
		blinkThread.start();
	}
	
	public void stopBlinking() {
		isBlinkingEnabled = false;
		if(blinkThread != null && blinkThread.isAlive()) {
			blinkThread.interrupt();
			try {
				blinkThread.join(50);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		isBlinkingColor = false;
	}
}