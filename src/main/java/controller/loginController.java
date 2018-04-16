package controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import controller.database.UserController;
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
    private JFXPasswordField pass,pass1;
    @FXML
    private JFXTextField user , user1;
    @FXML
    private Text sorry;
    @FXML
    private JFXTabPane tab;


    public loginController() {
    }

    public boolean checkForLogIn() throws IOException {
        UserController userController = new UserController();
        if(userController.validateLogging(user.getText() , pass.getText())){
            return true;
        }
        else if(user1.getText().equals("1") && pass1.getText().equals("1")){
       System.out.println("Correct login");
       return true;
   }
        System.out.println("Incorrect login");

        return false;
    }
    public void ShowMainPage (ActionEvent event) throws IOException {
        Parent CheckInPage = null;
        if(checkForLogIn()){
            if (tab.getSelectionModel().isSelected(0)){
                CheckInPage = FXMLLoader.load(getClass().getResource(String.valueOf("/MainWindow.fxml")));
            }else if(tab.getSelectionModel().isSelected(1) ){
                CheckInPage = FXMLLoader.load(getClass().getResource(String.valueOf("/ManagerWindow.fxml")));
            }
            Scene mainWindowScene = new Scene(CheckInPage);
            Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            app_stage.setScene(mainWindowScene);
            app_stage.show();
            System.out.println("Main window showed from loginController");
        }
        else{
            sorry.setText("Sorry user name or password is wrong");
        }

    }

}
