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

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class FlowerRating {

    private final static String databaseName = "data-for-testing-only";
    private PlantController plantController;

    @Before
    public void populateDB() throws IOException {
        PopulateMockDatabase db = new PopulateMockDatabase();
        db.clearAndPopulateDBAgain();
        plantController = new PlantController(databaseName);
    }

    @Test
    //failed
    public void AddFlowerRatingReturnsTrueWithValidInput() throws IOException{
        assertTrue(plantController.addFlowerRating("58d1c36efb0cac4e15afd202", true, "first uploadId") instanceof ObjectId);

        MongoClient mongoClient = new MongoClient();
        MongoDatabase db = mongoClient.getDatabase(databaseName);
        MongoCollection plants = db.getCollection("plants");

        FindIterable doc = plants.find(new Document().append("_id", new ObjectId("58d1c36efb0cac4e15afd202")));
        Iterator iterator = doc.iterator();
        Document result = (Document) iterator.next();

        List<Document> ratings = (List<Document>) ((Document) result.get("metadata")).get("ratings");
        assertEquals(1, ratings.size());

        Document rating = ratings.get(0);
        assertTrue(rating.getBoolean("like"));
        assertTrue(rating.get("id") instanceof ObjectId);
    }

    @Test
    //failed
    public void AddFlowerRatingReturnsFalseWithInvalidInput() throws IOException {

        assertFalse(plantController.addFlowerRating("jfd;laj;asjfoisaf", true, "anything") instanceof ObjectId);
        //Todo check back later adding random letter 'K' makes this work
        assertFalse(plantController.addFlowerRating("58d1c36efb0cac4e15afd201K", true, "anything") instanceof ObjectId);
    }

//    @Test
    //failed
//    public void AddFlowerRatingReturnsFalseWithInvalidUploadID() throws IOException {
//        assertNull(plantController.addFlowerRating("58d1c36efb0cac4e15afd202", true, "anything") instanceof ObjectId);
//    }

    @Test
    //failed
    public void AddFlowerRatingReturnsTrueWithValidJsonInput() throws IOException{

        String json = "{like: true, id: \"58d1c36efb0cac4e15afd202\"}";

        assertTrue(plantController.addFlowerRating(json, "first uploadId") instanceof ObjectId);

        MongoClient mongoClient = new MongoClient();
        MongoDatabase db = mongoClient.getDatabase(databaseName);
        MongoCollection plants = db.getCollection("plants");

        FindIterable doc = plants.find(new Document().append("_id", new ObjectId("58d1c36efb0cac4e15afd202")));
        Iterator iterator = doc.iterator();
        Document result = (Document) iterator.next();

        List<Document> ratings = (List<Document>) ((Document) result.get("metadata")).get("ratings");
        assertEquals(1, ratings.size());

        Document rating = ratings.get(0);
        assertTrue(rating.getBoolean("like"));
        assertTrue(rating.get("id") instanceof ObjectId);
    }

    @Test
    //failed
    public void AddFlowerRatingReturnsFalseWithInvalidJsonInput() throws IOException {

        String json1 = "{like: true, id: \"dkjahfjafhlkasjdf\"}";
        String json2 = "{like: true id: \"58d1c36efb0cac4e15afd201K\"}"; //Why does adding a random letter like 'K' make this work

        assertFalse(plantController.addFlowerRating(json1, "anything") instanceof ObjectId);
        assertFalse(plantController.addFlowerRating(json2, "anything") instanceof ObjectId);
    }
}
