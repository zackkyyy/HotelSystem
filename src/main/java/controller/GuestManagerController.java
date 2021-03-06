package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import model.Guest;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by IntelliJ IDEA.
 * <p>
 * This class is the controller for the guest Management window.
 * All functions for the guest managing will be handled here
 *
 * @Author: Zacky Kharboutli
 * @Date: 2018-03-29
 * @Project : HotelSystem
 */
public class GuestManagerController implements Initializable {

    @FXML
    private JFXListView list;
    @FXML
    private JFXTextField name, lastName, address, phoneNr, idField, identityNr, credit, notes,search;
    @FXML
    private JFXDatePicker birthday;
    @FXML
    private JFXButton checkInButton, reserveButton, checkOutButton, logOutBtn;
    private MenuController mu;
    private DBParser guestController;
    private String errMsg;



    /**
     * Initializing the scene for the guest management window
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mu = new MenuController();
        getGuestFromDb();

        if (list.getItems().size() != 0) {
            list.getSelectionModel().selectFirst();

            fillFields();
        }

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
        reserveButton.setOnAction(event -> {
            try {
                mu.showMainPage(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * This method is called when ever a guest is deleted from the list.
     * It delete the chosen guest from the text file which is used as database
     */

    public void deleteFromList(ActionEvent e) {
        String selected = list.getSelectionModel().getSelectedItem().toString();
        DBParser dbParser = new DBParser();
        Guest guest = dbParser.getGuestByName(selected);
        System.out.println(guest.getName());
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText("Are you sure you want to delete " + selected + " ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get().equals(ButtonType.OK)) {
            try {
                dbParser.deleteGuest(guest);
            } catch (ParseException e1) {
                System.err.println(e);
            }
            list.getItems().remove(selected);
            list.getItems().remove(0, list.getItems().size());
            getGuestFromDb();
            list.getSelectionModel().selectFirst();
            fillFields();
        }


    }

    /**
     * A method that get the information of the chosen guest from
     * the list and show his/her information to the
     */
    public void fillFields() {
        cancelAllEdited();
        DBParser dbParser = new DBParser();
        if (list.getItems().size() != 0) {
            String selectedItem = list.getSelectionModel().getSelectedItem().toString();
            Guest guest = dbParser.getGuestByName(selectedItem);
            // fill the fields by getting information from the database
            idField.setText(guest.getId());
            name.setText(guest.getName());
            lastName.setText(guest.getLastName());
            phoneNr.setText(guest.getPhoneNr());
            address.setText(guest.getAddress());
            identityNr.setText(guest.getIdentityNr());
            notes.setText(guest.getNotes());
            credit.setText(guest.getCreditCard());
            birthday.setValue(guest.getBirthday());
        } else {
            System.out.println("the list is empty");
        }
    }

    /**
     * A method to let the clerk edit the guest information from the guest management window
     * It gets the information from the textfield and save it to the guest information
     */
    public void editGuestInfo() throws ParseException {
        Object selectedItem = list.getSelectionModel().getSelectedItem();
        Guest temporaryGst = new Guest();
        // add values from text fields to the temporary guest
        if(validateFields()) {
            temporaryGst.setName(name.getText());
            temporaryGst.setLastName(lastName.getText());
            temporaryGst.setPhoneNr(phoneNr.getText());
            temporaryGst.setAddress(address.getText());
            temporaryGst.setCreditCard(credit.getText());
            temporaryGst.setNotes(notes.getText());
            temporaryGst.setIdentityNr(identityNr.getText());
            temporaryGst.setBirthday(birthday.getValue());
            temporaryGst.setId(idField.getText());
            guestController = new DBParser();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Are you sure you want to edit " + selectedItem + " ?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                list.getItems().remove(0, list.getItems().size());
                guestController.editGuest(temporaryGst);
                getGuestFromDb();
                returnToDefault();
            }
        }

    }
    public void getGuestFromDb() {
        list.getItems().removeAll(0,list.getItems().size());
        guestController = new DBParser();
        for (int i = 0; i < guestController.getGuestsInArray().size(); i++) {
            String str = guestController.getGuestsInArray().get(i).getName()+ " " + guestController.getGuestsInArray().get(i).getLastName();
            list.getItems().add(str);
        }
    }


    public void addGuest(ActionEvent actionEvent) throws IOException {
        AddGuestController controller = new AddGuestController();
        controller.showStage();
    }

    public void cancelAllEdited() {
        name.cancelEdit();
        lastName.cancelEdit();
        identityNr.cancelEdit();
        address.cancelEdit();
        credit.cancelEdit();
        notes.cancelEdit();
        phoneNr.cancelEdit();
    }


    public boolean validateFields(){
        if (name.getText().isEmpty() ||name.getText().matches(".*\\d+.*")) {
            name.setPromptText( "Incorrect name");
            name.setUnFocusColor(Color.RED);
            return false;

        } else if (lastName.getText().isEmpty()|| lastName.getText().matches(".*\\d+.*")) {
            lastName.setPromptText( "Incorrect last name");
            lastName.setUnFocusColor(Color.RED);
            return false;
        } else  if (identityNr.getText().isEmpty()) {
            identityNr.setPromptText( "Incorrect id number");
            identityNr.setUnFocusColor(Color.RED);
            return false;

        } else if (credit.getText().isEmpty()) {
            credit.setPromptText( "Incorrect credit card number");
            credit.setUnFocusColor(Color.RED);
            return false;
        }
        else if (address.getText().isEmpty()) {
            address.setPromptText( "Incorrect address");
            address.setUnFocusColor(Color.RED);
            return false;

        }else if (phoneNr.getText().isEmpty()) {
            phoneNr.setPromptText( "Incorrect phone number");
            phoneNr.setUnFocusColor(Color.RED);
            return false;
        }
        return true;
    }

    public void returnToDefault() {

        name.setPromptText("");
        name.setUnFocusColor(Paint.valueOf("bcbaba"));
        lastName.setPromptText("");
        lastName.setUnFocusColor(Paint.valueOf("bcbaba"));
        identityNr.setPromptText("");
        identityNr.setUnFocusColor(Paint.valueOf("bcbaba"));
        address.setPromptText("");
        address.setUnFocusColor(Paint.valueOf("bcbaba"));
        credit.setPromptText("");
        credit.setUnFocusColor(Paint.valueOf("bcbaba"));
        phoneNr.setPromptText("");
        phoneNr.setUnFocusColor(Paint.valueOf("bcbaba"));
        notes.setPromptText("");
        notes.setUnFocusColor(Paint.valueOf("bcbaba"));

    }
    public void findGuest() {
        DBParser dbParser = new DBParser();
        ArrayList<Guest> arrayList = dbParser.getGuestsInArray();
        if (!search.getText().isEmpty()) {
            list.getItems().remove(0, list.getItems().size());
            for (int i = 0; i < arrayList.size(); i++) {
                if ((arrayList.get(i).getName()+" "+arrayList.get(i).getLastName()).toLowerCase().contains(search.getText().toLowerCase())) {
                    list.getItems().addAll(arrayList.get(i).getName()+" "+arrayList.get(i).getLastName());
                }
            }
        }else {
            list.getItems().remove(0, list.getItems().size());
            getGuestFromDb();
        }
    }
}
