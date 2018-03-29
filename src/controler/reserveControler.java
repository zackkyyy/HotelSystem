package controler;


import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by zack on 2018-03-29.
 */
public class reserveControler {
    private String window= "fxml/MainWindow.fxml";


    public void showReservingWindow (ActionEvent event){
        window= "fxml/MainWindow.fxml";
}
    public void ShowCheckInPage (ActionEvent event) throws IOException {
        Parent Checkinpage = FXMLLoader.load(getClass().getResource(String.valueOf("../fxml/reserve.fxml")));
        Scene CheckinScene = new Scene(Checkinpage);
        Stage app_stage = (Stage)((Node) event.getSource()    ).getScene().getWindow();
        app_stage.setScene(CheckinScene);
        app_stage.show();
        System.out.print("s");
    }



}
