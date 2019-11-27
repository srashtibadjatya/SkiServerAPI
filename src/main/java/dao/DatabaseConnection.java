package dao;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class DatabaseConnection {

    private MongoClient mongoClient;
    private static final String MONGO_URI = System.getenv("MONGO_URI");
    private static final String DATABASE_NAME = System.getenv("DATABASE_NAME");

    /** Instance of a DatabaseConnection for MongoDB */
    private static DatabaseConnection connectionInstance = new DatabaseConnection();

    /**
     * Constructor for a 'DatabaseConnection that will also instantiate the MongoClient when called.
     */
    private DatabaseConnection() {
         mongoClient = MongoClients.create(MONGO_URI);
    }

    /**
     * Creates a new DatabaseConnection if one does not exist, or returns the current instance of a DatabaseConnection.
     * This follows the Singleton pattern.
     * @return a DatabaseConnection instance
     */
    public static DatabaseConnection getInstance() {
        if (connectionInstance == null) {
            connectionInstance = new DatabaseConnection();
        }
        return connectionInstance;
    }

    /**
     * @return a MongoClient instance
     */
    public MongoClient getMongoClient(){
        return connectionInstance.mongoClient;
    }

    /**
     * @return a MongoDatabase instance
     */
    public MongoDatabase getDatabase(){
        return mongoClient.getDatabase(DATABASE_NAME);
    }
}
