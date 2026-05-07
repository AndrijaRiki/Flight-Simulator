package gui;

import java.awt.*;
import java.awt.event.*;

import airports.Airport;
import timeHandling.Timer;

public class MouseController extends MouseAdapter {
    private Timer timer;
    private Canvas canvas;
    private int sqSize = 720 / 90;

    public MouseController(Timer timer, Canvas canvas) {
        this.timer = timer;
        this.canvas = canvas;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        timer.reset();

        int mouseX = e.getX();
        int mouseY = e.getY();

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        double cx = mouseX - width / 2.0;
        double cy = height / 2.0 - mouseY;
        mouseX = (int) ((cx / (width / 2.0)) * 90.0);
        mouseY = (int) ((cy / (height / 2.0)) * 90.0);

        for (Airport a : Airport.allAirports) {
            double dist = Math.hypot(a.getX() - mouseX, a.getY() - mouseY);
            if (dist <= sqSize / 2.0) {
                if (a.isBlinkingActive()) {
                    a.stopBlinking();
                    timer.go();
                    canvas.repaint();
                } else {
                    Airport.allAirports.forEach(Airport::stopBlinking);
                    a.startBlinking(() -> EventQueue.invokeLater(canvas::repaint));
                    timer.pause();
                }
                return;
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        timer.reset();

        int mouseX = e.getX();
        int mouseY = e.getY();

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        double cx = mouseX - width / 2.0;
        double cy = height / 2.0 - mouseY;

        int worldX = (int) ((cx / (width / 2.0)) * 90.0);
        int worldY = (int) ((cy / (height / 2.0)) * 90.0);

        boolean over = Airport.allAirports.stream().anyMatch(a -> Math.hypot(a.getX() - worldX, a.getY() - worldY) <= sqSize / 2.0);

        canvas.setCursor(Cursor.getPredefinedCursor(over ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));
    }

}
