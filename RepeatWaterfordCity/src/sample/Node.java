package sample;

import java.util.ArrayList;
import java.util.List;

public class Node extends javafx.scene.Node {

    private String name;
    private List<Edge> edges;
    private int x;
    private int y;

    public Node(String name, int x, int y) {
        this.name = name;
        this.edges = new ArrayList<Edge>();
        this.x = x;
        this.y = y;
    }
    //empty constructor makes empty object
    public Node(){

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getName() {
        return name;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public List<Node> getNeighbors() {
        List<Node> neighbors = new ArrayList<Node>(getEdges().size());
        for (Edge e : getEdges()) {
            neighbors.add(e.getTo());
        }
        return neighbors;
    }

    public void addOneWayNeighbor(Node neighbor, int weight, int longway){
        this.edges.add(new Edge(this, neighbor, weight, longway));
    }

    public void addTwoWayNeighbor(Node neighbor, int weight, int longway){
        addOneWayNeighbor(neighbor, weight, longway);
        neighbor.addOneWayNeighbor(this, weight, longway);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }

    public String fullString(){
        return name + "/" + x + "/" +y;
    }
}


