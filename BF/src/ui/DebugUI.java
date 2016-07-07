package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import utils.DataMgr;
import utils.SessionMgr;

/**
 * Created by adn55 on 16/7/1.
 */
class DebugUI extends Stage {

    public DebugUI(String code, String input) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("assets/DebugUI.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        this.setScene(scene);
        this.setTitle("调试器");
        this.initModality(Modality.APPLICATION_MODAL);

        scene.getStylesheets().clear();
        switch (DataMgr.data.theme) {
            case "styleDark":
                scene.getStylesheets().add(getClass().getResource("assets/dark.css").toString());
                break;
            case "styleDeepDark":
                scene.getStylesheets().add(getClass().getResource("assets/deepdark.css").toString());
                break;
        }
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.F8), btnStep::fire);

        this.code = code;
        this.input = input;
        onStartAction(null);
    }

    private String code, input, status;
    @FXML
    private TextArea textCode, textInput, textOutput, textMemory;
    @FXML
    private Button btnStart, btnStep, btnResume, btnStop;

    private void updateStatus() {
        JSONObject jsonObj = (JSONObject) new JSONTokener(status).nextValue();
        // update code view
        if (jsonObj.has("codeptr")) {
            int codePtr = jsonObj.getInt("codeptr");
            if (codePtr >= code.length()) {
                disableButton();
            }
            String codeText = "";
            for (int i = 0; i < code.length(); i++) {
                String codeChar = code.substring(i, i+1);
                if (codeChar.trim().isEmpty()) codeChar = " ";
                if (i == codePtr) {
                    codeText = codeText + "{" + codeChar + "}";
                } else {
                    codeText = codeText + " " + codeChar + " ";
                }
            }
            textCode.setText(codeText);
        }
        // update input view
        if (jsonObj.has("inputptr")) {
            int inputPtr = jsonObj.getInt("inputptr");
            String inputText = "";
            for (int i = 0; i < input.length(); i++) {
                if (i == inputPtr) {
                    inputText = inputText + "{" + input.charAt(i) + "}";
                } else {
                    inputText = inputText + " " + input.charAt(i) + " ";
                }
            }
            textInput.setText(inputText);
        }
        // update output view
        if (jsonObj.has("output")) {
            textOutput.setText(jsonObj.getString("output"));
        }
        // update memory view
        if (jsonObj.has("memptr") && jsonObj.has("memory")) {
            int ptr = jsonObj.getInt("memptr");
            JSONArray memory = jsonObj.getJSONArray("memory");
            String memoryText = "  地址\t 值\n";
            for (int i = 0; i < memory.length(); i++) {
                if (i == ptr) {
                    memoryText = memoryText + "-> ";
                } else {
                    memoryText = memoryText + "   ";
                }
                String memChar = Character.toString((char) memory.getInt(i));
                if (memChar.trim().isEmpty()) {
                    memChar = "";
                }
                memoryText = memoryText + i + "\t" + memory.get(i) + " '" + memChar + "'\n";
            }
            textMemory.setText(memoryText);
        }
    }

    private void disableButton() {
        btnStep.setDisable(true);
        btnResume.setDisable(true);
        btnStop.setDisable(true);
        btnStart.setDisable(false);
    }

    @FXML
    protected void onStartAction(ActionEvent t) {
        try {
            status = SessionMgr.debug("start", code, input);
            updateStatus();
            btnStart.setDisable(true);
            btnStep.setDisable(false);
            btnResume.setDisable(false);
            btnStop.setDisable(false);
        } catch (Exception e) {
            Dialogs.showError("调试会话启动失败：\n" + e.getLocalizedMessage());
        }
    }

    @FXML
    protected void onStepAction(ActionEvent t) {
        try {
            status = SessionMgr.debug("step", "", "");
            updateStatus();
        } catch (Exception e) {
            Dialogs.showError("调试会话错误：\n" + e.getLocalizedMessage());
            disableButton();
        }
    }

    @FXML
    protected void onResumeAction(ActionEvent t) {
        try {
            status = SessionMgr.debug("resume", "", "");
            updateStatus();
        } catch (Exception e) {
            Dialogs.showError("调试会话错误：\n" + e.getLocalizedMessage());
        }
        disableButton();
    }

    @FXML
    protected void onStopAction(ActionEvent t) {
        try {
            status = SessionMgr.debug("stop", "", "");
        } catch (Exception e) {
            Dialogs.showError("调试会话错误：\n" + e.getLocalizedMessage());
        }
        disableButton();
    }

}
