package ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import utils.DataMgr;
import utils.SessionMgr;

import java.util.Optional;

/**
 * Created by adn55 on 16/5/16.
 */
public class BFTab extends Tab {

    public BFTab(String name, String version) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("assets/BFTab.fxml"));
        loader.setController(this);
        Node tabNode = loader.load();
        this.setContent(tabNode);
        this.fileName = name;
        this.fileVersion = version;
        this.updateTabName();
        this.setFontSize(DataMgr.data.fontSize);

        textCode.textProperty().addListener((observable, oldValue, newValue) -> {
            modified = !textCode.getText().equals(originalCode);
            updateTabName();
        });

        this.setOnCloseRequest(event -> {
            if (modified) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("File modified");
                alert.setHeaderText(null);
                alert.setContentText("File \"" + this.getText().replace("* ", "") + "\" has been modified, save it?");
                ButtonType buttonTypeYes = new ButtonType("Save");
                ButtonType buttonTypeNo = new ButtonType("Discard");
                ButtonType buttonTypeCancel = new ButtonType("Cancel");
                alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo, buttonTypeCancel);
                Optional<ButtonType> result = alert.showAndWait();
                if (!result.isPresent()) {
                    event.consume();
                }
                if (result.get().equals(buttonTypeYes)) {
                    saveAction();
                } else if (!result.get().equals(buttonTypeNo)) {
                    event.consume();
                }
            }
        });
    }

    public void setCode(String code) {
        this.originalCode = code;
        textCode.setText(code);
    }

    public void setFontSize(double size) {
        textCode.setFont(new Font(textCode.getFont().getName(), size));
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private TextArea textCode, textInput, textOutput;

    public String fileName, fileVersion;
    private String originalCode = "";
    public boolean modified = false;

    private void updateTabName() {
        String modFlag = "";
        if (this.modified) {
            modFlag = "* ";
        }
        if (fileName.isEmpty()) {
            this.setText(modFlag + "Untitled" + fileVersion + ".bf");
        } else {
            this.setText(modFlag + fileName + ".bf (" + fileVersion + ")");
        }
    }

    private void saveToFile(String fileName) throws Exception {
        String newVersion = SessionMgr.saveFile(textCode.getText(), fileName);
        this.fileName = fileName;
        this.fileVersion = newVersion;
        this.originalCode = textCode.getText();
        this.modified = false;
        this.updateTabName();
    }

    public void saveAction() {
        if (!this.modified) return;
        if (!this.fileName.isEmpty()) {
            try {
                this.saveToFile(this.fileName);
            } catch (Exception e) {
                showError(e.getLocalizedMessage());
            }
        } else {
            saveAsAction();
        }
    }

    public void saveAsAction() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Save as");
        dialog.setHeaderText("Save \"" + this.getText().replace("* ", "") + "\" as");
        dialog.setContentText("Filename:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String filename = result.get();
            if (filename.isEmpty()) {
                showError("Filename cannot be empty!");
                saveAsAction();
                return;
            }
            try {
                this.saveToFile(filename);
            } catch (Exception e) {
                showError(e.getLocalizedMessage());
            }
        }
    }

    public void runAction() {
        String code = textCode.getText();
        String input = textInput.getText();
        if (code.isEmpty()) return;
        String output;
        try {
            output = SessionMgr.execute(code, input);
        } catch (Exception e) {
            output = "Execution error:\n" + e.getMessage();
        }
        textOutput.setText(output);
    }

}
