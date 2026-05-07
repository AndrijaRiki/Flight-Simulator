package gui;

import java.awt.Canvas;

import airports.AirportDispatcher;

public class SimulationController {

    public void start(Canvas canvas, Runnable repaintCallback) {
        AirportDispatcher.setOnAllFlightsFinished(() -> {
            PopupMsg.showMsg("All flights finished!");
        });
        AirportDispatcher.startAllDispatchers(canvas, repaintCallback, 720 / 90);
    }

    public void pause() {
        AirportDispatcher.pauseAllFlights();
    }

    public void resume() {
        AirportDispatcher.resumeAllFlights();
    }

    public void restart(Canvas canvas, Runnable repaintCallback) {
        AirportDispatcher.stopAllFlights();
        AirportDispatcher.startAllDispatchers(canvas, repaintCallback, 720 / 90);
    }

    public void stop() {
        AirportDispatcher.stopSimulation();
    }
}
