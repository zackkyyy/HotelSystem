package controller.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import model.Guest;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by IntelliJ IDEA.
 *
 * @User: Zacky Kharboutli
 * @Date: 2018-04-13
 * @Project : HotelSystem
 */


public class guestController {

    private Document doc;
    private MongoCollection persons;
    private MongoCursor <Document> cursor;

    public void createNewGuest(Guest newGuest , DataBase db) {
        db= new DataBase();
        doc = new Document();
        persons=db.getPersonsCollection();
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
            newGuest.setId(""+doc.getObjectId("_id"));

            //  final Node source = (Node) e.getSource();
        }catch (Exception e){
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
//          display the error message
            System.out.println("Failed to save");
        }
        System.out.println("Guest with id "+newGuest.getId() +" is now created");
    }
    public String [] getGuestNames( DataBase db){
        persons=db.getPersonsCollection();
        String [] listOfGuest = new String[(int) persons.count()];

        MongoCursor<Document> cursor = persons.find().iterator();

        for (int i = 0; i < persons.count(); i++) {
            Document doc = cursor.next();
            String name = doc.getString("name");
            String lastname = doc.getString("last name");
            listOfGuest[i]=(name + " "+lastname);
        }
        return listOfGuest;
    }

    public void editGuest(DataBase db, Object selectedItem , Guest guest) throws ParseException {
        persons=db.getPersonsCollection();
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
}
