package sample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DijkstraGraph {
    private Graph graph;
    int time;
    int distance;

    public DijkstraGraph(Graph graph) {
        this.graph = graph;
    }

    public Graph getGraph() {
        return graph;
    }

    public List<Node> getPath(Node from, Node to, String typeOfPath) {

        time = 0;
        distance = 0;

        Map<Node, Integer> nodeDistanceMapping = getNodeDistanceMapping(from);

        Map<Node, Node> previousNodeMapping = getPreviousNodeMapping();


        Set<Node> unsettled = new HashSet<Node>();
        unsettled.add(from);


        Set<Node> settled = new HashSet<Node>();


        while (unsettled.size() != 0) {

            Node currentNode = getLowestUnsettledNode(unsettled, nodeDistanceMapping);

            int currentDistance = nodeDistanceMapping.get(currentNode);
            unsettled.remove(currentNode);


            for (final Edge e : currentNode.getEdges()) {
                int currentNeighborDistance = nodeDistanceMapping.get(e.getTo());
                int newNeighborDistance = 0;

                if(typeOfPath == "short") {
                    newNeighborDistance = currentDistance + e.getWeight();
                    time = newNeighborDistance;
                }

                if(typeOfPath == "long") {
                    newNeighborDistance = currentDistance + e.getLongway();
                    distance = newNeighborDistance;
                }


                if (!settled.contains(e.getTo()) && newNeighborDistance < currentNeighborDistance) {
                    nodeDistanceMapping.put(e.getTo(), newNeighborDistance);
                    previousNodeMapping.put(e.getTo(), currentNode);
                    unsettled.add(e.getTo());
                }
            }

            settled.add(currentNode);
        }



        List<Node> path = new ArrayList<Node>();
        for (Node n = to; n != null; n = previousNodeMapping.get(n)) {
            path.add(n);
        }
        Collections.reverse(path);
        return path;

    }

    public void getInfo(){

        if(time != 0)
            System.out.println("Time: " + time);
        if(distance != 0)
            System.out.println("Distance: " + distance);
    }

    private Map<Node, Node> getPreviousNodeMapping() {
        Map<Node, Node> mappings = new HashMap<>();
        for(Node n : getGraph().getNodes()) {
            mappings.put(n, null);
        }
        return mappings;
    }

    private Node getLowestUnsettledNode(Set<Node> unsettled, Map<Node, Integer> distanceMapping) {
        Iterator<Node> iter = unsettled.iterator();
        if (!iter.hasNext()) {
            return null;
        }
        Node selected = iter.next();
        while (iter.hasNext()) {
            Node n = iter.next();
            if (distanceMapping.get(n) < distanceMapping.get(selected)) {
                selected = n;
            }
        }
        return selected;
    }

    private Map<Node, Integer> getNodeDistanceMapping(Node from) {
        Map<Node, Integer> mappings = new HashMap<Node, Integer>();
        for(Node n : getGraph().getNodes()) {
            mappings.put(n, Integer.MAX_VALUE);
        }
        mappings.put(from, 0);
        return mappings;
    }
}
