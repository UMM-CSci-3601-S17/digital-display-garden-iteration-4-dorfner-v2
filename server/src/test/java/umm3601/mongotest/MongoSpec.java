package umm3601.mongotest;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.*;
//import static com.mongodb.client.model.Projections.*;
import com.mongodb.client.model.Sorts;
import org.bson.Document;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Some simple "tests" that demonstrate our ability to
 * connect to a Mongo database and run some basic queries
 * against it.
 *
 * Created by mcphee on 20/2/17.
 */
public class MongoSpec {

    private MongoCollection<Document> userDocuments;

    @Before
    public void clearAndPopulateDB() {
        MongoClient mongoClient = new MongoClient();
        MongoDatabase db = mongoClient.getDatabase("testingdb");
        userDocuments = db.getCollection("users");
        userDocuments.drop();
        List<Document> testUsers = new ArrayList<Document>();
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
        userDocuments.insertMany(testUsers);
    }

    private List<Document> intoList(FindIterable<Document> documents) {
        List<Document> users = new ArrayList<Document>();
        documents.into(users);
        return users;
    }

    private int countUsers(FindIterable<Document> documents) {
        List<Document> users = intoList(documents);
        return users.size();
    }

    @Test
    public void shouldBeThreeUsers() {
        FindIterable<Document> documents = userDocuments.find();
        int numberOfUsers = countUsers(documents);
        assertEquals("Should be 3 total users", 3, numberOfUsers);
    }

    @Test
    public void shouldBeOneChris() {
        FindIterable<Document> documents = userDocuments.find(eq("name", "Chris"));
        int numberOfUsers = countUsers(documents);
        assertEquals("Should be 1 Chris", 1, numberOfUsers);
    }

    @Test
    public void shouldBeTwoOver25() {
        FindIterable<Document> documents = userDocuments.find(gt("age", 25));
        int numberOfUsers = countUsers(documents);
        assertEquals("Should be 2 over 25", 2, numberOfUsers);
    }

    @Test
    public void over25SortedByName() {
        FindIterable<Document> documents
                = userDocuments.find(gt("age", 25))
                    .sort(Sorts.ascending("name"));
        List<Document> docs = intoList(documents);
        assertEquals("Should be 2", 2, docs.size());
        assertEquals("First should be Jamie", "Jamie", docs.get(0).get("name"));
        assertEquals("Second should be Pat", "Pat", docs.get(1).get("name"));
    }

    /*
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

     */

}
