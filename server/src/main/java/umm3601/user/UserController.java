package umm3601.user;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import static com.mongodb.client.model.Filters.eq;

public class UserController {

    private final MongoCollection<Document> userCollection;

    public UserController() throws IOException {
        // Set up our server address
        // (Default host: 'localhost', default port: 27017)
        // ServerAddress testAddress = new ServerAddress();

        // Try connecting to the server
        //MongoClient mongoClient = new MongoClient(testAddress, credentials);
        MongoClient mongoClient = new MongoClient(); // Defaults!

        // Try connecting to a database
        MongoDatabase db = mongoClient.getDatabase("testdb");

        userCollection = db.getCollection("users");
    }

    // List users
    public String listUsers(Map<String, String[]> queryParams) {
        Document filterDoc = new Document();

        if (queryParams.containsKey("age")) {
            int targetAge = Integer.parseInt(queryParams.get("age")[0]);
            filterDoc = filterDoc.append("age", targetAge);
        }

        FindIterable<Document> matchingUsers = userCollection.find(filterDoc);

        return JSON.serialize(matchingUsers);
    }

    // Get a single user
    public String getUser(String id) {
        FindIterable<Document> jsonUsers
                = userCollection
                    .find(eq("_id", new ObjectId(id)));

        Iterator<Document> iterator = jsonUsers.iterator();

        Document user = iterator.next();

        return user.toJson();
    }

}