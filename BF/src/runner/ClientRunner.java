package runner;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.MainUI;

/**
 * Created by adn55 on 16/5/19.
 */
public class ClientRunner extends Application {
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

    }

    @Override
    public void stop() {
        System.exit(0);
    }
}
