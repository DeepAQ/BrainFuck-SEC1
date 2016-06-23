package ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Created by adn55 on 16/5/26.
 */
class AboutUI extends Stage {

    public AboutUI() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("assets/AboutUI.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        this.setScene(new Scene(root));
        this.setTitle("About");
        this.initStyle(StageStyle.UTILITY);
        this.initModality(Modality.APPLICATION_MODAL);
        this.setResizable(false);
    }
}
