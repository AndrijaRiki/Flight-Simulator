package gui;

import java.awt.*;

import airports.Airport;
import airports.AirportDispatcher;
import flights.Flying;
import timeHandling.Timer;

public class DrawingCanvas extends Canvas {
    private static final int CANVAS_DIM = 720;
    private static final int SQ_SIZE = CANVAS_DIM / 90;
    private Timer timer;
    private ShowData data;
    private Image offscreen;

    public DrawingCanvas(Timer timer, ShowData data) {
        this.timer = timer;
        this.data = data;
        setPreferredSize(new Dimension(CANVAS_DIM, CANVAS_DIM));
        setBackground(Color.WHITE);
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }

    @Override
    public void paint(Graphics g) {
        int width = getWidth(), height = getHeight();
        if (offscreen == null || offscreen.getWidth(this) != width || offscreen.getHeight(this) != height) {
            offscreen = createImage(width, height);
        }

        Graphics2D g2 = (Graphics2D) offscreen.getGraphics();
        g2.setColor(getBackground());
        g2.fillRect(0, 0, width, height);

        g2.translate(width / 2, height / 2);
        g2.scale(1, -1);

        DrawUtils.drawGrid(g2, width, height, width / 20);
        Airport.drawAirports(g2, width, height, SQ_SIZE, null, false, data);

        if (!AirportDispatcher.getActiveFlights().isEmpty()) {
            for (Flying f : AirportDispatcher.getActiveFlights()) {
                DrawUtils.drawFlight(g2, f, width, height);
            }
        }

        g.drawImage(offscreen, 0, 0, this);
        g2.dispose();
    }
}
