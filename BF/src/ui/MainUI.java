package ui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

/**
 * Created by adn55 on 16/5/14.
 */
public class MainUI extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private Scene scene;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("assets/MainUI.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        primaryStage.setTitle("BrainFuck IDE");
        scene = new Scene(root, 1024, 768);
        primaryStage.setScene(scene);
        this.setup();
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        System.exit(0);
        super.stop();
    }

    private int untitled = 0;

    private void setup() {
        newTab();
    }

    private void newTab() {
        untitled++;
        try {
            Tab tmpTab = new Tab("Untitled" + untitled, new BFTab());
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
        stop();
    }

    // View

    @FXML
    protected void onViewThemeAction(ActionEvent t) {
        scene.getStylesheets().clear();
        switch (((RadioMenuItem) t.getSource()).getId()) {
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
        System.out.println(tabPane.getSelectionModel().getSelectedItem().getText());
    }
}
