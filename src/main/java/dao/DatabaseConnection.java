package dao;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class DatabaseConnection implements ServletContextListener {

    private static MongoClient mongoClient;
    private static final String MONGO_URI = System.getenv("MONGO_URI");

    static {
        mongoClient = MongoClients.create(MONGO_URI);
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            mongoClient.close();
        } catch (Exception e) {
            System.out.println("Exception occurred ::: "+  e.getLocalizedMessage() +" - in class " +
                    e.getStackTrace()[0].getClassName()+
                    "in method " + e.getStackTrace()[0].getMethodName()
                    +" at line " + e.getStackTrace()[0].getLineNumber()+".");
        }
    }

    /**
     * @return a MongoClient instance
     */
    public static MongoCollection<Document> getCollection(String DBName, String collectionName){
        return mongoClient.getDatabase(DBName).getCollection(collectionName);
    }

}
