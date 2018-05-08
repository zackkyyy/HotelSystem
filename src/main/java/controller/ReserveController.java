package controller;


import com.itextpdf.text.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import model.Guest;
import model.Reservation;
import model.Room;
import org.bson.Document;
import org.controlsfx.control.textfield.TextFields;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;


/**
 * Created by IntelliJ IDEA.
 * <p>
 * This class is the controller for the reserve window.
 *
 * @Author: Zacky Kharboutli
 * @Date: 2018-03-29
 * @Project : HotelSystem
 */
public class ReserveController implements Initializable {

    @FXML
    private Pane pane;
    @FXML
    private VBox VBox;
    @FXML
    private TabPane tabPane;
    @FXML
    private JFXButton removeCitySearch, removeTypeSearch;
    @FXML
    private Button confirm;
    @FXML
    private JFXTextField nights, rooms, search, searchRoom, name, lastName,
            address, phoneNr, identityNr, creditCard, totalPrice, totalPrice1, totalPrice2, rooms1, rooms2, nights1, nights2;
    @FXML
    private JFXDatePicker checkOutField, checkInField;
    @FXML
    private MenuButton personsNumber, roomsNumber, cityName;
    @FXML
    private MenuItem p1, p2, p3, p4, r1, r2, r3, r4, r5, kalmar, växjö;
    @FXML
    private TableColumn<model.Room, Integer> roomNrCol, roomTypeCol, priceCol, cityCol;
    @FXML
    private TableColumn<model.Room, Integer> roomIdCol;
    @FXML
    private TableColumn<ImageView, ImageView> floorCol;
    @FXML
    private TableColumn<model.Room, Integer> qualityCol;
    @FXML
    private TableColumn<Room, Integer> bookedCol;
    @FXML
    private FontAwesomeIconView removeRoomSearch;
    @FXML
    private Label errorLabel, errorMsg, gstID, gstName, gstCredit, gstPhone, depDate, arrDate, nameLabel;
    @FXML
    private Label roomNr1, guestNr1, roomType1, roomCity1, roomNr2, guestNr2, roomType2, roomCity2,
            roomNr3, guestNr3, roomType3, roomCity3, roomNr4, guestNr4, roomType4, roomCity4;
    @FXML
    private HBox roomInfoBox1, roomInfoBox2, roomInfoBox3, roomInfoBox4;
    @FXML
    private JFXButton checkInButton, guestButton, checkOutButton, logOutBtn;
    private MenuController mu;
    private Label[] roomNrs = new Label[4];
    private Label[] guestNrs = new Label[4];
    private Label[] roomTypes = new Label[4];
    private Label[] roomCities = new Label[4];
    private HBox[] roomInfoBoxes = new HBox[4];

    private MongoCollection persons;
    private MongoCursor<Document> cursor;
    private Document doc;
    private MongoCollection roomsList;
    @FXML
    private TableView table;
   @FXML
   private Button print;
    private DBParser dbParser;
    private ObservableList<Room> listOfRooms;
    private Guest customer;
    private ObservableList<Room> bookedRoom;
    public Reservation reservation;

    public static boolean creditCardValidator(String str) {
        if (str.length() < 13 || str.length() > 16) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * This method is @Override because this class implement Initializble
     * Here when initialize the button or the menu items to their actions
     *
     * @param location
     * @param resources
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbParser = new DBParser();
        dbParser.deleteOldReservations();

        mu = new MenuController();
        errorMsg.setVisible(false);
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        checkInField.setValue(LocalDate.now());
        checkOutField.setValue(LocalDate.now().plusDays(1));
        disablePreviousDates();
        addToTable();  // add rooms to the table
        initRoomInfo();

        // Adds listener to table, updating the total price

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                updateRoomPrice();
            }
        });

		/*
        The following lines to decide the number of the Guest
        depending on the chosen number from the button menu
		 */
        p1.setOnAction(event1 -> {
            personsNumber.setText("1");
            filterByType("1");
        });
        p2.setOnAction(event1 -> {
            personsNumber.setText("2");
            filterByType("2");

        });
        p3.setOnAction(event1 -> {
            personsNumber.setText("3");
            filterByType("3");

        });
        p4.setOnAction(event1 -> {
            personsNumber.setText("4");
            filterByType("4");

        });

        /*
         * Initialize the number of Room
         */
        r1.setOnAction(event1 -> {
            rooms.setText("1");
            roomsNumber.setText("1");
        });
        r2.setOnAction(event1 -> {
            rooms.setText("2");
            roomsNumber.setText("2");


        });
        r3.setOnAction(event1 -> {
            rooms.setText("3");
            roomsNumber.setText("3");
        });
        r4.setOnAction(event1 -> {
            rooms.setText("4");
            roomsNumber.setText("4");
        });
        r5.setOnAction(event1 -> {
            rooms.setText("5");
            roomsNumber.setText("5");
        });

        kalmar.setOnAction(t -> {
            cityName.setText("Kalmar");
            filterByCity("Kalmar");
        });
        växjö.setOnAction(t -> {
            cityName.setText("Växjö");
            filterByCity("Växjö");
        });

        checkInButton.setOnAction(event -> {
            try {
                mu.ShowCheckInPage(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        checkOutButton.setOnAction(event -> {
            try {
                mu.showCheckOutWindow(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        logOutBtn.setOnAction(event -> {
            try {
                mu.showLogInWindow(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        guestButton.setOnAction(event -> {
            try {
                mu.showGuestManagement(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Method that checks the inputs of the data picker to ensure that the dates are right,
     * and calculate the number of staying nights
     */
    public void calculateNights() {
        // check that the check in is always before the check out
        if (checkInField.getValue().isAfter(checkOutField.getValue())) {
            checkOutField.setValue(checkInField.getValue().plusDays(1));
            nights.setText("1");
        } else {
            int numberOfNights = checkOutField.getValue().getDayOfYear() - checkInField.getValue().getDayOfYear();
            nights.setText(String.valueOf(numberOfNights));
            nights1.setText(String.valueOf(numberOfNights));
            nights2.setText(String.valueOf(numberOfNights));
        }
        updateDepartureDateCells();
        updateRoomPrice();


        updateRoomPrice();
    }

    public void moveToNextTab() {
        if (tabPane.getSelectionModel().isSelected(1)) {
            customer = getCustomer();

            if (customer != null) {
                gstName.setText(customer.getName() + " " + customer.getLastName());
                gstCredit.setText(customer.getCreditCard());
                gstID.setText(customer.getIdentityNr());
                gstPhone.setText(customer.getPhoneNr());
                gstCredit.setText(customer.getCreditCard());
                tabPane.getSelectionModel().selectNext();
                arrDate.setText(checkInField.getValue().toString());
                depDate.setText(checkOutField.getValue().toString());
            } else {
                System.out.println("No customer chosen");
            }
        }
        if (tabPane.getSelectionModel().isSelected(0)) {
            if (validatePrice(totalPrice)) {
                if (Integer.parseInt(roomsNumber.getText()) == table.getSelectionModel().getSelectedItems().size()) {
                    bookedRoom = table.getSelectionModel().getSelectedItems();
                    rooms1.setText(Integer.toString(bookedRoom.size()));
                    rooms2.setText(Integer.toString(bookedRoom.size()));
                    hideRoomInfoBoxes(bookedRoom.size());

                    double tempTotalPrice = 0.00;
                    for (int i = 0; i < bookedRoom.size(); i++) {
                        String s = bookedRoom.get(i).getRoomNr() + " ";
                        String city = bookedRoom.get(i).getStringCity();
                        roomNrs[i].setText(s);
                        roomCities[i].setText(city);
                        guestNrs[i].setText(personsNumber.getText());
                        tempTotalPrice += bookedRoom.get(i).getPrice() * Integer.parseInt(nights.getText());
                        totalPrice1.setText(totalPrice.getText());
                        totalPrice2.setText(totalPrice.getText());
                    }

                    errorLabel.setVisible(false);
                    tabPane.getSelectionModel().selectNext();
                } else {
                    int a = Integer.parseInt(roomsNumber.getText());
                    int b = table.getSelectionModel().getSelectedItems().size();
                    if (a > b) {
                        errorLabel.setText(("you need to select " + (a - b) + " more"));
                        errorLabel.setVisible(true);
                    } else {
                        errorLabel.setText(("More selected rooms than what you wanted"));
                        errorLabel.setVisible(true);
                    }
                }
            }
        }
    }

    public void moveToPreviousTab() {
        tabPane.getSelectionModel().selectPrevious();
        errorMsg.setVisible(false);
        updateRoomPrice();
    }

    public void moveToFirstTab() {
        tabPane.getSelectionModel().selectFirst();
        pane.setVisible(false);
        confirm.setVisible(false);
        VBox.setVisible(true);
        updateRoomPrice();

    }

    public void autoCompletion() {
        String[] listOfNames;
        DBParser dbParser = new DBParser();
        listOfNames = dbParser.getGuestNames();

        try {
            TextFields.bindAutoCompletion(search, listOfNames);
        } catch (Exception e) {
            search.setText("");
        }
    }

    public void addToTable() {
        dbParser = new DBParser();
        table.getItems().remove(0, table.getItems().size());
        listOfRooms = FXCollections.observableArrayList(dbParser.getAllRoom());
        table.setItems(listOfRooms);
        roomNrCol.setCellValueFactory(new PropertyValueFactory<Room, Integer>("roomNr"));
        priceCol.setCellValueFactory(new PropertyValueFactory<Room, Integer>("price"));
        cityCol.setCellValueFactory(new PropertyValueFactory<Room, Integer>("city"));
        roomTypeCol.setCellValueFactory(new PropertyValueFactory<Room, Integer>("roomType"));
        searchRoom.clear();
        removeTypeSearch.setVisible(false);
        removeCitySearch.setVisible(false);
    }

    public void filterByCity(String string) {
        ObservableList<Room> list;
        dbParser = new DBParser();

        if (!removeCitySearch.isVisible()) {
            list = table.getItems();
            table.getItems().removeAll();
            listOfRooms = FXCollections.observableArrayList(dbParser.filterByCity(cityName.getText(), list));
            table.setItems(listOfRooms);
            removeCitySearch.setVisible(true);

        } else if (removeCitySearch.isVisible() && !removeTypeSearch.isVisible()) {
            list = FXCollections.observableArrayList(dbParser.getAllRoom());
            table.getItems().removeAll();
            listOfRooms = FXCollections.observableArrayList(dbParser.filterByCity(cityName.getText(), list));
            table.setItems(listOfRooms);

        } else if (removeCitySearch.isVisible() && removeTypeSearch.isVisible()) {
            list = FXCollections.observableArrayList(dbParser.getAllRoom());
            ObservableList<Room> filteredByRoom = FXCollections.observableArrayList(dbParser.filterByRoomType(personsNumber.getText(), list));
            listOfRooms = FXCollections.observableArrayList(dbParser.filterByCity(cityName.getText(), filteredByRoom));
            table.getItems().removeAll();
            table.setItems(listOfRooms);
        }
    }

    public void filterByType(String string) {
        ObservableList<Room> list;
        dbParser = new DBParser();
        if (!removeTypeSearch.isVisible()) {
            list = table.getItems();
            table.getItems().removeAll();
            listOfRooms = FXCollections.observableArrayList(dbParser.filterByRoomType(personsNumber.getText(), list));
            table.setItems(listOfRooms);
            removeTypeSearch.setVisible(true);

        } else if (removeTypeSearch.isVisible() && !removeCitySearch.isVisible()) {
            list = FXCollections.observableArrayList(dbParser.getAllRoom());
            table.getItems().removeAll();
            listOfRooms = FXCollections.observableArrayList(dbParser.filterByRoomType(personsNumber.getText(), list));
            table.setItems(listOfRooms);

        } else if (removeCitySearch.isVisible() && removeTypeSearch.isVisible()) {
            list = FXCollections.observableArrayList(dbParser.getAllRoom());
            ObservableList<Room> filteredByCity = FXCollections.observableArrayList(dbParser.filterByCity(cityName.getText(), list));
            listOfRooms = FXCollections.observableArrayList(dbParser.filterByRoomType(personsNumber.getText(), filteredByCity));
            table.getItems().removeAll();
            table.setItems(listOfRooms);
        }
    }

    public void removeCitySearch() {
        dbParser = new DBParser();
        listOfRooms = FXCollections.observableArrayList(dbParser.getAllRoom());

        if (removeTypeSearch.isVisible()) {
            listOfRooms = FXCollections.observableArrayList(dbParser.filterByRoomType(personsNumber.getText(), listOfRooms));
            table.setItems(listOfRooms);
        } else {
            table.getItems().removeAll();
            table.setItems(listOfRooms);
        }
        removeCitySearch.setVisible(false);
    }

    public void removeTypeSearch() {
        dbParser = new DBParser();
        listOfRooms = FXCollections.observableArrayList(dbParser.getAllRoom());

        if (removeCitySearch.isVisible()) {
            listOfRooms = FXCollections.observableArrayList(dbParser.filterByCity(cityName.getText(), listOfRooms));
            table.setItems(listOfRooms);
        } else {
            table.getItems().removeAll();
            table.setItems(listOfRooms);
        }
        removeTypeSearch.setVisible(false);
        personsNumber.setText("1");
    }

    public void findRoom() {
        DBParser dbParser = new DBParser();
        ArrayList<Room> arrayList = dbParser.getAllRoom();
        String searchedRoom = searchRoom.getText();

        if (!searchRoom.getText().isEmpty()) {
            for (int i = 0; i < arrayList.size(); i++) {
                if (arrayList.get(i).getRoomNr() == Integer.parseInt(searchedRoom) && !arrayList.get(i).isBooked()) {
                    table.getItems().remove(0, table.getItems().size());
                    table.getItems().add(arrayList.get(i));
                }
            }
        } else {
            addToTable();
        }
    }

    public Guest getCustomerFromSearch() {
        Guest guest = null;
        String searchText = search.getText();
        ArrayList<Guest> guests = dbParser.getGuestsInArray();

        for (int i = 0; i < guests.size(); i++) {
            if (!((guests.get(i).getName() + " " + guests.get(i).getLastName()).equals(searchText))) {
                search.setText("");
                errorMsg.setText("wrong name");
                errorMsg.setVisible(true);
            } else {
                guest = guests.get(i);
            }
        }
        return guest;
    }

    public Guest getCustomer() {
        Guest guest;

        if (!search.getText().equals("")) {
            guest = getCustomerFromSearch();
            System.out.println("Customer found in list");
        } else {
            guest = saveNewCustomer();
            System.out.println("Customer is created");
        }
        return guest;
    }

    public Guest saveNewCustomer() {
        Guest guest = null;

        if (!checkFields()) {
            errorMsg.setVisible(true);
        } else {
            guest = new Guest(name.getText(), lastName.getText(), address.getText(), phoneNr.getText(), creditCard.getText(), identityNr.getText());
            dbParser.createNewGuest(guest);
        }
        return guest;

    }

    public boolean checkFields() {
        if (name.getText().isEmpty()) {
            errorMsg.setText("Name field should not be empty");
            return false;

        } else if (name.getText().matches(".*\\d+.*")) {
            errorMsg.setText("Name field should have only letters");
            return false;

        } else if (lastName.getText().isEmpty()) {
            errorMsg.setText("Last name field should not be empty");
            return false;

        } else if (lastName.getText().matches(".*\\d+.*")) {
            errorMsg.setText("Last name field should have only letters");
            return false;

        } else if (address.getText().isEmpty()) {
            errorMsg.setText("Address field should not be empty");
            return false;

        } else if (identityNr.getText().isEmpty()) {
            errorMsg.setText("ID number field should not be empty");
            return false;

        } else if (creditCard.getText().isEmpty()) {
            errorMsg.setText("Credit card field should not be empty");
            return false;

        } else if (!creditCardValidator(creditCard.getText())) {
            errorMsg.setText("Invalid Credit Card");
            return false;

        } else if (phoneNr.getText().isEmpty()) {
            errorMsg.setText("Phone nr field should not be empty");
            return false;

        }
        return true;
    }

    public void createReservation(ActionEvent actionEvent) throws FileNotFoundException, DocumentException {
        String guestID = customer.getName() + " " + customer.getLastName();

        for (int i = 0; i < bookedRoom.size(); i++) {
            reservation = new Reservation();
            int roomID = bookedRoom.get(i).getRoomNr();
            reservation.setGuest(guestID);
            reservation.setRoom(roomID);
            reservation.setArrivalDate(checkInField.getValue());
            reservation.setDepartureDate(checkOutField.getValue());
            reservation.setCheckedIn(false);
            reservation.setPrice(bookedRoom.get(i).getPrice() * Integer.parseInt(nights.getText()));
            bookedRoom.get(i).setBooked(true);
            dbParser.refreshRoomStatus(bookedRoom.get(i));
            dbParser.saveReservationToDB(reservation);

            this.reservation=reservation;
        }

        VBox.setVisible(false);
        confirm.setVisible(false);
        createPdfRec();
        pane.setVisible(true);
        addToTable();
    }

    // Initializes the room info arrays
    private void initRoomInfo() {
        // Initialize room info Hboxes array
        roomInfoBoxes[0] = roomInfoBox1;
        roomInfoBoxes[1] = roomInfoBox2;
        roomInfoBoxes[2] = roomInfoBox3;
        roomInfoBoxes[3] = roomInfoBox4;

        // Initializes room information labels array
        roomNrs[0] = roomNr1;
        roomNrs[1] = roomNr2;
        roomNrs[2] = roomNr3;
        roomNrs[3] = roomNr4;
        guestNrs[0] = guestNr1;
        guestNrs[1] = guestNr2;
        guestNrs[2] = guestNr3;
        guestNrs[3] = guestNr4;
        roomTypes[0] = roomType1;
        roomTypes[1] = roomType2;
        roomTypes[2] = roomType3;
        roomTypes[3] = roomType4;
        roomCities[0] = roomCity1;
        roomCities[1] = roomCity2;
        roomCities[2] = roomCity3;
        roomCities[3] = roomCity4;
    }

    // Hides all roomInfoBoxes and then displays the ones that are used
    private void hideRoomInfoBoxes(int amountRooms) {
        for (int i = 0; i < roomInfoBoxes.length; i++) {
            roomInfoBoxes[i].setVisible(false);
            roomInfoBoxes[i].setManaged(false);
        }

        for (int i = 0; i < amountRooms; i++) {
            roomInfoBoxes[i].setVisible(true);
            roomInfoBoxes[i].setManaged(true);
        }
    }

    private void updateRoomPrice() {
        ObservableList<Room> selectedRooms = table.getSelectionModel().getSelectedItems();
        double tempTotalPrice = 0;

        for (int i = 0; i < selectedRooms.size(); i++) {
            tempTotalPrice += selectedRooms.get(i).getPrice() * Integer.parseInt(nights.getText());
        }
        totalPrice.setText(String.valueOf(tempTotalPrice));
    }


    // Disables (grayes out) all dates before today in both the DatePickers
    private void disablePreviousDates() {
        Callback<DatePicker, DateCell> dayCellFactory;

        dayCellFactory = new Callback<DatePicker, DateCell>() {
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item.isBefore(LocalDate.now())) {
                            setDisable(true);
                        }
                    }
                };
            }
        };

        checkOutField.setDayCellFactory(dayCellFactory);
        checkInField.setDayCellFactory(dayCellFactory);

    }

    private void updateDepartureDateCells() {
        checkOutField.hide();
        Callback<DatePicker, DateCell> dayCellFactory;

        dayCellFactory = new Callback<DatePicker, DateCell>() {
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item.isBefore(checkInField.getValue().plusDays(1))) {
                            setDisable(true);
                        } else if (item.isAfter(checkInField.getValue())) {
                            setDisable(false);
                        }
                    }
                };
            }
        };
        checkOutField.setDayCellFactory(dayCellFactory);
    }


    public boolean validatePrice(JFXTextField ss) {
        ObservableList<Room> selectedRooms = table.getSelectionModel().getSelectedItems();
        String string = ss.getText();
        double price = 0;
        if (!string.contains(".")) {
            string = string + ".0";    // if number is not decimal add .0 so it is decimal
        }
        if (!string.matches("([0-9]*)\\.([0-9]*)")) {  // compare with decimal pattern and numbers so it is a valid double input
            errorLabel.setText("Price you entered is incorrect");
            errorLabel.setVisible(true);
            return false;
        }
        for (int i = 0; i < selectedRooms.size(); i++) {
            price += selectedRooms.get(i).getPrice() * Integer.parseInt(nights.getText());
        }
        if (!ss.getText().isEmpty()&& Double.valueOf(ss.getText()) > price) {   //compare price with max daily rate of the rooms
            errorLabel.setText("Price you entered is more than the max rate");
            errorLabel.setVisible(true);
            return false;
        }
        return true;
    }

    /* This method create a pdf file if we need it later  */

    public com.itextpdf.text.Document createPdfRec()  {
        com.itextpdf.text.Document document = new com.itextpdf.text.Document();
       //PdfWriter.getInstance(document, new FileOutputStream("file.pdf"));
        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        Chunk chunk = new Chunk("price: ", font);
       // Chunk chunk1 = new Chunk("price: "+totalPrice.getText(), font);
        try {
            document.add(chunk);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document.close();
        return document;
    }

    /**
     * This method prints the reservation details
     * TODO: make the printed object looks better
     * at the moment it copies every thing and print it
     */
    public void print() {
        Printer printer = Printer.getDefaultPrinter();
        Stage stage = new Stage(StageStyle.DECORATED);
        PrinterJob job = PrinterJob.createPrinterJob(printer);
        if (job != null) {
            boolean showDialog = job.showPrintDialog(stage);
            if (showDialog) {
                VBox.setVisible(true);
                boolean success = job.printPage(VBox);
                if (success) {
                    job.endJob();
                }
                VBox.setVisible(false);

            }
        }
    }


    public String makeRec() {
   return reservation.toString();
    }
 /*
    public void print(java.awt.event.ActionEvent e)  {
       UIManager.put("swing.boldMetal", Boolean.FALSE);
        JFrame f = new JFrame("Hello World Printer");
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {System.exit(0);}
        });
        JButton printButton = new JButton("Print Hello World");
        f.add("Center", printButton);
        f.pack();
        f.setVisible(true);




    }



    public static class HelloWorldPrinter implements Printable , ActionListener {


        public int print(Graphics g, PageFormat pf, int page) throws
                PrinterException {

            if (page > 0) {
                return NO_SUCH_PAGE;
            }


            Graphics2D g2d = (Graphics2D)g;
            g2d.translate(pf.getImageableX(), pf.getImageableY());


            g.drawString("Hello world!", 100, 100);

            return PAGE_EXISTS;
        }


        public static void main(String args[]) {

        }



        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPrintable(this);
            boolean ok = job.printDialog();
            if (ok) {
                try {
                    job.print();
                } catch (PrinterException ex) {

                }
            }
        }

    }
    **/
}

