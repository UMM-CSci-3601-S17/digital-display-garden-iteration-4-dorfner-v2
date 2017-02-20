package umm3601.mongotest;

/*
 * Inspired by:
 * http://mongodb.github.io/mongo-java-driver/3.4/driver/getting-started/quick-start/
 *
 */

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;

import com.mongodb.client.model.Sorts;
import org.bson.Document;

public class Mongotest {

    public void connectToMongo() {
        Block<Document> printBlock = new Block<Document>() {
            @Override
            public void apply(final Document document) {
                System.out.println(document.toJson());
            }
        };

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

        // Try connecting to a database
        MongoDatabase db = mongoClient.getDatabase("testdb");
        System.out.println("Connection to database successful");

        MongoCollection<Document> documents = db.getCollection("users");

        System.out.println("EVERYONE");
        documents.find().forEach(printBlock);

        System.out.println("\nJUST CHRIS");
        documents.find(eq("name", "Chris")).forEach(printBlock);

        System.out.println("\nOVER 25");
        documents.find(gt("age", 25)).forEach(printBlock);

        System.out.println("\nOVER 25, SORTED BY NAME");
        documents.find(gt("age", 25)).sort(Sorts.ascending("name")).forEach(printBlock);

        System.out.println("\nOVER 25 and IBMers");
        documents.find(and(gt("age", 25), eq("company", "IBM"))).forEach(printBlock);

        System.out.println("\nJUST NAME AND EMAIL");
        documents.find().projection(fields(include("name", "email"))).forEach(printBlock);

        System.out.println("\nJUST NAME AND EMAIL, NO IDs");
        documents.find().projection(fields(include("name", "email"), excludeId())).forEach(printBlock);

        System.out.println("\nJUST NAME AND EMAIL, NO IDs, SORTED BY COMPANY");
        documents.find()
                .sort(Sorts.ascending("company"))
                .projection(fields(include("name", "email"), excludeId()))
                .forEach(printBlock);
    }
}
