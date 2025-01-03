package edu.sru.dsm1015.TrafficSim.search;

import java.util.*;

import edu.sru.dsm1015.TrafficSim.agent.Route.SearchAlgorithm;
import edu.sru.dsm1015.TrafficSim.grid.Cell;
import edu.sru.dsm1015.TrafficSim.grid.Coordinate;
import edu.sru.dsm1015.TrafficSim.grid.Grid;

public class AStarSearchEngine extends AbstractSearchEngine implements SearchAlgorithm {

    public AStarSearchEngine(Grid grid, Coordinate start, Coordinate goal) {
        super(grid, start, goal);
        doSearchOn2DGrid();
    }

    private void doSearchOn2DGrid() {
        PriorityQueue<Location> openSet = new PriorityQueue<>(Comparator.comparingDouble(Location::getF));
        Map<Location, Location> cameFrom = new HashMap<>();

        Location startLocation = new Location(start.getX(), start.getY());
        startLocation.setG(0);
        startLocation.setH(heuristicCostEstimate(startLocation, goalLoc));
        startLocation.setF(startLocation.getH());

        openSet.add(startLocation);

        while (!openSet.isEmpty()) {
            Location current = openSet.poll();

            if (equals(current, goalLoc)) {
                reconstructPath(cameFrom, current);
                return;
            }

            Location[] neighbors = getPossibleMoves(current);

            for (Location neighbor : neighbors) {
                if (neighbor != null) {
                	double tentativeGScore = current.getG() + 1 * getTrafficDensityWeight(neighbor); // Assuming base move cost is 1


                    if (tentativeGScore < neighbor.getG()) {
                        cameFrom.put(neighbor, current);
                        neighbor.setG(tentativeGScore);
                        neighbor.setH(heuristicCostEstimate(neighbor, goalLoc));
                        neighbor.setF(neighbor.getG() + neighbor.getH());

                        if (!openSet.contains(neighbor)) {
                            openSet.add(neighbor);
                        }
                    }
                }
            }
        }
    }

    private double heuristicCostEstimate(Location a, Location b) {
        double baseCost = Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());
        double trafficDensityWeight = getTrafficDensityWeight(a);
        return baseCost * trafficDensityWeight;
    }

    private double getTrafficDensityWeight(Location loc) {
        Cell cell = grid.getCell(loc.getX(), loc.getY());
        int density = cell.getCitizens().size();
        double weight = 1 + (density / 10.0); // Change the denominator to adjust the weight scaling
        return weight;
    }

    private void reconstructPath(Map<Location, Location> cameFrom, Location current) {
        List<Location> totalPath = new ArrayList<>();
        totalPath.add(current);

        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            totalPath.add(current);
        }

        maxDepth = totalPath.size();
        Collections.reverse(totalPath);
        searchPath = totalPath.toArray(new Location[0]);
    }
}
