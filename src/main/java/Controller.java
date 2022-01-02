import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
    private ObservableList<String> placesObservableList;

    public static final String TRIANGLE_UP = "\u25B3";
    public static final String TRIANGLE_DOWN = "\u25BD";

    private Requester requester;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            requester = new Requester();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        Platform.runLater(() ->
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
            })
        );
    }

    private String getPlaceName(String placeWithDescription) {
        if (!placeWithDescription.contains("\n")) {
            return TRIANGLE_UP + " " + placeWithDescription.substring(placeWithDescription.indexOf(TRIANGLE_DOWN) + 2);
        }

        return placeWithDescription.substring(0, placeWithDescription.indexOf("\n"));
    }

    private boolean isInterestingPlace(String placeName) {
        return placeName.contains(TRIANGLE_DOWN) || placeName.contains(TRIANGLE_UP);
    }

    @FXML
    private void buttonClicked() throws Exception {
        Platform.runLater(() -> {
            weatherLabel.setText("");
            locationsList.setItems(null);
            placesList.setItems(null);
        });

        String requestedPlace = searchField.getText();
        requester.requestLocations(requestedPlace).thenAccept(names -> {
            ObservableList<String> locations = FXCollections.observableArrayList(names);
            Platform.runLater(() -> locationsList.setItems(locations));
        });
    }

    @FXML
    private void locationListClicked() throws Exception {
        String chosenLocation = locationsList.getSelectionModel().getSelectedItem();

        if (chosenLocation != null) {
            int chosenLocationId = locationsList.getSelectionModel().getSelectedIndex();

            CompletableFuture<List<String>> placesNames = requester.requestPlaces(chosenLocationId);
            CompletableFuture<Double> temp = requester.requestTemp(chosenLocationId);

            temp.thenAccept(temperature -> {
                Platform.runLater(() -> weatherLabel.setText(String.format("Weather: %.2g \u00B0C", temperature)));
            });

            placesNames.thenAccept(names -> {
                if (names == null) {
                    Platform.runLater(() -> placesList.setItems(null));
                    return;
                }

                placesObservableList = FXCollections.observableArrayList(names);
                Platform.runLater(() -> placesList.setItems(placesObservableList));
            });
        }
    }

    @FXML
    private void placesListClicked() throws ExecutionException, InterruptedException {
        String chosenPlace = placesList.getSelectionModel().getSelectedItem();

        if (chosenPlace != null && isInterestingPlace(chosenPlace)) {
            int chosenPlaceId = placesList.getSelectionModel().getSelectedIndex();

            CompletableFuture<String> description = requester.requestDescription(chosenPlaceId);

            description.thenAccept(descriptionText -> {
                String oldPlaceWithDescription = placesObservableList.get(chosenPlaceId);
                String placeName = getPlaceName(oldPlaceWithDescription);

                Platform.runLater(() -> placesObservableList.set(chosenPlaceId, placeName + "\n" + descriptionText));
            });
        }
    }
}
