package controller;


import com.jfoenix.controls.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import controller.database.DataBase;
import controller.database.roomController;
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
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Room;
import org.bson.Document;
import org.controlsfx.control.textfield.TextFields;

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
    private TextField ss;
    @FXML
    private TabPane tabPane;
    @FXML
    private JFXButton removeCitySearch ,removeTypeSearch;
    @FXML
    private JFXTextField nights ,rooms ,search , searchRoom;
    @FXML
    private JFXDatePicker checkOutField, checkInField;
    @FXML
    private MenuButton personsNumber, roomsNumber,cityName;
    @FXML
    private MenuItem p1 , p2 , p3 ,p4 ,p5,r1 , r2 ,r3 ,r4, r5, kalmar, växjö ;
    @FXML
    private TableColumn<model.Room, Integer> roomNrCol;
    @FXML
    private TableColumn<model.Room, Integer> roomTypeCol;
    @FXML
    private TableColumn<model.Room, Integer> priceCol;
    @FXML
    private TableColumn<model.Room, Integer> cityCol;
    @FXML
    private TableColumn<model.Room, Integer> roomIdCol;
    @FXML
    private TableColumn<ImageView, ImageView> floorCol;
    @FXML
    private TableColumn<model.Room, Integer> qualityCol;
    @FXML
    private TableColumn<model.Room, Integer> bookedCol;
    @FXML
    private FontAwesomeIconView removeRoomSearch;
    private DataBase db = new DataBase();
    private MongoCollection persons;
    private MongoCursor<Document> cursor;
    private Document doc;
    private MongoCollection roomsList;
    @FXML
    private TableView table;
    TreeItem<Room> root =null;
    private JFXTreeTableColumn<Room , String > roomNr  , city , price  , roomType;
    @FXML
    private JFXListView list1;
    private roomController roomController;
    private ObservableList<Room> listOfRooms;
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

        });
        p3.setOnAction(event1 -> {
            personsNumber.setText("3");
            filterByType("3");

        });
        p4.setOnAction(event1 -> {
            personsNumber.setText("4");

        });
        p5.setOnAction(event1 -> {
            personsNumber.setText("5");

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

    public int getNumberOfnights(){
        return Integer.parseInt(nights.getText());
    }
    public int getNumberOfRooms(){
        return 0;
    }

    public String getCityChoosen() {
        return String.valueOf(cityName.getText());
    }

    public void moveToNextTap()
    {
        tabPane.getSelectionModel().selectNext();
    }
    public void moveToPreviousTap(){
        tabPane.getSelectionModel().selectPrevious();
    }
    public void findGuest(){
        db = new DataBase();
        persons = db.getPersonsCollection();
        cursor = persons.find().iterator();
        String[] listOfNames =new String[(int) persons.count()] ;
        for (int i = 0; i < persons.count(); i++) {
            doc = cursor.next();
            listOfNames[i]=doc.getString("name")+" "+doc.getString("last name");
        }
       TextFields.bindAutoCompletion(search,listOfNames);
        }

    public void addToTable() {
        roomController=new roomController();
        table.getItems().remove(0,table.getItems().size());
        listOfRooms= FXCollections.observableArrayList(roomController.arrayListOfRoom());
        table.setItems(listOfRooms);
        roomNrCol.setCellValueFactory(new PropertyValueFactory<Room, Integer>("roomNr"));
        priceCol.setCellValueFactory(new PropertyValueFactory<Room, Integer>("price"));
        cityCol.setCellValueFactory(new PropertyValueFactory<Room, Integer>("city"));
        roomTypeCol.setCellValueFactory(new PropertyValueFactory<Room, Integer>("roomType"));
        searchRoom.clear();
    }


    public void filterByCity(String string  ) {
        ObservableList<Room> list = table.getItems();
        roomController=new roomController();
        roomController.filterByCity(cityName.getText() , list);
        table.getItems().removeAll();
        listOfRooms= FXCollections.observableArrayList(roomController.filterByCity(cityName.getText(), list));
        table.setItems( listOfRooms);
        removeCitySearch.setVisible(true);

    }

    public void filterByType(String string  ) {
        ObservableList<Room> list = table.getItems();
        roomController=new roomController();
        //roomController.filterByRoomType(personsNumber.getText() , list);
        table.getItems().removeAll();
        listOfRooms= FXCollections.observableArrayList(roomController.filterByRoomType(personsNumber.getText(), list));
        table.setItems( listOfRooms);
        removeTypeSearch.setVisible(true);
    }

    public void removeCitySearch(){
        roomController=new roomController();
        listOfRooms= FXCollections.observableArrayList(roomController.arrayListOfRoom());
        if (removeTypeSearch.isVisible()){
            listOfRooms= FXCollections.observableArrayList(roomController.filterByRoomType(personsNumber.getText(), listOfRooms));
            table.setItems(listOfRooms);
        }else {
            table.getItems().removeAll();
            table.setItems(listOfRooms);
        }
        removeCitySearch.setVisible(false);
    }
    public void removeTypeSearch(){
        roomController=new roomController();
        listOfRooms= FXCollections.observableArrayList(roomController.arrayListOfRoom());
        if (removeCitySearch.isVisible()){
            listOfRooms= FXCollections.observableArrayList(roomController.filterByCity(cityName.getText(), listOfRooms));
            table.setItems(listOfRooms);
        }else {
            table.getItems().removeAll();
            table.setItems(listOfRooms);
        }
        removeTypeSearch.setVisible(false);
    }
    public void findRoom(){
        db = new DataBase();
        roomsList = db.getRoomsColl();
        cursor = roomsList.find().iterator();
        String searchedRoom = searchRoom.getText();
        for (int i = 0; i < roomsList.count(); i++) {
            doc = cursor.next();
            if (doc.getInteger("room nr").toString().equals(searchedRoom)) {
                table.getItems().remove(0, table.getItems().size());
                table.getItems().add(roomController.createRoom(doc));
            }
        }
    }
}

