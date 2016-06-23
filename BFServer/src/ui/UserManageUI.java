package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import utils.UserMgr;

import java.util.Optional;


/**
 * Created by adn55 on 16/6/23.
 */
class UserManageUI extends Stage {
    public UserManageUI() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UserManageUI.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        this.setScene(new Scene(root));
        this.setTitle("User Management");
        this.initStyle(StageStyle.UTILITY);
        this.initModality(Modality.APPLICATION_MODAL);
        this.setResizable(false);

        userList.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            btnDelete.setDisable(true);
            btnReset.setDisable(true);
            if (newValue != null) {
                btnDelete.setDisable(false);
                btnReset.setDisable(false);
            }
        });
        this.updateList();
    }

    private void updateList() {
        userList.getItems().clear();
        for (String username : UserMgr.getUserList()) {
            userList.getItems().add(username);
        }
        btnDelete.setDisable(true);
        btnReset.setDisable(true);
        userList.requestFocus();
    }

    @FXML
    private ListView userList;
    @FXML
    private Button btnDelete, btnReset;
    @FXML
    private TextField inputUsername;
    @FXML
    private PasswordField inputPassword;

    @FXML
    protected void onAddAction(ActionEvent t) {
        String username = inputUsername.getText();
        String password = inputPassword.getText();
        if (username == null || password == null) {
            return;
        }
        int result = UserMgr.addUser(username, password);
        if (result == 0) {
            inputUsername.clear();
            inputPassword.clear();
            Dialogs.showInfo("User added!");
        } else {
            Dialogs.showError("This user already exists!");
        }
        this.updateList();
    }

    @FXML
    protected void onDeleteAction(ActionEvent t) {
        String username = (String) userList.getSelectionModel().getSelectedItem();
        if (username == null) {
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure to delete?\nThis action cannot be undone!");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            UserMgr.delUser(username);
        }
        this.updateList();
    }

    @FXML
    protected void onResetAction(ActionEvent t) {
        String username = (String) userList.getSelectionModel().getSelectedItem();
        if (username == null) {
            return;
        }

        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Prompt");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10, 20, 10, 20));
        vBox.setSpacing(10);
        vBox.getChildren().addAll(new Label("New password :"), passwordField);
        dialog.getDialogPane().setContent(vBox);
        dialog.setResultConverter(param -> {
            if (param == ButtonType.OK) {
                return passwordField.getText();
            } else {
                return "";
            }
        });

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.get().isEmpty()) {
            UserMgr.resetPassword(username, UserMgr.hash(result.get()));
            Dialogs.showInfo("Password has been reset.");
        }
        this.updateList();
    }

    @FXML
    protected void onCloseAction(ActionEvent t) {
        this.close();
    }
}
