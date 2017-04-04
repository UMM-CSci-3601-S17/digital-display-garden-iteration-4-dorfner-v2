package umm3601.plant;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import umm3601.digitalDisplayGarden.PlantController;

import java.io.IOException;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestPlantComment {

    private final static String databaseName = "data-for-testing-only";
    private PlantController plantController;

    @Before
    public void populateDB() throws IOException {
        PopulateMockDatabase db = new PopulateMockDatabase();
        db.clearAndPopulateDBAgain();
        plantController = new PlantController(databaseName);
    }

    @Test
    public void successfulInputOfComment() throws IOException {
        String json = "{ plantId: \"58d1c36efb0cac4e15afd278\", comment : \"Here is our comment for this test\" }";

        assertTrue(plantController.storePlantComment(json, "second uploadId"));

        MongoClient mongoClient = new MongoClient();
        MongoDatabase db = mongoClient.getDatabase(databaseName);
        MongoCollection<Document> commentDocuments = db.getCollection("comments");

        long contains = commentDocuments.count();
        assertEquals(1, contains);

        Iterator<Document> iter = commentDocuments.find().iterator();

        Document fromDb = iter.next();

        assertEquals("Here is our comment for this test", fromDb.getString("comment"));
        assertEquals("16040.0", fromDb.get("commentOnPlant"));
        assertEquals("second uploadId", fromDb.get("uploadId"));
    }

    @Test
    public void failedInputOfComment() throws IOException {
        String json = "{ plantId: \"58d1c36efb0cac4e15afd27\", comment : \"Here is our comment for this test\" }";

        assertFalse(plantController.storePlantComment(json, "second uploadId"));

        MongoClient mongoClient = new MongoClient();
        MongoDatabase db = mongoClient.getDatabase(databaseName);
        MongoCollection<Document> commentDocuments = db.getCollection("comments");

        long contains = commentDocuments.count();
        assertEquals(0, contains);
    }
}
