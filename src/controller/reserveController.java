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
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.ResourceBundle;

/**
 * Created by zack on 2018-03-29.
 */
public class reserveController implements Initializable {
    @FXML
    private JFXDatePicker checkOutField;
    @FXML
    private JFXDatePicker checkInField;
    @FXML
    private JFXTextField nights;
    @FXML
    private TextField personsNumber;
    @FXML
    private MenuItem p1;
    @FXML
    private MenuItem p2;
    @FXML
    private MenuItem p3;
    @FXML
    private MenuItem p4;
    @FXML
    private MenuItem p5;
    @FXML
    private MenuItem växjö;
    @FXML
    private MenuItem kalmar;
    @FXML
    private TextField cityName;
    
    /**
     * This method runs when ever the user press on the check in option in the header
     *
     * @param event check in page requested
     * @throws IOException
     */
    public void ShowCheckInPage(ActionEvent event) throws IOException {
        Parent checkInPage = FXMLLoader.load(getClass().getResource(String.valueOf("../View/checkInWindow.fxml")));
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
        Parent Checkinpage = FXMLLoader.load(getClass().getResource(String.valueOf("../View/checkOutWindow.fxml")));
        Scene mainWindowScene = new Scene(Checkinpage);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(mainWindowScene);
        app_stage.show();
        System.out.println("CheckOut window showed from reserveController");

    }
    /**
     * This method runs when ever the user press on the guest management option in the header
     *
     * @param event guest management page requested
     * @throws IOException
     */

    public void ShowGuestManagement(ActionEvent event) throws IOException {
        Parent checkInPage = FXMLLoader.load(getClass().getResource(String.valueOf("../View/guestManagement.fxml")));
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
        LocalDate checkIn1 = checkInField.getValue();  // get the check in date
        LocalDate checkout = checkOutField.getValue();   // get the check out date

        // check that the check in is always before the check out
        if (checkInField.getValue().isAfter(checkout)) {
            checkOutField.setValue(checkIn1.plusDays(1));
            checkout = checkOutField.getValue();
            intervalPeriod = Period.between(checkIn1, checkout);
            String numberOfNights = intervalPeriod.toString().substring(1, intervalPeriod.toString().length() - 1);
            nights.setText(numberOfNights);
        }
        // check if the check in date is not before today
        else if (checkIn1.isBefore(LocalDate.now())) {
            checkInField.setValue(LocalDate.now());
            checkOutField.setValue(checkIn1.plusDays(1));
            checkIn1 = checkInField.getValue();
            checkout = checkOutField.getValue();
            intervalPeriod = Period.between(checkIn1, checkout);
            String numberOfNights = intervalPeriod.toString().substring(1, intervalPeriod.toString().length() - 1);
            nights.setText(numberOfNights);
        } else {
            intervalPeriod = Period.between(checkIn1, checkout);
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
        The following lines to decide the number of the guest
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
        
        disablePreviousDates();
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
}
