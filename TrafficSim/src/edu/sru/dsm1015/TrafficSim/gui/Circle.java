package edu.sru.dsm1015.TrafficSim.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JPanel;

import edu.sru.dsm1015.TrafficSim.grid.Coordinate;


/*
 * TrafficSim - Circle
 * edu.sru.dsm1015.TrafficSim.gui.Circle
 * This class represents a Circle object for the TrafficSim program. Circles are used to visualize
 * citizens on the grid, with customizable size, color, and label properties. Circles can be drawn
 * on top of grid cells, as well as associated with a JButton object.
 */
public class Circle extends JPanel {
	
	private int radius;
	private Color color;
	private Coordinate coordinates;
	private Rectangle bounds;
	private String label; //citizen number
	
	public Circle(Coordinate coordinates, JButton button, String label) {
		setOpaque(false); // make the background transparent
		setVisible(true);
		this.bounds = button.getBounds();
		setBounds(this.bounds); // 100 by 100 px
		this.radius = 15;
		this.color = Color.PINK;
		this.coordinates = coordinates;
		this.label = label;
	}
	
	
	//custom color
	public Circle(Coordinate coordinates, JButton button, String label, Color color) {
		setOpaque(false); // make the background transparent
		setVisible(true);
		this.bounds = button.getBounds();
		setBounds(this.bounds); // 100 by 100 px
		this.radius = 15;
		this.color = color;
		this.coordinates = coordinates;
		this.label = label;
	}
	
	public float getRadius() {
		return this.radius;
	}
	
	public void setCoordinates(int X, int Y) {
		Coordinate tempCoor = new Coordinate(X, Y);
		this.coordinates = tempCoor;
	}
	
	public Coordinate getCoordinates() {
		return this.coordinates;
	}
	
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Draw the circle
        g.setColor(color);
        int x = coordinates.getX() - radius / 2;
        int y = coordinates.getY() - radius / 2;
        g.fillOval(x, y, radius, radius);
        // Draw the label
        //g.setColor(Color.BLACK);
        //g.setFont(new Font("Arial", Font.BOLD, 12));
        //g.drawString(label, (getWidth() - g.getFontMetrics().stringWidth(label)) / 2, (getHeight() + g.getFontMetrics().getHeight() / 2) / 2);
    }
	
}
