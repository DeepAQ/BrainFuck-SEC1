package ui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import utils.SessionMgr;

/**
 * Created by adn55 on 16/5/20.
 */
public class LoginUI extends Stage {
    public Scene scene;

    public LoginUI() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("assets/LoginUI.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        this.scene = new Scene(root);
        this.setScene(this.scene);
        this.setTitle("Login");
        this.initStyle(StageStyle.UTILITY);
        this.initModality(Modality.APPLICATION_MODAL);
        this.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
            }
        });
        inputUsername.requestFocus();
    }

    public void setLoginSuccessHandler(LoginSuccessHandler handler) {
        this.loginSuccessHandler = handler;
    }

    @FXML
    private TextField inputServer;
    @FXML
    private TextField inputUsername;
    @FXML
    private PasswordField inputPassword;
    @FXML
    private CheckBox checkRemember;
    @FXML
    private Button btnLogin;
    @FXML
    private Label labelStatus;

    private LoginSuccessHandler loginSuccessHandler;

    @FXML
    protected void onLoginAction(ActionEvent t) {
        String username = inputUsername.getText();
        String password = inputPassword.getText();
        if (username.isEmpty() || password.isEmpty()) return;
        try {
            SessionMgr.host = inputServer.getText();
            SessionMgr.login(username, password);
            if (checkRemember.isSelected()) {
                SessionMgr.saveLoginInfo(username, password);
            }
            this.close();
            loginSuccessHandler.onLoginSuccess();
        } catch (Exception e) {
            labelStatus.setText("Login failed: " + e.getMessage());
        }
    }

    public interface LoginSuccessHandler {
        void onLoginSuccess();
    }
}