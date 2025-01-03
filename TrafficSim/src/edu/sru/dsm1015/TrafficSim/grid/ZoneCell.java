package edu.sru.dsm1015.TrafficSim.grid;

import java.awt.Color;
import java.util.Set;

import edu.sru.dsm1015.TrafficSim.agent.Citizen;
import edu.sru.dsm1015.TrafficSim.datastructures.Edge;
import edu.sru.dsm1015.TrafficSim.datastructures.Node;

/*
 * TrafficSim - ZoneCell
 * edu.sru.dsm1015.TrafficSim.grid.ZoneCell
 * Represents a zone cell in the grid that extends the base Cell class. 
 * Stores information about the zone type, nodes, edges, and attraction rate.
 */
public class ZoneCell extends Cell {

    private String zoneType;
    private Node[][] nodes;
  	private Edge[] edges;
    private float attraction;


    public ZoneCell(Coordinate coordinates, String zoneType) {
        super(coordinates);
        this.CellType = 2;
        switch(zoneType){
            case "residential":
                this.zoneType = "residential";
                this.setColor(new Color(175,255,102));
                //low attraction
                this.attraction = (float) (Math.random() * 0.2 + 0.1); // Random value between 0.1 and 0.3
                break;
            case "commercial":
                this.zoneType = "commercial";
                this.setColor(new Color(102,178,255));
                //high attraction
                this.attraction = (float) (Math.random() * 0.3 + 0.7); // Random value between 0.7 and 1.0
                break;
            case "offices":
                this.zoneType = "offices";
                this.setColor(new Color(255,255,102));
                //medium attraction
                this.attraction = (float) (Math.random() * 0.2 + 0.4); // Random value between 0.4 and 0.6
                break;
        }
    }
    
   
    public void setAttraction(float attr) {
        this.attraction = attr;
    }

    public float getAttraction() {
        return this.attraction;
    }

    public void setZoneType(String zoneType) {
        this.zoneType = zoneType;
    }

    public String getZoneType() {
        return this.zoneType;
    }
    
    @Override
    public Node[][] getNodes(){
    	return this.nodes;
    }
    
    @Override
    public Set<Citizen> getCitizens() {
    	return this.citizens;
    }
    
    @Override
    public void addCitizen(Citizen citizen) {
    	this.citizens.add(citizen);
    }
    
    @Override
    public void removeCitizen(Citizen citizen) {
    	this.citizens.remove(citizen);
    }
    
    // toString method
    @Override
    public String toString() {
        return "Cell{" +
                " coordinates=" + coordinates +
                ", zone type=" + zoneType +
                ", attraction rate" + attraction +
                "\n citizens: " + citizens.size() +
                "\n waypoints: " + waypoints.size() +
                '}';
    }
    
}
