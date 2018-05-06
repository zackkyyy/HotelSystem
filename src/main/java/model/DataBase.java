package model;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author: Zacky Kharboutli
 * @Date: 2018-04-09
 * @Project : HotelSystem
 */
public class DataBase {
	MongoClientURI clientURI;
	MongoClient mongoClient;
	MongoDatabase database;
	String uri;
	MongoCollection persons , userColl ,roomsColl,reservations;
	Document doc;

	public DataBase() {
		/*    ******OLD CONNECTION SET UP IN CASE ******* uncomment if needed
		uri = "mongodb://localhost:27017,localhost:27017/replicaSet=hotelSystem";
		clientURI = new MongoClientURI(uri);
		mongoClient = new MongoClient(clientURI);
		database = mongoClient.getDatabase("hotelSystem");
		persons = database.getCollection("persons");
		roomsColl = database.getCollection("rooms");
		userColl= database.getCollection("users");
		reservations=database.getCollection("reservations");
		 */

		try {
			uri = "mongodb://zacky:group15@hotelmanagerdb-shard-00-00-nxz5u.mongodb.net:27017,hotelmanagerdb-shard-00-01-nxz5u.mongodb.net:27017,hotelmanagerdb-shard-00-02-nxz5u.mongodb.net:27017/test?ssl=true&replicaSet=HotelManagerDB-shard-0&authSource=admin";
			clientURI = new MongoClientURI(uri);
			mongoClient = new MongoClient(clientURI);
			database = mongoClient.getDatabase("hotelSystem");
			persons = database.getCollection("persons");
			roomsColl = database.getCollection("rooms");
			userColl = database.getCollection("users");
			reservations = database.getCollection("reservations");
		}catch (Exception e){
			System.err.println(e);
		}
	}

	public MongoCollection getPersonsCollection() {
		return persons;
	}

	public MongoCollection getRoomsColl() {
		return roomsColl;
	}

	public MongoCollection getUsersCollection() {
		return userColl;
	}

	public MongoCollection getReservationsCollection(){return reservations;}
}
