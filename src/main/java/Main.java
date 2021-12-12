import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;


public class Main extends Application {
    private static final double STAGE_WIDTH = 1120;
    private static final double STAGE_HEIGHT = 540;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/application.fxml")));
        stage.setTitle("Traveler guidebook");
        stage.setMaxWidth(STAGE_WIDTH);
        stage.setMinWidth(STAGE_WIDTH);
        stage.setMaxHeight(STAGE_HEIGHT);
        stage.setMinHeight(STAGE_HEIGHT);
        stage.setMaximized(true);
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}