package edu.sru.dsm1015.TrafficSim.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

import edu.sru.dsm1015.TrafficSim.grid.Cell;
import edu.sru.dsm1015.TrafficSim.grid.Grid;
import edu.sru.dsm1015.TrafficSim.grid.RoadCell;


/*
 * TrafficSim - TrafficDensityOverlay
 * edu.sru.dsm1015.TrafficSim.gui.TrafficDensityOverlay
 * This class represents the Traffic Density Overlay component for the TrafficSim program. It is used to
 * visualize the traffic density of cells on the grid, based on the number of citizens within each cell.
 * The overlay can be toggled on and off, and the historical density values can be cleared or displayed.
 */
public class TrafficDensityOverlay extends JComponent {

    private Grid grid;
    private float[][] historicalDensity;
    private boolean historicalFlag;

    public TrafficDensityOverlay(Grid grid) {
        this.grid = grid;
        setVisible(false);
        setOpaque(false); // make the background transparent
        historicalDensity = new float[Grid.getWidth()][Grid.getHeight()];
        historicalFlag = false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;
        float weight = 0.1f; // Adjust this value to control the lingering effect
        for (int i = 0; i < Grid.getWidth(); i++) {
            for (int j = 0; j < Grid.getHeight(); j++) {
                Cell cell = grid.getCell(i, j);
                if(cell instanceof RoadCell) {
                	int density = cell.getCitizens().size(); // adjust this as needed
                    historicalDensity[i][j] = weight * density + (1 - weight) * historicalDensity[i][j];
                	if(density == 1) {
                		density = 0;
                	}
                	float intensity = 0;
                	if(historicalFlag) {
                		intensity = Math.min(1, historicalDensity[i][j] / 10f); // adjust the denominator for scaling
                	} else {
                		intensity = Math.min(1, density / 30f); // adjust the denominator for scaling
                	}
                    g2d.setColor(new Color(1, 0, 0, intensity));
                    g2d.fillRect(cell.getButton().getX(), cell.getButton().getY(), cell.getButton().getWidth(), cell.getButton().getHeight());
     
                }          
            }
        }
    }

    public void toggleVisibility() {
        setVisible(!isVisible()); // Toggle the built-in visibility
        if (isVisible()) {
            repaint();
        }
    }
    
    public void setVisibility(boolean v) {
    	setVisible(v);
    	if(isVisible()) {
    		repaint();
    	}
    }
    
    public void toggleHistorical() {
    	historicalFlag = !historicalFlag;
    	repaint();
    }
    
    public void setHistoricalFlag(boolean f) {
    	historicalFlag = f;
    	repaint();
    }
    
    public void setGrid(Grid grid) {
    	this.grid = grid;
    	clear();
    }
    
    public void clear() {
    	historicalDensity = new float[Grid.getWidth()][Grid.getHeight()];
        repaint(); // Update the overlay to reflect the cleared historical density values
    }
}
