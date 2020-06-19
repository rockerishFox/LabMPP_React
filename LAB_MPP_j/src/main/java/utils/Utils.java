package utils;

import javafx.scene.control.Alert;

public class Utils {

    public static void showWarning(String message){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning Dialog");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    public static void showInformation(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
}
