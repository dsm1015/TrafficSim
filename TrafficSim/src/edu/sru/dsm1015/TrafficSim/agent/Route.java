package edu.sru.dsm1015.TrafficSim.agent;

import edu.sru.dsm1015.TrafficSim.grid.Coordinate;
import edu.sru.dsm1015.TrafficSim.grid.Grid;
import edu.sru.dsm1015.TrafficSim.search.AStarSearchEngine;
import edu.sru.dsm1015.TrafficSim.search.BreadthFirstSearchEngine;
import edu.sru.dsm1015.TrafficSim.search.DepthFirstSearchEngine;
import edu.sru.dsm1015.TrafficSim.search.Location;

/*
 * TrafficSim - Route
 * edu.sru.dsm1015.TrafficSim.agent.Route
 * Represents a route between two coordinates in the grid using search algorithms,
 * and contains nested classes for managing the route as a queue.
 */

public class Route {
    private CoordinateQueue route;
    private Coordinate start;
    private Coordinate destination;
    private int index;
    private Route internalRoute;
    private SearchAlgorithm searchAlgorithm;

    public Route(Grid grid, Coordinate start, Coordinate destination, String searchAlgorithm) {
        this.start = start;
        this.destination = destination;
        if(searchAlgorithm == "dfs") {
        	SearchAlgorithm dfs = new DepthFirstSearchEngine(grid, start, destination);
        	this.searchAlgorithm = dfs;
        } else if(searchAlgorithm == "bfs") {
        	SearchAlgorithm bfs = new BreadthFirstSearchEngine(grid, start, destination);
        	this.searchAlgorithm = bfs;
        } else if (searchAlgorithm == "astar") {
        	SearchAlgorithm astar = new AStarSearchEngine(grid, start, destination);
        	this.searchAlgorithm = astar;
        //default
        } else {
        	SearchAlgorithm bfs = new BreadthFirstSearchEngine(grid, start, destination);
        	this.searchAlgorithm = bfs;
        }
        this.route = createRoute(grid, start, destination);
    }

    public CoordinateQueue createRoute(Grid grid, Coordinate start, Coordinate destination) {
        this.index = 0;
        Location[] path = searchAlgorithm.getPath();
        //((AbstractSearchEngine) searchAlgorithm).printPath(path);

        CoordinateQueue queue = new CoordinateQueue();

        // Loop through the searchPath array and add the coordinates to the queue
        for (int i = 0; i < path.length; i++) {
            Location location = path[i];
            if (location != null) {
                queue.addToBackOfQueue(new Coordinate(location.getX(), location.getY()));
            }
        }

        return queue;
    }
	
	protected class CoordinateQueue {
		private Coordinate [] queue;
        private int tail, head, len;
        public CoordinateQueue(int num) {
            queue = new Coordinate[num];
            head = tail = 0;
            len = num;
        }
        public CoordinateQueue() {
            this(400);
        }
        public void addToBackOfQueue(Coordinate n) {
            queue[tail] = n;
            if (tail >= (len - 1)) {
                tail = 0;
            } else {
                tail++;
            }
        }
        public Coordinate removeFromFrontOfQueue() {
        	Coordinate ret = queue[head];
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
        public Coordinate peekAtFrontOfQueue() {
            return queue[head];
        }
    }
	
	
	public CoordinateQueue getRoute(){
		return this.route;
	}
	
	public Coordinate getStart() {
		return this.start;
	}
	
	public Coordinate getDestination() {
		return this.destination;
	}
	
	public int routeLength() {
		return route.len;
	}
	
	public Coordinate peekRoute() {
		return route.peekAtFrontOfQueue();
	}
	
	public Coordinate dequeueRoute() {
		return route.removeFromFrontOfQueue();
	}
	
	public void setIndex(int i) {
		this.index = i;
	}
	
	public int getIndex() {
		return this.index;
	}
	
	public Route getInternalRoute() {
		return this.internalRoute;
	}
	
	public void setInternalRoute(Route internalRoute) {
		this.internalRoute = internalRoute;
	}
	
	public interface SearchAlgorithm {
	    Location[] getPath();
	}
}

