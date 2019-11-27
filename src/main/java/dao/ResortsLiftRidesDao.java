package dao;


import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

public class ResortsLiftRidesDao {

    DatabaseConnection dbConn = DatabaseConnection.getInstance();
    MongoCollection<Document> collection = dbConn.getDatabase().getCollection("resorts_lift_rides");


    public int getTotalVertical(Document query, Document fields){
        int totalVertical = 0;
        FindIterable<Document> docs = collection.find(query).projection(fields);
        MongoCursor<Document> cursor = docs.cursor();
        while (cursor.hasNext()) {
            totalVertical+= cursor.next().getInteger("vertical");
        }
        return totalVertical;
    }
    public void createLiftRide(Document resortLiftRideDocument) {
        collection.insertOne(resortLiftRideDocument);
    }
}