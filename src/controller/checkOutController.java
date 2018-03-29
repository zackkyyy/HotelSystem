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
public class checkOutController {

    public void ShowMainPage (ActionEvent event) throws IOException {
        Parent Checkinpage = FXMLLoader.load(getClass().getResource(String.valueOf("../View/MainWindow.fxml")));
        Scene mainWindowScene = new Scene(Checkinpage);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(mainWindowScene);
        app_stage.show();
        System.out.print("main window showed");
    }

    public void showCheckInWindow (ActionEvent event) throws IOException {
        Parent checkInPage = FXMLLoader.load(getClass().getResource(String.valueOf("../View/checkInWindow.fxml")));
        Scene CheckInScene = new Scene(checkInPage);
        Stage app_stage = (Stage)((Node) event.getSource()    ).getScene().getWindow();
        app_stage.setScene(CheckInScene);
        app_stage.show();
        System.out.print("s");

    }
    public void ShowGuestManagement (ActionEvent event) throws IOException {
        Parent checkInPage = FXMLLoader.load(getClass().getResource(String.valueOf("../View/guestManagement.fxml")));
        Scene CheckInScene = new Scene(checkInPage);
        Stage app_stage = (Stage)((Node) event.getSource()    ).getScene().getWindow();
        app_stage.setScene(CheckInScene);
        app_stage.show();
        System.out.print("s");
    }
}
