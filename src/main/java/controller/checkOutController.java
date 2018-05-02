package controller;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by IntelliJ IDEA.
 *
 * This class is the controller for the check out window. All functions
 * in the check out will be handled here
 *
 * @Author: Zacky Kharboutli
 * @Date: 2018-03-29
 * @Project : HotelSystem
 */



public class checkOutController implements Initializable {
    @FXML
    private JFXButton checkInButton ,reserveButton, guestButton , logOutBtn;
    private MenuController mu;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mu=new MenuController();

        checkInButton.setOnAction(event -> {
            try {
                mu.ShowCheckInPage(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        reserveButton.setOnAction(event -> {
            try {
                mu.ShowMainPage(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        logOutBtn.setOnAction(event -> {
            try {
                mu.showLogInWindow(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        guestButton.setOnAction(event -> {
            try {
                mu.showGuestManagement(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
