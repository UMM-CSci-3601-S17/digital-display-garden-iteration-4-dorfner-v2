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

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class FlowerRating {

    @Before
    public void populateDB() throws IOException {
        PopulateMockDatabase db = new PopulateMockDatabase();
        db.clearAndPopulateDBAgain();
    }

    @Test
    public void AddFlowerRatingReturnsTrueWithValidInput() throws IOException{
        PlantController plantController = new PlantController();

        assertTrue(plantController.addFlowerRating("58d1c36efb0cac4e15afd202", true));

        MongoClient mongoClient = new MongoClient();
        MongoDatabase db = mongoClient.getDatabase("test");
        MongoCollection plants = db.getCollection("plants");

        FindIterable<Document> doc = plants.find(new Document().append("_id", new ObjectId("58d1c36efb0cac4e15afd202")));
        Iterator<Document> iterator = doc.iterator();
        Document result = iterator.next();

        List<Document> ratings = (List<Document>) ((Document) result.get("metadata")).get("ratings");
        assertEquals(1, ratings.size());

        Document rating = ratings.get(0);
        assertTrue(rating.getBoolean("like"));
        assertEquals(new ObjectId("58d1c36efb0cac4e15afd202"),rating.get("ratingOnObjectOfId"));
    }

    @Test
    public void AddFlowerRatingReturnsFalseWithInvalidInput() throws IOException {
        PlantController plantController = new PlantController();

        assertFalse(plantController.addFlowerRating("jfd;laj;asjfoisaf", true));
        assertFalse(plantController.addFlowerRating("58d1c36efb0cac4e15afd201", true));
    }

    @Test
    public void AddFlowerRatingReturnsTrueWithValidJsonInput() throws IOException{
        PlantController plantController = new PlantController();

        String json = "{like: true, id: \"58d1c36efb0cac4e15afd202\"}";

        assertTrue(plantController.addFlowerRating(json));

        MongoClient mongoClient = new MongoClient();
        MongoDatabase db = mongoClient.getDatabase("test");
        MongoCollection plants = db.getCollection("plants");

        FindIterable<Document> doc = plants.find(new Document().append("_id", new ObjectId("58d1c36efb0cac4e15afd202")));
        Iterator<Document> iterator = doc.iterator();
        Document result = iterator.next();

        List<Document> ratings = (List<Document>) ((Document) result.get("metadata")).get("ratings");
        assertEquals(1, ratings.size());

        Document rating = ratings.get(0);
        assertTrue(rating.getBoolean("like"));
        assertEquals(new ObjectId("58d1c36efb0cac4e15afd202"),rating.get("ratingOnObjectOfId"));
    }

    @Test
    public void AddFlowerRatingReturnsFalseWithInvalidJsonInput() throws IOException {
        PlantController plantController = new PlantController();

        String json1 = "{like: true, id: \"dkjahfjafhlkasjdf\"}";
        String json2 = "{like: true id: \"58d1c36efb0cac4e15afd201\"}";

        assertFalse(plantController.addFlowerRating(json1));
        assertFalse(plantController.addFlowerRating(json2));
    }
}
