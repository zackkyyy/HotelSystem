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
import model.Reservation;
import model.Room;
import model.User;
import model.enums.Quality;
import org.bson.Document;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author: Zacky Kharboutli
 * @Date: 2018-04-16
 * @Project : HotelSystem
 */


public class ManagerController implements Initializable {
    @FXML
    FontAwesomeIconView addUserButton;
    @FXML
    private Tab userTab, roomTab;
    @FXML
    private TabPane tabPane1;
    @FXML
    private JFXListView userList, roomList;
    @FXML
    private JFXTextField password, userName, userLastName, UserFirstName, price, roomNr;
    @FXML
    private MenuButton roomType, city, month, smoking ,adjacent,qualityBtn;
    @FXML
    private MenuItem kalmarItem, växjöItem, singleItem, doubleItem, tripleItem, apartmentItem, jan, feb, mar, apr, may,
            jun, jul, aug, sep, oct, nov, dec, vxj, klr,adjYes , adjNo ,smoYes , smoNo,star5,star4,star3,star2,star1;
    @FXML
    private Label errorLabel;
    @FXML
    private TableColumn<model.Reservation, Integer> arrivalCol, departCol, priceCol, guestCol, roomCol;
    @FXML
    private TableView table;
    private ObservableList<Reservation> listOfReservations;

    private DBParser userController, dbParser;
    private DBParser roomController;
    private MongoCollection users, rooms;
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
        smoYes.setOnAction(event -> {
            smoking.setText("Yes");
        });
        smoNo.setOnAction(event -> {
            smoking.setText("No");
        });
        adjNo.setOnAction(event -> {
            adjacent.setText("No");
        });
        adjYes.setOnAction(event -> {
            adjacent.setText("Yes");
        });


        star1.setOnAction(event -> {
            qualityBtn.setText("★");
        });
        star2.setOnAction(event -> {
            qualityBtn.setText("★★");
        });

        star3.setOnAction(event -> {
            qualityBtn.setText("★★★");
        });

        star4.setOnAction(event -> {
            qualityBtn.setText("★★★★");
        });

        star5.setOnAction(event -> {
            qualityBtn.setText("★★★★★");
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
     */
    public void goToUserTab() {
        tabPane1.getSelectionModel().select(userTab);
        getDataFromDB();
    }

    /**
     * move to the room management tab as in the fx file the pages are tabs
     */
    public void goToRoomTab() {
        tabPane1.getSelectionModel().select(roomTab);
        getDataFromDB();
        roomList.getItems().sorted();
    }

    public void goToReservationTab() {
        tabPane1.getSelectionModel().selectLast();
        addReservationsToTable();
    }

    /**
     * Method to fill the list of users or rooms from the database
     * it calls @getUserNames and @getArrayOfRooms from DBParser
     */
    private void getDataFromDB() {

        userController = new DBParser();
        roomController = new DBParser();
        if (tabPane1.getSelectionModel().isSelected(1)) {
            userList.getItems().remove(0, userList.getItems().size());
            for (int i = 0; i < userController.getUserNames().length; i++) {
                userList.getItems().add(userController.getUserNames()[i]);
            }
        } else if (tabPane1.getSelectionModel().isSelected(2)) {
            roomList.getItems().remove(0, roomList.getItems().size());
            for (int i = 0; i < roomController.getAllRoom().size(); i++) {
                String str = roomController.getAllRoom().get(i).getRoomNr() + "    " + roomController.getAllRoom().get(i).getCity().toString();
                roomList.getItems().add(str);
                roomList.getItems().sorted();
            }
        } else if (tabPane1.getSelectionModel().isSelected(3)) {

        }
    }

    /**
     * Method for deleting a room or user from the list when the manager logs in
     *
     * @param actionEvent any actions performed on delete button
     */
    public void deleteFromList(ActionEvent actionEvent) {
        if (tabPane1.getSelectionModel().isSelected(1)) {
            errorLabel.setVisible(false);
            String selected = userList.getSelectionModel().getSelectedItem().toString();
            DBParser dbParser = new DBParser();
            User user = dbParser.getUserByName(selected);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Are you sure you want to delete " + selected + " ?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                dbParser.deleteUser(user);
                userList.getItems().remove(selected);
                userList.getItems().remove(0, userList.getItems().size());
                getDataFromDB();
                clearAllFields();
            }


        } else if (tabPane1.getSelectionModel().isSelected(2)) {
            errorLabel.setVisible(false);
            ArrayList<Room> arrayOfRooms = dbParser.getAllRoom();
            String selected = roomList.getSelectionModel().getSelectedItem().toString();
            for (int i = 0; i < arrayOfRooms.size(); i++) {
                if (selected.contains(String.valueOf(arrayOfRooms.get(i).getRoomNr()) + "    " + arrayOfRooms.get(i).getCity().toString())) {
                    Room room = arrayOfRooms.get(i);
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Are you sure you want to delete room number " + selected + " ?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                        roomList.getItems().remove(selected);
                        roomList.getItems().remove(0, roomList.getItems().size());
                        dbParser.deleteRoom(room);
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
     *
     * @param mouseEvent
     */
    public void fillFields(MouseEvent mouseEvent) {
        DBParser dbParser = new DBParser();
        if (tabPane1.getSelectionModel().isSelected(1)) {
            errorLabel.setVisible(false);
            cancelAllEdited();
            String selectedItem = userList.getSelectionModel().getSelectedItem().toString();
            User user = dbParser.getUserByName(selectedItem);
            // fill the fields by getting information from the database
            UserFirstName.setText(user.getName());
            userLastName.setText(user.getLastName());
            password.setText(user.getPassword());
            userName.setText(user.getUserName());

        } else if (tabPane1.getSelectionModel().isSelected(2)) {
            errorLabel.setVisible(false);
            cancelAllEdited();
            String selectedItem = roomList.getSelectionModel().getSelectedItem().toString();
            ArrayList<Room> arrayOfRooms = dbParser.getAllRoom();
            for (int i = 0; i < arrayOfRooms.size(); i++) {
                if (selectedItem.contains(String.valueOf(arrayOfRooms.get(i).getRoomNr()) + "    " + arrayOfRooms.get(i).getCity().toString())) {
                    // fill the fields by getting information from the database
                    Room room = arrayOfRooms.get(i);
                    roomNr.setText(String.valueOf(room.getRoomNr()));
                    roomType.setText(room.getRoomType().toString());
                    price.setText(String.valueOf(room.getPrice()));
                    city.setText(room.getCity().toString());
                    smoking.setText(room.getSmoking().toString());
                    adjacent.setText(room.getAdjoined().toString());
                    qualityBtn.setText(room.getQuality().toString());
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
     *
     * @param actionEvent
     */
    public void editUserInfo(ActionEvent actionEvent) {
        if (tabPane1.getSelectionModel().isSelected(1)) {
            Object selectedItem = userList.getSelectionModel().getSelectedItem();
            User temporaryUser = new User();

            // add values from text fields to the temporary guest
            if (!selectedItem.toString().contains(UserFirstName.getText() + " " + userLastName.getText())) {
                errorLabel.setText("You cannot change the name or the last name");
                errorLabel.setVisible(true);
            } else {
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
                    userController.editUser(selectedItem, temporaryUser);
                    getDataFromDB();
                    errorLabel.setVisible(false);
                }
            }
        } else if (tabPane1.getSelectionModel().isSelected(2)) {
            String selectedItem = roomList.getSelectionModel().getSelectedItem().toString();
            Room temporaryRoom = new Room();

            // add values from text fields to the temporary guest

            temporaryRoom.setRoomType(model.enums.RoomType.toEnum(roomType.getText()));
            temporaryRoom.setCity(model.enums.City.toEnum(city.getText()));
            temporaryRoom.setPrice(Double.valueOf(price.getText()));
            temporaryRoom.setRoomNr(Integer.parseInt(roomNr.getText()));
            temporaryRoom.setAdjoined(model.enums.Adjacent.toEnum(adjacent.getText()));
            temporaryRoom.setSmoking(model.enums.Smoking.toEnum(smoking.getText()));
            temporaryRoom.setQuality(Quality.toEnum(qualityBtn.getText()));
            roomController = new DBParser();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Are you sure you want to edit room nr " + selectedItem + " ?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                roomList.getItems().remove(0, userList.getItems().size());
                roomController.refreshRoomStatus( temporaryRoom);
                getDataFromDB();
                errorLabel.setVisible(false);
            }
        }
    }

    /**
     * Method that adds room in the database according to the information entered by the manager
     */
    public void addRoom() {
        roomController = new DBParser();
        Room room = new Room();
        if (checkForEmptyFields()) {
            // rooms
            room.setRoomNr((Integer.parseInt(roomNr.getText())));
            room.setPrice(Double.valueOf(price.getText()));
            room.setCity(model.enums.City.toEnum(city.getText()));
            room.setRoomType(model.enums.RoomType.toEnum(roomType.getText()));
            room.setAdjoined(model.enums.Adjacent.toEnum(adjacent.getText()));
            room.setSmoking(model.enums.Smoking.toEnum(smoking.getText()));
            room.setQuality(Quality.toEnum(qualityBtn.getText()));
            roomController.createNewRoom(room);
            roomList.getItems().remove(0, roomList.getItems().size());
            getDataFromDB();
        } else {
            System.out.println("something wrong");
        }
    }

    /**
     * Method to add users from manager log in to the database
     *
     */
    public void addUser() {

        userController = new DBParser();
        String password1 = password.getText();
        /*
             Save the password in the system in encrypted way
         */
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password1);

        if (checkFields()) {
            User newUser = new User();
            newUser.setName(UserFirstName.getText());
            newUser.setLastName(userLastName.getText());
            newUser.setPassword(hashedPassword);
            newUser.setUserName(userName.getText());
            userController.createNewUser(newUser);
            userList.getItems().remove(0, userList.getItems().size());
            getDataFromDB();
            clearAllFields();
        } else {
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
     * @return true/false
     */
    public boolean checkForEmptyFields() {
        if (roomNr.getText().isEmpty() || city.getText().isEmpty()
                || price.getText().isEmpty() || roomType.getText().isEmpty()) {
            System.out.println("room nr is empty");
            return false;
        }

        for (int i = 0; i < roomList.getItems().size(); i++) {
            if (roomList.getItems().get(i).toString().contains(roomNr.getText() + "    " + city.getText())) {
                System.out.println("it is exist");
                return false;
            }
        }
        return true;
    }

    /**
     * validate the fields of new user when the manger tries to add a new user
     *
     * @return true/false
     * generate an error message to be shown on the screen
     */
    private boolean checkFields() {
        if (UserFirstName.getText().isEmpty()) {
            errMsg = "The name field is empty";
            return false;
        } else if (UserFirstName.getText().matches(".*\\d+.*")) {
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

        } else if (userName.getText().isEmpty()) {
            errMsg = "The username field is empty";
            return false;
        }

        for (int i = 0; i < userList.getItems().size(); i++) {
            if (userList.getItems().get(i).toString().equals(UserFirstName.getText() + " " + userLastName.getText())) {
                errMsg = "user is already exist";
                return false;
            }
        }
        return true;
    }

    public void addReservationsToTable() {
        dbParser = new DBParser();
//        if (table.getItems().size()>0) {
//            table.getItems().remove(0, table.getItems().size());
//        }
        listOfReservations = FXCollections.observableArrayList(dbParser.getAllReservations());
        table.setItems(listOfReservations);

        arrivalCol.setCellValueFactory(new PropertyValueFactory<Reservation, Integer>("arrivalDate"));
        departCol.setCellValueFactory(new PropertyValueFactory<Reservation, Integer>("departureDate"));
        priceCol.setCellValueFactory(new PropertyValueFactory<Reservation, Integer>("price"));
        guestCol.setCellValueFactory(new PropertyValueFactory<Reservation, Integer>("guest"));
        roomCol.setCellValueFactory(new PropertyValueFactory<Reservation, Integer>("room"));
    }

    /**
     * this method is to search for a reservation by the month
     *
     * @newValue the chosen month from the menu button
     */
    public void filterReservationsByMonth() {
        dbParser = new DBParser();
        listOfReservations = FXCollections.observableArrayList(dbParser.getAllReservations());
        FilteredList<Reservation> filteredData = new FilteredList<>(listOfReservations, p -> true);
        month.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(t -> {
                // If filter text is empty, display all reservations.
                if (newValue == null || newValue.isEmpty()) {
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
