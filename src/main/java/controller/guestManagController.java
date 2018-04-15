package controller;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import controller.database.DataBase;
import controller.database.guestController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import model.Guest;
import org.bson.Document;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by IntelliJ IDEA.
 * <p>
 * This class is the controller for the guest Management window.
 * All functions for the guest managing will be handled here
 *
 * @User: Zacky Kharboutli
 * @Date: 2018-03-29
 * @Project : HotelSystem
 */
public class guestManagController implements Initializable {
    @FXML
    private JFXListView list;
    @FXML
    private JFXTextField name, lastName, address, phoneNr, idField, identityNr, credit, notes;
    @FXML
    private JFXDatePicker birthday;
    private DataBase db= new DataBase();
    private MongoCollection persons;
    private MongoCursor<Document> cursor;
    private Document doc;
    private guestController guestController;
    /**
     * Initializing the scene for the guest management window
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        getGuestFromDb();
        list.getSelectionModel().selectFirst();
        fillFields();
    }


    public void ShowMainPage(ActionEvent actionEvent) throws IOException {
        Parent mainWindow = FXMLLoader.load(getClass().getResource(String.valueOf("/MainWindow.fxml")));
        Scene mainWindowScene = new Scene(mainWindow);
        Stage app_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        app_stage.setScene(mainWindowScene);
        app_stage.show();
        System.out.println("Main window showed from guestManagController");
    }

    public void showCheckInWindow(ActionEvent actionEvent) throws IOException {
        Parent CheckInPage = FXMLLoader.load(getClass().getResource(String.valueOf("/checkInWindow.fxml")));
        Scene checkInScene = new Scene(CheckInPage);
        Stage app_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        app_stage.setScene(checkInScene);
        app_stage.show();
        System.out.println("CheckIn window showed from guestManagController");
    }

    public void showCheckOutWindow(ActionEvent actionEvent) throws IOException {
        Parent CheckOutPage = FXMLLoader.load(getClass().getResource(String.valueOf("/checkOutWindow.fxml")));
        Scene CheckoutScene = new Scene(CheckOutPage);
        Stage app_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        app_stage.setScene(CheckoutScene);
        app_stage.show();
        System.out.println("CheckOut window showed from guestManagController");
    }

    /**
     * This method is called when ever a guest is deleted from the list.
     * It delete the chosen guest from the text file which is used as database
     *
     * @throws IOException
     */

    public void deleteFromList(ActionEvent e) throws IOException {
        persons=db.getPersonsCollection();
        String selected = list.getSelectionModel().getSelectedItem().toString();
        cursor = persons.find().iterator();
        for (int i = 0; i < persons.count(); i++) {
            doc = cursor.next();

            if (selected.contains(doc.getString("name") + " " + doc.getString("last name"))
                    && selected.length() == doc.getString("name").length() + doc.getString("last name").length() + 1) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Are you sure you want to delete " + selected + " ?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                    persons.deleteOne(doc);
                    list.getItems().remove(selected);
                    list.getItems().remove(0, list.getItems().size());
                    getGuestFromDb();
                }
            }
        }
    }

    /**
     * A method that get the information of the chosen guest from
     * the list and show his/her information to the
     */
    public void fillFields() {
        cancelAllEdited();
        persons=db.getPersonsCollection();
        String selectedItem = list.getSelectionModel().getSelectedItem().toString();
        System.out.println(selectedItem + " the selected one");
        cursor = persons.find().iterator();
        for (int i = 0; i < persons.count(); i++) {
            doc = cursor.next();
            if (selectedItem.contains(doc.getString("name") + " " + doc.getString("last name"))
                    && selectedItem.length() == doc.getString("name").length() + doc.getString("last name").length() + 1) {
                // fill the fields by getting information from the database
                idField.setText("" + doc.getObjectId("_id"));
                name.setText(doc.getString("name"));
                lastName.setText(doc.getString("last name"));
                phoneNr.setText(doc.getString("phone nr"));
                address.setText(doc.getString("address"));
                identityNr.setText(doc.getString("identity nr"));
                notes.setText(doc.getString("notes"));
                credit.setText(doc.getString("credit card"));
                if (doc.getDate("birthday")==null){
                    birthday.setValue(LocalDate.now());
                }else {
                    birthday.setValue(doc.getDate("birthday").toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

                }
            }

        }


    }

    /**
     * A method to let the clerk edit the guest information from the guest management window
     * It gets the information from the textfield and save it to the guest information
     *
     * @throws IOException
     */
    public void editGuestInfo() throws IOException, ParseException {
        Object selectedItem = list.getSelectionModel().getSelectedItem();
        Guest temporaryGst = new Guest();
        db = new DataBase();
        // add values from text fields to the temporary guest
        temporaryGst.setName(name.getText());
        temporaryGst.setLastName(lastName.getText());
        temporaryGst.setPhoneNr(phoneNr.getText());
        temporaryGst.setAddress(address.getText());
        temporaryGst.setCreditCard(credit.getText());
        temporaryGst.setNotes( notes.getText());
        temporaryGst.setIdentityNr(identityNr.getText());
        temporaryGst.setBirthday(birthday.getValue());

        guestController= new guestController();
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText("Are you sure you want to edit " + selectedItem + " ?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get().equals(ButtonType.OK)) {
            list.getItems().remove(0, list.getItems().size());
            guestController.editGuest(db ,selectedItem , temporaryGst);
            getGuestFromDb();
        }
    }


    public void AddGuest(ActionEvent actionEvent) throws IOException {
        addGuestController controller = new addGuestController();
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

    public void getGuestFromDb() {
        list.getItems().removeAll();
        db = new DataBase();
        guestController=new guestController();
        list.getItems().removeAll();
        for (int i = 0 ; i<guestController.getGuestNames(db).length;i++){
            list.getItems().add(guestController.getGuestNames(db)[i]);
        }
    }
}
