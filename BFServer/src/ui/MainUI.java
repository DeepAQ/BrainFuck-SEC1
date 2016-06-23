package ui;

import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import service.HttpService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import utils.LogUtils;

/**
 * Created by adn55 on 16/5/13.
 */
public class MainUI extends Stage {

    public MainUI() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainUI.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        this.setScene(new Scene(root));
        this.setTitle("BrainFuck Server");
        this.setup();
    }

    private void setup() {
        LogUtils.init(this.textLogs);

        choiceLogLevel.setItems(FXCollections.observableArrayList("Debug", "Info", "Warning", "Error"));
        choiceLogLevel.getSelectionModel().select(0);
        choiceLogLevel.getSelectionModel().selectedIndexProperty().addListener(
                (observable, oldValue, newValue) -> {
                    LogUtils.setLogLevel((int) newValue);
                }
        );
    }

    private HttpService mainService;

    @FXML
    private ToggleButton btnStartStop;
    @FXML
    private TextField inputPort;
    @FXML
    private TextArea textLogs;
    @FXML
    private ChoiceBox<String> choiceLogLevel;

    @FXML
    protected void onExitAction() {
        System.exit(0);
    }

    @FXML
    protected void onStartStopClicked() {
        if (btnStartStop.isSelected()) {
            int port;
            btnStartStop.setSelected(false);
            try {
                port = Integer.parseInt(inputPort.getText());
            } catch (Exception e) {
                return;
            }
            mainService = new HttpService(port);
            if (mainService.start()) {
                btnStartStop.setSelected(true);
                btnStartStop.setText("Stop");
            }
        } else {
            mainService.stop();
            btnStartStop.setSelected(false);
            btnStartStop.setText("Start");
        }
    }

    @FXML
    protected void onUserManageClicked(ActionEvent t) throws Exception {
        new UserManageUI().show();
    }
}
