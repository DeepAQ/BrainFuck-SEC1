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
        this.setup();
    }

    private int untitled = 0;

    private void setup() {
        newTab();
    }

    private void newTab() {
        untitled++;
        try {
            Tab tmpTab = new Tab("Untitled" + untitled + ".bf", new BFTab());
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
