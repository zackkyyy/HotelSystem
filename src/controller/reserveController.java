package controller;


import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;

/**
 * Created by zack on 2018-03-29.
 */
public class reserveController {
    @FXML
    private JFXDatePicker checkOut;
    @FXML
    private JFXDatePicker checkIn;

    @FXML
    private JFXTextField nights;



    public void ShowCheckInPage (ActionEvent event) throws IOException {
        Parent checkInPage = FXMLLoader.load(getClass().getResource(String.valueOf("../View/checkInWindow.fxml")));
        Scene CheckInScene = new Scene(checkInPage);
        Stage app_stage = (Stage)((Node) event.getSource()    ).getScene().getWindow();
        app_stage.setScene(CheckInScene);
        app_stage.show();
        System.out.print("s");
    }

    public void calculateNights (){
        LocalDate cehckin = checkIn.getValue();
        LocalDate checkout = checkOut.getValue();
        Period intervalPeriod = Period.between(cehckin, checkout);
        String numberOfnights= intervalPeriod.toString().substring(1,intervalPeriod.toString().length()-1);
        nights.setText(numberOfnights);

    }
    public void showCheckOutWindow (ActionEvent event) throws IOException {
        Parent Checkinpage = FXMLLoader.load(getClass().getResource(String.valueOf("../View/checkOutWindow.fxml")));
        Scene mainWindowScene = new Scene(Checkinpage);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(mainWindowScene);
        app_stage.show();

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
