package dao;


import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

public class ResortsLiftRidesDao {

    private final String DB_NAME = "upic";
    private final String COLLECTION_NAME = "resorts_lift_rides";

    public int getTotalVertical(Document query, Document fields){
        int totalVertical = 0;
        MongoCollection<Document> collection = DatabaseConnection.getCollection(DB_NAME, COLLECTION_NAME);
        FindIterable<Document> docs = collection.find(query).projection(fields);
        MongoCursor<Document> cursor = docs.cursor();
        while (cursor.hasNext()) {
            totalVertical+= cursor.next().getInteger("vertical");
        }
        return totalVertical;
    }
    public void createLiftRide(Document resortLiftRideDocument) {
        DatabaseConnection.getCollection(DB_NAME, COLLECTION_NAME).insertOne(resortLiftRideDocument);
    }
}