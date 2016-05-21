package runner;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import ui.LoginUI;
import ui.MainUI;
import ui.SplashUI;
import utils.DataMgr;
import utils.SessionMgr;

/**
 * Created by adn55 on 16/5/19.
 */
public class ClientRunner extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private Stage mainStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Setup the application
        setup();
        // Show splash screen
        mainStage = new SplashUI();
        mainStage.show();
        Task delayedTask = new Task() {
            @Override
            protected Object call() throws Exception {
                Thread.sleep(1000);
                return null;
            }
        };
        delayedTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                // Try auto login
                if (!SessionMgr.tryAutoLogin()) {
                    try {
                        LoginUI loginStage = new LoginUI();
                        loginStage.setLoginSuccessHandler(new LoginUI.LoginSuccessHandler() {
                            @Override
                            public void onLoginSuccess() {
                                showMainStage();
                            }
                        });
                        loginStage.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    showMainStage();
                }
            }
        });
        new Thread(delayedTask).start();
    }

    private void showMainStage() {
        try {
            mainStage.close();
            mainStage = new MainUI();
            mainStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setup() {
        DataMgr.loadFromFile();
    }

    @Override
    public void stop() {
        System.exit(0);
    }
}
