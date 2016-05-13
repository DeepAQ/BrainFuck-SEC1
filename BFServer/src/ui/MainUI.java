package ui;

import httpSrv.HttpService;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 * Created by adn55 on 16/5/13.
 */
public class MainUI extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainUI.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        primaryStage.setTitle("BrainFuck Server");
        primaryStage.setScene(new Scene(root, 600, 400));
        LogUtils.logArea = this.textLogs;
        primaryStage.show();
        webView.getEngine().load(getClass().getResource("ServerDebugger.html").toString());
    }

    private HttpService mainService;

    @FXML
    private ToggleButton btnStartStop;
    @FXML
    private TextField inputPort;
    @FXML
    private TextArea textLogs;
    @FXML
    private WebView webView;

    @FXML
    protected void onExitAction() {
        System.exit(0);
    }

    @FXML
    protected void onStartStopClicked() {
        if (btnStartStop.isSelected()) {
            int port = 8081;
            btnStartStop.setSelected(false);
            try {
                port = Integer.parseInt(inputPort.getText());
            } catch (Exception e) {
                //TODO:
                return;
            }
            mainService = new HttpService(port);
            try {
                mainService.start();
            } catch (Exception e) {
                e.printStackTrace();
                //TODO:
                return;
            }
            btnStartStop.setSelected(true);
            btnStartStop.setText("Stop");
        } else {
            mainService.stop();
            btnStartStop.setSelected(false);
            btnStartStop.setText("Start");
        }
    }
}
