package edu.sru.dsm1015.TrafficSim.search;

import edu.sru.dsm1015.TrafficSim.agent.Route.SearchAlgorithm;
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
public class BreadthFirstSearchEngine extends AbstractSearchEngine implements SearchAlgorithm {
    public BreadthFirstSearchEngine(Grid grid, Coordinate start, Coordinate goal) {
        super(grid, start, goal);
        doSearchOn2DGrid();
    }

    private void doSearchOn2DGrid() {
        int width = grid.getWidth();
        int height = grid.getHeight();
        boolean alReadyVisitedFlag[][] = new boolean[width][height];
        //float distanceToNode[][] = new float[width][height];
        Location predecessor[][] = new Location[width][height];
        LocationQueue queue = new LocationQueue();

        for (int i=0; i<width; i++) {
            for (int j=0; j<height; j++) {
                alReadyVisitedFlag[i][j] = false;
                //distanceToNode[i][j] = 10000000.0f;
                predecessor[i][j] = null;
            }
        }

        alReadyVisitedFlag[startLoc.getX()][startLoc.getY()] = true;
        //distanceToNode[startLoc.width][startLoc.height] = 0.0f;
        queue.addToBackOfQueue(startLoc);
        boolean success = false;
    outer:      
        while (queue.isEmpty() == false) {
            Location head = queue.peekAtFrontOfQueue();
            if (head == null) break; // ??
            Location [] connected = getPossibleMoves(head);
            for (int i=0; i<4; i++) {
                if (connected[i] == null) break;
                int w = connected[i].getX();
                int h = connected[i].getY();
                if (alReadyVisitedFlag[w][h] == false) {
                    //distanceToNode[w][h] = distanceToNode[w][h] + 1.0f;
                    alReadyVisitedFlag[w][h] = true;
                    predecessor[w][h] = head;
                    queue.addToBackOfQueue(connected[i]);
                    if (equals(connected[i], goalLoc)) {
                        success = true;
                        break outer; // we are done
                    }
                }
            }
            queue.removeFromFrontOfQueue(); // ignore return value
        }
	 	// now calculate the shortest path from the predecessor array:
	    maxDepth = 0;
	    if (success) {
	        searchPath[maxDepth++] = goalLoc;
	        for (int i=0; i<100; i++) {
	            searchPath[maxDepth] = predecessor[searchPath[maxDepth - 1].getX()][searchPath[maxDepth - 1].getY()];
	            maxDepth++;
	            if (equals(searchPath[maxDepth - 1], startLoc))  break;  // back to starting node
	        }
	    }
	
	    // Reverse the order of the searchPath array
	    for (int i = 0; i < maxDepth / 2; i++) {
	        Location temp = searchPath[i];
	        searchPath[i] = searchPath[maxDepth - 1 - i];
	        searchPath[maxDepth - 1 - i] = temp;
	    }
    }
    protected class LocationQueue {
        public LocationQueue(int num) {
            queue = new Location[num];
            head = tail = 0;
            len = num;
        }
        public LocationQueue() {
            this(400);
        }
        public void addToBackOfQueue(Location n) {
            queue[tail] = n;
            if (tail >= (len - 1)) {
                tail = 0;
            } else {
                tail++;
            }
        }
        public Location removeFromFrontOfQueue() {
            Location ret = queue[head];
            if (head >= (len - 1)) {
                head = 0;
            } else {
                head++;
            }
            return ret;
        }
        public boolean isEmpty() {
            return head == (tail + 1);
        }
        public Location peekAtFrontOfQueue() {
            return queue[head];
        }       
        private Location [] queue;
        private int tail, head, len;
    }
}
