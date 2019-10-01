package sample;

public class Edge {
    private Node from;
    private Node to;
    private int weight;
    private int longway;


    public Edge(Node from, Node to, int weight, int longway) {
        this.from = from;
        this.to = to;
        this.weight = weight;
        this.longway = longway;
    }

    public Node getFrom() {
        return from;
    }

    public Node getTo() {
        return to;
    }

    public int getWeight() {
        return weight;
    }

    public int getLongway() {
        return longway;
    }


    @Override
    public String toString() {
        return from.getName() + "/" + to.getName() + "/" + weight + "/" +  getLongway();
    }
}

