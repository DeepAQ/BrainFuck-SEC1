package ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;

/**
 * Created by adn55 on 16/5/16.
 */
public class BFTab extends Tab {

    public BFTab(String name, String version) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("assets/BFTab.fxml"));
        loader.setController(this);
        Node tabNode = loader.load();
        this.setContent(tabNode);
        this.setText(name);
        this.fileName = name;
        this.fileVersion = version;
        String title = name;
        if (!version.isEmpty()) {
            title = title + version;
        }
        this.setText(title);
    }

    @FXML
    public TextArea textCode, textInput, textOutput;

    public String fileName, fileVersion;

}
