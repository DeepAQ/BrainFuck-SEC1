package runner;

import javafx.application.Application;
import javafx.stage.Stage;
import ui.LoginUI;
import ui.MainUI;
import ui.SplashUI;
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
        // Show splash screen
        try {
            mainStage = new SplashUI();
            setup();
            mainStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Check user login state
        if (SessionMgr.sessionId.isEmpty()) {
            LoginUI loginStage = new LoginUI();
            loginStage.setLoginSuccessHandler(new LoginUI.LoginSuccessHandler() {
                @Override
                public void onLoginSuccess() {
                    try {
                        mainStage.close();
                        mainStage = new MainUI();
                        mainStage.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            loginStage.show();
        }
    }

    public void setup() {

    }

    @Override
    public void stop() {
        System.exit(0);
    }
}
