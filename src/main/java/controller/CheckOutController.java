package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import model.Reservation;
import model.Room;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
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
public class CheckOutController implements Initializable {
	@FXML
	private JFXDatePicker date;
	@FXML
	private JFXButton checkInButton ,reserveButton, guestButton , logOutBtn;
	private MenuController mu;
	@FXML
	private TableView table;
	@FXML
	private TableColumn<Reservation, Integer> arrivalCol,departCol,priceCol,guestCol,roomCol;
	@FXML
	private Text selectionError, checkedOutText;
	private ObservableList<Reservation> reservations;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		mu = new MenuController();
		date.setValue(LocalDate.now());

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

	public void getReservation(){
		DBParser dbParser = new DBParser();
		ObservableList<Reservation> listOfReservations = FXCollections.observableArrayList(dbParser.getCheckedInByDate(date.getValue()));

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

	public void checkOut() {
		DBParser dbParser = new DBParser();
		reservations = table.getSelectionModel().getSelectedItems();
		checkedOutText.setVisible(false);
		selectionError.setVisible(false);

		if(reservations.size() == 0) {
			selectionError.setVisible(true);
		} else {
			checkedOutText.setVisible(true);

			// Deletes the reservations and unbooks the rooms
			for(int i = 0; i < reservations.size(); i++) {	
				int roomNumber = reservations.get(i).getRoom();

				Room tempRoom = dbParser.copyRoomByRoomNumber(roomNumber);
				tempRoom.setBooked(false);
				dbParser.refreshRoomStatus(tempRoom);
				dbParser.deleteReservationByRoomNumber(roomNumber);
			}
		}
		// Updates the table
		getReservation();
	}
}
