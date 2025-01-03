package edu.sru.dsm1015.TrafficSim.search;

import edu.sru.dsm1015.TrafficSim.agent.Route.SearchAlgorithm;
import edu.sru.dsm1015.TrafficSim.grid.Coordinate;
import edu.sru.dsm1015.TrafficSim.grid.Grid;

/**
 * 2D Maze Search: Performs a depth first search in a maze
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
public class DepthFirstSearchEngine extends AbstractSearchEngine implements SearchAlgorithm {
    private boolean[][] visited;
    private boolean goalFound;

    public DepthFirstSearchEngine(Grid grid, Coordinate start, Coordinate goal) {
        super(grid, start, goal);
        visited = new boolean[grid.getWidth()][grid.getHeight()];
        goalFound = false;
        doSearchOn2DGrid();
    }

    private void doSearchOn2DGrid() {
        maxDepth = 0;
        dfsRecursive(startLoc, 0);
    }

    private void dfsRecursive(Location loc, int depth) {
        visited[loc.getX()][loc.getY()] = true;
        searchPath[depth] = loc;

        if (equals(loc, goalLoc)) {
            maxDepth = depth + 1; // Increase the maxDepth by 1
            searchPath[maxDepth] = goalLoc; // Add the goal location to the searchPath array
            return;
        }

        Location[] neighbors = getPossibleMoves(loc);
        for (Location neighbor : neighbors) {
            if (neighbor != null && !visited[neighbor.getX()][neighbor.getY()]) {
                dfsRecursive(neighbor, depth + 1);

                // If the goal has been found, stop searching
                if (maxDepth != 0) {
                    return;
                }
            }
        }

        // Backtrack
        visited[loc.getX()][loc.getY()] = false;
    }
}
