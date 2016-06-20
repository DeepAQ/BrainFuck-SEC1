package ui;

import javafx.scene.control.Alert;

/**
 * Created by adn55 on 16/6/20.
 */
class Dialogs {
    public static void showDialog(Alert.AlertType type, String title, String header, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showError(String message) {
        showDialog(Alert.AlertType.ERROR, "Error", null, message);
    }

    public static void showInfo(String message) {
        showDialog(Alert.AlertType.INFORMATION, "Info", null, message);
    }
}
