package runner;

import javafx.application.Application;
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

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage = new MainUI();
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
