package gui;

import java.awt.*;

import flights.Flying;

public class DrawUtils {

    public static void drawGrid(Graphics2D g2, int width, int height, int step) {
        g2.setColor(Color.LIGHT_GRAY);
        for (int x = -width / 2; x <= width / 2; x += step)
            g2.drawLine(x, -height / 2, x, height / 2);
        for (int y = -height / 2; y <= height / 2; y += step)
            g2.drawLine(-width / 2, y, width / 2, y);
    }

    public static void drawFlight(Graphics2D g2, Flying f, int width, int height) {
    	double fx = f.getPx();
        double fy = f.getPy();
        int size = 6;
        
        String coordText = String.format("(%.1f, %.1f)", fx / 4, fy / 4);
        g2.scale(1, -1); // flip back to normal text
        int textWidth = g2.getFontMetrics().stringWidth(coordText);
        int textHeight = g2.getFontMetrics().getHeight();
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect((int) fx + size / 2 + 5, (int) -fy - textHeight, textWidth + 6, textHeight);
        g2.setColor(Color.WHITE);
        g2.drawString(coordText, (int) fx + size / 2 + 8, (int) -fy - 4);
        g2.scale(1, -1); // restore flip
        
        g2.setColor(Color.BLUE);
        g2.fillOval((int) fx - size / 2, (int) fy - size / 2, size, size);
    }
}
