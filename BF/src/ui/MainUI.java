package ui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import utils.DataMgr;
import utils.SessionMgr;

/**
 * Created by adn55 on 16/5/14.
 */
public class MainUI extends Stage {
    public Scene scene;

    public MainUI() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("assets/MainUI.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        this.scene = new Scene(root, 1024, 600);
        this.setScene(this.scene);
        this.setTitle("BrainFuck IDE");
        this.getIcons().add(new Image(getClass().getResourceAsStream("assets/StageIcon.png")));

        if (!DataMgr.data.theme.isEmpty()) {
            switchTheme(DataMgr.data.theme);
        }
        newTab();
    }

    private int untitled = 0;

    private void newTab() {
        untitled++;
        try {
            Tab tmpTab = new BFTab("Untitled" + untitled, "");
            tabPane.getTabs().add(tmpTab);
            tabPane.getSelectionModel().select(tmpTab);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private TabPane tabPane;

    // File
    //   New
    @FXML
    protected void onFileNewAction(ActionEvent t) {
        newTab();
    }

    //   Exit
    @FXML
    protected void onFileExitAction(ActionEvent t) throws Exception {
        Platform.exit();
    }

    // View
    //   Theme
    @FXML
    protected void onViewThemeAction(ActionEvent t) {
        String theme = ((RadioMenuItem) t.getSource()).getId();
        switchTheme(theme);
        DataMgr.data.theme = theme;
        DataMgr.saveToFile();
    }

    private void switchTheme(String theme) {
        scene.getStylesheets().clear();
        switch (theme) {
            case "styleDark":
                scene.getStylesheets().add(getClass().getResource("assets/dark.css").toString());
                break;
            case "styleDeepDark":
                scene.getStylesheets().add(getClass().getResource("assets/deepdark.css").toString());
                System.out.println("Fantasy!");
                break;
        }
    }

    // Run
    @FXML
    protected void onRunAction(ActionEvent t) {
        BFTab currentTab = (BFTab) tabPane.getSelectionModel().getSelectedItem();
        String code = currentTab.textCode.getText();
        String input = currentTab.textInput.getText();
        if (code.isEmpty()) return;
        String output;
        try {
            output = SessionMgr.execute(code, input);
        } catch (Exception e) {
            output = "Execution error:\n" + e.getMessage();
        }
        currentTab.textOutput.setText(output);
    }
}
