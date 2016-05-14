package ui;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

/**
 * Created by adn55 on 16/5/14.
 */
public class MainUI extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private Scene scene;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainUI.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        primaryStage.setTitle("BrainFuck IDE");
        scene = new Scene(root, 1024, 768);
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    @FXML
    private TabPane tabPane;

    @FXML
    protected void onLightAction() {
        scene.getStylesheets().clear();
    }

    @FXML
    protected void onDarkAction() {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClass().getResource("dark.css").toString());
    }

    @FXML
    protected void onDeepDarkAction() {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClass().getResource("deepdark.css").toString());
    }
}
