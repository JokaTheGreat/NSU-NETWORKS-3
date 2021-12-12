import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;

public class Controller {

    @FXML
    private TextField searchField;
    @FXML
    private Button searchButton;
    @FXML
    private Label weatherLabel;
    @FXML
    private ListView<String> locationsList;
    @FXML
    private ListView<String> placesList;

    @FXML
    private void buttonClicked() throws Exception {
        /*String requestedPlace = searchField.getText();
        Coords coords = Net.getCoords(requestedPlace);
        List<Hits> hits = coords.getHits();
        List<String> names = new ArrayList<>();

        for (Hits hit : hits) {
            names.add(hit.getName());
        }

        for (String name : names) {
            System.out.println(name);
        }

        ObservableList<String> locations = FXCollections.observableArrayList(names);
        locationsList = new ListView<>(locations);*/
    }
}
