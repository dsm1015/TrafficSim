package edu.sru.dsm1015.TrafficSim.agent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.swing.JLayeredPane;

import edu.sru.dsm1015.TrafficSim.grid.Cell;
import edu.sru.dsm1015.TrafficSim.grid.Coordinate;
import edu.sru.dsm1015.TrafficSim.grid.Grid;
import edu.sru.dsm1015.TrafficSim.grid.ZoneCell;
import edu.sru.dsm1015.TrafficSim.gui.Circle;
import edu.sru.dsm1015.TrafficSim.gui.Waypoint;

/*
 * TrafficSim - Citizen
 * edu.sru.dsm1015.TrafficSim.agent.Citizen
 * The Citizen class represents a citizen in a city simulation. 
 * It contains fields for citizen number, ID, grid and local coordinates, home and work cells,
 *  travel and work rates, a point of interest, a route, a circle for visualization, and a circle layer. 
 *  The class provides methods for setting and getting the route, setting the home and work cells, finding a suitable work cell, 
 *  getting the travel rate, ID, citizen number, and coordinates, getting and removing the waypoint, initializing and executing the 
 *  movement of the citizen, and getting a string representation of the citizen.
 */
public class Citizen {
	private static int citizenCounter = 0;
	private int citizenNumber;
	private UUID id;
	private Coordinate gridCoordinates;
	private Coordinate localCoordinates; //Local coordinate within Cell
	private Route route;
	private Circle circle;
	private Grid grid;
	private Coordinate POI; //Point of Interest
	private Waypoint waypoint;
	private ZoneCell home;
	private ZoneCell work;
	private double travelRate;
	private double workRate;
	protected JLayeredPane circleLayer;
	
	public Citizen(Grid grid, Cell cell, JLayeredPane circleLayer) {
	    // Set the ID and citizen number
	    this.id = UUID.randomUUID();
	    this.citizenNumber = ++citizenCounter;

	    // Set the grid coordinates and local coordinates to the center of the cell button
	    this.gridCoordinates = cell.getCoordinates();
	    this.localCoordinates = new Coordinate(cell.getButton().getWidth() / 2, cell.getButton().getHeight() / 2);

	    // Set the circle and grid
	    this.circle = new Circle(this.localCoordinates, cell.getButton(), String.valueOf(this.citizenNumber));
	    this.grid = grid;

	    // Set the point of interest, route, and waypoint to null
	    this.POI = null;
	    this.route = null;
	    this.waypoint = null;

	    // Set the circle layer
	    this.circleLayer = circleLayer;

	    // Initialize travelRate and workRate
	    this.travelRate = 0.1 + Math.random() * 0.3; // Random value between 0.1 and 0.4
	    this.workRate = 0.4 + Math.random() * 0.4; // Random value between 0.4 and 0.8

	    // Set the home cell if the current cell is a residential cell
	    if(cell instanceof ZoneCell) {
	        setHome((ZoneCell) cell);
	    }
	    
	    //set work cell
	    setWork(findWorkCell());

	    // Initialize the route
	    //initRoute();
	}
	
	public void setRoute(Route route) {
		this.route = route;
	}
	
	public Route getRoute() {
		return this.route;
	}
	
	public void setHome(ZoneCell cell) {
		if(cell.getZoneType() == "residential") {
			this.home = cell;
		} else {
			System.out.println("This is not a residential cell. Home not set");
		}
	}
	
	public void setWork(ZoneCell cell) {
		if(cell == null) {
			return;
		}
		if(cell.getZoneType() == "offices" || cell.getZoneType() == "commercial") {
			this.work = cell;
		} else {
			System.out.println("This is not a office/commercial cell. Work not set");
		}
	}
	
	public ZoneCell findWorkCell() {
		// Get all cells
	    List<Cell> allCells = new ArrayList<>();
	    for (int i = 0; i < Grid.getWidth(); i++) {
	        for (int j = 0; j < Grid.getHeight(); j++) {
	            allCells.add(grid.getCell(i, j));
	        }
	    }

	    // Filter cells that are instances of ZoneCell and have work zones
	    List<ZoneCell> workZoneCells = allCells.stream()
	            .filter(cell -> cell instanceof ZoneCell)
	            .map(cell -> (ZoneCell) cell)
	            .filter(zoneCell -> zoneCell.getZoneType() == "commercial" || zoneCell.getZoneType() == "offices")
	            .collect(Collectors.toList());

	    // Return a random work zone or null if there are no suitable work zones
	    if (!workZoneCells.isEmpty()) {
	        Collections.shuffle(workZoneCells);
	        return workZoneCells.get(0);
	    } else {
	        return null;
	    } 
	}
	
	public ZoneCell getHome() {
		return this.home;
	}
	
	public double getTravelRate() {
		return this.travelRate;
	}
	
	public UUID getID() {
		return this.id;
	}
	
	public int getCitizenNumber() {
        return citizenNumber;
    }
	
	public void setCoordinates(Coordinate newCoor) {
		this.gridCoordinates = newCoor;
	}
	
	public Coordinate getCoordinates() {
		return this.gridCoordinates;
	}
	
	public Circle getCircle() {
		return this.circle;
	}
	
	public void removeWaypoint(Cell cell) {
		this.waypoint.removeFromLayer(circleLayer);
		cell.removeWaypoint(this.waypoint);
		this.waypoint = null;
		circleLayer.paintImmediately(grid.getCell(this.gridCoordinates).getButton().getBounds());
	}
	
	public Waypoint getWaypoint() {
		return this.waypoint;
	}
	
	public Coordinate getPOI() {
		return this.POI;
	}
	
	public void initRoute() {
		//go to work first
		Cell destination = this.work;
		if(this.work == null) {
			destination = getNextGridDestination();
		}
		this.POI = destination.getCoordinates();
		this.route = new Route(this.grid, this.gridCoordinates, this.POI, grid.getSearchAlgorithm());
		this.waypoint = new Waypoint(this.POI, destination.getButton(), circleLayer, String.valueOf(this.citizenNumber));
		destination.addWaypoint(this.waypoint);
		
	}
	
	public void move() {
		
		if(this.route != null && route.peekRoute() != null) {
			// remove circle from this cell and move it to another cell.
			
			//if next is same as current, skip
			if(this.route.peekRoute().equals(this.gridCoordinates)) {
				this.route.dequeueRoute();
				return;
			}
			Cell currentCell = this.grid.getCell(gridCoordinates);
			Cell nextCell = this.grid.getCell(route.peekRoute());
			this.gridCoordinates = nextCell.getCoordinates();
			nextCell.addCitizen(this);
			currentCell.removeCitizen(this);
			this.route.dequeueRoute();
			
			//graphic updates
			circleLayer.remove(this.circle);
			this.circle = new Circle(this.localCoordinates, nextCell.getButton(), String.valueOf(this.citizenNumber));
			circleLayer.add(this.circle, JLayeredPane.PALETTE_LAYER);
			circleLayer.paintImmediately(currentCell.getButton().getBounds());
			circleLayer.paintImmediately(nextCell.getButton().getBounds());
		} else if(this.waypoint != null) {
			//route is complete, so remove waypoint and route
			this.route = null;
			removeWaypoint(this.grid.getCell(gridCoordinates));
		} else {
			// Generate a random number between 0 and 1
	        double randomValue = Math.random();
	        
	        
	        // Compare the random number to the Citizen's travelRate
	        // if home and lower than workRate then go to work
	        if (randomValue <= this.workRate && this.gridCoordinates == this.home.getCoordinates()) {
	        	// Go to work
	            if (this.work == null) {
	                this.work = findWorkCell();
	            }
	            if (this.work != null) {
	                this.route = new Route(this.grid, this.gridCoordinates, this.work.getCoordinates(), grid.getSearchAlgorithm());
	                this.waypoint = new Waypoint(this.work.getCoordinates(), this.work.getButton(), circleLayer, String.valueOf(this.citizenNumber));
	                if(this.work.getWaypoints().contains(this.waypoint)) {
	                	return;
	                }
	                this.POI = this.work.getCoordinates();
	                this.work.addWaypoint(this.waypoint);
	            }
	            
	        } else if (randomValue <= this.travelRate) {
	        	// Generate a new travel route
	            Cell nextDestination = getNextGridDestination();
	            this.POI = nextDestination.getCoordinates();
	            this.route = new Route(this.grid, this.gridCoordinates, nextDestination.getCoordinates(), grid.getSearchAlgorithm());
	            this.waypoint = new Waypoint(this.POI, nextDestination.getButton(), circleLayer, String.valueOf(this.citizenNumber));
	            nextDestination.addWaypoint(this.waypoint);
	                
	        } else {
	            // Go back home
	            this.route = new Route(this.grid, this.gridCoordinates, this.home.getCoordinates(), grid.getSearchAlgorithm());
	            this.waypoint = new Waypoint(this.home.getCoordinates(), this.home.getButton(), circleLayer, String.valueOf(this.citizenNumber));
	            this.POI = this.home.getCoordinates();
	            this.home.addWaypoint(this.waypoint);
	        }
		}
		
		
	}
	
	
	private Cell getNextGridDestination() {
		
		// Get all cells
	    List<Cell> allCells = new ArrayList<>();
	    for (int i = 0; i < Grid.getWidth(); i++) {
	        for (int j = 0; j < Grid.getHeight(); j++) {
	            allCells.add(grid.getCell(i, j));
	        }
	    }

	    // Filter cells that are instances of ZoneCell
	    List<ZoneCell> zoneCells = allCells.stream()
	            .filter(cell -> cell instanceof ZoneCell)
	            .map(cell -> (ZoneCell) cell)
	            .collect(Collectors.toList());

	    // Calculate the sum of all attraction rates
	    float sumOfAttractions = 0;
	    for (ZoneCell cell : zoneCells) {
	        sumOfAttractions += cell.getAttraction();
	    }

	    // Generate a random number between 0 and the sum of all attraction rates
	    double randomValue = Math.random() * sumOfAttractions;

	    // Select a cell based on the attraction rate
	    float currentAttractionSum = 0;
	    for (ZoneCell cell : zoneCells) {
	        currentAttractionSum += cell.getAttraction();
	        if (randomValue <= currentAttractionSum) {
	            return cell;
	        }
	    }
		
	    // If no suitable cell is found, return a random cell
		Random rand = new Random();
        int gridWidth = Grid.getWidth();
        int gridHeight = Grid.getHeight();
        int randomX = rand.nextInt(gridWidth);
        int randomY = rand.nextInt(gridHeight);
        return grid.getCell(randomX, randomY);
    }
	
		
	
	@Override
    public String toString() {
        return "Citizen{" +
                "id=" + citizenNumber +
                ", coordinates=" + gridCoordinates +
                ", home=" + home +
                ", work=" + work +
                ", traveling to: " + POI +
                '}';
    }
	
}
