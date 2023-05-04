package edu.sru.dsm1015.TrafficSim.datastructures;

public class Edge {
    /**
	 * Variable for storing the value of source (start) node
	 */
    private Node source;
    /**
	 * Variable for storing the value of target (end) node
	 */
    private Node target;
    /**
	 * Variable for storing the value of edge weight
	 */
    private double weight;


    public Edge(Node source, Node target, double weight) {
        this.source = source;
        this.target = target;
        this.weight = weight;
    }

    /**
	 * Getter method for the source variable
	 * @return The source node of the edge
	 */
    public Node getSource() {
        return source;
    }

    /**
	 * Setter method for the source variable
	 * @param source
	 */
    public void setSource(Node source) {
        this.source = source;
    }

    /**
	 * Getter method for the target variable
	 * @return The target node of the edge
	 */
    public Node getTarget() {
        return target;
    }

    /**
	 * Setter method for the target variable
	 * @param target
	 */
    public void setTarget(Node target) {
        this.target = target;
    }

    /**
	 * Getter method for the weight variable
	 * @return The weight of the edge
	 */
    public double getWeight() {
        return weight;
    }

    /**
	 * Setter method for the weight variable
	 * @param weight
	 */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
	  * Method for concatenating output into a single line
	  * @return The source/target of the edge and its weight all strung in one single-line output 
	  */
    @Override
    public String toString() {
        return source + " -> " + target + " (" + weight + ")";
    }
}
