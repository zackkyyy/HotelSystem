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
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by IntelliJ IDEA.
 *
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
    private JFXTextField name, lastName,address ,phoneNr;

    private ArrayList<Guest> memberList;
    FileHandler fileHandler;

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
            deleteGuest(selectedItem);
            }
    }

    /**
     * This method is called when ever a guest is deleted from the list.
     * It delete the chosen guest from the text file which is used as database
     *
     * @param selectedItem the guest that is chosen from the list
     * @throws IOException
     */

    public void deleteGuest(Object selectedItem) throws IOException {
        for (int i =0 ; i<memberList.size(); i++ ){
            //compare with the guest by checking the first name, the second name and the length
            // so we avoid having troubles of similar names of guests
            if (selectedItem.toString().contains(memberList.get(i).getName())
                    && selectedItem.toString().contains(memberList.get(i).getLastName())
                   && selectedItem.toString().length()-1==memberList.get(i).getName().length() + memberList.get(i).getLastName().length() ){
                System.out.println(memberList.get(i).getName() + " is now deleted");
                this.memberList.remove(memberList.get(i));  // remove the guest from the list
            }
            fileHandler.writeFile(memberList);
        }
    }

    /**
     * A method that get the information of the chosen guest from
     * the list and show his/her information to the
     *
     */
    public void fillGaps(){

       Object selectedItem = list.getSelectionModel().getSelectedItem();
       for(int i=0; i<memberList.size() ;i++){
           if (selectedItem!=null &&selectedItem.toString().contains(memberList.get(i).getName()) && selectedItem.toString().contains(memberList.get(i).getLastName())){
               //System.out.println(memberList.get(i).getName() + "  " +memberList.get(i).getLastName());
               name.setText(memberList.get(i).getName());
               lastName.setText(memberList.get(i).getLastName());
               address.setText(memberList.get(i).getAddress());
               phoneNr.setText(memberList.get(i).getPhoneNr());
           }
       }
    }

    /**
     * A method to let the clerk edit the guest information from the guest management window
     * It gets the information from the textfield and save it to the guest information
     * @throws IOException
     */
    public void editGuestInfo(    ) throws IOException {
        Object selectedItem = list.getSelectionModel().getSelectedItem();

        // compare the selected item with guest we have in the list
        for(int i=0; i<memberList.size() ;i++) {
            if (selectedItem.toString().contains(memberList.get(i).getName()) && selectedItem.toString().contains(memberList.get(i).getLastName())) {
                // adding the new information to the guest
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Are you sure you want to edit "+selectedItem +" ?");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                    memberList.get(i).setPhoneNr(phoneNr.getText());
                    memberList.get(i).setAddress(address.getText());
                    memberList.get(i).setLastName(lastName.getText());
                    memberList.get(i).setName(name.getText());
                    list.getItems().remove(selectedItem); // delete the item from the list and add it again
                    list.getItems().add(memberList.get(i).getName() + " " +memberList.get(i).getLastName());
                    list.getSelectionModel().selectLast(); // this line to keep the selection mode on the same member
                }

                }
        }

        fileHandler.writeFile(memberList);
    }

    /**
     * Initializing the scene for the guest management window
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources)   {
        fileHandler  = new FileHandler();
        try {
            memberList = fileHandler.readFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // add the guest from the file to the list
       updateList();
        fillGaps();
    }

    public void updateList(){
        for (int i = 0; i<memberList.size() ;i++) {
           list.getItems().addAll(memberList.get(i).getName() + " " +memberList.get(i).getLastName() );

        }

    }

    public void emptyGasp(){

    }
}
