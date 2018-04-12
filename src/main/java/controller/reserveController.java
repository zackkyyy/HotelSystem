package controller;


import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.ResourceBundle;

/**
 * Created by IntelliJ IDEA.
 *
 * This class is the controller for the reserve window.
 *
 * @User: Zacky Kharboutli
 * @Date: 2018-03-29
 * @Project : HotelSystem
 */
public class reserveController implements Initializable {

    @FXML
    private TabPane sss;
    @FXML
    private JFXTextField nights ,rooms;
    @FXML
    private JFXDatePicker checkOutField, checkInField;
    @FXML
    private MenuButton personsNumber, roomsNumber,cityName;
    @FXML
    private MenuItem p1 , p2 , p3 ,p4 ,p5,r1 , r2 ,r3 ,r4, r5, kalmar, växjö ;

    /**
     * This method runs when ever the user press on the check in option in the header
     *
     * @param event check in page requested
     * @throws IOException
     */
    public void ShowCheckInPage(ActionEvent event) throws IOException {
        Parent checkInPage = FXMLLoader.load(getClass().getResource(String.valueOf("/checkInWindow.fxml")));
        Scene CheckInScene = new Scene(checkInPage);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(CheckInScene);
        app_stage.show();
        System.out.println("CheckIn window showed from reserveController");
    }
    /**
     * This method runs when ever the user press on the check out option in the header
     *
     * @param event check out page requested
     * @throws IOException
     */
    public void showCheckOutWindow(ActionEvent event) throws IOException {
        Parent Checkinpage = FXMLLoader.load(getClass().getResource(String.valueOf("/checkOutWindow.fxml")));
        Scene mainWindowScene = new Scene(Checkinpage);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(mainWindowScene);
        app_stage.show();
        System.out.println("CheckOut window showed from reserveController");

    }
    /**
     * This method runs when ever the user press on the Guest management option in the header
     *
     * @param event Guest management page requested
     * @throws IOException
     */

    public void ShowGuestManagement(ActionEvent event) throws IOException {
        Parent checkInPage = FXMLLoader.load(getClass().getResource(String.valueOf("/guestManagement.fxml")));
        Scene CheckInScene = new Scene(checkInPage);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(CheckInScene);
        app_stage.show();
        System.out.println("GuestManagement window showed from reserveController");
    }

    /**
     * Method that checks the inputs of the data picker to ensure that the dates are right,
     * and calculate the number of staying nights
     */
    public void calculateNights() {
        Period intervalPeriod;

        // check that the check in is always before the check out
        if (checkInField.getValue().isAfter(checkOutField.getValue())) {
            checkOutField.setValue(checkInField.getValue().plusDays(1));
            nights.setText("1");
        }
        else {
            intervalPeriod = Period.between(checkInField.getValue(), checkOutField.getValue());
            String numberOfNights = intervalPeriod.toString().substring(1, intervalPeriod.toString().length() - 1);
            nights.setText(numberOfNights);
        }
    }
    /**
     *  This method is @Override because this class implement Initializble
     *  Here when initialize the button or the menu items to their actions
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        checkInField.setValue(LocalDate.now());
        checkOutField.setValue(LocalDate.now().plusDays(1));
        disablePreviousDates();
       /*
         Choose the city from the button menu
        */
        växjö.setOnAction(event1 -> {
            cityName.setText("Växjö");
        });
        kalmar.setOnAction(event1 -> {
            cityName.setText("Kalmar");

        });


        /*
        The following lines to decide the number of the Guest
        depending on the chosen number from the button menu
         */
        p1.setOnAction(event1 -> {
            personsNumber.setText("1");

        });
        p2.setOnAction(event1 -> {
            personsNumber.setText("2");

        });
        p3.setOnAction(event1 -> {
            personsNumber.setText("3");

        });
        p4.setOnAction(event1 -> {
            personsNumber.setText("4");

        });
        p5.setOnAction(event1 -> {
            personsNumber.setText("5");

        });
        /*
         * Initialize the number of room
         */
        r1.setOnAction(event1 ->{
            rooms.setText("1");
            roomsNumber.setText("1");
        });
        r2.setOnAction(event1 ->{
            rooms.setText("2");
            roomsNumber.setText("2");
        });
        r3.setOnAction(event1 ->{
            rooms.setText("3");
            roomsNumber.setText("3");
        });
        r4.setOnAction(event1 ->{
            rooms.setText("4");
            roomsNumber.setText("4");
        });
        r5.setOnAction(event1 ->{
            rooms.setText("5");
            roomsNumber.setText("5");
        });

        

    }
    
    // Disables (grayes out) all dates before today in both the DatePickers
    private void disablePreviousDates() {
    	Callback<DatePicker, DateCell> dayCellFactory;
    	
    	dayCellFactory = new Callback<DatePicker, DateCell>() {
    		public DateCell call(final DatePicker datePicker) {
    			return new DateCell() {
    				@Override public void updateItem(LocalDate item, boolean empty) {
    					super.updateItem(item, empty);

    					if(item.isBefore(LocalDate.now())) {
    						setDisable(true);
    					}
    				}
    			};
    		}
    	};

    	checkOutField.setDayCellFactory(dayCellFactory);
    	checkInField.setDayCellFactory(dayCellFactory);
    }
    /**
     * Setters and getters
     */

    public int getNumberOfnights(){
        return Integer.parseInt(nights.getText());
    }
    public int getNumberOfRooms(){
        return 0;
    }

    public void moveToNextTap(ActionEvent actionEvent){
        sss.getSelectionModel().selectNext();
    }
    public void moveToPreviousTap(ActionEvent actionEvent){
        sss.getSelectionModel().selectPrevious();
    }


}
