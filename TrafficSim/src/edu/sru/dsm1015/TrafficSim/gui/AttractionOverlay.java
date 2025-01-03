package edu.sru.dsm1015.TrafficSim.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Set;

import javax.swing.JComponent;

import edu.sru.dsm1015.TrafficSim.grid.Cell;
import edu.sru.dsm1015.TrafficSim.grid.Grid;
import edu.sru.dsm1015.TrafficSim.grid.ZoneCell;

/*
 * TrafficSim - AttractionOverlay
 * edu.sru.dsm1015.TrafficSim.gui.AttractionOverlay
 * This class represents the Attraction Overlay component for the TrafficSim program. It visualizes the historical
 * attraction rate of the cells on the grid, based on the number of waypoints within each cell. The overlay can be
 * toggled on and off, and the historical attraction values can be cleared. Additionally, it can manage the visibility
 * of waypoints on the grid.
 */
public class AttractionOverlay extends JComponent {
	
	private Grid grid;
	private float[][] historicalAttraction;

    public AttractionOverlay(Grid grid) {
        this.grid = grid;
        setVisible(false);
        setOpaque(false); // make the background transparent
        historicalAttraction = new float[Grid.getWidth()][Grid.getHeight()];
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        // Set color intensity based on the number of waypoints in each cell
        Graphics2D g2d = (Graphics2D) g;
        float weight = 0.1f; // Adjust this value to control the lingering effect
        for (int i = 0; i < Grid.getWidth(); i++) {
            for (int j = 0; j < Grid.getHeight(); j++) {
                Cell cell = grid.getCell(i, j);
                int numberOfWaypoints = cell.getWaypoints().size();
                if (cell instanceof ZoneCell) {
                    historicalAttraction[i][j] = weight * numberOfWaypoints + (1 - weight) * historicalAttraction[i][j];
                    float intensity = Math.min(1, historicalAttraction[i][j] / 10f);

                    // Define the start (lighter) and end (darker) colors
                    Color startColor = new Color(144, 238, 144); // Light green
                    Color endColor = new Color(0, 128, 0); // Dark green

                    // Interpolate between the start and end colors based on intensity
                    int red = (int) (startColor.getRed() + intensity * (endColor.getRed() - startColor.getRed()));
                    int green = (int) (startColor.getGreen() + intensity * (endColor.getGreen() - startColor.getGreen()));
                    int blue = (int) (startColor.getBlue() + intensity * (endColor.getBlue() - startColor.getBlue()));

                    g2d.setColor(new Color(red, green, blue));
                    g2d.fillRect(cell.getButton().getX(), cell.getButton().getY(), cell.getButton().getWidth(), cell.getButton().getHeight());
                }
            }
        }
    }


    public void toggleAttractionVisibility() {
    	setVisible(!isVisible()); // Toggle the built-in visibility
        if (isVisible()) {
            repaint();
        }
    }
    
    public void toggleWaypointVisibility() {
    	for (int i = 0; i < Grid.getWidth(); i++) {
            for (int j = 0; j < Grid.getHeight(); j++) {
                Cell cell = grid.getCell(i, j);
                	Set<Waypoint> waypoints = cell.getWaypoints();
                	for(Waypoint waypoint: waypoints) {
                		waypoint.toggleVisibility();
                	}
            }
        }
    }
    
    public void setVisibility(boolean b) {
    	setVisible(b); // Toggle the built-in visibility
        if (isVisible()) {
            repaint();
        }
    }
    
    public void setGrid(Grid grid) {
    	this.grid = grid;
    	clear();
    }
    
    public void clear() {
    	historicalAttraction = new float[Grid.getWidth()][Grid.getHeight()];
        repaint(); // Update the overlay to reflect the cleared historical density values
    }
    
}
