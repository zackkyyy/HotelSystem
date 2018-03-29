package controler;

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
    String password ="password";
    String userName ="admin";
    @FXML
    private JFXPasswordField pass;
    @FXML
    private JFXTextField user;

    @FXML
    private Text sorry;

    public loginController() {
    }

    public boolean checkForLogIn() throws IOException {
   if( (user.getText().equals("admin"))&& (pass.getText().equals("password")) ){
        System.out.println("right");
        return  true;
    }
        System.out.println("false");
        System.out.println(pass.getText());
        System.out.println(user.getText());
        return false;
    }
    public void ShowMainPage (ActionEvent event) throws IOException {
        if (checkForLogIn()) {
            Parent Checkinpage = FXMLLoader.load(getClass().getResource(String.valueOf("../fxml/MainWindow.fxml")));
            Scene mainWindowScene = new Scene(Checkinpage);
            Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            app_stage.setScene(mainWindowScene);
            app_stage.show();
            System.out.print("main window showed");
        }else
            sorry.setText("Sorry user name or password is wrong");
    }

}
