package controller;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.DataBase;
import model.Guest;
import model.Reservation;
import model.Room;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
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
    private VBox Vbox;
    @FXML
    private TabPane tabPane;
    @FXML
    private JFXButton removeCitySearch ,removeTypeSearch;
    @FXML
    private JFXTextField nights ,rooms ,search , searchRoom , name , lastName, address, phoneNr,identityNr,creditCard;
    @FXML
    private JFXDatePicker checkOutField, checkInField;
    @FXML
    private MenuButton personsNumber, roomsNumber,cityName;
    @FXML
    private MenuItem p1 , p2 , p3 ,p4 ,r1 , r2 ,r3 ,r4, r5, kalmar, växjö ;
    @FXML
    private TableColumn<model.Room, Integer> roomNrCol,roomTypeCol,priceCol,cityCol;
    @FXML
    private TableColumn<model.Room, Integer> roomIdCol;
    @FXML
    private TableColumn<ImageView, ImageView> floorCol;
    @FXML
    private TableColumn<model.Room, Integer> qualityCol;
    @FXML
    private TableColumn<Room, Integer> bookedCol;
    @FXML
    private FontAwesomeIconView removeRoomSearch;
    @FXML
    private Label errorLabel,errorMsg ,romNr, romType,gstNr, gstID, gstName, gstCredit, romCity, gstPhone,depDate, arrDate ;
    private DataBase db = new DataBase();
    private MongoCollection persons;
    private MongoCursor<Document> cursor;
    private Document doc;
    private MongoCollection roomsList;
    @FXML
    private TableView table;
    private DBParser dbParser;
    private ObservableList<Room> listOfRooms;
    private Guest customer;
    private ObservableList<Room> bookedRoom;
    /**
     * This method runs when ever the user press on the check in option in the header
     *
     * @param event check in page requested
     * @throws IOException
     */
    public void ShowCheckInPage(ActionEvent event) throws IOException {
        Parent CheckInPage = FXMLLoader.load(getClass().getResource(String.valueOf("/checkInWindow.fxml")));
        Scene CheckInScene = new Scene(CheckInPage);
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
        Parent CheckInPage = FXMLLoader.load(getClass().getResource(String.valueOf("/checkOutWindow.fxml")));
        Scene mainWindowScene = new Scene(CheckInPage);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(mainWindowScene);
        app_stage.show();
        System.out.println("CheckOut window showed from reserveController");

    }
    /**
     * This method runs when ever the user press on log out option in the header
     *
     * @param event log out button pressed
     * @throws IOException
     */
    public void showLogInWindow(ActionEvent event) throws IOException {
        Parent LogInWindow = FXMLLoader.load(getClass().getResource(String.valueOf("/Untitled.fxml")));
        Scene mainWindowScene = new Scene(LogInWindow);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(mainWindowScene);
        app_stage.show();
        System.out.println("log in showed from reserveController");

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
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        checkInField.setValue(LocalDate.now());
        checkOutField.setValue(LocalDate.now().plusDays(1));
        disablePreviousDates();
        addToTable();  // add rooms to the table

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

        /*
         * Initialize the number of Room
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

        kalmar.setOnAction(t -> {
            cityName.setText("Kalmar");
            filterByCity("Kalmar" );
        });
        växjö.setOnAction(t -> {
            cityName.setText("Växjö");
            filterByCity("Växjö");
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


    public void moveToNextTap()
    {
        if (tabPane.getSelectionModel().isSelected(1)){
            customer = getCustomer();
            gstName.setText(customer.getName()+" "+customer.getLastName());
            gstCredit.setText(customer.getCreditCard());
            gstID.setText(customer.getIdentityNr());
            gstPhone.setText(customer.getPhoneNr());
            gstCredit.setText(customer.getCreditCard());
            tabPane.getSelectionModel().selectNext();
           // if(gst!=null){

            //}
            //else {
              //  System.out.println("choose a guest");
            //}
        }
        if (tabPane.getSelectionModel().isSelected(0)){
            if(Integer.parseInt(roomsNumber.getText())== table.getSelectionModel().getSelectedItems().size()) {
                bookedRoom = table.getSelectionModel().getSelectedItems();
                for (int i = 0; i < bookedRoom.size(); i++) {
                    String s= bookedRoom.get(i).getRoomNr() + " ";
                    String city=bookedRoom.get(i).getStringCity();
                    String type = bookedRoom.get(i).getRoomType().toString();
                    romNr.setText(s);
                    romCity.setText(city);
                    romType.setText(type);
                    gstNr.setText(personsNumber.getText());
                }

                errorLabel.setVisible(false);
                tabPane.getSelectionModel().selectNext();
            }

            else{
                int a=Integer.parseInt(roomsNumber.getText());
                int b =table.getSelectionModel().getSelectedItems().size();
                if (a>b){
                errorLabel.setText(("you need to select " +(a - b) + " more"));
                errorLabel.setVisible(true);
                } else {
                    errorLabel.setText(("More selected rooms than what you wanted"));
                    errorLabel.setVisible(true);
                }
            }

        }
    }

    public void moveToPreviousTap(){
        tabPane.getSelectionModel().selectPrevious();
    }
    public void autoCompletation(){
        db = new DataBase();
        persons = db.getPersonsCollection();
        cursor = persons.find().iterator();
        String[] listOfNames =new String[(int) persons.count()] ;
        for (int i = 0; i < persons.count(); i++) {
            doc = cursor.next();
            listOfNames[i]=doc.getString("name")+" "+doc.getString("last name");
        }
        try {
            TextFields.bindAutoCompletion(search,listOfNames);
        }catch (Exception e){
            search.setText("");
        }
        }

    public void addToTable() {
        dbParser =new DBParser();
        table.getItems().remove(0,table.getItems().size());
        listOfRooms= FXCollections.observableArrayList(dbParser.getAllRoom());
        table.setItems(listOfRooms);
        roomNrCol.setCellValueFactory(new PropertyValueFactory<Room, Integer>("roomNr"));
        priceCol.setCellValueFactory(new PropertyValueFactory<Room, Integer>("price"));
        cityCol.setCellValueFactory(new PropertyValueFactory<Room, Integer>("city"));
        roomTypeCol.setCellValueFactory(new PropertyValueFactory<Room, Integer>("roomType"));
        searchRoom.clear();
        removeTypeSearch.setVisible(false);
        removeCitySearch.setVisible(false);

    }


    public void filterByCity(String string  ) {
        ObservableList<Room> list;
        dbParser =new DBParser();
        if(!removeCitySearch.isVisible()) {
            list = table.getItems();
            table.getItems().removeAll();
            listOfRooms = FXCollections.observableArrayList(dbParser.filterByCity(cityName.getText(), list));
            table.setItems(listOfRooms);
            removeCitySearch.setVisible(true);
        }else if (removeCitySearch.isVisible()&&!removeTypeSearch.isVisible()){
            list=FXCollections.observableArrayList(dbParser.getAllRoom());
            table.getItems().removeAll();
            listOfRooms=FXCollections.observableArrayList( dbParser.filterByCity(cityName.getText(), list));
            table.setItems(listOfRooms);
        }else if (removeCitySearch.isVisible() && removeTypeSearch.isVisible()){
            list=FXCollections.observableArrayList(dbParser.getAllRoom());
            ObservableList<Room>   filteredByRoom= FXCollections.observableArrayList(dbParser.filterByRoomType(personsNumber.getText(), list));
            listOfRooms= FXCollections.observableArrayList(dbParser.filterByCity(cityName.getText(), filteredByRoom));
          table.getItems().removeAll();
            table.setItems(listOfRooms);
        }
    }

    public void filterByType(String string  ) {
        ObservableList<Room> list ;
        dbParser =new DBParser();
        if(!removeTypeSearch.isVisible()){
            list = table.getItems();
            table.getItems().removeAll();
            listOfRooms= FXCollections.observableArrayList(dbParser.filterByRoomType(personsNumber.getText(), list));
            table.setItems( listOfRooms);
            removeTypeSearch.setVisible(true);
        }else if (removeTypeSearch.isVisible()&&!removeCitySearch.isVisible()){
            list=FXCollections.observableArrayList(dbParser.getAllRoom());
            table.getItems().removeAll();
            listOfRooms=FXCollections.observableArrayList( dbParser.filterByRoomType(personsNumber.getText(), list));
            table.setItems(listOfRooms);
        }else if (removeCitySearch.isVisible() && removeTypeSearch.isVisible()){
            list=FXCollections.observableArrayList(dbParser.getAllRoom());
            ObservableList<Room>   filteredByCity= FXCollections.observableArrayList(dbParser.filterByCity(cityName.getText(), list));
            listOfRooms= FXCollections.observableArrayList(dbParser.filterByRoomType(personsNumber.getText(), filteredByCity));
            table.getItems().removeAll();
            table.setItems(listOfRooms);
        }

        }

    public void removeCitySearch(){
        dbParser =new DBParser();
        listOfRooms= FXCollections.observableArrayList(dbParser.getAllRoom());
        if (removeTypeSearch.isVisible()){
            listOfRooms= FXCollections.observableArrayList(dbParser.filterByRoomType(personsNumber.getText(), listOfRooms));
            table.setItems(listOfRooms);
        } else{
            table.getItems().removeAll();
            table.setItems(listOfRooms);
        }
        removeCitySearch.setVisible(false);
    }
    public void removeTypeSearch(){
        dbParser =new DBParser();
        listOfRooms= FXCollections.observableArrayList(dbParser.getAllRoom());
        if (removeCitySearch.isVisible()){
            listOfRooms= FXCollections.observableArrayList(dbParser.filterByCity(cityName.getText(), listOfRooms));
            table.setItems(listOfRooms);
        }else {
            table.getItems().removeAll();
            table.setItems(listOfRooms);
        }
        removeTypeSearch.setVisible(false);
        personsNumber.setText("1");
    }
    public void findRoom(){
        db = new DataBase();
        roomsList = db.getRoomsColl();
        cursor = roomsList.find().iterator();
        String searchedRoom = searchRoom.getText();
        if(!searchRoom.getText().isEmpty()) {
            for (int i = 0; i < roomsList.count(); i++) {
                doc = cursor.next();
                if (doc.getInteger("room nr").toString().equals(searchedRoom) && !doc.getBoolean("is booked")) {
                    table.getItems().remove(0, table.getItems().size());
                    table.getItems().add(dbParser.createRoom(doc));
                }
            }
        }else
        {
            addToTable();
        }
    }

    public Guest getCustomer() {
        // TODO fix Error when search is not to be used
        Guest gst= new Guest();
        String ss = search.getText();
        ArrayList<Guest> guests = dbParser.getGuestsInArray();

            for (int i = 0; i < guests.size() ; i++) {
                if (((guests.get(i).getName() + " " + guests.get(i).getLastName()).equals(ss))) {
                    gst = guests.get(i);
                }else {
                    search.setText("");
                    errorMsg.setText("wrong name");
                }
            }

        return gst;
    }


    public Guest saveNewCustomer(){
            if(checkFields()) {
                Guest gst = new Guest(name.getText(), lastName.getText(), address.getText(), phoneNr.getText(), creditCard.getText(), identityNr.getText());
                dbParser.createNewGuest(gst, db);
                return gst;
            }
        errorMsg.setVisible(true);
         return   saveNewCustomer();
    }

    public boolean checkFields(){
        if (name.getText().isEmpty()  ) {
            errorMsg.setText("Name field should not be empty");
            return false;
        }else if(name.getText().matches(".*\\d+.*")){
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

        } else  if (identityNr.getText().isEmpty()) {
            errorMsg.setText("ID number field should not be empty");
            return false;

        } else if (creditCard.getText().isEmpty()) {
            errorMsg.setText("Credit card field should not be empty");
            return false;

        } else if (phoneNr.getText().isEmpty()) {
            errorMsg.setText("Phone nr field should not be empty");
            return false;

        }
        return true;
    }




    public void createReservation(ActionEvent actionEvent) {
        //TODO reservation Confirmation
        Reservation reservation = new Reservation();
        ObjectId guestID=dbParser.getGuestID(customer);
        ObjectId roomID=dbParser.getRoomID(bookedRoom.get(0));
        reservation.setGuest(guestID);
        reservation.setRoom(roomID);
        reservation.setArrivalDate(checkInField.getValue());
        reservation.setDepartureDate(checkOutField.getValue());
        reservation.setCheckedIn(false);
        reservation.setPrice(bookedRoom.get(0).getPrice());
        dbParser.saveReservationToDB(reservation);

    }
}

