package controller;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import model.DataBase;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import model.Room;
import model.User;
import org.bson.Document;

import java.util.Optional;

/**
 * Created by IntelliJ IDEA.
 *
 * @User: Zacky Kharboutli
 * @Date: 2018-04-16
 * @Project : HotelSystem
 */


public class MangerController {
    @FXML
    FontAwesomeIconView addUserButton;
    @FXML
    private Tab userTab, roomTab ;
    @FXML
    private TabPane tabPane1;
    @FXML
    private JFXListView userList , roomList;
    @FXML
    private JFXTextField password, userName ,userLastName  ,UserFirstName , roomType , price , roomNr , city ;
    @FXML
    private Label errorLabel;
    private DataBase db;
    private DBParser userController;
    private DBParser roomController;
    private MongoCollection users , rooms;
    private Document doc;
    private MongoCursor<Document> cursor;
    private String errMsg;



    public void goToUserTab()   {
        tabPane1.getSelectionModel().select(userTab);
        getDataFromDB();
    }
    public void goToRoomTab()   {
        tabPane1.getSelectionModel().select(roomTab);
        getDataFromDB();
    }


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
    }



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
                    price.setText(String.valueOf(doc.getInteger("price")));
                    city.setText(doc.getString("city"));
                }

            }
        }


    }

    private void cancelAllEdited() {
        errorLabel.setVisible(false);
        UserFirstName.cancelEdit();
        userName.cancelEdit();
        password.cancelEdit();
        userLastName.cancelEdit();
    }

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
            //TODO fix the enum translation problem in room editing
            String selectedItem = roomList.getSelectionModel().getSelectedItem().toString();
            Room temporaryRoom = new Room();
            db = new DataBase();
            // add values from text fields to the temporary guest

            temporaryRoom.setRoomType(model.enums.roomType.toEnum(roomType.getText()));
            temporaryRoom.setCity(model.enums.city.toEnum(city.getText()));
            temporaryRoom.setPrice(Integer.parseInt(price.getText()));
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

    private boolean checkFields() {
        //TODO   check if user is already exist
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
        return true;
    }

}
