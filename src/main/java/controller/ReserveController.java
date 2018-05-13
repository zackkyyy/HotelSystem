package controller;


import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import model.Guest;
import model.Reservation;
import model.Room;
import org.controlsfx.control.textfield.TextFields;

import javax.print.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;


/**
 * Created by IntelliJ IDEA.
 * <p>
 * This class is the controller for the reserve window.
 *
 * @Author: Zacky Kharboutli
 * @Date: 2018-03-29
 * @Project : HotelSystem
 */
public class ReserveController implements Initializable {

    @FXML
    private Pane pane;
    @FXML
    private VBox VBox;
    @FXML
    private HBox hBox;
    @FXML
    private TabPane tabPane;
    @FXML
    private JFXButton removeCitySearch, removeTypeSearch, removeQualitySearch;
    @FXML
    private JFXTextField nights, rooms, search, searchRoom, name, lastName,
            address, phoneNr, identityNr, creditCard, totalPrice, totalPrice1, totalPrice2, rooms1, rooms2, nights1, nights2;
    @FXML
    private JFXDatePicker checkOutField, checkInField;
    @FXML
    private JFXToggleButton adjacentBtn, smokingBtn;
    @FXML
    private MenuButton personsNumber, roomsNumber, cityName, quality;
    @FXML
    private MenuItem p1, p2, p3, p4, r1, r2, r3, r4, kalmar, växjö, star1, star2, star3, star4, star5;
    @FXML
    private TableColumn<model.Room, Integer> roomNrCol, roomTypeCol, priceCol, cityCol;
    @FXML
    private TableColumn<model.Room, Integer> roomIdCol;
    @FXML
    private TableColumn<Room, Integer> floorCol;
    @FXML
    private TableColumn<model.Room, Integer> qualityCol;
    @FXML
    private Label errorLabel, errorMsg, gstID, gstName, gstCredit, gstPhone, depDate, arrDate, nameLabel;
    @FXML
    private Label roomNr1, guestNr1, roomType1, roomCity1, roomNr2, guestNr2, roomType2, roomCity2,
            roomNr3, guestNr3, roomType3, roomCity3, roomNr4, guestNr4, roomType4, roomCity4;
    @FXML
    private HBox roomInfoBox1, roomInfoBox2, roomInfoBox3, roomInfoBox4;
    @FXML
    private JFXButton checkInButton, guestButton, checkOutButton, logOutBtn;
    private MenuController mu;
    private Label[] roomNrs = new Label[4];
    private Label[] guestNrs = new Label[4];
    private Label[] roomTypes = new Label[4];
    private Label[] roomCities = new Label[4];
    private HBox[] roomInfoBoxes = new HBox[4];
    @FXML
    private TableView table;
    private DBParser dbParser;
    private ObservableList<Room> listOfRooms;
    private Guest customer;
    private ObservableList<Room> bookedRoom;
    public Reservation reservation;
    private boolean smokingFilter, cityFilter,typeFilter,adjacentFilter,qualityFilter =false;

    public static boolean creditCardValidator(String str) {
        if (str.length() < 13 || str.length() > 16) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * This method is @Override because this class implement Initializble
     * Here when initialize the button or the menu items to their actions
     *
     * @param location
     * @param resources
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        removeQualitySearch.setVisible(false);
        smokingBtn.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (smokingBtn.isSelected() == false) {
                    removeSmokingFilter();
                } else {
                    filterBySmoking();
                }
            }
        });
        adjacentBtn.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (adjacentBtn.isSelected() == false) {
                    addToTable();  // add rooms to the table
                } else {
                    filterByAdjacent();
                }
            }
        });

        dbParser = new DBParser();
        dbParser.deleteOldReservations();

        mu = new MenuController();
        errorMsg.setVisible(false);
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        checkInField.setValue(LocalDate.now());
        checkOutField.setValue(LocalDate.now().plusDays(1));
        disablePreviousDates();
        addToTable();  // add rooms to the table
        initRoomInfo();

        // Adds listener to table, updating the total price

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                updateRoomPrice();
            }
        });

		/*
        The following lines to decide the number of the Guest
        depending on the chosen number from the button menu
		 */
        p1.setOnAction(event1 -> {
            personsNumber.setText("1");
            filterByType("1");
        });
        p2.setOnAction(event1 -> {
            personsNumber.setText("2");
            filterByType("2");

        });
        p3.setOnAction(event1 -> {
            personsNumber.setText("3");
            filterByType("3");

        });
        p4.setOnAction(event1 -> {
            personsNumber.setText("4");
            filterByType("4");

        });
        star1.setOnAction(event -> {
            quality.setText("★");
            filterByQuality("★");
        });
        star2.setOnAction(event -> {
            quality.setText("★★");
            filterByQuality("★★");
        });
        star3.setOnAction(event -> {
            quality.setText("★★★");
            filterByQuality("★★★");
        });
        star4.setOnAction(event -> {
            quality.setText("★★★★");
            filterByQuality("★★★★");
        });
        star5.setOnAction(event -> {
            quality.setText("★★★★★");
            filterByQuality("★★★★★");
        });

        /*
         * Initialize the number of Room
         */
        r1.setOnAction(event1 -> {
            rooms.setText("1");
            roomsNumber.setText("1");
        });
        r2.setOnAction(event1 -> {
            rooms.setText("2");
            roomsNumber.setText("2");


        });
        r3.setOnAction(event1 -> {
            rooms.setText("3");
            roomsNumber.setText("3");
        });
        r4.setOnAction(event1 -> {
            rooms.setText("4");
            roomsNumber.setText("4");
        });

        kalmar.setOnAction(t -> {
            cityName.setText("Kalmar");
            filterByCity("Kalmar");
        });
        växjö.setOnAction(t -> {
            cityName.setText("Växjö");
            filterByCity("Växjö");
        });

        checkInButton.setOnAction(event -> {
            try {
                mu.showCheckInPage(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        checkOutButton.setOnAction(event -> {
            try {
                mu.showCheckOutWindow(event);
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

    /**
     * Method that checks the inputs of the data picker to ensure that the dates are right,
     * and calculate the number of staying nights
     */
    public void calculateNights() {
        // check that the check in is always before the check out
        if (checkInField.getValue().isAfter(checkOutField.getValue())) {
            checkOutField.setValue(checkInField.getValue().plusDays(1));
            nights.setText("1");
        } else {
            int numberOfNights = checkOutField.getValue().getDayOfYear() - checkInField.getValue().getDayOfYear();
            nights.setText(String.valueOf(numberOfNights));
            nights1.setText(String.valueOf(numberOfNights));
            nights2.setText(String.valueOf(numberOfNights));
        }
        updateDepartureDateCells();
        updateRoomPrice();


        updateRoomPrice();
    }
    public void clearFields(){
        name.clear();
        lastName.clear();
        phoneNr.clear();
        address.clear();
        creditCard.clear();
        identityNr.clear();
        errorMsg.setVisible(false);
    }
    public void moveToNextTab() {
        if (tabPane.getSelectionModel().isSelected(1)) {
            customer = getCustomer();

            if (customer != null) {
                gstName.setText(customer.getName() + " " + customer.getLastName());
                gstCredit.setText(customer.getCreditCard());
                gstID.setText(customer.getIdentityNr());
                gstPhone.setText(customer.getPhoneNr());
                gstCredit.setText(customer.getCreditCard());
                tabPane.getSelectionModel().selectNext();
                arrDate.setText(checkInField.getValue().toString());
                depDate.setText(checkOutField.getValue().toString());
                clearFields();
            } else {
                System.out.println("No customer chosen");
            }
        }
        if (tabPane.getSelectionModel().isSelected(0)) {
            if (validatePrice(totalPrice)) {
                if (Integer.parseInt(roomsNumber.getText()) == table.getSelectionModel().getSelectedItems().size()) {
                    bookedRoom = table.getSelectionModel().getSelectedItems();
                    rooms1.setText(Integer.toString(bookedRoom.size()));
                    rooms2.setText(Integer.toString(bookedRoom.size()));
                    hideRoomInfoBoxes(bookedRoom.size());

                    double tempTotalPrice = 0.00;
                    for (int i = 0; i < bookedRoom.size(); i++) {
                        String s = bookedRoom.get(i).getRoomNr() + " ";
                        String city = bookedRoom.get(i).getStringCity();
                        String type = bookedRoom.get(i).getRoomType().toString();

                        roomNrs[i].setText(s);
                        roomCities[i].setText(city);
                        guestNrs[i].setText(personsNumber.getText());
                        roomTypes[i].setText(type);
                        tempTotalPrice += bookedRoom.get(i).getPrice() * Integer.parseInt(nights.getText());
                        totalPrice1.setText(totalPrice.getText());
                        totalPrice2.setText(totalPrice.getText());
                    }

                    errorLabel.setVisible(false);
                    tabPane.getSelectionModel().selectNext();
                } else {
                    int a = Integer.parseInt(roomsNumber.getText());
                    int b = table.getSelectionModel().getSelectedItems().size();
                    if (a > b) {
                        errorLabel.setText(("you need to select " + (a - b) + " more"));
                        errorLabel.setVisible(true);
                    } else {
                        errorLabel.setText(("More selected rooms than what you wanted"));
                        errorLabel.setVisible(true);
                    }
                }
            }
        }
    }

    public void moveToPreviousTab() {
        tabPane.getSelectionModel().selectPrevious();
        errorMsg.setVisible(false);
        updateRoomPrice();
    }

    public void moveToFirstTab() {
        tabPane.getSelectionModel().selectFirst();
        pane.setVisible(false);
        hBox.setVisible(true);
        VBox.setVisible(true);
        updateRoomPrice();

    }

    public void autoCompletion() {
        String[] listOfNames = new String[dbParser.getGuestsInArray().size()];
        DBParser dbParser = new DBParser();
        for (int i = 0; i < dbParser.getGuestsInArray().size(); i++) {
            String str = dbParser.getGuestsInArray().get(i).getName() + " " + dbParser.getGuestsInArray().get(i).getLastName();
            listOfNames[i] = str;
        }
        try {
            TextFields.bindAutoCompletion(search, listOfNames);
        } catch (Exception e) {
            search.setText("");
        }
    }

    public void addToTable() {
        dbParser = new DBParser();
        table.getItems().remove(0, table.getItems().size());
        listOfRooms = FXCollections.observableArrayList(getFreeRoom());
        table.setItems(listOfRooms);
        roomNrCol.setCellValueFactory(new PropertyValueFactory<Room, Integer>("roomNr"));
        priceCol.setCellValueFactory(new PropertyValueFactory<Room, Integer>("price"));
        cityCol.setCellValueFactory(new PropertyValueFactory<Room, Integer>("city"));
        roomTypeCol.setCellValueFactory(new PropertyValueFactory<Room, Integer>("roomType"));
        floorCol.setCellValueFactory(new PropertyValueFactory<Room, Integer>("smoking"));
        roomIdCol.setCellValueFactory(new PropertyValueFactory<Room, Integer>("adjoined"));
        qualityCol.setCellValueFactory(new PropertyValueFactory<Room, Integer>("quality"));
        searchRoom.clear();
        removeTypeSearch.setVisible(false);
        removeCitySearch.setVisible(false);
    }

    public void filterBySmoking() {
        ObservableList<Room> list = table.getItems();
        ArrayList<Room>list1 = new ArrayList<Room>();
        list.stream().filter(s -> s.getSmoking().toString().equals("Yes"))
                .forEach(list1::add);
        table.getItems().removeAll();
        list = FXCollections.observableArrayList(list1);
        table.setItems(list);
        smokingFilter=true;
    }

    public void filterByQuality(String str) {
        dbParser = new DBParser();
        if (!removeQualitySearch.isVisible()) {
            ObservableList<Room> list = table.getItems();
            ArrayList<Room>list1 = new ArrayList<Room>();
            list.stream().filter(s -> s.getQuality().toString().equals(str))
                    .forEach(list1::add);
            table.getItems().removeAll();
            list = FXCollections.observableArrayList(list1);
            table.setItems(list);
            removeQualitySearch.setVisible(true);
            qualityFilter=true;
        } else {
            ArrayList<Room> listOfRooms = getFreeRoom();
            ObservableList<Room> list;
            ArrayList<Room>list1 = new ArrayList<Room>();
            listOfRooms.stream().filter(s -> s.getQuality().toString().equals(str))
                    .forEach(list1::add);
            table.getItems().removeAll();
            list = FXCollections.observableArrayList(list1);
            table.setItems(list);
            removeQualitySearch.setVisible(true);
            qualityFilter=true;
        }
    }

    public void removeSmokingFilter() {
        if(removeTypeSearch.isVisible() &&removeCitySearch.isVisible()&& removeQualitySearch.isVisible()){
        filterByCity(cityName.getText());
        filterByQuality(quality.getText());
        } else if (removeCitySearch.isVisible() && removeTypeSearch.isVisible() &&!removeQualitySearch.isVisible()) {
            filterByCity(cityName.getText());
        } else if (removeTypeSearch.isVisible() && !removeCitySearch.isVisible() &&!removeQualitySearch.isVisible()) {
            filterByType(personsNumber.getText());
        } else if (!removeTypeSearch.isVisible() && removeCitySearch.isVisible()&&!removeQualitySearch.isVisible()) {
            filterByCity(cityName.getText());
        } else if (!removeCitySearch.isVisible() && !removeTypeSearch.isVisible() && !adjacentBtn.isSelected()) {
            addToTable();
            adjacentBtn.setSelected(false);
        } else if (adjacentBtn.isSelected()) {
            filterByAdjacent();
        }
        smokingFilter=false;
    }


    public void removeQualitySearch() {
        ArrayList<Room> list1 = new ArrayList<Room>();
        if(typeFilter && cityFilter && smokingFilter && adjacentFilter){
            ArrayList<Room> listOfRooms = getFreeRoom();

            listOfRooms.stream().filter(s->s.getSmoking().toString().equals("Yes")&&s.getAdjoined().toString().equals("yes")
          &&s.getCity().toString().equals(cityName.getText())).
                    forEach(list1::add);

        }else if( cityFilter && smokingFilter && adjacentFilter){
            ArrayList<Room> listOfRooms = getFreeRoom();

            listOfRooms.stream().filter(s->s.getSmoking().toString().equals("Yes")&&s.getAdjoined().toString().equals("yes")
                    &&s.getCity().toString().equals(cityName.getText())).
                    forEach(list1::add);

        }else if(smokingFilter && adjacentFilter){
            ArrayList<Room> listOfRooms = getFreeRoom();

            listOfRooms.stream().filter(s->s.getSmoking().toString().equals("Yes")&&s.getAdjoined().toString().equals("yes")
            ).forEach(list1::add);

        }
        else if(smokingFilter ){
            ArrayList<Room> listOfRooms = getFreeRoom();

            listOfRooms.stream().filter(s->s.getSmoking().toString().equals("Yes")
            ).forEach(list1::add);

        }
        table.getItems().removeAll();
        ObservableList<Room> list = FXCollections.observableArrayList(list1);
        table.setItems(list);
        removeQualitySearch.setVisible(false);
        qualityFilter=false;
    }

    public void filterByAdjacent() {
        dbParser = new DBParser();
        ObservableList<Room> list = table.getItems();
        ;
        ArrayList<Room> listOfRooms = new ArrayList<Room>();

        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
            if (list.get(i).getAdjoined().toString().equals("Yes")) {
                listOfRooms.add(list.get(i));
            }
        }
        table.getItems().removeAll();
        list = FXCollections.observableArrayList(listOfRooms);
        table.setItems(list);
        adjacentFilter=true;
    }
    public void filterByCity(String string) {
        ObservableList<Room> list;
        dbParser = new DBParser();

        if (!removeCitySearch.isVisible()) {
            list = table.getItems();
            table.getItems().removeAll();
            listOfRooms = FXCollections.observableArrayList(dbParser.filterByCity(cityName.getText(), list));
            table.setItems(listOfRooms);
            removeCitySearch.setVisible(true);

        } else if (removeCitySearch.isVisible() && !removeTypeSearch.isVisible()) {
            list = FXCollections.observableArrayList(getFreeRoom());
            table.getItems().removeAll();
            listOfRooms = FXCollections.observableArrayList(dbParser.filterByCity(cityName.getText(), list));
            table.setItems(listOfRooms);

        } else if (removeCitySearch.isVisible() && removeTypeSearch.isVisible()) {
            list = FXCollections.observableArrayList(getFreeRoom());
            ObservableList<Room> filteredByRoom = FXCollections.observableArrayList(dbParser.filterByRoomType(personsNumber.getText(), list));
            listOfRooms = FXCollections.observableArrayList(dbParser.filterByCity(cityName.getText(), filteredByRoom));
            table.getItems().removeAll();
            table.setItems(listOfRooms);
        }
        cityFilter=true;
    }

    public void filterByType(String string) {
        ObservableList<Room> list;
        dbParser = new DBParser();
        if (!removeTypeSearch.isVisible()) {
            list = table.getItems();
            table.getItems().removeAll();
            listOfRooms = FXCollections.observableArrayList(dbParser.filterByRoomType(personsNumber.getText(), list));
            table.setItems(listOfRooms);
            removeTypeSearch.setVisible(true);

        } else if (removeTypeSearch.isVisible() && !removeCitySearch.isVisible()) {
            list = FXCollections.observableArrayList(getFreeRoom());
            table.getItems().removeAll();
            listOfRooms = FXCollections.observableArrayList(dbParser.filterByRoomType(personsNumber.getText(), list));
            table.setItems(listOfRooms);

        } else if (removeCitySearch.isVisible() && removeTypeSearch.isVisible()) {
            list = FXCollections.observableArrayList(getFreeRoom());
            ObservableList<Room> filteredByCity = FXCollections.observableArrayList(dbParser.filterByCity(cityName.getText(), list));
            listOfRooms = FXCollections.observableArrayList(dbParser.filterByRoomType(personsNumber.getText(), filteredByCity));
            table.getItems().removeAll();
            table.setItems(listOfRooms);
        }
        typeFilter=true;
    }

    public void removeCitySearch() {
        dbParser = new DBParser();
        listOfRooms = FXCollections.observableArrayList(getFreeRoom());
        if (removeTypeSearch.isVisible()) {
            listOfRooms = FXCollections.observableArrayList(dbParser.filterByRoomType(personsNumber.getText(), listOfRooms));
            table.setItems(listOfRooms);
        } else {
            table.getItems().removeAll();
            table.setItems(listOfRooms);
        }
        removeCitySearch.setVisible(false);
        cityFilter=false;
    }

    public void removeTypeSearch() {
        dbParser = new DBParser();
        listOfRooms = FXCollections.observableArrayList(getFreeRoom());

        if (removeCitySearch.isVisible()) {
            listOfRooms = FXCollections.observableArrayList(dbParser.filterByCity(cityName.getText(), listOfRooms));
            table.setItems(listOfRooms);
        } else {
            table.getItems().removeAll();
            table.setItems(listOfRooms);
        }
        removeTypeSearch.setVisible(false);
        personsNumber.setText("1");
        typeFilter=false;
    }

    public ArrayList<Room> getFreeRoom() {
        ArrayList<Room> listOfFreeRooms = new ArrayList<Room>();
        for (int i = 0; i < dbParser.getAllRoom().size(); i++) {
            if (!dbParser.getAllRoom().get(i).isBooked()) {
                listOfFreeRooms.add(dbParser.getAllRoom().get(i));
            }
        }
        return listOfFreeRooms;
    }

    public void findRoom() {
        DBParser dbParser = new DBParser();
        ArrayList<Room> arrayList = getFreeRoom();
        String searchedRoom = searchRoom.getText();

        if (!searchRoom.getText().isEmpty()) {
            for (int i = 0; i < arrayList.size(); i++) {
                if (arrayList.get(i).getRoomNr() == Integer.parseInt(searchedRoom) && !arrayList.get(i).isBooked()) {
                    table.getItems().remove(0, table.getItems().size());
                    table.getItems().add(arrayList.get(i));
                }
            }
        } else {
            addToTable();
        }
    }

    public Guest getCustomerFromSearch() {
        Guest guest = null;
        String searchText = search.getText();
        ArrayList<Guest> guests = dbParser.getGuestsInArray();

        for (int i = 0; i < guests.size(); i++) {
            if (!((guests.get(i).getName() + " " + guests.get(i).getLastName()).equals(searchText))) {
                search.setText("");
                errorMsg.setText("wrong name");
                errorMsg.setVisible(true);
            } else {
                guest = guests.get(i);
            }
        }
        return guest;
    }

    public Guest getCustomer() {
        Guest guest;

        if (!search.getText().equals("")) {
            guest = getCustomerFromSearch();
            System.out.println("Customer found in list");
        } else {
            guest = saveNewCustomer();
            System.out.println("Customer is created");
        }
        return guest;
    }

    public Guest saveNewCustomer() {
        Guest guest = null;

        if (!checkFields()) {
            errorMsg.setVisible(true);
        } else {
            guest = new Guest(name.getText(), lastName.getText(), address.getText(), phoneNr.getText(), creditCard.getText(), identityNr.getText());
            dbParser.createNewGuest(guest);
        }
        return guest;

    }

    public boolean checkFields() {
        if (name.getText().isEmpty()) {
            errorMsg.setText("Name field should not be empty");
            return false;

        } else if (name.getText().matches(".*\\d+.*")) {
            errorMsg.setText("Name field should have only letters");
            return false;

        } else if (lastName.getText().isEmpty()) {
            errorMsg.setText("Last name field should not be empty");
            return false;

        } else if (lastName.getText().matches(".*\\d+.*")) {
            errorMsg.setText("Last name field should have only letters");
            return false;

        } else if (address.getText().isEmpty()) {
            errorMsg.setText("Address field should not be empty");
            return false;

        } else if (identityNr.getText().isEmpty()) {
            errorMsg.setText("ID number field should not be empty");
            return false;

        } else if (creditCard.getText().isEmpty()) {
            errorMsg.setText("Credit card field should not be empty");
            return false;

        } else if (!creditCardValidator(creditCard.getText())) {
            errorMsg.setText("Invalid Credit Card");
            return false;

        } else if (phoneNr.getText().isEmpty()) {
            errorMsg.setText("Phone nr field should not be empty");
            return false;

        }
        return true;
    }

    public void createReservation(ActionEvent actionEvent) throws FileNotFoundException, DocumentException {
        String guestID = customer.getName() + " " + customer.getLastName();
        ArrayList<Integer> bookedRooms = new ArrayList<Integer>();
        double price = 0;
        for (int i = 0; i < bookedRoom.size(); i++) {
            bookedRooms.add(bookedRoom.get(i).getRoomNr());
            price += bookedRoom.get(i).getPrice();
            bookedRoom.get(i).setBooked(true);
            dbParser.refreshRoomStatus(bookedRoom.get(i));
        }
        System.out.println(bookedRooms.toString() + "booked numbers");
        reservation = new Reservation();
        //int roomID = bookedRoom.get(i).getRoomNr();
        reservation.setGuest(guestID);
        reservation.setRooms(bookedRooms);
        reservation.setArrivalDate(checkInField.getValue());
        reservation.setDepartureDate(checkOutField.getValue());
        reservation.setCheckedIn(false);
        reservation.setPrice(price * Integer.parseInt(nights.getText()));
        dbParser.saveReservationToDB(reservation);
        generateInvoice(reservation);


        VBox.setVisible(false);
        hBox.setVisible(false);
        generateInvoice(reservation);
        pane.setVisible(true);
        addToTable();
    }

    // Initializes the room info arrays
    private void initRoomInfo() {
        // Initialize room info Hboxes array
        roomInfoBoxes[0] = roomInfoBox1;
        roomInfoBoxes[1] = roomInfoBox2;
        roomInfoBoxes[2] = roomInfoBox3;
        roomInfoBoxes[3] = roomInfoBox4;

        // Initializes room information labels array
        roomNrs[0] = roomNr1;
        roomNrs[1] = roomNr2;
        roomNrs[2] = roomNr3;
        roomNrs[3] = roomNr4;
        guestNrs[0] = guestNr1;
        guestNrs[1] = guestNr2;
        guestNrs[2] = guestNr3;
        guestNrs[3] = guestNr4;
        roomTypes[0] = roomType1;
        roomTypes[1] = roomType2;
        roomTypes[2] = roomType3;
        roomTypes[3] = roomType4;
        roomCities[0] = roomCity1;
        roomCities[1] = roomCity2;
        roomCities[2] = roomCity3;
        roomCities[3] = roomCity4;
    }

    // Hides all roomInfoBoxes and then displays the ones that are used
    private void hideRoomInfoBoxes(int amountRooms) {
        for (int i = 0; i < roomInfoBoxes.length; i++) {
            roomInfoBoxes[i].setVisible(false);
            roomInfoBoxes[i].setManaged(false);
        }

        for (int i = 0; i < amountRooms; i++) {
            roomInfoBoxes[i].setVisible(true);
            roomInfoBoxes[i].setManaged(true);
        }
    }

    private void updateRoomPrice() {
        ObservableList<Room> selectedRooms = table.getSelectionModel().getSelectedItems();
        double tempTotalPrice = 0;

        for (int i = 0; i < selectedRooms.size(); i++) {
            tempTotalPrice += selectedRooms.get(i).getPrice() * Integer.parseInt(nights.getText());
        }
        totalPrice.setText(String.valueOf(tempTotalPrice));
    }


    // Disables (grayes out) all dates before today in both the DatePickers
    private void disablePreviousDates() {
        Callback<DatePicker, DateCell> dayCellFactory;

        dayCellFactory = new Callback<DatePicker, DateCell>() {
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item.isBefore(LocalDate.now())) {
                            setDisable(true);
                        }
                    }
                };
            }
        };

        checkOutField.setDayCellFactory(dayCellFactory);
        checkInField.setDayCellFactory(dayCellFactory);

    }

    private void updateDepartureDateCells() {
        checkOutField.hide();
        Callback<DatePicker, DateCell> dayCellFactory;
        dayCellFactory = new Callback<DatePicker, DateCell>() {
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item.isBefore(checkInField.getValue().plusDays(1))) {
                            setDisable(true);
                        } else if (item.isAfter(checkInField.getValue())) {
                            setDisable(false);
                        }
                    }
                };
            }
        };
        checkOutField.setDayCellFactory(dayCellFactory);
    }


    public boolean validatePrice(JFXTextField ss) {
        ObservableList<Room> selectedRooms = table.getSelectionModel().getSelectedItems();
        String string = ss.getText();
        double price = 0;
        if (!string.contains(".")) {
            string = string + ".0";    // if number is not decimal add .0 so it is decimal
        }
        if (!string.matches("([0-9]*)\\.([0-9]*)")) {  // compare with decimal pattern and numbers so it is a valid double input
            errorLabel.setText("Price you entered is incorrect");
            errorLabel.setVisible(true);
            return false;
        }
        for (int i = 0; i < selectedRooms.size(); i++) {
            price += selectedRooms.get(i).getPrice() * Integer.parseInt(nights.getText());
        }
        if (!ss.getText().isEmpty() && Double.valueOf(ss.getText()) > price) {   //compare price with max daily rate of the rooms
            errorLabel.setText("Price you entered is more than the max rate");
            errorLabel.setVisible(true);
            return false;
        }
        return true;
    }

    /* This method create a pdf file if we need it later  */

    public com.itextpdf.text.Document generateInvoice(Reservation reservation) throws FileNotFoundException, DocumentException {
        com.itextpdf.text.Document layoutDocument = new com.itextpdf.text.Document(PageSize.A6);
        PdfWriter.getInstance(layoutDocument, new FileOutputStream("file.pdf"));
        layoutDocument.open();
        layoutDocument.add(new Paragraph("INVOICE"));
        layoutDocument.add(new Paragraph("Lineaus Hotel"));
        layoutDocument.add(new Paragraph("Växjö/Kalmar"));
        layoutDocument.add(new Paragraph("   "));
        layoutDocument.add(new Paragraph("   "));
        layoutDocument.add(new Paragraph(" Customer :" + reservation.getGuest()));
        layoutDocument.add(new Paragraph("   "));
        layoutDocument.add(new Paragraph("   "));

        PdfPTable table1 = new PdfPTable(2);

        table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table1.setWidthPercentage(110f);
        table1.getDefaultCell().setPadding(3);
        table1.addCell("Arrival");
        table1.addCell(reservation.getArrivalDate().toString());
        table1.addCell("Departure");
        table1.addCell(reservation.getDepartureDate().toString());
        table1.addCell("Number of nights");
        table1.addCell(nights.getText());
        table1.addCell("Number of rooms");
        table1.addCell(rooms.getText());
        table1.addCell("Number of guest");
        table1.addCell(personsNumber.getText());
        table1.addCell("Total");
        table1.addCell(reservation.getPrice().toString());
        layoutDocument.add(table1);

        layoutDocument.close();
        return layoutDocument;
    }

    /**
     * This method prints the reservation details
     * at the moment it copies every thing and print it
     */
    public void print() throws PrintException, MalformedURLException {
        PrintService defaultPrintService = PrintServiceLookup.lookupDefaultPrintService();
        DocPrintJob printerJob = defaultPrintService.createPrintJob();
        File pdfFile = new File("file.pdf");
        SimpleDoc simpleDoc = new SimpleDoc(pdfFile.toURL(), DocFlavor.URL.AUTOSENSE, null);
        printerJob.print(simpleDoc, null);
        System.out.println("printed");
    }

}

