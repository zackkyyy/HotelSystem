package controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by zack on 2018-03-29.
 */
public class loginController {
    @FXML
    private JFXPasswordField pass;
    @FXML
    private JFXTextField user;

    @FXML
    private Text sorry;

    public loginController() {
    }

    public boolean checkForLogIn() throws IOException {
   if( (user.getText().equals("1"))&& (pass.getText().equals("1")) ){
        System.out.println("Correct login");
        return  true;
    }
        System.out.println("Incorrect login");
        System.out.println(pass.getText());
        System.out.println(user.getText());
        return false;
    }
    public void ShowMainPage (ActionEvent event) throws IOException {
        if (checkForLogIn()) {
            Parent CheckInPage = FXMLLoader.load(getClass().getResource(String.valueOf("/MainWindow.fxml")));
            Scene mainWindowScene = new Scene(CheckInPage);
            Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            app_stage.setScene(mainWindowScene);
            app_stage.show();
            System.out.println("Main window showed from loginController");
        }else
            sorry.setText("Sorry user name or password is wrong");
    }

}
