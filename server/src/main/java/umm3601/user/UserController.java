package umm3601.user;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.util.JSON;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

public class UserController {

    private final MongoDatabase db;

    public UserController() throws IOException {
        // Set up our server address
        // (Default host: 'localhost', default port: 27017)
        ServerAddress testAddress = new ServerAddress();

        // Try connecting to the server
        //MongoClient mongoClient = new MongoClient(testAddress, credentials);
        MongoClient mongoClient = new MongoClient(); // Defaults!

        // Try connecting to a database
        db = mongoClient.getDatabase("testdb");
    }

    // List users
    public String listUsers(Map<String, String[]> queryParams) {
        MongoCollection<Document> documents = db.getCollection("users");

        Document filterDoc = new Document();

        if (queryParams.containsKey("age")) {
            int targetAge = Integer.parseInt(queryParams.get("age")[0]);
            filterDoc = filterDoc.append("age", targetAge);
        }

        FindIterable<Document> jsonUsers = documents.find(filterDoc);

        JSON json = new JSON();

        String serialize = json.serialize(jsonUsers);

        return serialize;
    }

    // Get a single user
    public String getUser(String id) {
        FindIterable<Document> jsonUsers
                = db.getCollection("users")
                    .find(eq("_id", new ObjectId(id)));

        Iterator<Document> iterator = jsonUsers.iterator();

        Document user = iterator.next();

        return user.toJson();
    }

}