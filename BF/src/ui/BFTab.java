package ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utils.DataMgr;
import utils.SessionMgr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by adn55 on 16/5/16.
 */
class BFTab extends Tab {

    public BFTab(String name, String version) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("assets/BFTab.fxml"));
        loader.setController(this);
        Node tabNode = loader.load();
        this.setContent(tabNode);
        this.fileName = name;
        this.fileVersion = version;
        this.updateTabName();
        this.setFontSize(DataMgr.data.fontSize);
        this.changeList.add("");
        this.caretList.add(0);

        textCode.textProperty().addListener((observable, oldValue, newValue) -> {
            modified = !textCode.getText().equals(originalCode);
            updateTabName();
            if (saveChangeThread.isAlive()) {
                saveChangeThread.interrupt();
            }
            saveChangeThread = new Thread(saveChange);
            saveChangeThread.start();
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
                if (result.isPresent() && result.get().equals(buttonTypeYes)) {
                    saveAction();
                } else if (!result.isPresent() || !result.get().equals(buttonTypeNo)) {
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

    @FXML
    private TextArea textCode, textInput, textOutput;

    public String fileName;
    private String fileVersion;
    private String originalCode = "";
    public boolean modified = false;

    private final ArrayList<String> changeList = new ArrayList<>();
    private final ArrayList<Integer> caretList = new ArrayList<>();
    private int changeIndex = 0;
    private Thread saveChangeThread = new Thread();
    private final Runnable saveChange = new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                return;
            }
            if (changeIndex >= 0 && textCode.getText().equals(changeList.get(changeIndex))) {
                return;
            }
            while (changeList.size() > changeIndex + 1) {
                changeList.remove(changeIndex + 1);
            }
            changeList.add(textCode.getText());
            caretList.add(textCode.getCaretPosition());
            if (changeList.size() > 100) {
                changeList.remove(0);
                caretList.remove(0);
            }
            changeIndex = changeList.size() - 1;
        }
    };

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
                Dialogs.showError(e.getLocalizedMessage());
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
                Dialogs.showError("Filename cannot be empty!");
                saveAsAction();
                return;
            }
            try {
                this.saveToFile(filename);
            } catch (Exception e) {
                Dialogs.showError(e.getLocalizedMessage());
            }
        }
    }

    public void exportAction(Stage parent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(fileName);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("BrainFuck source code", "*.bf"));
        File file = fileChooser.showSaveDialog(parent);
        if (file == null) return;
        try {
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), "utf-8");
            writer.write(textCode.getText());
            writer.flush();
            writer.close();
        } catch (Exception ignored) {}
    }

    public void cutAction() {
        textCode.cut();
    }

    public void copyAction() {
        textCode.copy();
    }

    public void pasteAction() {
        textCode.paste();
    }

    public void undoAction() {
        textCode.undo();
    }

    public void redoAction() {
        textCode.redo();
    }

    public void smartUndoAction() {
        if (changeIndex > 0) {
            changeIndex--;
            textCode.setText(changeList.get(changeIndex));
            textCode.positionCaret(caretList.get(changeIndex));
        }
    }

    public void smartRedoAction() {
        if (changeIndex < changeList.size() - 1) {
            changeIndex++;
            textCode.setText(changeList.get(changeIndex));
            textCode.positionCaret(caretList.get(changeIndex));
        }
    }

    public void runAction() {
        String code = textCode.getText();
        String input = textInput.getText();
        String output;
        try {
            String[] result = SessionMgr.execute(code, input);
            output = result[0] + "\n====================\nExecution success, used " + result[1] + "ms";
        } catch (Exception e) {
            output = "Execution error:\n" + e.getMessage();
        }
        textOutput.setText(output);
    }

    public void debugAction() throws Exception {
        DebugUI debugUI = new DebugUI(textCode.getText(), textInput.getText());
        debugUI.show();
    }

}
