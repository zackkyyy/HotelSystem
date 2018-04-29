package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Reservation;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by IntelliJ IDEA.
 *
 * This class is the controller for the check in window. All functions
 * in the check in will be handled here
 *
 * @Author: Zacky Kharboutli
 * @Date: 2018-03-29
 * @Project : HotelSystem
 */
public class checkInController implements Initializable {
    @FXML
    JFXDatePicker date;
    @FXML
    JFXButton prevBut , nextBut;
    @FXML
    private TableView table;
    @FXML
    private TableColumn<Reservation, Integer> arrivalCol,departCol,priceCol,guestCol,roomCol;


    public void ShowMainPage (ActionEvent event) throws IOException {
            Parent Checkinpage = FXMLLoader.load(getClass().getResource(String.valueOf("/MainWindow.fxml")));
            Scene mainWindowScene = new Scene(Checkinpage);
            Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            app_stage.setScene(mainWindowScene);
            app_stage.show();
            System.out.println("Main window showed from checkInController");
    }

    public void showCheckOutWindow (ActionEvent event) throws IOException {
        Parent Checkinpage = FXMLLoader.load(getClass().getResource(String.valueOf("/checkOutWindow.fxml")));
        Scene mainWindowScene = new Scene(Checkinpage);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(mainWindowScene);
        app_stage.show();
        System.out.println("CheckOut window showed from checkInController");

    }
    public void ShowGuestManagement (ActionEvent event) throws IOException {
        Parent checkInPage = FXMLLoader.load(getClass().getResource(String.valueOf("/guestManagement.fxml")));
        Scene CheckInScene = new Scene(checkInPage);
        Stage app_stage = (Stage)((Node) event.getSource()    ).getScene().getWindow();
        app_stage.setScene(CheckInScene);
        app_stage.show();
        System.out.println("GuestManagement window showed from checkInController");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        date.setValue(LocalDate.now());
        getReservation();

    }

    public void getReservation(){
       DBParser dbParser =new DBParser();
        ArrayList<Reservation> todaysReser =new ArrayList<>() ;
        table.getItems().remove(0,table.getItems().size());
        ObservableList<Reservation> listOfReservations = FXCollections.observableArrayList(dbParser.getReservationByDate(date.getValue()));

        table.setItems(listOfReservations);
        arrivalCol.setCellValueFactory(new PropertyValueFactory<Reservation,Integer>("arrivalDate"));
        departCol.setCellValueFactory(new PropertyValueFactory<Reservation,Integer>("departureDate"));
        priceCol.setCellValueFactory(new PropertyValueFactory<Reservation,Integer>("price"));
        guestCol.setCellValueFactory(new PropertyValueFactory<Reservation,Integer>("guest"));
        roomCol.setCellValueFactory(new PropertyValueFactory<Reservation,Integer>("room"));
    }

    public void nextDay(ActionEvent e){
        date.setValue(date.getValue().plusDays(1));
        getReservation();
    }
    public void prevDay(ActionEvent e){
        date.setValue(date.getValue().minusDays(1));
        getReservation();
    }
}
