package controller;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.DataBase;
import model.Reservation;
import model.Room;
import model.User;
import org.bson.Document;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author: Zacky Kharboutli
 * @Date: 2018-04-16
 * @Project : HotelSystem
 */


public class MangerController implements Initializable {
    @FXML
    FontAwesomeIconView addUserButton;
    @FXML
    private Tab userTab, roomTab ;
    @FXML
    private TabPane tabPane1;
    @FXML
    private JFXListView userList , roomList;
    @FXML
    private JFXTextField password, userName ,userLastName  ,UserFirstName ,  price , roomNr;
    @FXML
    private MenuButton roomType, city , month, city1 ;
    @FXML
    private MenuItem kalmarItem, växjöItem, singleItem, doubleItem, tripleItem, apartmentItem
            ,jan, feb , mar ,apr, may, jun,jul,aug ,sep, oct,nov ,dec , vxj , klr;
    @FXML
    private Label errorLabel;
    @FXML
    private TableColumn<model.Reservation, Integer> arrivalCol,departCol,priceCol,guestCol,roomCol;
    @FXML
    private TableView table;
    private DataBase db;
    private ObservableList<Reservation> listOfReservations;

    private DBParser userController,dbParser;
    private DBParser roomController;
    private MongoCollection users , rooms;
    private Document doc;
    private MongoCursor<Document> cursor;
    private String errMsg;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		addReservationsToTable();
        filterReservationsByMonth();

	    växjöItem.setOnAction(event1 -> {
            city.setText("Växjö");
        });
		
		kalmarItem.setOnAction(event1 -> {
            city.setText("Kalmar");
        });
		
		singleItem.setOnAction(event1 -> {
            roomType.setText("Single");
        });
		
		doubleItem.setOnAction(event1 -> {
			roomType.setText("Double");
        });
		
		tripleItem.setOnAction(event1 -> {
			roomType.setText("Triple");
        });
		
		apartmentItem.setOnAction(event1 -> {
			roomType.setText("Apartment");
        });

		apr.setOnAction(event -> {
		    month.setText("APRIL");
        });
        may.setOnAction(event -> {
            month.setText("May");
        });
        jan.setOnAction(event -> {
            month.setText("January");
        });
        feb.setOnAction(event -> {
            month.setText("February");
        });
        mar.setOnAction(event -> {
            month.setText("March");
        });
        jun.setOnAction(event -> {
            month.setText("June");
        });
        jul.setOnAction(event -> {
            month.setText("July");
        });
        aug.setOnAction(event -> {
            month.setText("August");
        });
        sep.setOnAction(event -> {
            month.setText("September");
        });
        oct.setOnAction(event -> {
            month.setText("October");
        });
        nov.setOnAction(event -> {
            month.setText("November");
        });
        dec.setOnAction(event -> {
            month.setText("December");
        });




	}


    /**
     * This method runs when ever the user press on log out option in the header
     *
     * @param event log out button pressed
     * @throws IOException
     */
    public void showLogInWindow(ActionEvent event) throws IOException {
        MenuController mu = new MenuController();
        mu.showLogInWindow(event);
    }
    /**
     * move to the user management tab as in the fx file the pages are tabs
     *
     */
    public void goToUserTab()   {
        tabPane1.getSelectionModel().select(userTab);
        getDataFromDB();
    }
    /**
     * move to the room management tab as in the fx file the pages are tabs
     *
     */
    public void goToRoomTab()   {
        tabPane1.getSelectionModel().select(roomTab);
        getDataFromDB();
        roomList.getItems().sorted();
    }

    public void goToReservationTab()   {
        tabPane1.getSelectionModel().selectLast();
        addReservationsToTable();
    }
    /**
     * Method to fill the list of users or rooms from the database
     * it calls @getUserNames and @getArrayOfRooms from DBParser
     */
    private void getDataFromDB() {
        db = new DataBase();
        userController =new DBParser();
        roomController =new DBParser();
        if(tabPane1.getSelectionModel().isSelected(1)) {
            userList.getItems().remove(0, userList.getItems().size());
            for (int i = 0; i < userController.getUserNames(db).length; i++) {
                userList.getItems().add(userController.getUserNames(db)[i]);
            }
        }else if (tabPane1.getSelectionModel().isSelected(2)){
            roomList.getItems().remove(0,roomList.getItems().size());
            for (int i = 0; i < roomController.getArrayOfRoom(db).length ; i++) {
                roomList.getItems().add(roomController.getArrayOfRoom(db)[i]);
                roomList.getItems().sorted();
            }
        }
        else if (tabPane1.getSelectionModel().isSelected(3)){

        }
    }

    /**
     *  Method for deleting a room or user from the list when the manager logs in
     *
     * @param actionEvent
     *                  any actions performed on delete button
     */
    public void deleteFromList(ActionEvent actionEvent) {
        if(tabPane1.getSelectionModel().isSelected(1)) {
            errorLabel.setVisible(false);
            users=db.getUsersCollection();
            String selected = userList.getSelectionModel().getSelectedItem().toString();
            cursor = users.find().iterator();
            for (int i = 0; i < users.count(); i++) {
                Document doc = cursor.next();

                if (selected.contains(doc.getString("name") + " " + doc.getString("last name"))
                        && selected.length() == doc.getString("name").length() + doc.getString("last name").length() + 1) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Are you sure you want to delete " + selected + " ?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                        users.deleteOne(doc);
                        userList.getItems().remove(selected);
                        userList.getItems().remove(0, userList.getItems().size());
                        getDataFromDB();
                        clearAllFields();
                    }
                }
            }
        }else if(tabPane1.getSelectionModel().isSelected(2)) {
            errorLabel.setVisible(false);
            rooms=db.getRoomsColl();
            String selected = roomList.getSelectionModel().getSelectedItem().toString();
            cursor = rooms.find().iterator();
            for (int i = 0; i < rooms.count(); i++) {
                Document doc = cursor.next();

                if (selected.contains(doc.getInteger("room nr") + "")) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Are you sure you want to delete room number " + doc.getInteger("room nr") + " ?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                        roomList.getItems().remove(selected);
                        roomList.getItems().remove(0, roomList.getItems().size());
                        rooms.deleteOne(doc);
                        getDataFromDB();
                        clearAllFields();
                    }
                }
            }
        }

    }

    /**
     * Method that fills the information of the object choosen in the list
     * of users or room into the corresponding fields
     * @param mouseEvent
     */
    public void fillFields(MouseEvent mouseEvent) {
        if(tabPane1.getSelectionModel().isSelected(1)) {
            errorLabel.setVisible(false);
            cancelAllEdited();
            users=db.getUsersCollection();
            String selectedItem = userList.getSelectionModel().getSelectedItem().toString();
            System.out.println(selectedItem + " the selected one");
            cursor = users.find().iterator();
            for (int i = 0; i < users.count(); i++) {
                doc = cursor.next();
                if (selectedItem.contains(doc.getString("name") + " " + doc.getString("last name"))
                        && selectedItem.length() == doc.getString("name").length() + doc.getString("last name").length() + 1) {
                    // fill the fields by getting information from the database
                    UserFirstName.setText(doc.getString("name"));
                    userLastName.setText(doc.getString("last name"));
                    password.setText(doc.getString("password"));
                    userName.setText(doc.getString("username"));
                }

            }
        }else if (tabPane1.getSelectionModel().isSelected(2)){
            errorLabel.setVisible(false);
            cancelAllEdited();
            rooms=db.getRoomsColl();
            String selectedItem = roomList.getSelectionModel().getSelectedItem().toString();
            System.out.println(selectedItem + " the selected one");
            cursor = rooms.find().iterator();
            for (int i = 0; i < rooms.count(); i++) {
                doc = cursor.next();
                if (selectedItem.contains(doc.getInteger("room nr")+"")) {
                    // fill the fields by getting information from the database
                    roomNr.setText(String.valueOf(doc.getInteger("room nr")));
                    roomType.setText(doc.getString("room type"));
                    price.setText(String.valueOf(doc.getDouble("price")));
                    city.setText(doc.getString("city"));
                }

            }
        }


    }

    /**
     * cancel all the changes have been made on the fields if save is not pressed
     */
    private void cancelAllEdited() {
        errorLabel.setVisible(false);
        UserFirstName.cancelEdit();
        userName.cancelEdit();
        password.cancelEdit();
        userLastName.cancelEdit();
    }

    /**
     * Method to be peroformed when the manager changes something about a user or a room
     * it takes the information from the fields and edit the corresponding file in the database
     * @param actionEvent
     */
    public void editUserInfo(ActionEvent actionEvent) {
        if (tabPane1.getSelectionModel().isSelected(1)){
            Object selectedItem = userList.getSelectionModel().getSelectedItem();
            User temporaryUser = new User();
            db = new DataBase();
            // add values from text fields to the temporary guest
            if(!selectedItem.toString().contains(UserFirstName.getText()+" "+userLastName.getText())){
                errorLabel.setText("You cannot change the name or the last name");
                errorLabel.setVisible(true);
            }else {
                temporaryUser.setName(UserFirstName.getText());
                temporaryUser.setLastName(userLastName.getText());
                temporaryUser.setPassword(password.getText());
                temporaryUser.setUserName(userName.getText());


                userController = new DBParser();
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Are you sure you want to edit " + selectedItem + " ?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                    userList.getItems().remove(0, userList.getItems().size());
                    userController.editUser(db, selectedItem, temporaryUser);
                    getDataFromDB();
                    errorLabel.setVisible(false);
                }
            }
        }else if (tabPane1.getSelectionModel().isSelected(2)){
            String selectedItem = roomList.getSelectionModel().getSelectedItem().toString();
            Room temporaryRoom = new Room();
            db = new DataBase();
            // add values from text fields to the temporary guest

            temporaryRoom.setRoomType(model.enums.roomType.toEnum(roomType.getText()));
            temporaryRoom.setCity(model.enums.city.toEnum(city.getText()));
            temporaryRoom.setPrice(Double.valueOf(price.getText()));
            temporaryRoom.setRoomNr(Integer.parseInt(roomNr.getText()));
                roomController = new DBParser();
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Are you sure you want to edit room nr " + selectedItem + " ?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                    roomList.getItems().remove(0, userList.getItems().size());
                    roomController.editRoom(db, selectedItem, temporaryRoom);
                    getDataFromDB();
                    errorLabel.setVisible(false);
                }

        }

    }

    /**
     * Method that adds room in the database according to the information entered by the manager
     */
    public void addRoom(){
        db=new DataBase();
        roomController= new DBParser();
        Room room = new Room();
        if(checkForEmptyFields()) {
            // rooms
            room.setRoomNr((Integer.parseInt(roomNr.getText())));
            room.setPrice(Double.valueOf(price.getText()));
            room.setCity(model.enums.city.toEnum(city.getText()));
            room.setRoomType(model.enums.roomType.toEnum(roomType.getText()));
            roomController.createNewRoom(room, db);
            roomList.getItems().remove(0, roomList.getItems().size());
            getDataFromDB();
        }else{
            System.out.println("something wrong");
        }
    }

    /**
     * Method to add users
     */
    public void addUser (){
        db=new DataBase();
        userController=new DBParser();
        if (checkFields()) {
            User newUser = new User();
            newUser.setName(UserFirstName.getText());
            newUser.setLastName(userLastName.getText());
            newUser.setPassword(password.getText());
            newUser.setUserName(userName.getText());
            userController.createNewUser(newUser ,db);
            userList.getItems().remove(0,userList.getItems().size());
            getDataFromDB();
            clearAllFields();
        }else
        {
            errorLabel.setText(errMsg);
            errorLabel.setVisible(true);
        }

    }

    private void clearAllFields() {
        UserFirstName.clear();
        userName.clear();
        password.clear();
        userLastName.clear();
    }

    /**
     * Validate the fields when the manager tries to add room
     * Fields should not be empty and validate if the room is already exist
     *
     * @return
     *         true/false
     */
    public boolean checkForEmptyFields(){
        if (roomNr.getText().isEmpty() || city.getText().isEmpty()
                ||price.getText().isEmpty() || roomType.getText().isEmpty()){
            System.out.println("room nr is empty");
            return false;
        }
        for (int i =0 ; i<roomList.getItems().size(); i++){
            if (roomList.getItems().get(i).toString().contains(roomNr.getText()+"    "+city.getText())){
                System.out.println("it is exist");
                return false;
                }
        }

        return true;

    }

    /**
     * validate the fields of new user when the manger tries to add a new user
     *
     * @return
     *        true/false
     *        generate an error message to be shown on the screen
     */
    private boolean checkFields() {
        if (UserFirstName.getText().isEmpty()  ) {
            errMsg = "The name field is empty";
            return false;
        }else if(UserFirstName.getText().matches(".*\\d+.*")){
            errMsg = "Name field has an invalid input";
            return false;

        } else if (userLastName.getText().isEmpty()) {
            errMsg = "The last name field is empty";
            return false;
        } else if (userLastName.getText().matches(".*\\d+.*")) {
            errMsg = "The last name field has an invalid input";
            return false;

        } else if (password.getText().isEmpty()) {
            errMsg = "The password field is empty";
            return false;

        } else  if (userName.getText().isEmpty()) {
            errMsg = "The username field is empty";
            return false;

        }
        for (int i=0 ;i<userList.getItems().size();i++){
            if (userList.getItems().get(i).toString().equals(UserFirstName.getText()+" "+userLastName.getText())){
            errMsg="user is already exist";
            return false;
            }
        }


        return true;
    }
    public void addReservationsToTable(){
        dbParser =new DBParser();
        table.getItems().remove(0,table.getItems().size());
        listOfReservations= FXCollections.observableArrayList(dbParser.getAllReservations());
        table.setItems(listOfReservations);

        arrivalCol.setCellValueFactory(new PropertyValueFactory<Reservation,Integer>("arrivalDate"));
        departCol.setCellValueFactory(new PropertyValueFactory<Reservation,Integer>("departureDate"));
        priceCol.setCellValueFactory(new PropertyValueFactory<Reservation,Integer>("price"));
        guestCol.setCellValueFactory(new PropertyValueFactory<Reservation,Integer>("guest"));
        roomCol.setCellValueFactory(new PropertyValueFactory<Reservation,Integer>("room"));
    }

    public void filterReservationsByMonth(){
        dbParser =new DBParser();
       // table.getItems().remove(0,table.getItems().size());
        listOfReservations= FXCollections.observableArrayList(dbParser.getAllReservations());
        System.out.println(listOfReservations.get(2).getArrivalDate().getMonth()+"sdsadadadada");
/*         listOfReservations.stream()
                .filter(t ->
                        t.getRoom()==205 )
                .collect(Collectors.toList());

        table.setItems(listOfReservations);
        arrivalCol.setCellValueFactory(new PropertyValueFactory<Reservation,Integer>("arrivalDate"));
        departCol.setCellValueFactory(new PropertyValueFactory<Reservation,Integer>("departureDate"));
        priceCol.setCellValueFactory(new PropertyValueFactory<Reservation,Integer>("price"));
        guestCol.setCellValueFactory(new PropertyValueFactory<Reservation,Integer>("guest"));
        roomCol.setCellValueFactory(new PropertyValueFactory<Reservation,Integer>("room"));

// Instead it work when i do
// .filter(t ->  t.getAccount().getBalandsce().doubleValue() > 0 )

*/

        FilteredList<Reservation> filteredData = new FilteredList<>(listOfReservations, p -> true);


        month.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(t -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty() ) {

                    return true;
                }
                if (t.getArrivalDate().getMonth().name().equals(month.getText().toUpperCase())) {
                    return true; // Filter matches month
                }
                return false; // Does not match.
            });
        });
        SortedList<Reservation> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
    }



}
