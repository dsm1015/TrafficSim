package edu.sru.dsm1015.TrafficSim.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;

import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import edu.sru.dsm1015.TrafficSim.grid.Coordinate;

/*
* TrafficSim - Waypoint
* edu.sru.dsm1015.TrafficSim.gui.Waypoint
* This class represents a Waypoint object for the TrafficSim program. Waypoints are used to
* visualize points of interest within the grid cells, with customizable properties such as
* color and label. Waypoints are drawn as stars on top of grid cells and can be toggled on and off.
*/
public class Waypoint extends JPanel {
    private Coordinate coordinates;
    private JButton parentButton;
    private Color color;
    private String label;
    private Rectangle bounds;

    public Waypoint(Coordinate coordinates, JButton parentButton, JLayeredPane circleLayer, String label) {
    	setOpaque(false); // make the background transparent
		setVisible(true);
		this.bounds = parentButton.getBounds();
		setBounds(this.bounds); // 100 by 100 px
        this.coordinates = coordinates;
        this.parentButton = parentButton;
        this.color = Color.ORANGE;
        this.label = label;
        circleLayer.add(this, JLayeredPane.PALETTE_LAYER);
    }

    public Coordinate getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinate coordinates) {
        this.coordinates = coordinates;
    }

    public void removeFromLayer(JLayeredPane circleLayer) {
        circleLayer.remove(this);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
    
    	//STAR
        int xPoints[] = {9, 15, 0, 18, 3};
        int yPoints[] = {0, 18, 6, 6, 18};
    	
    	// Center of cell
    	int centerX = parentButton.getWidth() / 2;
        int centerY = parentButton.getHeight() / 2;
        
        // Adjust the star points by adding the center point
        for (int i = 0; i < xPoints.length; i++) {
            xPoints[i] = xPoints[i] - 9 + centerX; // Subtract half of the maximum x-coordinate value and add centerX
            yPoints[i] = yPoints[i] - 9 + centerY; // Subtract half of the maximum y-coordinate value and add centerY
        }
        
        Graphics2D g2d = (Graphics2D) g;
        GeneralPath star = new GeneralPath();

        star.moveTo(xPoints[0], yPoints[0]);
        for (int i = 1; i < xPoints.length; i++) {
            star.lineTo(xPoints[i], yPoints[i]);
        }
        star.closePath();
        
        // Draw the star
        g2d.setColor(color);
        g2d.fill(star);
        
        // Draw the label
        //g.setColor(Color.BLACK);
        //g2d.setFont(new Font("Arial", Font.BOLD, 12));
        //g2d.drawString(label, (getWidth() - g2d.getFontMetrics().stringWidth(label)) / 2, (getHeight() + g2d.getFontMetrics().getHeight() / 2) / 2);
    }
    
    public void toggleVisibility() {
    	setVisible(!isVisible()); // Toggle the built-in visibility
        if (isVisible()) {
            repaint();
        }
    }
}
