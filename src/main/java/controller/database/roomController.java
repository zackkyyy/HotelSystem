package controller.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import javafx.collections.ObservableList;
import model.Room;
import model.enums.city;
import model.enums.roomType;
import org.bson.Document;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 *
 *  This is controller for the rooms that connect the database with view
 *  All functions related to room are handled in this class and called from the reservationController
 *
 * @User: Zacky Kharboutli
 * @Date: 2018-04-14
 * @Project : HotelSystem
 */


public class roomController {
    private Document doc;
    private MongoCollection rooms;
    private MongoCursor  <Document> cursor;
    private DataBase db = new DataBase();

    /**
     *  Convert a String to city
     *
     * @param city1  a string that refer to the city
     * @return  @enum city
     */
    private city toCity(String city1) {
        if (city1.contains("Växjö")){
            return city.VÄXJÖ;
        }else  if(city1.contains("Kalmar")){
            return city.KALMAR;
        }else return null;

    }

    /**
     *  Convert a String to room type
     * @param room_type  a string that refer to the room type
     * @return  @enum roomType
     */
    private roomType toRoomType(String room_type) {
        if (room_type.equals("single")){
            return roomType.SINGLE;
        }else  if(room_type.contains("triple")){
            return roomType.TRIPLE;
        }else  if(room_type.contains("Double")){
            return roomType.DOUBLE;
        }else  if(room_type.contains("Apartment")){
            return roomType.APARTMENT;
        }
        else return null;

    }

    /**
     *  This method is called when the user try to look for room in one of the cities (Växjö/Kalmar)
     *  The user choose the city from the UI using the menu button
     *
     * @param string  The chosen city that is used to filter the rooms
     * @param list    list of rooms from the list view in the UI
     * @return     a new list of room after filtering the old list
     */
    public ArrayList<Room> filterByCity(String string, ObservableList<Room> list){

        ArrayList<Room> listOfRooms = new ArrayList<Room>();

        for (int i = 0; i < list.size(); i++) {

            if (list.get(i).getCity().toString().equals(string)) {

                listOfRooms.add(createRoom(list.get(i)));
            }
        }
        return listOfRooms;
    }

    /**
     *  This method is called when the user try to look for specific type of
     *  room from the UI.
     *
     * @param string  The room type that is used to filter the rooms
     * @param list    list of rooms from the list view in the UI
     * @return     a new list of room after filtering the old list
     */
    public ArrayList<Room> filterByRoomType(String string, ObservableList<Room> list) {

        ArrayList<Room> listOfRooms = new ArrayList<Room>();

        for (int i = 0; i < list.size(); i++) {
            if (string.equals("1")) {
                if (list.get(i).getRoomType().toString().equals("Single")) {

                    listOfRooms.add(createRoom(list.get(i)));
                }
            } else if (string.equals("2")) {
                if (list.get(i).getRoomType().toString().equals("Double")) {

                    listOfRooms.add(createRoom(list.get(i)));
                }
            }
            else if (string.equals("3")) {
                if (list.get(i).getRoomType().toString().equals("Triple")) {

                    listOfRooms.add(createRoom(list.get(i)));
                }
            }
            else if (string.equals("4")) {
                if (list.get(i).getRoomType().toString().equals("Apartment")) {

                    listOfRooms.add(createRoom(list.get(i)));
                }
            }
        }
        return listOfRooms;

    }

    /**
     *  Create a room to be added to the list from the parameter
     *  this method is created to avoid duplicate codes
     *
     * @param room
     * @return   a new room
     *
     */
    public Room createRoom(Room room){
        model.enums.roomType roomType = (room.getRoomType());
        boolean booked = room.isBooked();
        int roomNr = room.getRoomNr();
        int price = room.getPrice();
        city city = room.getCity();
        Room room1 = new Room(roomType, booked, roomNr, price, city);
        return room1;
    }

    /**
     *   This method generate an array list of rooms from the database
     *
     * @return
     *          list of room in array list
     */
    public ArrayList<Room> getAllRoom() {
         db= new DataBase();
        rooms=db.getRoomsColl();
        cursor = rooms.find().iterator();
        ArrayList<Room> listOfRooms = new ArrayList<Room>();

        for (int i = 0; i < rooms.count(); i++) {
            doc = cursor.next();
            model.enums.roomType roomType = toRoomType(doc.getString("room type"));
            boolean booked = doc.getBoolean("is booked");
            int roomNr=      doc.getInteger("room nr");
            int price =      doc.getInteger("price");
            city city=toCity(doc.getString("city"));
            Room room = new Room(  roomType, booked,  roomNr,  price,  city);
            listOfRooms.add(room);
        }
        return listOfRooms;
    }
    public Room createRoom(Document doc){
        model.enums.roomType roomType = toRoomType(doc.getString("room type"));
        boolean booked =doc.getBoolean("is booked");
        int roomNr =doc.getInteger("room nr");
        int price =doc.getInteger("price");
        city city = toCity(doc.getString("city"));
        Room room1 = new Room(roomType, booked, roomNr, price, city);
        return room1;
    }

}