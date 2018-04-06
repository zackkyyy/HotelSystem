package controller;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
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

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by zack on 2018-03-29.
 */
public class guestManagController implements Initializable {
    @FXML
    private JFXListView list;
    @FXML
    private JFXTextField name, lastName,address ;

    private ArrayList<Guest> memberList;
    FileHandler fileHandler;

    public void readFiles() throws IOException, ParseException {

    }
    public ArrayList<Guest> getMemberList() {

        return  new ArrayList<Guest>(memberList);
    }
    public void ShowMainPage(ActionEvent actionEvent) throws IOException {
        Parent mainWindow = FXMLLoader.load(getClass().getResource(String.valueOf("../View/MainWindow.fxml")));
        Scene mainWindowScene = new Scene(mainWindow);
        Stage app_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        app_stage.setScene(mainWindowScene);
        app_stage.show();
        System.out.println("Main window showed from guestManagController");
    }

    public void showCheckInWindow(ActionEvent actionEvent) throws IOException {
        Parent CheckInPage = FXMLLoader.load(getClass().getResource(String.valueOf("../View/checkInWindow.fxml")));
        Scene checkInScene = new Scene(CheckInPage);
        Stage app_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        app_stage.setScene(checkInScene);
        app_stage.show();
        System.out.println("CheckIn window showed from guestManagController");
    }

    public void showCheckOutWindow(ActionEvent actionEvent) throws IOException {
        Parent CheckOutPage = FXMLLoader.load(getClass().getResource(String.valueOf("../View/checkOutWindow.fxml")));
        Scene CheckoutScene = new Scene(CheckOutPage);
        Stage app_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        app_stage.setScene(CheckoutScene);
        app_stage.show();
        System.out.println("CheckOut window showed from guestManagController");
    }


    /**
     * Delete items from the Guest list
     * @param e
     */
    public void deleteFromList(ActionEvent e) throws IOException {

        Object selectedItem = list.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText("Are you sure you want to delete "+selectedItem +" ?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get().equals(ButtonType.OK)) {
            list.getItems().remove(selectedItem);
                   }
    }

    public void deleteGuest(Object selectedItem){

    }

    public void fillGaps(){

       Object selectedItem = list.getSelectionModel().getSelectedItem();
       for(int i=0; i<memberList.size() ;i++){
           if (selectedItem.toString().contains(memberList.get(i).getName()) && selectedItem.toString().contains(memberList.get(i).getLastName())){
               //System.out.println(memberList.get(i).getName() + "  " +memberList.get(i).getLastName());
               name.setText(memberList.get(i).getName());
               lastName.setText(memberList.get(i).getLastName());
               address.setText(memberList.get(i).getAddress());
           }
       }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)   {
        fileHandler  = new FileHandler();
        try {
            memberList = fileHandler.readFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // add the guest from the file to the list
        for (int i = 0; i<memberList.size() ;i++) {
            list.getItems().addAll(memberList.get(i).getName() + " " +memberList.get(i).getLastName() );
        }
    }
    public void saveFile() throws IOException {
        fileHandler.writeFile(memberList);
    }

}
