package umm3601.mongotest;

/*
 * Inspired by:
 * http://mongodb.github.io/mongo-java-driver/3.4/driver/getting-started/quick-start/
 *
 */

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.MongoException;

import com.mongodb.ServerAddress;
import java.util.ArrayList;

public class Mongotest {

    public void connectToMongo() {
        try {

            //// Set up our authentication credentials
            //// Thanks to: http://stackoverflow.com/a/21860551
            /*
            List<MongoCredential> credentials = new ArrayList<MongoCredentials>();
            credentials.add(
                MongoCredential.createMongoCRCredential(
                   "test_user", // Username
                   "testdb", // Database we want to authenticate for
                   "Super@wes0mePa55w0rd".toCharArray()

                )
            );
            */

            // Set up our server address
            // (Default host: 'localhost', default port: 27017)
            ServerAddress testAddress = new ServerAddress();

            // Try connecting to the server
            //MongoClient mongoClient = new MongoClient(testAddress, credentials);
            MongoClient mongoClient = new MongoClient(); // Defaults!
            System.out.println("Connection to database successful");

            // Try connecting to a database
            MongoDatabase db = mongoClient.getDatabase("testdb");

        }catch(Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }
}
