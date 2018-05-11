package controller;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import model.Guest;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author: Zacky Kharboutli
 * @Date: 2018-04-10
 * @Project : HotelSystem
 */
public class AddGuestController {
	@FXML
	private JFXTextField name, lastName, address, phoneNr, identityNr, creditCard;
	private Stage stage;
	private Scene scene;
	private String errMsg;

	public void showStage() throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource(String.valueOf("/addGuest.fxml")));
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

	public void saveNewGuest() {
		DBParser guestController = new DBParser();
		
		if (checkFields()) {
			Guest newGuest = new Guest();
			newGuest.setName(name.getText());
			newGuest.setLastName(lastName.getText());
			newGuest.setAddress(address.getText());
			newGuest.setPhoneNr(phoneNr.getText());
			newGuest.setIdentityNr(identityNr.getText());
			newGuest.setCreditCard(creditCard.getText());
			guestController.createNewGuest(newGuest);
			clearFields();
		} else if (!checkFields()) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText(errMsg);
			alert.showAndWait();
		}
	}

	public boolean checkFields() {
		if (name.getText().isEmpty()) {
			errMsg = "The name field is empty";
			return false;
			
		} else if(name.getText().matches(".*\\d+.*")){
			errMsg = "Name field has an invalid input";
			return false;

		} else if (lastName.getText().isEmpty()) {
			errMsg = "The last name field is empty";
			return false;
		} else if (lastName.getText().matches(".*\\d+.*")) {
			errMsg = "The last name field has an invalid input";
			return false;

		} else if (address.getText().isEmpty()) {
			errMsg = "The address field is empty";
			return false;

		} else  if (identityNr.getText().isEmpty()) {
			errMsg = "The Id number field is empty";
			return false;

		} else if (creditCard.getText().isEmpty()) {
			errMsg = "The credit card field is empty";
			return false;

		} else if (phoneNr.getText().isEmpty()) {
			errMsg = "The phone nr field is empty";
			return false;
		}
		return true;
	}
	
	public void clearFields(){
		name.setText("");
		lastName.setText("");
		phoneNr.setText("");
		address.setText("");
		creditCard.setText("");
		identityNr.setText("");
	}
}
