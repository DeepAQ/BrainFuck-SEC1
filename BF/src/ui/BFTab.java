package ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;

/**
 * Created by adn55 on 16/5/16.
 */
public class BFTab extends SplitPane {

    public BFTab() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("assets/BFTab.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
    }

    @FXML
    public TextArea textCode, textInput, textOutput;

    public String fileName;
    public int fileVersion;

}
