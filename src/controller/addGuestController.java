package controller;

import com.jfoenix.controls.JFXTextField;
import com.mongodb.client.MongoCollection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.bson.Document;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 *
 * @User: Zacky Kharboutli
 * @Date: 2018-04-10
 * @Project : HotelSystem
 */


public class addGuestController {
    @FXML
    private JFXTextField name, lastName, address, phoneNr, identityNr, creditCard;
    private MongoCollection persons;
    DataBase db;
    Stage stage;
    Scene scene;
    String errMsg;

    public void showStage() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(String.valueOf("../View/addGuest.fxml")));
        scene = new Scene(root, 400, 350);
        stage = new Stage();
        stage.setTitle("New Guest");
        stage.setScene(scene);
        stage.setMinWidth(400);
        stage.setMinHeight(350);
        stage.setMaxHeight(350);
        stage.setMaxWidth(400);
        stage.show();


    }

    public void SaveNewGuest(ActionEvent actionEvent) {

        db = new DataBase();
        persons = db.getDatabase().getCollection("persons");
        if (checkIfNotEmpty()) {
            try {
                Document doc = new Document("name", name.getText())
                        .append("last name", lastName.getText())
                        .append("phone nr", phoneNr.getText())
                        .append("address", address.getText())
                        .append("credit card", creditCard.getText())
                        .append("identity nr", identityNr.getText());
                persons.insertOne(doc);

                name.setText("");
                lastName.setText("");
                phoneNr.setText("");
                address.setText("");
                creditCard.setText("");
                identityNr.setText("");
                stage.hide();
            } catch (Exception e) {
                System.out.println(e.getClass().getName() + ": " + e.getMessage());
//          display the error message
                System.out.println("Failed to save");
            }
        } else if (!checkIfNotEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(errMsg);
            alert.showAndWait();
        }


    }

    public boolean checkIfNotEmpty() {

        if (identityNr.getText().isEmpty()) {
            errMsg = "The Id number field is empty";
            return false;
        } else if (name.getText().isEmpty()) {
            errMsg = "The name field is empty";

            return false;
        } else if (lastName.getText().isEmpty()) {
            errMsg = "The last name field is empty";

            return false;
        } else if (creditCard.getText().isEmpty()) {
            errMsg = "The credit card field is empty";

            return false;
        } else if (address.getText().isEmpty()) {
            errMsg = "The address field is empty";

            return false;
        } else if (phoneNr.getText().isEmpty()) {
            errMsg = "The phone nr field is empty";

            return false;
        }
        return true;
    }
}
