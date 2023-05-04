package edu.sru.dsm1015.TrafficSim.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import edu.sru.dsm1015.TrafficSim.grid.Cell;
import edu.sru.dsm1015.TrafficSim.grid.Coordinate;
import edu.sru.dsm1015.TrafficSim.grid.Grid;

/**
 * 2D Maze Search
 *
 * <p/>
 * Copyright 1998-2012 by Mark Watson. All rights reserved.
 * <p/>
 * This software is can be used under either of the following licenses:
 * <p/>
 * 1. LGPL v3<br/>
 * 2. Apache 2
 * <p/>
 */
public class AbstractSearchEngine {
    public AbstractSearchEngine(Grid grid, Coordinate start, Coordinate goal) {
    	//create a maze
        this.grid = grid;
        
        //start/goal
        this.start= start;
        this.goal = goal;
        
        //allocates searchPath of Dimension objects
        initSearch();
    }
    public Grid getGrid() { return grid; }
    protected Grid grid;
    /**
     * We will use the Java type Location (fields width and height will
     * encode the coordinates in x and y directions) for the search path:
     */
    protected Location [] searchPath = null;
    protected int pathCount;
    protected int maxDepth;
    protected Location startLoc, goalLoc, currentLoc;
    protected boolean isSearching = true;
    protected Coordinate start, goal;

    //allocates searchPath of Dimension objects
    protected void initSearch() {
        if (searchPath == null) {
            searchPath = new Location[1000];
            for (int i=0; i<1000; i++) {
                searchPath[i] = new Location();
            }
        }
        pathCount = 0;
        startLoc = new Location(start.getX(), start.getY());
        currentLoc = startLoc;
        goalLoc = new Location(goal.getX(), goal.getY());
        searchPath[pathCount++] = currentLoc;
    }

    protected boolean equals(Location d1, Location d2) {
        return d1.getX() == d2.getX() && d1.getY() == d2.getY();
    }
    

    public Location [] getPath() {
      Location [] ret = new Location[maxDepth];
      for (int i=0; i<maxDepth; i++) {
        ret[i] = searchPath[i];
      }
      return ret;
    }
    
    public void printPath(Location[] path) {
        for (Location location : path) {
            if (location != null) {
                System.out.println("Location: (" + location.getX() + ", " + location.getY() + ")");
            }
        }
        System.out.println();
    }
    
    protected Location [] getPossibleMoves(Location loc) {
        Location tempMoves [] = new Location[4];
        tempMoves[0] = tempMoves[1] = tempMoves[2] = tempMoves[3] = null;
        int x = loc.getX();
        int y = loc.getY();
        int num = 0;
        Cell leftCell = grid.getCell(x - 1, y);
        if (leftCell != null && (leftCell.getCellType() == 1 || leftCell == grid.getCell(goal))) {
        	Location leftLoc = new Location(x - 1, y);
            leftLoc.setG(Double.MAX_VALUE);
            tempMoves[num++] = leftLoc;
        }
        
        Cell rightCell = grid.getCell(x + 1, y);
        if (rightCell != null && (rightCell.getCellType() == 1 || rightCell == grid.getCell(goal))) {
        	Location rightLoc = new Location(x + 1, y);
            rightLoc.setG(Double.MAX_VALUE);
            tempMoves[num++] = rightLoc;
        }
        
        Cell lowerCell = grid.getCell(x, y - 1);
        if (lowerCell != null && (lowerCell.getCellType() == 1 || lowerCell == grid.getCell(goal))) {
        	Location lowerLoc = new Location(x, y -1 );
            lowerLoc.setG(Double.MAX_VALUE);
            tempMoves[num++] = lowerLoc;
        }
        
        Cell upperCell = grid.getCell(x, y + 1);
        if (upperCell != null && (upperCell.getCellType() == 1 || upperCell == grid.getCell(goal))) {
        	Location upperLoc = new Location(x, y + 1);
        	upperLoc.setG(Double.MAX_VALUE);
            tempMoves[num++] = upperLoc;
        }
        return tempMoves;
    }
    
    protected Location[] getPossibleMovesByDensity(Location loc) {
        List<Location> tempMoves = new ArrayList<>();
        int x = loc.getX();
        int y = loc.getY();

        // Get neighbors
        Cell[] neighbors = new Cell[]{
                grid.getCell(x - 1, y),
                grid.getCell(x + 1, y),
                grid.getCell(x, y - 1),
                grid.getCell(x, y + 1)
        };

        // Sort neighbors by the number of citizens in each cell (lower is better)
        Arrays.sort(neighbors, Comparator.comparingInt(cell -> cell != null ? cell.getCitizens().size() : Integer.MAX_VALUE));

        for (Cell neighbor : neighbors) {
            if (neighbor != null && (neighbor.getCellType() == 1 || neighbor == grid.getCell(goal))) {
                tempMoves.add(new Location(neighbor.getCoordinates().getX(), neighbor.getCoordinates().getY()));
            }
        }

        return tempMoves.toArray(new Location[0]);
    }
}
