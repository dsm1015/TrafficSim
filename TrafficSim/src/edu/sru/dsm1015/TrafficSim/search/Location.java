package edu.sru.dsm1015.TrafficSim.search;


public class Location {
    private int x;
    private int y;
    private double f;
    private double g;
    private double h;

    public Location(int x, int y) {
        this.setX(x);
        this.setY(y);
    }

    public Location() {
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getG() {
        return g;
    }

    public void setG(double g) {
        this.g = g;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

	public double getF() {
		return f;
	}

	public void setF(double f) {
		this.f = f;
	}
}

