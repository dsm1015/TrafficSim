package edu.sru.dsm1015.TrafficSim.grid;

/*
 * TrafficSim - Coordinate
 * edu.sru.dsm1015.TrafficSim.grid.Coordinate
 * Represents a coordinate in a 2D grid with x and y values.
 */
public class Coordinate {

    private int x;
    private int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean equals(Coordinate other) {
        if (other instanceof Coordinate) {
            Coordinate otherPoint = (Coordinate) other;
            return this.x == otherPoint.getX() && this.y == otherPoint.getY();
        }
        return false;
    }
    
    @Override
    public String toString() {
    	return "x: " + this.x + ", y: " + this.y;
    }
}
