package flights;

import java.awt.Canvas;
import java.awt.EventQueue;

import airports.Airport;
import airports.AirportDispatcher;

public class Flying implements Runnable {
	private int x1, x2, y1, y2, duration, reloadTime, startH, startMin, totalMin;
	private double speed; //broj piksela od duzine koji se predje
	private double len;
	private Flight let;
	private Airport a1, a2;
	private double sx, sy; //stepx, stepy
	private double px, py;
	private Canvas canvas;
	private Runnable method;
	private int width, height, sqSize;
	private double scaleX, scaleY;
	private boolean visible = false;  // shown on map
	private boolean flying = false;   // actually moving
	
	public Flying(Flight let, Canvas canvas, Runnable repaintCallback, int sqSize) {
		this.canvas = canvas;
		this.method = repaintCallback;
		this.width = canvas.getWidth();
		this.height = canvas.getHeight();
		this.let = let;
		scaleX = (width / 2.0) / 90.0;
		scaleY = (height / 2.0) / 90.0;
		
		a1 = Airport.getAirport(let.getDepartureCode());
		a2 = Airport.getAirport(let.getArrivalCode());
		x1 = a1.getX();
		y1 = a1.getY();
		x2 = a2.getX();
		y2 = a2.getY();
		this.sqSize = sqSize;
		this.startH = let.getStartHour();
		this.startMin = let.getStartMinute();
		this.totalMin = let.getStartHour() * 60 + let.getStartMinute();
		duration = let.getDuration();
		reloadTime = 200;
		setSteps();
		
		px = x1;
		py = y1;
	}
	
	@Override
	public void run() {
	    px = x1 * scaleX;
	    py = y1 * scaleY;
	    
	    double dist = 0;
	    
	    visible = true;
	    flying = false;
	    EventQueue.invokeLater(method);
	    
	    try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {}
	    
	    visible = true;	

	    try {
	        while (dist < len) {
	            if (stopped) break;

	            synchronized (this) {
	                while (paused && !stopped) {
	                    try { wait(); } 
	                    catch (InterruptedException ie) { Thread.currentThread().interrupt(); return; }
	                }
	            }

	            if (stopped) break;

	            px += sx * scaleX;
	            py += sy * scaleY;
	            dist += speed;

	            EventQueue.invokeLater(method);
	            Thread.sleep(reloadTime);
	        }
	    } catch (InterruptedException e) {
	        Thread.currentThread().interrupt();
	    }

	    px = x2 * scaleX;
	    py = y2 * scaleY;
	    EventQueue.invokeLater(method);
	    AirportDispatcher.onFlightFinished(this);
	}
	
	private void setSteps() {
		 int x = x2 - x1;
		 int y = y2 - y1;
		 double l = Math.sqrt(x*x + y*y);
		 len = l;
		 speed = l * reloadTime /(1000 * (duration / 10));
		 sx = (speed * x/l);
		 sy = (speed * y/l);
	}
	 
	 public double getPx() {
		 return px;
	 }
	 
	 public double getPy() {
		 return py;
	 }
	 
	 private volatile boolean paused = false;
	 private volatile boolean stopped = false;

	 public void pause() {
	    synchronized (this) {
	        paused = true;
	    }
	}

	public void resumeFlight() {
	    synchronized (this) {
	        paused = false;
	        this.notifyAll();
	    }
	}

	public void stopFlight() {
	    synchronized (this) {
	        stopped = true;
	        paused = false;
	        this.notifyAll();
	    }
	}
	
	public boolean isVisible() { return visible; }
	public boolean isFlying()  { return flying; }

}