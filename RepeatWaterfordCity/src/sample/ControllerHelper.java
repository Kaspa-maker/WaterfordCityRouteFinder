package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.io.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import static sample.Controller.getPlaceList;

public class ControllerHelper {

    @FXML
    private TextField Name;

    @FXML
    private TextField StreetName;

    @FXML
    private TextField Distance;

    @FXML
    private TextField CoordinateX;

    @FXML
    private TextField CoordinateY;

    @FXML
    private TextField TimeTaken;

    @FXML
    public ChoiceBox<Node> From;

    @FXML
    public ChoiceBox<Node> To;

    Controller c = new Controller();

    /*submitInformation method adds the nodes to the list once the button submit is clicked*/
    public void submitInformation(ActionEvent event) throws IOException {

        List<Node> list = new ArrayList();
        list = getPlaceList();

        Node newNode = new sample.Node(Name.getText(), Integer.parseInt(CoordinateX.getText()), Integer.parseInt(CoordinateY.getText()));
        list.add(newNode);


        c.passValues(list, newNode);

    }

    /*pathHelper method saves all the edges created between two nodes into a txt file that can later be loaded to draw
    * all the edges.*/
   public void pathHelper(){

       try {
           FileWriter fstream = new FileWriter("WaterfordCityEdges.txt", true);
           BufferedWriter out = new BufferedWriter(fstream);

           From.getValue().addTwoWayNeighbor(To.getValue(), Integer.parseInt(TimeTaken.getText()), Integer.parseInt(Distance.getText()));

           out.write("\n" + StreetName.getText() + "/" + From.getValue() + "/" + To.getValue() + "/" + Integer.parseInt(TimeTaken.getText()) + "/" + Integer.parseInt((Distance.getText())));
           out.close();

       }
       catch (Exception ex){
           System.err.println("Error: " + ex.getMessage());
       }

   }

   /*showNodes method shows all the nodes that can be connected by the edge and allows the user to use a choice box
   * to connect nodes he wants*/
   public void showNodes(ActionEvent event) {

       for(int i = 0; i < getPlaceList().size(); i++){
           From.getItems().add(getPlaceList().get(i));
       }
       From.setValue(getPlaceList().get(0));

       for(int i = 0; i < getPlaceList().size(); i++){
           To.getItems().add(getPlaceList().get(i));
       }
       To.setValue(getPlaceList().get(1));
   }

}
