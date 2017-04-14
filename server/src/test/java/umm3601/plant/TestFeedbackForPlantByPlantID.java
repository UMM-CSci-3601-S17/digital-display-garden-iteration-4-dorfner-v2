package umm3601.plant;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.*;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import umm3601.digitalDisplayGarden.PlantController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class TestFeedbackForPlantByPlantID {
    private final static String databaseName = "data-for-testing-only";
    private PlantController plantController;
    MongoClient mongoClient;
    MongoDatabase currentDB;
    MongoCollection plants;
    BsonElement like = new BsonElement("like", BsonBoolean.TRUE);
    BsonElement dislike = new BsonElement("like", BsonBoolean.FALSE);

    @Before
    public void populateDB() throws IOException {
        PopulateMockDatabase db = new PopulateMockDatabase();
        db.clearAndPopulateDBAgain();
        plantController = new PlantController(databaseName);
        mongoClient = new MongoClient();
        currentDB = mongoClient.getDatabase(databaseName);
        plants = currentDB.getCollection("plants");
    }

    @Test
    public void oneLikeTest() throws IOException {
        BsonElement rateID = new BsonElement("ratingOnObjectOfId",
                new BsonObjectId(new ObjectId("58d1c36efb0cac4e15afd202")));

        Document metadataDoc = new Document();
        List<BsonElement> rateInfo = new ArrayList<>();
        rateInfo.add(like);
        rateInfo.add(rateID);
        BsonDocument rate = new BsonDocument(rateInfo);
        List<BsonValue> rateWrapper = new ArrayList<>();
        rateWrapper.add(rate);
        BsonArray ratings = new BsonArray(rateWrapper);

        metadataDoc.append("pageViews", 0);
        metadataDoc.append("ratings", ratings);
        metadataDoc.append("comments", new BsonArray());

        Document searchQuery = new Document("_id", new ObjectId("58d1c36efb0cac4e15afd202"));
        Document updateThingy = new Document("$set", new Document("metadata", metadataDoc));

        plants.updateOne(searchQuery, updateThingy);

        String expect1 = "{ \"interactionCount\" : 1}";
        String plant1 = plantController.getFeedbackForPlantByPlantID("16001.0", "first uploadId");

        assertEquals("Incorrect interaction count for plant 16001.0", expect1, plant1);

    }

    @Test
    public void likeAndDislikeTest() throws IOException {
        BsonElement rateID = new BsonElement("ratingOnObjectOfId",
                new BsonObjectId(new ObjectId("58d1c36efb0cac4e15afd203")));

        Document metadataDoc = new Document();
        List<BsonElement> rateInfo = new ArrayList<>();
        List<BsonValue> rateWrapper = new ArrayList<>();
        rateInfo.add(like);
        rateInfo.add(rateID);
        BsonDocument rate = new BsonDocument(rateInfo);
        rateWrapper.add(rate);

        rateInfo = new ArrayList<>();
        rateInfo.add(dislike);
        rateInfo.add(rateID);
        rate = new BsonDocument(rateInfo);
        rateWrapper.add(rate);
        BsonArray ratings = new BsonArray(rateWrapper);

        metadataDoc.append("pageViews", 0);
        metadataDoc.append("ratings", ratings);
        metadataDoc.append("comments", new BsonArray());

        Document searchQuery = new Document("_id", new ObjectId("58d1c36efb0cac4e15afd203"));
        Document updateThingy = new Document("$set", new Document("metadata", metadataDoc));
//        updateThingy.append("$set", new Document().append("metadata", metadataDoc));

        plants.updateOne(searchQuery, updateThingy);

        String expect = "{ \"interactionCount\" : 2}";
        String plant = plantController.getFeedbackForPlantByPlantID("16008.0", "first uploadId");

        assertEquals("Incorrect interaction count for plant 16008.0", expect, plant);

    }
}
