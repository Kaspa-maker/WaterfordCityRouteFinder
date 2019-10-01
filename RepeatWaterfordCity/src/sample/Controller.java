package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


import static java.lang.Integer.parseInt;

public class Controller {

    public static List<sample.Node> placeList;
    public static List<Edge> edgeList;


    public static List<sample.Node> getPlaceList() {
        return placeList;
    }

    public static void setPlaceList(List<sample.Node> placeList) {
        Controller.placeList = placeList;
    }

    public static List<sample.Edge> getEdgeList() {
        return edgeList;
    }

    public static void setEdgeList(List<sample.Edge> edgeList) {
        Controller.edgeList = edgeList;
    }

    sample.Node startN;
    sample.Node midN;
    sample.Node endN;

    public static sample.Node myNewNode;
    public static sample.Node getMyNewNode() {
        return myNewNode;
    }

    public static void setMyNewNode(sample.Node myNewNode) {
        Controller.myNewNode = myNewNode;
    }

    BufferedImage bi;

    @FXML
    public  ChoiceBox<Node> From;

    @FXML
    private ChoiceBox<Node>Through;

    @FXML
    public ChoiceBox<Node>To;

    @FXML
    private ImageView img;

    public Label getFromOutput() {
        return fromOutput;
    }

    public void setFromOutput(Label fromOutput) {
        this.fromOutput = fromOutput;
    }



    @FXML
    private Label fromOutput,throughOutput,toOutput,streetOutput,timeOutput,distanceOutput;

    /*openMap method loads the image and populates the for, through and to choice boxes with the nodes
    * and also loads all the edges that were ever put into the program*/
    @FXML
    public void openMap() throws IOException {
        Image image = new Image("/sample/WC.jpg");
        img.setImage(image);


        setPlaceList(loadList());
        setEdgeList(loadEdgeList());

        for(int i = 0; i < placeList.size(); i++){
            From.getItems().add(placeList.get(i));
        }
        From.setValue(placeList.get(0));

        for(int i = 0; i < placeList.size(); i++){
            Through.getItems().add(placeList.get(i));
        }
        Through.setValue(placeList.get(0));

        for(int i = 0; i < placeList.size(); i++){
            To.getItems().add(placeList.get(i));
        }
        To.setValue(placeList.get(1));


        connectEdges();
    }

    /*drawRoads method gets the coordinates of from value and to value and creates a visible
    * link between the two nodes by drawing a line on the interface showing a path from one node
    * to another.*/
    @FXML
    public void drawRoads(ActionEvent event){
        sample.Node node1 = (sample.Node) From.getValue();
        sample.Node node2 = (sample.Node) To.getValue();

        for (sample.Edge edge : getEdgeList()){
            if(edge.getFrom().getName() == ((sample.Node) From.getValue()).getName() && edge.getTo().getName() == ((sample.Node) To.getValue()).getName()){
                node1 = edge.getFrom();
                node2 = edge.getTo();
            }
        }
        bi = SwingFXUtils.fromFXImage(img.getImage(), null);
        Graphics G = bi.getGraphics();
        G.setColor(Color.BLACK);
        Graphics2D G2D = (Graphics2D) G;
        G2D.setStroke(new BasicStroke(5));
        G.drawLine(node1.getX(),node1.getY(),node2.getX(),node2.getY());
        G.dispose();
        Image image = SwingFXUtils.toFXImage(bi,null);
        img.setImage(image);
    }
    /*shortestRoad method does the samething as drawRoads except it also takes the weight of
    * edge that was put in and using the Dijkstra graph it looks for a path from one node to another
    * that has to lowest weight. In order to do that it checks all the edges that can lead to either node
    * and calculates their weight and chooses the path with the lowest score. Once it calculates the path
    * it uses drawline build in function to draw the shortest path the the person can take to get from one node
    * to another*/
    public void shortestRoad(ActionEvent event) throws IOException {

        Graph g = new Graph(getPlaceList());

        DijkstraGraph dg = new DijkstraGraph(g);

        bi = SwingFXUtils.fromFXImage(img.getImage(),null);

        for(int i = 0; i<getPlaceList().size();i++){
            if(getPlaceList().get(i) == From.getValue()){
                startN = getPlaceList().get(i);
            }
        }

        for(int i = 0; i<getPlaceList().size();i++){
            if(getPlaceList().get(i) == Through.getValue()){
                midN = getPlaceList().get(i);
            }
        }

        for(int i = 0; i<getPlaceList().size();i++){
            if(getPlaceList().get(i) == To.getValue()){
                endN = getPlaceList().get(i);
            }
        }

        dg.getPath(startN, midN, "short");
        dg.getPath(midN,endN, "short");

        dg.getInfo();

        Graphics G = bi.getGraphics();
        G.setColor(Color.RED);
        Graphics2D G2D = (Graphics2D) G;
        G2D.setStroke(new BasicStroke(5));

        List<sample.Node>nodelist = new ArrayList<>();
        for(sample.Node n: dg.getPath(startN, midN,"short")){
            nodelist.add(n);
            System.out.println(n);
        }
        for(int i = 0; i < nodelist.size(); i++){
            if(i + 1 != nodelist.size()){
                sample.Node n1 = nodelist.get(i);
                sample.Node n2 = nodelist.get(i + 1);
                G.drawLine(n1.getX(), n1.getY(), n2.getX(), n2.getY());
            }
        }

        List<sample.Node>nodelist2 = new ArrayList<>();
        for(sample.Node n: dg.getPath(midN, endN,"short")){
            nodelist2.add(n);
            System.out.println(n);
        }
        for(int i = 0; i < nodelist2.size(); i++){
            if(i + 1 != nodelist2.size()){
                sample.Node n1 = nodelist2.get(i);
                sample.Node n2 = nodelist2.get(i + 1);
                G.drawLine(n1.getX(), n1.getY(), n2.getX(), n2.getY());
            }
        }


        G.dispose();
        Image image = SwingFXUtils.toFXImage(bi, null);
        img.setImage(image);

    }

    /*longestRoad does exaclty the same as shortestRoad except instead of counting the shortest path
    * it chooses the biggest weight to create the path for the longest road from one node to another*/
    public void longestRoad(ActionEvent event) throws IOException {

        Graph g = new Graph(getPlaceList());

        DijkstraGraph dg = new DijkstraGraph(g);

        bi = SwingFXUtils.fromFXImage(img.getImage(),null);

        for(int i = 0; i<getPlaceList().size();i++){
            if(getPlaceList().get(i) == From.getValue()){
                startN = getPlaceList().get(i);
            }
        }

        for(int i = 0; i<getPlaceList().size();i++){
            if(getPlaceList().get(i) == Through.getValue()){
                midN = getPlaceList().get(i);
            }
        }

        for(int i = 0; i<getPlaceList().size();i++){
            if(getPlaceList().get(i) == To.getValue()){
                endN = getPlaceList().get(i);
            }
        }

        dg.getPath(startN, midN, "long");
        dg.getPath(midN,endN, "long");

        Graphics G = bi.getGraphics();
        G.setColor(Color.PINK);
        Graphics2D G2D = (Graphics2D) G;
        G2D.setStroke(new BasicStroke(5));


        List<sample.Node>nodelist = new ArrayList<>();
        for(sample.Node n: dg.getPath(startN, midN, "long")){
            nodelist.add(n);
            System.out.println(n);
        }
        for(int i = 0; i < nodelist.size(); i++){
            if(i + 1 != nodelist.size()){
                sample.Node n1 = nodelist.get(i);
                sample.Node n2 = nodelist.get(i + 1);
                G.drawLine(n1.getX(), n1.getY(), n2.getX(), n2.getY());
            }
        }

        List<sample.Node>nodelist2 = new ArrayList<>();
        for(sample.Node n: dg.getPath(midN, endN,"long")){
            nodelist2.add(n);
            System.out.println(n);
        }
        for(int i = 0; i < nodelist2.size(); i++){
            if(i + 1 != nodelist2.size()){
                sample.Node n1 = nodelist2.get(i);
                sample.Node n2 = nodelist2.get(i + 1);
                G.drawLine(n1.getX(), n1.getY(), n2.getX(), n2.getY());
            }
        }

        G.dispose();
        Image image = SwingFXUtils.toFXImage(bi, null);
        img.setImage(image);

        dg.getInfo();

    }


    /*addDestination creates a new window in fxml that prompts the user with a window allowing
    * to add new destinations*/
    @FXML
    public void addDestination(ActionEvent event) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("NewDestination.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    /*addPath creates a new window in fxml that prompts the user with a window allowing him to
    * connect nodes */
    @FXML
    public void addPath(ActionEvent event) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("NewPath.fxml"));
        setPlaceList(placeList);
        Scene scene = new Scene(root);
        Stage stage = new Stage();

        stage.setScene(scene);
        stage.show();

    }

    /*passValues method pass all the values between controller and controllerhelper classes
    * and later sets those values here in the controller and replaces the old placelist with new list nodeslist
    * with updated values and it does the same thing for node*/
    public void passValues(List<sample.Node> nodesList, sample.Node newNode){
        setPlaceList(nodesList);
        setMyNewNode(newNode);
   }


   /*refreshNodes allows the user to refresh the program after adding new nodes with out turning on and off the whole
   * program*/
    @FXML
    public void refreshNodes(ActionEvent event) throws IOException {

        setEdgeList(loadEdgeList());

        /*From = new ChoiceBox<>();
        To = new ChoiceBox<>();
        Through = new ChoiceBox<>();*/

        for(int i = 0; i < getPlaceList().size(); i++){
            From.getItems().add(getPlaceList().get(i));
        }
        From.setValue(getPlaceList().get(0));

        for(int i = 0; i < getPlaceList().size(); i++){
            Through.getItems().add(getPlaceList().get(i));
        }
        Through.setValue(getPlaceList().get(0));

        for(int i = 0; i < getPlaceList().size(); i++){
            To.getItems().add(getPlaceList().get(i));
        }
        To.setValue(getPlaceList().get(getPlaceList().size() - 1));


    }

    /*saveIntoFile saves all the nodes into a txt file allowing the program to save all the added nodes
     to be used later once the program needs loading*/
   @FXML
    public void saveIntoFile(ActionEvent event){
        try {
            FileWriter fstream = new FileWriter("WaterfordCity.txt", true);
            BufferedWriter out = new BufferedWriter(fstream);

            /*for(int i = 0; i < getPlaceList().size(); i++){
                out.write(getPlaceList().get(i).fullString() + "\n");
            }*/

            if(getMyNewNode().getName() != null && getMyNewNode().getX() != 0 && getMyNewNode().getY() != 0)
                out.write("\n" + getMyNewNode().getName() + "/" + getMyNewNode().getX() + "/" + getMyNewNode().getY() );

            out.close();

        }
        catch (Exception ex){
            System.err.println("Error: " + ex.getMessage());
        }

    }

    /*loadlist loads all the nodes that were saved in previous runs from the txt file and allows the user to use the
    * previous nodes that he made*/
    public List<sample.Node> loadList() throws IOException {
        FileReader fileReader = new FileReader("WaterfordCity.txt");

        BufferedReader bufferedReader = new BufferedReader(fileReader);

        List<sample.Node> listFromFile = new ArrayList<>() ;


        String thisLine = null;
        while ((thisLine = bufferedReader.readLine()) != null) {
            String[] splited = thisLine.split("/");
            listFromFile.add(new sample.Node(splited[0], parseInt(splited[1]), parseInt(splited[2])));
        }

        bufferedReader.close();

        return listFromFile;

    }

    /*loadEdgeList loads all the edges that were previously saved*/
    @FXML
    public List<sample.Edge> loadEdgeList() throws IOException {
        FileReader fileReader = new FileReader("WaterfordCityEdges.txt");

        BufferedReader bufferedReader = new BufferedReader(fileReader);

        List<sample.Edge> listFromFile = new ArrayList<>() ;


        String thisLine = null;
        sample.Node node1 = new sample.Node();
        sample.Node node2 = new sample.Node();
        while ((thisLine = bufferedReader.readLine()) != null) {
            String[] splited = thisLine.split("/");
            for(sample.Node node : getPlaceList()){
                if (splited[1] == node.getName())
                {
                    node1 = node;
                }
            }
            for(sample.Node node : getPlaceList()){
                if (splited[2] == node.getName())
                {
                    node2 = node;
                }
            }
            int weight = Integer.parseInt(splited[3]);
            int longway = Integer.parseInt(splited[4]);

            listFromFile.add(new sample.Edge(node1, node2, weight, longway));
        }

        bufferedReader.close();

        return listFromFile;

    }


    /*loadFromFile is a confirmation load that allows the programmer to check if the save actually work as it prints out
    * the results into the console confirming that everything was saved correctly */
    @FXML
    public void loadFromFile(ActionEvent event) throws IOException {
        FileReader fileReader = new FileReader("WaterfordCity.txt");

        BufferedReader bufferedReader = new BufferedReader(fileReader);


        String thisLine = null;
        while ((thisLine = bufferedReader.readLine()) != null) {
            System.out.println(thisLine);
        }

        bufferedReader.close();



        FileReader fileReaderEdges = new FileReader("WaterfordCityEdges.txt");

        BufferedReader bufferedReaderEdges = new BufferedReader(fileReaderEdges);


        String thisLineEdge = null;
        while ((thisLineEdge = bufferedReaderEdges.readLine()) != null) {
            System.out.println(thisLineEdge);
        }

        bufferedReaderEdges.close();
    }

    /*all the edges that connect the nodes that were created.*/
    public void connectEdges() throws IOException {
        getPlaceList().get(1).addTwoWayNeighbor(getPlaceList().get(15), 22, 2);
        getPlaceList().get(1).addTwoWayNeighbor(getPlaceList().get(2), 29,3);
        getPlaceList().get(15).addTwoWayNeighbor(getPlaceList().get(2), 7,1);
        getPlaceList().get(1).addTwoWayNeighbor(getPlaceList().get(4), 16,1);
        getPlaceList().get(4).addTwoWayNeighbor(getPlaceList().get(5), 17,1);
        getPlaceList().get(4).addTwoWayNeighbor(getPlaceList().get(3), 13,1);
        getPlaceList().get(4).addTwoWayNeighbor(getPlaceList().get(6), 21,2);
        getPlaceList().get(3).addTwoWayNeighbor(getPlaceList().get(6), 8,1);
        getPlaceList().get(3).addTwoWayNeighbor(getPlaceList().get(5), 7,1);
        getPlaceList().get(5).addTwoWayNeighbor(getPlaceList().get(8), 17,2);
        getPlaceList().get(8).addTwoWayNeighbor(getPlaceList().get(7), 11,1);
        getPlaceList().get(8).addTwoWayNeighbor(getPlaceList().get(9), 9,2);
        getPlaceList().get(9).addTwoWayNeighbor(getPlaceList().get(10), 29,3);
        getPlaceList().get(1).addTwoWayNeighbor(getPlaceList().get(11), 11,1);
        getPlaceList().get(5).addTwoWayNeighbor(getPlaceList().get(12), 9,1);
        getPlaceList().get(12).addTwoWayNeighbor(getPlaceList().get(6), 15,2);
        getPlaceList().get(8).addTwoWayNeighbor(getPlaceList().get(13), 2,0);
        getPlaceList().get(7).addTwoWayNeighbor(getPlaceList().get(14), 23,2);
        getPlaceList().get(14).addTwoWayNeighbor(getPlaceList().get(9), 21,2);

    }



    public void Information() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Information.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();

        stage.setScene(scene);
        stage.show();
    }

}
