package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import utils.SessionMgr;

/**
 * Created by adn55 on 16/6/20.
 */
public class ChangePasswordUI extends Stage {
    private Scene scene;

    public ChangePasswordUI() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("assets/ChangePasswordUI.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        this.scene = new Scene(root);
        this.setScene(this.scene);
        this.setTitle("Login");
        this.initStyle(StageStyle.UTILITY);
        this.initModality(Modality.APPLICATION_MODAL);
        this.setResizable(false);

        inputOldPassword.requestFocus();
    }


    @FXML
    private PasswordField inputOldPassword, inputNewPassword, inputNewPassword2;

    @FXML
    protected void onChangeAction(ActionEvent t) {
        String oldPassword = inputOldPassword.getText();
        String newPassword = inputNewPassword.getText();
        String newPassword2 = inputNewPassword2.getText();
        if (oldPassword.isEmpty() || newPassword.isEmpty() || newPassword2.isEmpty()) return;
        if (!newPassword.equals(newPassword2)) {
            Dialogs.showError("New passwords does not match!");
        }
        try {
            SessionMgr.changePassword(oldPassword, newPassword);
            Dialogs.showInfo("Password changed");
            this.close();
        } catch (Exception e) {
            Dialogs.showError(e.getLocalizedMessage());
        }
    }

    @FXML
    protected void onCancelAction(ActionEvent t) {
        this.close();
    }

    @FXML
    protected void onInputKeyPressed(KeyEvent t) {
        if (t.getCode().equals(KeyCode.ENTER)) {
            onChangeAction(null);
        }
    }
}
