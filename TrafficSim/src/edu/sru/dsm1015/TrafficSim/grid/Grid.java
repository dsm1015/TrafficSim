package edu.sru.dsm1015.TrafficSim.grid;

import java.util.ArrayList;
import java.util.List;

/*
 * TrafficSim - Grid
 * edu.sru.dsm1015.TrafficSim.grid.Grid
 * Represents a grid containing cells. Provides methods to manage cells and their properties.
 */
public class Grid {
  private static int width;
  private static int height;
  private Cell[][] cells;
  private String searchAlgorithm;

  public Grid(int width, int height) {
      Grid.width = width;
      Grid.height = height;
      cells = new Cell[width][height];
      this.searchAlgorithm = "bfs";
  }

  public static int getWidth() {
      return width;
  }

  public static int getHeight() {
      return height;
  }
  
  public void setSearchAlgorithm(String a) {
	  this.searchAlgorithm = a;
  }
  
  public String getSearchAlgorithm() {
	  return this.searchAlgorithm;
  }

  public void initialize() {
      // Initialize each cell with a default value
      for (int i = 0; i < width; i++) {
          for (int j = 0; j < height; j++) {
              Coordinate tempCoordinate = new Coordinate(i, j);
              cells[i][j] = new Cell(tempCoordinate);
              
          }
      }
  }

  public Cell getCell(int x, int y) {
	  if (x >= width || x < 0 || y >= height || y < 0) {
		  return null;
	  }
      return cells[x][y];
  }
  
  public Cell getCell(Coordinate coor) {
      return cells[coor.getX()][coor.getY()];
  }

  public void setCell(int x, int y, Cell cell) {
      cells[x][y] = cell;
  }
  
  public Cell[][] getCells() {
	  return cells;
  }
  
  public boolean areCellsAdjacent(Cell cell1, Cell cell2) {
	    int x1 = cell1.getX();
	    int y1 = cell1.getY();
	    int x2 = cell2.getX();
	    int y2 = cell2.getY();

	    // Check if the cells are horizontally adjacent
	    if (Math.abs(x1 - x2) == 1 && y1 == y2) {
	    	System.out.println("horizontal");
	        return true;
	    }

	    // Check if the cells are vertically adjacent
	    if (Math.abs(y1 - y2) == 1 && x1 == x2) {
	        return true;
	    }

	    System.out.println("not adjacent");
	    // If neither of the above conditions are true, the cells are not adjacent
	    return false;
	}
  
  public List<Coordinate> getNeighbors(Coordinate cell) {
      List<Coordinate> neighbors = new ArrayList<>();
      int[] rowOffsets = {-1, 0, 1, -1, 1, -1, 0, 1};
      int[] colOffsets = {-1, -1, -1, 0, 0, 1, 1, 1};

      for (int i = 0; i < 8; i++) {
          int newRow = cell.getX() + rowOffsets[i];
          int newCol = cell.getY() + colOffsets[i];

          if (newRow >= 0 && newRow < this.width && newCol >= 0 && newCol < this.height) {
              neighbors.add(new Coordinate(newRow, newCol));
          }
      }

      return neighbors;
  }
  
  // toString method
  @Override
  public String toString() {
	  String result = "";
	  for(int i = 0; i < cells.length; i++){
          for(int j = 0; j < cells[i].length; j++){
              result += (cells[i][j].getCoordinates() + " ");
          }
          result += "\n";
      }
	  return result;
  }
  
  public void reset() {
	  for(int i = 0; i < cells.length; i++){
          for(int j = 0; j < cells[i].length; j++){
        	  cells[i][j].reset();
          }
      }
  }
}