import RPCprotocol.RPCProxy;
import controllers.ClientController;
import controllers.ControllerLogIn;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainClientFX extends Application {
    ClientController controller;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        RPCProxy proxy = new RPCProxy("localhost",55555);
        this.controller = new ClientController(proxy);
        initializeLogInWindow(primaryStage);
        primaryStage.show();
    }

    private void initializeLogInWindow(Stage primaryStage) throws IOException {
        primaryStage.setTitle("BasketTracker Log In");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("views/LogIn.fxml"));
        AnchorPane root = loader.load();
        primaryStage.setScene( new Scene(root,600,400) );
        ControllerLogIn controllerLogIn = loader.getController();
        controllerLogIn.setServiceUser(controller);
        controllerLogIn.setObserver(controller);
        controllerLogIn.setPrimaryStage(primaryStage);
    }
}
