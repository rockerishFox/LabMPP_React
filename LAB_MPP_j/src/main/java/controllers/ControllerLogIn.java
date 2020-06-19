package controllers;

import domain.Meci;
import domain.Utilizator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import services.IService;
import utils.Observable;
import utils.Utils;

import java.io.IOException;

public class ControllerLogIn {
    IService serviceUser;
    Observable observable;


    private Stage primaryStage;

    @FXML
    TextField textFieldUsername;
    @FXML
    TextField textFieldPassword;

    @FXML
    Label labelErrorUsername;
    @FXML
    Label labelErrorPassword;

    public void setServiceUser(IService serviceUser) {
        this.serviceUser = serviceUser;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    public void initialize() {
        textFieldUsername.setText("");
        textFieldPassword.setText("");
        labelErrorUsername.setText("");
        labelErrorPassword.setText("");

    }

    public void closeWindow() {
        primaryStage.close();
    }

    @FXML
    public void textFieldUsernameClearError() {
        labelErrorUsername.setText("");
    }

    @FXML
    public void textFieldPasswordClearError() {
        labelErrorPassword.setText("");
    }

    @FXML
    public void buttonLogInClicked() {
        String username = textFieldUsername.getText();
        String password = textFieldPassword.getText();
        if (username.isEmpty() || password.isEmpty()) {
            if (username.isEmpty()) {
                labelErrorUsername.setText("Please insert an username!");
            }
            if (password.isEmpty()) {
                labelErrorPassword.setText("Please insert a password!");
            }
            return;
        }

        Utilizator user = serviceUser.findByUsername(username);
        if (user == null) {
            labelErrorUsername.setText("Username does not exist!");
            textFieldUsername.setText("");
            textFieldPassword.setText("");

            return;
        }
        if (!password.equals(user.getPassword())) {
            labelErrorPassword.setText("Incorrect password!");
            textFieldPassword.setText("");
            return;
        }

        try {
            boolean succes = serviceUser.addToLoggedInUsers(user);
            if (succes) {
                initiateUserWindow(user);
                closeWindow();
            } else {
                Utils.showWarning("User already logged in!");
                textFieldUsername.setText("");
                textFieldPassword.setText("");
                textFieldPasswordClearError();
                textFieldUsernameClearError();
            }
        } catch (IOException x) {
            x.printStackTrace();
        }
    }

    private void initiateUserWindow(Utilizator user) throws IOException {
        Stage primaryStage = new Stage();
        primaryStage.setTitle("BasketTracker");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/UserApp.fxml"));

        AnchorPane root = (AnchorPane) loader.load();
        primaryStage.setScene(new Scene(root, 610, 400));

        ControllerUser controller = loader.getController();
        controller.startUp(this.serviceUser, user, primaryStage);
        controller.setObserver(this.observable);

        primaryStage.show();
    }

    public void setObserver(Observable controller) {
        this.observable = controller;
    }
}
