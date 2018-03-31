package controller;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import model.Guest;

import java.io.IOException;

/**
 * Created by zack on 2018-03-29.
 */
public class guestManagController {
    @FXML
    private JFXListView ss;
    @FXML
    private JFXTextField name, lastName,address ;

    Guest guest;
    public void ShowMainPage(ActionEvent actionEvent) throws IOException {
        Parent mainWindow = FXMLLoader.load(getClass().getResource(String.valueOf("../View/MainWindow.fxml")));
        Scene mainWindowScene = new Scene(mainWindow);
        Stage app_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        app_stage.setScene(mainWindowScene);
        app_stage.show();
        System.out.println("Main window showed from guestManagController");
    }

    public void showCheckInWindow(ActionEvent actionEvent) throws IOException {
        Parent CheckInPage = FXMLLoader.load(getClass().getResource(String.valueOf("../View/checkInWindow.fxml")));
        Scene checkInScene = new Scene(CheckInPage);
        Stage app_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        app_stage.setScene(checkInScene);
        app_stage.show();
        System.out.println("CheckIn window showed from guestManagController");
    }

    public void showCheckOutWindow(ActionEvent actionEvent) throws IOException {
        Parent CheckOutPage = FXMLLoader.load(getClass().getResource(String.valueOf("../View/checkOutWindow.fxml")));
        Scene CheckoutScene = new Scene(CheckOutPage);
        Stage app_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        app_stage.setScene(CheckoutScene);
        app_stage.show();
        System.out.println("CheckOut window showed from guestManagController");
    }


    /**
     * Delete items from the Guest list
     * @param e
     */
    public void deleteFromList(ActionEvent e){

        Object listOfNames1 =ss.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("heder");
        alert.setContentText("Are you sure you want to delete "+listOfNames1);
        alert.show();
        ss.getItems().remove(listOfNames1);
    }

    /**
     * This method to try to add items to the list by pressing on the log out button now
     *
     *TODO change it later
     * @param actionEvent
     */
    public void addToList(ActionEvent actionEvent) {
       /* String s = null;
        for (int i = 0 ; i<10 ;i++){
         s ="Item"+i;
            ss.getItems().add(s); }
        */



        guest = new Guest("Zacky", "Kharboutli ", "Stallvägen 20");
        ss.getItems().add(guest.toString());
    }

    public void fillGaps(){
        Object listOfNames1 =ss.getSelectionModel().getSelectedItem();
        name.setText(guest.getName());
        lastName.setText(guest.getLastName());
        address.setText(guest.getAddress());

    }

}
