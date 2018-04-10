package controller;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * Created by IntelliJ IDEA.
 *
 * @User: Zacky Kharboutli
 * @Date: 2018-04-09
 * @Project : HotelSystem
 */


public class DataBase {
    MongoClientURI clientURI;
    MongoClient mongoClient;
    MongoDatabase database;
    String uri;
    MongoCollection persons;

    public DataBase() {
        uri = "mongodb://localhost:27017,localhost:27017/replicaSet=hotelSystem";
        clientURI = new MongoClientURI(uri);
        mongoClient = new MongoClient(clientURI);

        database = mongoClient.getDatabase("hotelSystem");
        persons = database.getCollection("persons");

    }

    public MongoCollection getPersonsCollection() {
        return persons;
    }

    public MongoDatabase getDatabase() {
        return database;
    }
}