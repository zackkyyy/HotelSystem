package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by zack on 2018-03-29.
 */
public class guestManagController {
    public void ShowMainPage(ActionEvent actionEvent) throws IOException {
        Parent mainWindow = FXMLLoader.load(getClass().getResource(String.valueOf("../View/MainWindow.fxml")));
        Scene mainWindowScene = new Scene(mainWindow);
        Stage app_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        app_stage.setScene(mainWindowScene);
        app_stage.show();
    }

    public void showCheckInWindow(ActionEvent actionEvent) throws IOException {
        Parent Checkinpage = FXMLLoader.load(getClass().getResource(String.valueOf("../View/checkInWindow.fxml")));
        Scene checkInScene = new Scene(Checkinpage);
        Stage app_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        app_stage.setScene(checkInScene);
        app_stage.show();
    }

    public void showCheckOutWindow(ActionEvent actionEvent) throws IOException {
        Parent Checkoutpage = FXMLLoader.load(getClass().getResource(String.valueOf("../View/checkOutWindow.fxml")));
        Scene CheckoutScene = new Scene(Checkoutpage);
        Stage app_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        app_stage.setScene(CheckoutScene);
        app_stage.show();
    }
}
