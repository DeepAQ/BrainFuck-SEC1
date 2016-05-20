package runner;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.MainUI;
import utils.DataMgr;

/**
 * Created by adn55 on 16/5/19.
 */
public class ServerRunner extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    MainUI mainUI;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("BrainFuck Server");
        mainUI = new MainUI();
        primaryStage.setScene(new Scene(mainUI, 600, 400));
        setup();
        primaryStage.show();
    }

    public void setup() {
        DataMgr.loadFromFile();
    }

    @Override
    public void stop() {
        System.exit(0);
    }
}
