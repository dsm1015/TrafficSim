package edu.sru.dsm1015.TrafficSim.datastructures;

import edu.sru.dsm1015.TrafficSim.grid.Coordinate;

/**
 * This class represents a node in a road network, which contains coordinates and directional information.
 */
public class Node {
    private Coordinate coordinates;
    private int cardinalDirection; //0-N, 1-E, 2-S, 3-W
    private boolean isRight;

    
    /**
     * Constructor for the Node class without coordinates
     * @param cardinalDirection The cardinal direction the node is facing, represented by an integer (0-N, 1-E, 2-S, 3-W).
     * @param isRight Whether the node is a right-hand node or not.
     */
    public Node(int cardinalDirection, boolean isRight) {
        this.cardinalDirection = cardinalDirection;
        this.isRight = isRight;
        try {
	        switch (cardinalDirection) {
		        case 0:
		            if(isRight) {
		            	this.coordinates = new Coordinate(75,0);
		            } else {
		            	this.coordinates = new Coordinate(25,0);
		            }
		            break;
		        case 1:
		        	if(isRight) {
		            	this.coordinates = new Coordinate(100,75);
		            } else {
		            	this.coordinates = new Coordinate(100,25);
		            }
		        	break;
		        case 2:
		        	if(isRight) {
		            	this.coordinates = new Coordinate(25,100);
		            } else {
		            	this.coordinates = new Coordinate(75,100);
		            }
		        	break;
		        case 3:
		        	if(isRight) {
		            	this.coordinates = new Coordinate(0,25);
		            } else {
		            	this.coordinates = new Coordinate(0,75);
		            }
		        	break;
		        default:
		            throw new IllegalArgumentException("Invalid cardinal direction: " + cardinalDirection);
		    }
        } catch (IllegalArgumentException e){
        	System.err.println(e.getMessage());
        	this.coordinates = new Coordinate(50,50); //default to center
        }
    }
    
    /**
     * Constructor for the Node class.
     * @param coordinate The coordinates of the node.
     * @param cardinalDirection The cardinal direction the node is facing, represented by an integer (0-N, 1-E, 2-S, 3-W).
     * @param isRight Whether the node is a right-hand node or not.
     */
    public Node(Coordinate coordinate, int cardinalDirection, boolean isRight) {
        this.coordinates = coordinate;
        this.cardinalDirection = cardinalDirection;
        this.isRight = isRight;
    }
    
    /**
     * Getter method for the coordinates attribute.
     * @return The coordinates of the node.
     */
    public Coordinate getCoordinates() {
        return this.coordinates;
    }

    /**
     * Setter method for the coordinates attribute.
     * @param coordinates The new coordinates of the node.
     */
    public void setCoordinates(Coordinate coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Getter method for the cardinalDirection attribute.
     * @return The cardinal direction the node is facing, represented by an integer (0-N, 1-E, 2-S, 3-W).
     */
    public int getCardinalDirection() {
        return this.cardinalDirection;
    }

    /**
     * Setter method for the cardinalDirection attribute.
     * @param cardinalDirection The new cardinal direction the node is facing, represented by an integer (0-N, 1-E, 2-S, 3-W).
     */
    public void setCardinalDirection(int cardinalDirection) {
        this.cardinalDirection = cardinalDirection;
    }

    /**
     * Getter method for the isRight attribute.
     * @return Whether the node is a right-hand node or not.
     */
    public boolean isRight() {
        return this.isRight;
    }

    /**
     * Setter method for the isRight attribute.
     * @param isRight Whether the node is a right-hand node or not.
     */
    public void setRight(boolean isRight) {
        this.isRight = isRight;
    }

    /**
     * Converts the integer representation of a cardinal direction to its corresponding string representation.
     * @param cardinalDirection The integer representation of the cardinal direction.
     * @return The string representation of the cardinal direction.
     * @throws IllegalArgumentException If the integer is not a valid cardinal direction (0-3).
     */
    public static String cardinalIntToString(int cardinalDirection) {
        switch (cardinalDirection) {
            case 0:
                return "North";
            case 1:
                return "East";
            case 2:
                return "South";
            case 3:
                return "West";
            default:
                throw new IllegalArgumentException("Invalid cardinal direction: " + cardinalDirection);
        }
    }

    /**
     * Returns a string representation of the node.
     * @return A string representation of the node.
     */
    @Override
    public String toString() {
        return "Node at " + this.coordinates.toString() + ", facing " + cardinalIntToString(this.cardinalDirection) + ", isRight: " + this.isRight;
    }
}
