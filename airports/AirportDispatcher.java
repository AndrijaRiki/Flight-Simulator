package airports;

import java.awt.Canvas;
import java.awt.EventQueue;
import java.util.*;

import flights.Flight;
import flights.Flying;

public class AirportDispatcher implements Runnable {
    private static final List<Flying> activeFlights = Collections.synchronizedList(new ArrayList<>());
    private static Runnable repaintCallback;
    private static Runnable onAllFlightsFinished;
    private static Canvas canvas;
    private static int sqSize;

    private final String kod; // airport code

    public AirportDispatcher(String kod) {
        this.kod = kod;
    }

    public static List<Flying> getActiveFlights() {
        synchronized (activeFlights) {
            return new ArrayList<>(activeFlights);
        }
    }

    
    public static void setOnAllFlightsFinished(Runnable callback) {
        onAllFlightsFinished = callback;
    }

    public static void startAllDispatchers(Canvas c, Runnable repaint, int size) {
        canvas = c;
        repaintCallback = repaint;
        sqSize = size;

        // clear previous runs
        activeFlights.clear();

        // Start one dispatcher per airport
        for (Map.Entry<String, TreeSet<Flight>> entry : Airport.waitingQueue.entrySet()) {
            Thread dispatcherThread = new Thread(new AirportDispatcher(entry.getKey()));
            dispatcherThread.start();
        }
    }
    
    @Override
    public void run() {
        TreeSet<Flight> flights = Airport.waitingQueue.get(kod);
        if (flights == null || flights.isEmpty()) return;

        // First, wait until the first scheduled departure for this airport
        Flight firstFlight = flights.first();
        int firstStartMins = firstFlight.getStartHour() * 60 + firstFlight.getStartMinute();

        // 10 min simulation time = 1 sec real → 1 min = 0.1 sec = 100 ms
        long initialWait = (long) (firstStartMins * 100);

        try {
            Thread.sleep(initialWait);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        int previousStartTime = firstStartMins;

        for (Flight let : flights) {
            Airport a1 = Airport.getAirport(let.getDepartureCode());
            Airport a2 = Airport.getAirport(let.getArrivalCode());
            if (a1 == null || a2 == null || !a1.getVisibleState() || !a2.getVisibleState())
                continue;

            int startTimeInMins = let.getStartHour() * 60 + let.getStartMinute();

            int diffInMins = startTimeInMins - previousStartTime;
            previousStartTime = startTimeInMins;

            long waitMillis = (long) (diffInMins * 1000);

            try {
                Thread.sleep(waitMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            Flying f = new Flying(let, canvas, repaintCallback, sqSize);
            activeFlights.add(f);

            Thread flightThread = new Thread(() -> {
                f.run();
                onFlightFinished(f);
            });
            flightThread.start();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {}
        }
    }


    public static synchronized void onFlightFinished(Flying f) {
        activeFlights.remove(f);
        if (activeFlights.isEmpty() && onAllFlightsFinished != null) {
            EventQueue.invokeLater(onAllFlightsFinished);
        }
    }

    public synchronized static void stopAllFlights() {
        synchronized (activeFlights) {
            for (Flying f : activeFlights) {
                f.stopFlight();
            }
            activeFlights.clear();
        }
    }

    public synchronized static void pauseAllFlights() {
        synchronized (activeFlights) {
            for (Flying f : activeFlights) {
                f.pause();
            }
        }
    }

    public synchronized static void resumeAllFlights() {
        synchronized (activeFlights) {
            for (Flying f : activeFlights) {
                f.resumeFlight();
            }
        }
    }
    
    public synchronized static void stopSimulation() {
        // Zaustavi sve letove
        synchronized (activeFlights) {
            for (Flying f : activeFlights) {
                f.stopFlight();
            }
            activeFlights.clear();
        }

        repaintCallback = null;
        onAllFlightsFinished = null;
        canvas = null;
    }
}
