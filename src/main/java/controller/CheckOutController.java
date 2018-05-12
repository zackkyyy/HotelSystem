package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import model.Guest;
import model.Reservation;
import model.Room;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by IntelliJ IDEA.
 * <p>
 * This class is the controller for the check out window. All functions
 * in the check out will be handled here
 *
 * @Author: Zacky Kharboutli
 * @Date: 2018-03-29
 * @Project : HotelSystem
 */
public class CheckOutController implements Initializable {
    @FXML
    private JFXDatePicker date;
    @FXML
    private JFXButton checkInButton, reserveButton, guestButton, logOutBtn, noteButton;
    private MenuController mu;
    @FXML
    private TableView table;
    @FXML
    private TableColumn<Reservation, Integer> arrivalCol, departCol, priceCol, guestCol, roomCol;
    @FXML
    private Text selectionError, checkedOutText;
    @FXML
    private JFXTextArea noteText;
    private ObservableList<Reservation> reservations;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        mu = new MenuController();
        date.setValue(LocalDate.now());

        checkInButton.setOnAction(event -> {
            try {
                mu.showCheckInPage(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        reserveButton.setOnAction(event -> {
            try {
                mu.showMainPage(event);
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

    public void getReservation() {
        DBParser dbParser = new DBParser();
        ObservableList<Reservation> listOfReservations = FXCollections.observableArrayList(dbParser.getAllReservations());
        ArrayList<Reservation> filteredList = new ArrayList<Reservation>();
        for (int i = 0; i < listOfReservations.size(); i++) {
            if (listOfReservations.get(i).getDepartureDate().equals(date.getValue()) && listOfReservations.get(i).getCheckedIn()) {
                filteredList.add(listOfReservations.get(i));
            }
        }
        listOfReservations = FXCollections.observableArrayList(filteredList);
        table.setItems(listOfReservations);
        arrivalCol.setCellValueFactory(new PropertyValueFactory<Reservation, Integer>("arrivalDate"));
        departCol.setCellValueFactory(new PropertyValueFactory<Reservation, Integer>("departureDate"));
        priceCol.setCellValueFactory(new PropertyValueFactory<Reservation, Integer>("price"));
        guestCol.setCellValueFactory(new PropertyValueFactory<Reservation, Integer>("guest"));
        roomCol.setCellValueFactory(new PropertyValueFactory<Reservation, Integer>("rooms"));
    }

    public void nextDay(ActionEvent e) {
        date.setValue(date.getValue().plusDays(1));
        selectionError.setVisible(false);
        checkedOutText.setVisible(false);
        getReservation();
    }

    public void prevDay(ActionEvent e) {
        date.setValue(date.getValue().minusDays(1));
        selectionError.setVisible(false);
        checkedOutText.setVisible(false);
        getReservation();
    }

    public void checkOut() {
        DBParser dbParser = new DBParser();
        reservations = table.getSelectionModel().getSelectedItems();
        checkedOutText.setVisible(false);

        if (reservations.size() == 0) {
            selectionError.setVisible(true);
        } else {
            selectionError.setVisible(false);
            reservations = table.getSelectionModel().getSelectedItems();
            checkedOutText.setVisible(false);

                // Deletes the reservations and unbook the rooms
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Do you want to add notes in the guest profile");
                Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
                okButton.setText("Yes");
                Button NoButton = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
                NoButton.setText("No");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                    for (int i = 0; i < reservations.size(); i++) {
                        String GuestName = reservations.get(i).getGuest();
                        ArrayList<Integer> arrayList=new ArrayList<Integer>();
                        checkedOutText.setVisible(true);

                        for (int z=0;z<reservations.get(i).getRooms().size() ; z++){
                            arrayList.add(reservations.get(i).getRooms().get(z));
                        }
                        ArrayList<Room> tempRoom = dbParser.copyRoomByRoomNumber(arrayList);
                        for(int j = 0;j<tempRoom.size();j++){
                            tempRoom.get(j).setBooked(false);
                            dbParser.refreshRoomStatus(tempRoom.get(j));
                        }
                        dbParser.deleteReservationByRoomNumber(reservations.get(i).getId());
                        getReservation();


                        noteText.setVisible(true);
                        noteButton.setVisible(true);
                        noteButton.setOnAction(event -> {
                            Guest gst = dbParser.getGuestByName(GuestName);
                            gst.setNotes(noteText.getText());
                            try {
                                dbParser.editGuest(gst);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            noteText.setVisible(false);
                            noteButton.setVisible(false);
                        });

                    }
                } else {
                    for (int i = 0; i < reservations.size(); i++) {
                        ArrayList<Integer> arrayList=new ArrayList<Integer>();
                        checkedOutText.setVisible(true);

                        for (int z=0;z<reservations.get(i).getRooms().size() ; z++){
                            arrayList.add(reservations.get(i).getRooms().get(z));
                        }
                        ArrayList<Room> tempRoom = dbParser.copyRoomByRoomNumber(arrayList);
                        for(int j = 0;j<tempRoom.size();j++){
                            tempRoom.get(j).setBooked(false);
                            dbParser.refreshRoomStatus(tempRoom.get(j));
                        }
                        dbParser.deleteReservationByRoomNumber(reservations.get(i).getId());
                    }
                }

            // Updates the table
            getReservation();
        }
    }
}
