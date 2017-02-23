package umm3601.user;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import org.bson.*;
import org.bson.codecs.*;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.json.JsonReader;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

/**
 * JUnit tests for the UserController.
 *
 * Created by mcphee on 22/2/17.
 */
public class UserControllerSpec
{
    private UserController userController;
    private String samsIdString;

    @Before
    public void clearAndPopulateDB() throws IOException {
        MongoClient mongoClient = new MongoClient();
        MongoDatabase db = mongoClient.getDatabase("test");
        MongoCollection<Document> userDocuments = db.getCollection("users");
        userDocuments.drop();
        List<Document> testUsers = new ArrayList<>();
        testUsers.add(Document.parse("{\n" +
                "                    name: \"Chris\",\n" +
                "                    age: 25,\n" +
                "                    company: \"UMM\",\n" +
                "                    email: \"chris@this.that\"\n" +
                "                }"));
        testUsers.add(Document.parse("{\n" +
                "                    name: \"Pat\",\n" +
                "                    age: 37,\n" +
                "                    company: \"IBM\",\n" +
                "                    email: \"pat@something.com\"\n" +
                "                }"));
        testUsers.add(Document.parse("{\n" +
                "                    name: \"Jamie\",\n" +
                "                    age: 37,\n" +
                "                    company: \"Frogs, Inc.\",\n" +
                "                    email: \"jamie@frogs.com\"\n" +
                "                }"));
        Document sam = Document.parse("{\n" +
                "                    name: \"Sam\",\n" +
                "                    age: 45,\n" +
                "                    company: \"Frogs, Inc.\",\n" +
                "                    email: \"sam@frogs.com\"\n" +
                "                }");
        ObjectId samsId = new ObjectId();
        samsIdString = samsId.toHexString();
        System.out.println(samsIdString);
        sam.put("_id", JSON.serialize(samsId));
        System.out.println(sam);
        testUsers.add(sam);
        System.out.println(testUsers);
        userDocuments.insertMany(testUsers);

        // It might be important to construct this _after_ the DB is set up
        // in case there are bits in the constructor that care about the state
        // of the database.
        userController = new UserController();
    }

    // http://stackoverflow.com/questions/34436952/json-parse-equivalent-in-mongo-driver-3-x-for-java
    private BsonArray parseJsonArray(String json) {
        final CodecRegistry codecRegistry
                = CodecRegistries.fromProviders(Arrays.asList(
                new ValueCodecProvider(),
                new BsonValueCodecProvider(),
                new DocumentCodecProvider()));

        JsonReader reader = new JsonReader(json);
        BsonArrayCodec arrayReader = new BsonArrayCodec(codecRegistry);

        return arrayReader.decode(reader, DecoderContext.builder().build());
    }

    private static String getName(BsonValue val) {
        BsonDocument doc = val.asDocument();
        return ((BsonString) doc.get("name")).getValue();
    }

    @Test
    public void getAllUsers() {
        Map<String, String[]> emptyMap = new HashMap<>();
        String jsonResult = userController.listUsers(emptyMap);
        System.out.println(jsonResult);
        BsonArray docs = parseJsonArray(jsonResult);
        System.out.println(docs);

        assertEquals("Should be 4 users", 4, docs.size());
        List<String> names = docs
                .stream()
                .map(UserControllerSpec::getName)
                .sorted()
                .collect(Collectors.toList());
        List<String> expectedNames = Arrays.asList("Chris", "Jamie", "Pat", "Sam");
        assertEquals("Names should match", expectedNames, names);
    }

    @Test
    public void getUsersWhoAre37() {
        Map<String, String[]> argMap = new HashMap<>();
        argMap.put("age", new String[] { "37" });
        String jsonResult = userController.listUsers(argMap);
        System.out.println(jsonResult);
        BsonArray docs = parseJsonArray(jsonResult);
        System.out.println(docs);

        assertEquals("Should be 2 users", 2, docs.size());
        List<String> names = docs
                .stream()
                .map(UserControllerSpec::getName)
                .sorted()
                .collect(Collectors.toList());
        List<String> expectedNames = Arrays.asList("Jamie", "Pat");
        assertEquals("Names should match", expectedNames, names);
    }

    @Test
    public void getSamById() {
        String jsonResult = userController.getUser(samsIdString);
        System.out.println(jsonResult);
        Document sam = Document.parse(jsonResult);
        System.out.println(sam);

        assertEquals("Name should match", "Sam", sam.get("name"));
    }
}
