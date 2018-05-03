package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import java.time.chrono.ChronoLocalDate;
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
public class CheckInController implements Initializable {
	@FXML
	private JFXDatePicker date;
	@FXML
	private JFXTextField txtField;
	@FXML
	private JFXButton prevBut, nextBut;
	@FXML
	private TableView table;
	@FXML
	private TableColumn<Reservation, Integer> arrivalCol, departCol, priceCol, guestCol, roomCol;
	@FXML
	private Text errorText, checkedInText;
	@FXML
	private JFXButton checkOutButton ,reserveButton, guestButton , logOutBtn;
	private MenuController mu;
	private ObservableList<Reservation> reservations;
	private ObservableList<Reservation> listOfReservations;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		mu = new MenuController();
		date.setValue(LocalDate.now());
		getReservation();

		checkOutButton.setOnAction(event -> {
			try {
				mu.showCheckOutWindow(event);
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
		// table.getItems().remove(0,table.getItems().size());
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
		txtField.setText("");
		checkedInText.setVisible(false);
		errorText.setVisible(false);
		getReservation();
	}
	public void prevDay(ActionEvent e){
		date.setValue(date.getValue().minusDays(1));
		txtField.setText("");
		checkedInText.setVisible(false);
		errorText.setVisible(false);
		getReservation();
	}

	public void checkIn() {
		DBParser dbParser = new DBParser();
		reservations = table.getSelectionModel().getSelectedItems();
		checkedInText.setText("Reservations has been checked-in");
		checkedInText.setVisible(false);
		errorText.setVisible(false);

		if(reservations.size() == 0) {
			errorText.setText("No reservation selected");
			errorText.setVisible(true);
		} else {
			for(int i = 0; i < reservations.size(); i++) {	
				if(isCorrectDates(reservations.get(i))) {
					reservations.get(i).setCheckedIn(true);
					dbParser.refreshReservationStatus(reservations.get(i));
					checkedInText.setVisible(true);
				} else {
					errorText.setText("Error: You can only check in on/after the arrival date and before the departure date");
					errorText.setVisible(true);
				}
			}
		}
		getReservation();
	}

	/**
	 * this method is to search for a reservation by the guest name
	 * @newValue
	 *           the text typed in the search field
	 */
	public void findReservationByGuestName(){
		checkedInText.setVisible(false);
		errorText.setVisible(false);
		DBParser dbParser = new DBParser();
		listOfReservations = FXCollections.observableArrayList(dbParser.getAllReservations());
		FilteredList<Reservation> filteredData = new FilteredList<>(listOfReservations, p -> true);
		txtField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(t -> {
				// If filter text is empty, display all reservation.
				if (newValue == null || newValue.isEmpty() ) {
					return true;
				}
				if (t.getGuest().toLowerCase().contains(txtField.getText().toLowerCase())) {
					return true; // Filter matches month
				}
				return false; // Does not match.
			});
		});
		SortedList<Reservation> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(table.comparatorProperty());
		table.setItems(sortedData);
	}

	public void cancelReservation(){
		DBParser dbParser = new DBParser();
		reservations = table.getSelectionModel().getSelectedItems();
		checkedInText.setText("Reservation Has been canceled");
		if(reservations.size() == 0) {
			errorText.setText("No reservation selected");
			errorText.setVisible(true);
		} else {
			errorText.setVisible(false);
			checkedInText.setVisible(true);

			for(int i = 0; i < reservations.size(); i++) {
				if(LocalDate.now().isAfter(reservations.get(i).getArrivalDate().minusDays(1))
						||LocalDate.now().isEqual(reservations.get(i).getArrivalDate().minusDays(1))){
					int numberOfNights = reservations.get(i).getDepartureDate().getDayOfYear()- reservations.get(i).getArrivalDate().getDayOfYear();
					errorText.setText("NOTE: Customer should pay  "+ reservations.get(i).getPrice()/numberOfNights);
					errorText.setVisible(true);
					int roomNumber = reservations.get(i).getRoom();
					Room tempRoom = dbParser.copyRoomByRoomNumber(roomNumber);
					tempRoom.setBooked(false);
					dbParser.refreshRoomStatus(tempRoom);
					dbParser.deleteReservationByRoomNumber(roomNumber);
				}
				int roomNumber = reservations.get(i).getRoom();
				Room tempRoom = dbParser.copyRoomByRoomNumber(roomNumber);
				tempRoom.setBooked(false);
				dbParser.refreshRoomStatus(tempRoom);
				dbParser.deleteReservationByRoomNumber(roomNumber);
			}

		}
		getReservation();
	}

	// Returns false if current date is before arrival date OR if current date is after departure date. Otherwise it returns true
	private boolean isCorrectDates(Reservation reservation) {
		if(LocalDate.now().isBefore(reservation.getArrivalDate()) || LocalDate.now().isAfter(reservation.getDepartureDate())) {
			return false;
		}
		return true;
	}
}
