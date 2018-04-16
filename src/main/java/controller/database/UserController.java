package controller.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import model.User;
import org.bson.Document;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by IntelliJ IDEA.
 *
 * @User: Zacky Kharboutli
 * @Date: 2018-04-16
 * @Project : HotelSystem
 */


public class UserController {

    private Document doc;
    private MongoCollection users;
    private MongoCursor<Document> cursor;
    public UserController() {

    }


    public Object[] getUserNames(DataBase db) {
        users=db.getUsersCollection();
        String [] listOfGuest = new String[(int) users.count()];

        MongoCursor<Document> cursor = users.find().iterator();
        for (int i = 0; i < users.count(); i++) {
            Document doc = cursor.next();
            listOfGuest[i]=(doc.getString("name") + " "+doc.getString("last name"));
        }
        return listOfGuest;
    }

    public void editUser(DataBase db, Object selectedItem, User temporaryUser) {
        users=db.getUsersCollection();
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
        users=db.getUsersCollection();
        try {

            doc = new Document("name", newUser.getName())
                    .append("last name", newUser.getLastName())
                    .append("password", newUser.getPassword())
                    .append("username", newUser.getUserName());

            users.insertOne(doc);
        }catch (Exception e){
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
            System.out.println("Failed to save");
        }
        System.out.println("Guest with id "+newUser.getName() +" is now created");
    }

    public Boolean validateLogging(String username , String password){
        DataBase db= new DataBase();
        users=db.getUsersCollection();
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
}
