package controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;

/**
 * @Author: Zacky Kharboutli
 * @Date: 2018-03-29
 * @Project : HotelSystem
 */
public class LoginController {
	@FXML
	private JFXPasswordField pass,pass1;
	@FXML
	private JFXTextField user , user1;
	@FXML
	private Text sorry;
	@FXML
	private JFXTabPane tab;

	public boolean checkForLogIn() {
		DBParser userController = new DBParser();

		if(user1.getText().equals("1") && pass1.getText().equals("1")){
			System.out.println("Correct login");
			return true;
		} else  if(userController.validateLogging(user.getText(), pass.getText())){
			return true;
		}
		System.out.println("Incorrect login");

		return false;
	}
	
	public void showMainPage (ActionEvent event) throws IOException {
		Parent CheckInPage = null;

		if(checkForLogIn()){
			if (tab.getSelectionModel().isSelected(0)){
				CheckInPage = FXMLLoader.load(getClass().getResource(String.valueOf("/MainWindow.fxml")));
			} else if(tab.getSelectionModel().isSelected(1) ){
				CheckInPage = FXMLLoader.load(getClass().getResource(String.valueOf("/ManagerWindow.fxml")));
			}
			Scene mainWindowScene = new Scene(CheckInPage);
			Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			app_stage.setScene(mainWindowScene);
			app_stage.show();
			System.out.println("Main window showed from loginController");
		} else {
			sorry.setText("Sorry user name or password is wrong");
		}

	}

	public void aboutUs() throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource(String.valueOf("/contactUs.fxml")));
		Scene scene = new Scene(root, 400, 350);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.setMinWidth(400);
		stage.setMinHeight(350);
		stage.setMaxHeight(350);
		stage.setMaxWidth(400);
		stage.show();
	}



	public void goToGithubPage() {
		try {
			Desktop.getDesktop().browse(new URL("https://github.com/zackkyyy/FrontDisk-Manager").toURI());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
