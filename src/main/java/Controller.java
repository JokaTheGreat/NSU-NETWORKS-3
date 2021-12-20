import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Pair;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

public class Controller implements Initializable {

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

    private Requester requester;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            requester = new Requester();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        placesList.setCellFactory(param -> new ListCell<>(){
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                }
                else {
                    double cellWidth = param.getWidth() - 30;

                    setMinWidth(cellWidth);
                    setMaxWidth(cellWidth);
                    setPrefWidth(cellWidth);

                    setWrapText(true);

                    setText(item);
                }
            }
        });
    }

    @FXML
    private void buttonClicked() throws Exception {
        weatherLabel.setText("");
        locationsList.setItems(null);
        placesList.setItems(null);

        String requestedPlace = searchField.getText();
        List<String> names = requester.requestLocations(requestedPlace).get();

        ObservableList<String> locations = FXCollections.observableArrayList(names);
        locationsList.setItems(locations);
    }

    @FXML
    private void locationListClicked() throws Exception {
        String chosenLocation = locationsList.getSelectionModel().getSelectedItem();

        if (chosenLocation != null) {
            int chosenLocationId = locationsList.getSelectionModel().getSelectedIndex();

            CompletableFuture<List<CompletableFuture<Pair<String, String>>>> placesWithDescriptions = requester.requestPlacesWithDescriptions(chosenLocationId);
            CompletableFuture<Double> temp = requester.requestTemp(chosenLocationId);

            String weather = String.format("Weather: %.2g \u00B0C",
                    temp.get()
            );
            weatherLabel.setText(weather);

            List<CompletableFuture<Pair<String,String>>> placesWithDescriptionsList = placesWithDescriptions.get();

            if (placesWithDescriptionsList == null) {
                placesList.setItems(null);
                return;
            }

            List<String> placesWithDescription = new ArrayList<>();
            for(CompletableFuture<Pair<String, String>> futurePair : placesWithDescriptionsList) {
                Pair<String, String> nameAndDescr = null;
                try {
                    nameAndDescr = futurePair.get();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                if (nameAndDescr != null) {
                    String name = nameAndDescr.getKey();
                    if (name != null && name != "") {
                        placesWithDescription.add(
                                String.format("%s\n\n%s", name, nameAndDescr.getValue())
                        );
                    }
                }
            }

            ObservableList<String> places = FXCollections.observableArrayList(placesWithDescription);
            placesList.setItems(places);
        }
    }
}
