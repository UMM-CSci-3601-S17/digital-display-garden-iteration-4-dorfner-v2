package umm3601.plant;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import umm3601.digitalDisplayGarden.PlantController;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

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

        assertTrue(plantController.addComment(json, "second uploadId"));

        MongoClient mongoClient = new MongoClient();
        MongoDatabase db = mongoClient.getDatabase(databaseName);
        MongoCollection<Document> plants = db.getCollection("plants");

        Document filterDoc = new Document();

        filterDoc.append("_id", new ObjectId("58d1c36efb0cac4e15afd278"));
        filterDoc.append("uploadId", "second uploadId");

        Iterator<Document> iter = plants.find(filterDoc).iterator();

        Document plant = iter.next();
        List<Document> plantComments = (List<Document>) ((Document) plant.get("metadata")).get("comments");
        long comments = plantComments.size();

        assertEquals(1, comments);
        assertEquals("Here is our comment for this test", plantComments.get(0).getString("comment"));
        assertNotNull(plantComments.get(0).getObjectId("_id"));
      }

    @Test
    public void failedInputOfComment() throws IOException {
        String json = "{ plantId: \"58d1c36efb0cac4e15afd27\", comment : \"Here is our comment for this test\" }";

        assertFalse(plantController.addComment(json, "second uploadId"));

        MongoClient mongoClient = new MongoClient();
        MongoDatabase db = mongoClient.getDatabase(databaseName);
        MongoCollection<Document> plants = db.getCollection("plants");

        FindIterable findIterable = plants.find();
        Iterator iterator = findIterable.iterator();
        while(iterator.hasNext()){
            Document plant = (Document) iterator.next();
            List<Document> plantComments = (List<Document>) ((Document) plant.get("metadata")).get("comments");
            assertEquals(0,plantComments.size());
        }
    }
}
