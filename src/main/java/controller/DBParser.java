package controller;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;

import javafx.collections.ObservableList;
import model.*;
import model.enums.City;
import model.enums.RoomType;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Year;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author: Zacky Kharboutli
 * @Date: 2018-04-18
 * @Project : HotelSystem
 */
public class DBParser {
	private Document doc;
	private MongoCollection persons;
	private MongoCursor<Document> cursor;
	private MongoCollection rooms;
	private DataBase db = new DataBase();
	private MongoCollection users;
	private MongoCollection reservations;


	public void createNewGuest(Guest newGuest , DataBase db) {
		db = new DataBase();
		doc = new Document();
		persons = db.getPersonsCollection();

		try {
			java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.now().toString());

			doc = new Document("name", newGuest.getName())
					.append("last name", newGuest.getLastName())
					.append("phone nr", newGuest.getPhoneNr())
					.append("address", newGuest.getAddress())
					.append("credit card", newGuest.getCreditCard())
					.append("identity nr", newGuest.getIdentityNr())
					.append("birthday", date)
					.append("notes", "");

			persons.insertOne(doc);
			newGuest.setId("" + doc.getObjectId("_id"));

			//  final Node source = (Node) e.getSource();
		} catch (Exception e){
			System.out.println(e.getClass().getName() + ": " + e.getMessage());
			//          display the error message
			System.out.println("Failed to save");
		}
		System.out.println("Guest with id " + newGuest.getId() + " is now created");
	}

	public String [] getGuestNames( DataBase db){
		persons = db.getPersonsCollection();
		String [] listOfGuest = new String[(int) persons.count()];
		MongoCursor<Document> cursor = persons.find().iterator();

		for (int i = 0; i < persons.count(); i++) {
			Document doc = cursor.next();
			String name = doc.getString("name");
			String lastname = doc.getString("last name");
			listOfGuest[i] = (name + " " + lastname);
		}
		return listOfGuest;
	}

	public void editGuest(DataBase db, Object selectedItem , Guest guest) throws ParseException {
		persons = db.getPersonsCollection();
		cursor = persons.find().iterator();
		ObjectId objectId = null;

		for (int i = 0; i < persons.count(); i++) {
			doc = cursor.next();
			if (selectedItem.toString().contains(doc.getString("name"))) {
				objectId = doc.getObjectId("_id");
			}
		}

		java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse(guest.getBirthday().toString());
		persons.updateOne(eq("_id", objectId), new Document("$set",
				new Document("last name", guest.getLastName())
				.append("name", guest.getName())
				.append("phone nr", guest.getPhoneNr())
				.append("address", guest.getAddress())
				.append("credit card", guest.getCreditCard())
				.append("notes", guest.getNotes())
				.append("identity nr", guest.getIdentityNr())
				.append("birthday", date)));
	}

	public ArrayList<Guest> getGuestsInArray() {
		db = new DataBase();
		persons = db.getPersonsCollection();
		cursor = persons.find().iterator();
		ArrayList<Guest> listOfGuest = new ArrayList<Guest>();

		for (int i = 0; i < persons.count(); i++) {
			doc = cursor.next();

			String name = doc.getString("name");
			String lastName = doc.getString("last name");
			String address = doc.getString("address");
			String phoneNr = doc.getString("phone nr");
			String creditCard = doc.getString("credit card");
			String identity_nr = doc.getString("identity nr");
			Guest gst = new Guest(name , lastName,address , phoneNr ,creditCard ,identity_nr);
			listOfGuest.add(gst);

		}
		return listOfGuest;
	}

	public ObjectId getGuestID(Guest gst){
		ObjectId id =null;
		persons = db.getPersonsCollection();
		cursor = persons.find().iterator();
		for (int i = 0; i < persons.count(); i++) {
			doc = cursor.next();
			if (doc.getString("identity nr").equals(gst.getIdentityNr())){
				id = doc.getObjectId("_id");
			}
		}
		return id;
	}

	/**
	 *  Convert a String to city
	 *
	 * @param city1  a string that refer to the city
	 * @return  @enum city
	 */
	private City toCity(String city1) {
		if (city1.contains("Växjö")){
			return City.VÄXJÖ;
		} else if(city1.contains("Kalmar")){
			return City.KALMAR;
		} else return null;
	}

	/**
	 *  Convert a String to room type
	 * @param room_type  a string that refer to the room type
	 * @return  @enum roomType
	 */
	private RoomType toRoomType(String room_type) {
		if (room_type.equals("Single")){
			return RoomType.SINGLE;
		} else  if(room_type.contains("Triple")){
			return RoomType.TRIPLE;
		} else  if(room_type.contains("Double")){
			return RoomType.DOUBLE;
		} else  if(room_type.contains("Apartment")){
			return RoomType.APARTMENT;
		} else if (room_type.equals("")){
			return null;
		} else {
			return null;
		}
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
				listOfRooms.add(list.get(i));
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
					listOfRooms.add(list.get(i));
				}
			} else if (string.equals("2")) {
				if (list.get(i).getRoomType().toString().equals("Double")) {
					listOfRooms.add(list.get(i));
				}
			}
			else if (string.equals("3")) {
				if (list.get(i).getRoomType().toString().equals("Triple")) {
					listOfRooms.add(list.get(i));
				}
			}
			else if (string.equals("4")) {
				if (list.get(i).getRoomType().toString().equals("Apartment")) {
					listOfRooms.add(list.get(i));
				}
			}
		}
		return listOfRooms;
	}

	/**
	 *   This method generate an array list of rooms from the database
	 *
	 * @return
	 *          list of room in array list
	 */
	public ArrayList<Room> getAllRoom() {
		db = new DataBase();
		rooms = db.getRoomsColl();
		cursor = rooms.find().iterator();
		ArrayList<Room> listOfRooms = new ArrayList<Room>();

		for (int i = 0; i < rooms.count(); i++) {
			doc = cursor.next();
			if (!doc.getBoolean("is booked")) {
				RoomType roomType = toRoomType(doc.getString("room type"));
				boolean booked = doc.getBoolean("is booked");
				int roomNr = doc.getInteger("room nr");
				Double price = doc.getDouble("price");
				City city = toCity(doc.getString("city"));
				Room room = new Room(roomType, booked, roomNr, price, city);
				listOfRooms.add(room);
			}
		}
		return listOfRooms;
	}
	public ObjectId getRoomID(Room room){
		ObjectId id = null;
		rooms = db.getRoomsColl();
		cursor = rooms.find().iterator();

		for (int i = 0; i < rooms.count(); i++) {
			doc = cursor.next();
			if (doc.getInteger("room nr" )== room.getRoomNr() && doc.getString("city").equals(room.getCity().toString())){
				id = doc.getObjectId("_id");
			}
		}
		return id;
	}

	public Room createRoom(Document doc){
		RoomType roomType = toRoomType(doc.getString("room type"));
		boolean booked = doc.getBoolean("is booked");
		int roomNr = doc.getInteger("room nr");
		Double price = doc.getDouble("price");
		City city = toCity(doc.getString("city"));
		Room room1 = new Room(roomType, booked, roomNr, price, city);
		return room1;
	}
	
	public Object[] getArrayOfRoom(DataBase db) {
		rooms = db.getRoomsColl();
		String [] listOfGuest = new String[(int) rooms.count()];
		MongoCursor<Document> cursor = rooms.find().iterator();
		for (int i = 0; i < rooms.count(); i++) {
			Document doc = cursor.next();
			listOfGuest[i] = (doc.getInteger("room nr") + "    "+doc.getString("city"));
		}
		return listOfGuest;
	}

	public void editRoom(DataBase db, String selectedItem, Room temporaryRoom) {
		ObjectId objectId = null;
		rooms = db.getRoomsColl();
		cursor = rooms.find().iterator();
		
		for (int i = 0; i < rooms.count(); i++) {
			doc = cursor.next();
			if (selectedItem.contains(doc.getInteger("room nr") + "")) {
				objectId = doc.getObjectId("_id");
			}
		}
		
		rooms.updateOne(eq("_id", objectId), new Document("$set",
				new Document("room nr", temporaryRoom.getRoomNr())
				.append("room type", temporaryRoom.getRoomType().toString())
				.append("price", temporaryRoom.getPrice())
				.append("city", temporaryRoom.getCity().toString())));
	}

	public void refreshRoomStatus(Room room){
		rooms.updateOne(eq("room nr", room.getRoomNr() ), new Document("$set",
				new Document("room nr", room.getRoomNr())
				.append("room type", room.getRoomType().toString())
				.append("price", room.getPrice())
				.append("is booked", room.isBooked())
				.append("city", room.getCity().toString())));
	}

	public void refreshReservationStatus(Reservation reservation){
		reservations = db.getReservationsCollection();
		Date arrivalDate = Date.from(reservation.getArrivalDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date departureDate = Date.from(reservation.getDepartureDate().atStartOfDay(ZoneId.systemDefault()).toInstant());

		System.out.println(reservation.getCheckedIn());
		System.out.println(reservations.updateOne(eq("room", reservation.getRoom() ), new Document("$set",
				new Document("guest", reservation.getGuest())
				.append("room", reservation.getRoom())
				.append("arrival", arrivalDate)
				.append("departure", departureDate)
				.append("price", reservation.getPrice())
				.append("is checkedIn", reservation.getCheckedIn()))));
	}

	public Object[] getUserNames(DataBase db) {
		users = db.getUsersCollection();
		String [] listOfGuest = new String[(int) users.count()];

		MongoCursor<Document> cursor = users.find().iterator();
		for (int i = 0; i < users.count(); i++) {
			Document doc = cursor.next();
			listOfGuest[i]=(doc.getString("name") + " "+doc.getString("last name"));
		}
		return listOfGuest;
	}

	public void editUser(DataBase db, Object selectedItem, User temporaryUser) {
		users = db.getUsersCollection();
		cursor = users.find().iterator();
		ObjectId objectId = null;
		for (int i = 0; i < users.count(); i++) {
			doc = cursor.next();
			if (selectedItem.toString().contains(doc.getString("name"))) {
				objectId = doc.getObjectId("_id");
			}
		}
		users.updateOne(eq("_id", objectId), new Document("$set",
				new Document("name", temporaryUser.getName())
				.append("last name", temporaryUser.getLastName())
				.append("username", temporaryUser.getUserName())
				.append("password", temporaryUser.getPassword())));
	}

	public void createNewUser(User newUser, DataBase db) {
		doc = new Document();
		users = db.getUsersCollection();
		try {
			doc = new Document("name", newUser.getName())
					.append("last name", newUser.getLastName())
					.append("password", newUser.getPassword())
					.append("username", newUser.getUserName());

			users.insertOne(doc);
		} catch (Exception e){
			System.out.println(e.getClass().getName() + ": " + e.getMessage());
			System.out.println("Failed to save");
		}
		System.out.println("Guest with id "+newUser.getName() +" is now created");
	}

	/**
	 *    This method is a validator that checks if the entered password and user name
	 *    matches any of the registered users in the DB
	 * @param username
	 * 				String that user enter to log in
	 * @param password
	 * 				Password as String that user used to log in
	 * @return
	 * 			True if there is a match, otherwise false
	 */
	public Boolean validateLogging(String username , String password){
		DataBase db = new DataBase();
		users = db.getUsersCollection();
		cursor = users.find().iterator();
		
		for (int i = 0; i < users.count(); i++) {
			doc = cursor.next();
			if (username.equals(doc.getString("username"))) {
				if(password.equals(doc.getString("password"))){
					return true;
				}
			}
		}
		return false;
	}
	public User findUserByLogInInfo(String userName , String pass){
		User user ;
		DataBase db = new DataBase();
		users = db.getUsersCollection();
		cursor = users.find().iterator();
		for (int i = 0; i < users.count(); i++) {
			doc = cursor.next();
			if (doc.getString("username").equals(userName) && doc.getString("password").equals(pass)) {
				user= new User(doc.getString("name"), doc.getString("last name"),doc.getString("username"),doc.getString("password"));
			return user;
			}
		}
		return null;

	}

	public void saveReservationToDB(Reservation reservation) {
		reservations = db.getReservationsCollection();
		Date ArrivalDate = Date.from(reservation.getArrivalDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date departureDate = Date.from(reservation.getDepartureDate().atStartOfDay(ZoneId.systemDefault()).toInstant());

		doc = new Document("guest", reservation.getGuest())
				.append("room", reservation.getRoom())
				.append("arrival", ArrivalDate)
				.append("departure", departureDate)
				.append("price", reservation.getPrice())
				.append("is checkedIn", reservation.getCheckedIn());

		reservations.insertOne( doc);
	}

	public void createNewRoom(Room room, DataBase db) {
		db = new DataBase();
		rooms = db.getRoomsColl();
		doc = new Document("room nr" , room.getRoomNr())
				.append("room type", room.getRoomType().toString())
				.append("price",room.getPrice())
				.append("is booked",false)
				.append("city",room.getCity().toString());

		rooms.insertOne(doc);
	}

	public int getRoomNumberByID(ObjectId id){
		rooms = db.getRoomsColl();
		cursor = rooms.find().iterator();
		int roomNr = 0;
		for (int i = 0; i < rooms.count(); i++) {
			doc = cursor.next();
			if (doc.getObjectId("_id").equals(id)){
				roomNr=doc.getInteger("room nr");
			}
		}
		return roomNr;
	}

	public Room copyRoomByRoomNumber(int roomNumber){
		rooms = db.getRoomsColl();
		cursor = rooms.find().iterator();

		Room room = null;	
		for (int i = 0; i < rooms.count(); i++) {
			doc = cursor.next();
			if (doc.getInteger("room nr").equals(roomNumber)){
				room = createRoom(doc);
			}
		}
		return room;
	}

	public void deleteReservationByRoomNumber(int roomNumber){
		reservations = db.getReservationsCollection();
		reservations.deleteOne(eq("room", roomNumber));
	}

	public ArrayList<Reservation> getAllReservations() {
		db = new DataBase();
		reservations = db.getReservationsCollection();
		cursor = reservations.find().iterator();
		ArrayList<Reservation> listOfReservation = new ArrayList<Reservation>();
		Reservation reservation ;
		System.out.println(Integer.parseInt(Year.now().toString()+""));
		
		for (int i = 0; i < reservations.count(); i++) {
			doc = cursor.next();
			System.out.println(doc.getDate("arrival").toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
			LocalDate arrivalDate = doc.getDate("arrival").toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate departureDate = doc.getDate("departure").toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			Double price = doc.getDouble("price");
			int roomID = doc.getInteger("room");
			String guestID = doc.getString("guest");
			reservation = new Reservation(roomID, guestID, arrivalDate, departureDate, price);
			listOfReservation.add(reservation);
			System.out.println(reservation.toString());
		}
		return listOfReservation;
	}

	public ArrayList<Reservation> getReservationByDate(LocalDate date) {
		Date d = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
		db = new DataBase();
		reservations = db.getReservationsCollection();
		cursor = reservations.find().iterator();
		ArrayList<Reservation> listOfReservation = new ArrayList<Reservation>();
		Reservation reservation ;
		System.out.println();

		for (int i = 0; i < reservations.count(); i++) {
			doc = cursor.next();
			if(doc.getDate("arrival").equals(d) && !doc.getBoolean("is checkedIn")){
				reservation= getReservationFromDB(doc);
				listOfReservation.add(reservation);
			}

		}
		return listOfReservation;
	}

	public ArrayList<Reservation> getCheckedInByDate(LocalDate date) {
		Date d = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
		db = new DataBase();
		reservations = db.getReservationsCollection();
		cursor = reservations.find().iterator();
		ArrayList<Reservation> listOfReservation = new ArrayList<Reservation>();
		Reservation reservation ;
		for (int i = 0; i < reservations.count(); i++) {
			doc = cursor.next();
			if(doc.getDate("departure").equals(d) && doc.getBoolean("is checkedIn")){
				reservation= getReservationFromDB(doc);
				listOfReservation.add(reservation);
			}
		}
		return listOfReservation;
	}
	
	// Deletes reservations that has departure date before todays date and unbooks the corresponding rooms
	public void deleteOldReservations() {
		Instant instant = Instant.now().minus(1, ChronoUnit.DAYS);
		Date time = Date.from(instant);
		Bson filter = Filters.lt("departure", time);
		
		reservations = db.getReservationsCollection();
		MongoCursor<Document> reservationCursor = reservations.find().iterator();

		// Unbooks the rooms
		for (int i = 0; i < reservations.count(); i++) {
			doc = reservationCursor.next();

			if(doc.getDate("departure").before(time)){
				int roomID = doc.getInteger("room");
				
				Room tempRoom = copyRoomByRoomNumber(roomID);
				tempRoom.setBooked(false);
				refreshRoomStatus(tempRoom);
			}
		}
		
		// Deletes the reservations
		reservations.deleteMany(filter);


	public void refreshGuestInfo(Guest guest) {

		persons.updateOne(eq("identity nr", guest.getIdentityNr()), new Document("$set",
				new Document("name", guest.getName())
						.append("last name", guest.getLastName())
						.append("phone nr", guest.getPhoneNr())
						.append("address", guest.getAddress())
						.append("credit card", guest.getCreditCard())
						.append("identity nr", guest.getIdentityNr())
						.append("notes", guest.getNotes())));
	}

	public Guest getGuestByName(String guestName) {
		persons = db.getPersonsCollection();
		cursor = persons.find().iterator();

		Guest guest = null;
		for (int i = 0; i < persons.count(); i++) {
			doc = cursor.next();
			if ((doc.getString("name")+" "+doc.getString("last name")).equals(guestName)){
				guest = getGuestFromDB(doc);
			}
		}
		return guest;
	}
	/**
	 *  Method that copy the information of a guest from a document in the
	 *  database and creates a reservation out of it
	 *
	 * @param doc
	 * 			The document that is used to create the guest
	 * @return
	 * 			Guest
	 */
	private Guest getGuestFromDB(Document doc) {
		String name = doc.getString("name");
		String lastName = doc.getString("last name");
		String address = doc.getString("address");
		String phoneNr = doc.getString("phone nr");
		String creditCard = doc.getString("credit card");
		String identity_nr = doc.getString("identity nr");
		String notes = doc.getString("notes");
		Guest guest  = new Guest(name , lastName ,address, phoneNr,creditCard,identity_nr,creditCard, notes);
		return guest;
	}

	/**
	 *  Method that copy the information of a reservation from a document in the
	 *  dataBase and creates a reservation out of it
	 *
	 * @param doc
	 * 			The document that is used to create the reservation
	 * @return
	 * 			Reservation
	 */
	private Reservation getReservationFromDB(Document doc){
		LocalDate arrivalDate = doc.getDate("arrival").toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate departureDate = doc.getDate("departure").toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		Double price = doc.getDouble("price");
		int roomID = doc.getInteger("room");
		String guestID = doc.getString("guest");
		Reservation reservation = new Reservation(roomID, guestID, arrivalDate, departureDate, price);
		return reservation;

	}
}
