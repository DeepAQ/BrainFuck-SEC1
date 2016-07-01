package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by adn55 on 16/7/1.
 */
public class DebugUI extends Stage {

    public DebugUI(String code, String input) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("assets/DebugUI.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        this.setScene(new Scene(root));
        this.setTitle("Debugger");
        this.initModality(Modality.APPLICATION_MODAL);

        this.code = code;
        this.input = input;
    }

    private String code, input, status;
    @FXML
    private TextArea textCode, textInput, textOutput, textMemory;

    @FXML
    protected void onStartAction(ActionEvent t) {

    }

    @FXML
    protected void onStepAction(ActionEvent t) {

    }

    @FXML
    protected void onResumeAction(ActionEvent t) {

    }

    @FXML
    protected void onStopAction(ActionEvent t) {

    }

}
