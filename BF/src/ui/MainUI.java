package ui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import utils.DataMgr;

import java.util.ArrayList;

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
        newEmptyTab();
    }

    private int untitled = 0;

    private void newEmptyTab() {
        untitled++;
        try {
            Tab tmpTab = new BFTab("" , Integer.toString(untitled));
            tabPane.getTabs().add(tmpTab);
            tabPane.getSelectionModel().select(tmpTab);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private TabPane tabPane;

    // File
    @FXML // New
    protected void onFileNewAction(ActionEvent t) {
        newEmptyTab();
    }

    @FXML // Open
    protected void onFileOpenAction(ActionEvent t) throws Exception {
        FileOpenUI fileOpenUI = new FileOpenUI(tabPane);
        fileOpenUI.show();
    }

    @FXML // Save
    protected void onFileSaveAction(ActionEvent t) {
        BFTab currentTab = (BFTab) tabPane.getSelectionModel().getSelectedItem();
        currentTab.saveAction();
    }

    @FXML // Save as
    protected void onFileSaveAsAction(ActionEvent t) {
        BFTab currentTab = (BFTab) tabPane.getSelectionModel().getSelectedItem();
        currentTab.saveAsAction();
    }

    private void closeTab(Tab tab) {
        Event event = new Event(EventType.ROOT);
        tab.getOnCloseRequest().handle(event);
        if (!event.isConsumed()) {
            tabPane.getTabs().remove(tab);
        }
    }

    @FXML // Close
    protected void onFileCloseAction(ActionEvent t) {
        BFTab currentTab = (BFTab) tabPane.getSelectionModel().getSelectedItem();
        closeTab(currentTab);
    }

    @FXML // Save All
    protected void onFileSaveAllAction(ActionEvent t) {
        for (Tab tab : tabPane.getTabs()) {
            ((BFTab) tab).saveAction();
        }
    }

    @FXML // Close All
    protected void onFileCloseAllAction(ActionEvent t) {
        ArrayList<Tab> tabs = new ArrayList<>();
        tabs.addAll(tabPane.getTabs());
        tabs.forEach(this::closeTab);
    }

    @FXML // Exit
    protected void onFileExitAction(ActionEvent t) {
        Platform.exit();
    }

    // View
    @FXML // Theme
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
        currentTab.runAction();

    }
}
