package edu.sru.dsm1015.TrafficSim.grid;

import java.awt.Color;

import edu.sru.dsm1015.TrafficSim.datastructures.Edge;
import edu.sru.dsm1015.TrafficSim.datastructures.Node;

/*
 * TrafficSim - RoadCell
 * edu.sru.dsm1015.TrafficSim.grid.RoadCell
 * Represents a road cell in the grid that extends the base Cell class. 
 * Stores information about nodes, edges, and orientation.
 */
public class RoadCell extends Cell {

    private Node[][] nodes;
    private Edge[] edges;
    private int orientation; //0 - Horizontal, 1 - Vertical, 2 - Full Intersection

    public RoadCell(Coordinate coordinates) {
        super(coordinates);
        this.nodes = new Node[4][2]; //4 sets of 2 nodes; NESW
        this.setColor(new Color(128,128,128));
        this.CellType = 1;
      
    }
    
    @Override
    public Node[][] getNodes(){
    	return this.nodes;
    }
    
    public void setOrientation(int o) {
    	this.orientation = o;
    }
    
    public int getOrientation() {
    	return this.orientation;
    }
    
    // toString method
    @Override
    public String toString() {
        return "Cell{" +
                " coordinates=" + coordinates +
                " citizens: " + citizens.size() +
                '}';
    }
    
    
    
}
