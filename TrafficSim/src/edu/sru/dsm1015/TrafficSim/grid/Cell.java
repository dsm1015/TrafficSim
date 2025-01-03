package edu.sru.dsm1015.TrafficSim.grid;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;

import edu.sru.dsm1015.TrafficSim.agent.Citizen;
import edu.sru.dsm1015.TrafficSim.datastructures.Node;
import edu.sru.dsm1015.TrafficSim.gui.Waypoint;

/*
 * TrafficSim - Cell
 * edu.sru.dsm1015.TrafficSim.grid.Cell
 * Represents a single cell in the grid with properties such as coordinates, traffic density, and color.
 */
public class Cell {

    private int cellSideLength;
    protected Coordinate coordinates;
    private double trafficDensity;
    private Color color;
    protected Set<Citizen> citizens;
    protected Set<Waypoint> waypoints;
    protected int CellType; //blank-0 road-1 zone-2
    protected JButton button;

    public Cell(Coordinate coordinates){
        this.cellSideLength = 5;
        this.coordinates = coordinates;
        this.trafficDensity = 0;
        this.color = new Color(255, 255, 255); // white
        this.citizens = new HashSet<Citizen>();
        this.waypoints = new HashSet<Waypoint>();
        this.CellType = 0;
    }

    public Cell(Coordinate coordinates, Color color){
        this.cellSideLength = 5;
        this.coordinates = coordinates;
        this.trafficDensity = 0;
        this.color = color; // passed color
        this.citizens = new HashSet<Citizen>();
        this.waypoints = new HashSet<Waypoint>();
        this.CellType = 0;
    }

    // Getters and setters
    public int getCellSideLength() {
        return cellSideLength;
    }

    public void setCellSideLength(int cellSideLength) {
        this.cellSideLength = cellSideLength;
    }

    public Coordinate getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinate coordinates) {
        this.coordinates = coordinates;
    }
    
    public int getX() {
    	return this.coordinates.getX();
    }
    
    public int getY() {
    	return this.coordinates.getY();
    }

    public double getTrafficDensity() {
        return trafficDensity;
    }

    public void setTrafficDensity(double trafficDensity) {
        this.trafficDensity = trafficDensity;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    
    public Set<Citizen> getCitizens() {
    	return this.citizens;
    }
    
    public void addCitizen(Citizen citizen) {
    	this.citizens.add(citizen);
    }
    
    public void removeCitizen(Citizen citizen) {
    	if(this.citizens.contains(citizen)) {
    		this.citizens.remove(citizen);
    	}
    	return;
    }
    
    public Set<Waypoint> getWaypoints() {
    	return this.waypoints;
    }
    
    public void addWaypoint(Waypoint w) {
    	this.waypoints.add(w);
    }
    
    public void removeWaypoint(Waypoint w) {
    	if(this.waypoints.contains(w)) {
    		this.waypoints.remove(w);
    	}
    	return;
    }
    
    public void setCellType(int t) {
    	this.CellType = t;
    }
    
    public int getCellType() {
    	return this.CellType;
    }
    
    public Node[][] getNodes(){
    	return null;
    }
    
    public void setButton(JButton b){
    	this.button = b;
    }
    
    public JButton getButton() {
    	return this.button;
    }
    

    // toString method
    @Override
    public String toString() {
        return "Cell{" +
                " coordinates=" + coordinates +
                " citizens: " + citizens.size() +
                '}';
    }

    // equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cell)) return false;
        Cell cell = (Cell) o;
        return cellSideLength == cell.cellSideLength &&
                Double.compare(cell.trafficDensity, trafficDensity) == 0 &&
                coordinates.equals(cell.coordinates) &&
                color.equals(cell.color);
    }

    // paint method (to draw the cell on a graphics object)
    public void paint(Graphics g) {
        g.setColor(color);
        g.fillRect(coordinates.getX(), coordinates.getY(), cellSideLength, cellSideLength);
    }

    // computeTrafficFlow method (to compute the traffic flow based on the traffic density)
    public double computeTrafficFlow() {
        // Use a simple linear function to compute the traffic flow
        return 0.5 * trafficDensity;
    }
    
    public void reset() {
        // Clear citizens and waypoints
        this.citizens.clear();
        this.waypoints.clear();

        // Update the cell's button if it exists
        if (this.button != null) {
            this.button.setBackground(this.color);
        }
    }


    
}
