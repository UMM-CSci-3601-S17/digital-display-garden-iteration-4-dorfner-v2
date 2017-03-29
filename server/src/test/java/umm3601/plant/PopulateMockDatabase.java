package umm3601.plant;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.*;
import org.bson.types.ObjectId;
import org.junit.Before;
import umm3601.digitalDisplayGarden.PlantController;

import javax.print.Doc;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class PopulateMockDatabase {

    private final static String databaseName = "data-for-testing-only";

    public PopulateMockDatabase(){

    }

    private PlantController plantController;
    private String begoniaIdString;
    public String hexAlternantheraID;

    public void clearAndPopulateDB() throws IOException {
    }

    public void clearAndPopulateDBAgain() throws IOException {
        MongoClient mongoClient = new MongoClient();
        MongoDatabase db = mongoClient.getDatabase(databaseName);
        MongoCollection plants = db.getCollection("plants");
        db.drop();

        //First Plant Alternanthera
        Document alternanthera = new Document();
        alternanthera.append("_id", new ObjectId("58d1c36efb0cac4e15afd202"));
        alternanthera.append("uploadId", "first uploadId");

        alternanthera.append("commonName", "Alternanthera");
        alternanthera.append("cultivar", "Experimental");
        alternanthera.append("gardenLocation", "10.0");
        alternanthera.append("Comments", "Name change from Purple Prince   14x18 spreader");
        alternanthera.append("HBHangBasketCContainerWWall", "");
        alternanthera.append("id", "16001.0");
        alternanthera.append("source", "PA");
        alternanthera.append("SSeedVVeg", "S");

        Document metadataDoc = new Document();
        metadataDoc.append("pageViews", 0);
        metadataDoc.append("ratings", new BsonArray());
        
        alternanthera.append("metadata", metadataDoc);
        //alternanthera.append("garden", "hello!");
        plants.insertOne(alternanthera );

        //Second Plant Begonia
        Document begonia = new Document();
        begonia.append("_id", new ObjectId("58d1c36efb0cac4e15afd203"));
        begonia.append("uploadId", "first uploadId");

        begonia.append("commonName", "Begonia");
        begonia.append("cultivar", "Megawatt Rose Green Leaf");
        begonia.append("gardenLocation", "10.0");
        begonia.append("Comments", "Grow in same sun or shade area; grow close proximity to each other for comparison");
        begonia.append("HBHangBasketCContainerWWall", "");
        begonia.append("id", "16008.0");
        begonia.append("source", "PA");
        begonia.append("SSeedVVeg", "S");

        Document metadataDoc1 = new Document();
        metadataDoc1.append("pageViews", 0);
        metadataDoc1.append("ratings", new BsonArray());

        begonia.append("metadata", metadataDoc1);
        plants.insertOne(begonia);

        //Third Plant Dianthus
        Document dianthus = new Document();
        dianthus.append("_id", new ObjectId("58d1c36efb0cac4e15afd204"));
        dianthus.append("uploadId", "second uploadId");

        dianthus.append("commonName", "Dianthus");
        dianthus.append("cultivar", "Jolt™ Pink F1");
        dianthus.append("gardenLocation", "7.0");
        dianthus.append("Comments", "");
        dianthus.append("HBHangBasketCContainerWWall", "");
        dianthus.append("id", "16040.0");
        dianthus.append("source", "AAS");
        dianthus.append("SSeedVVeg", "S");

        Document metadataDoc2 = new Document();
        metadataDoc2.append("pageViews", 0);
        metadataDoc2.append("ratings", new BsonArray());

        dianthus.append("metadata", metadataDoc2);
        plants.insertOne(dianthus);

        //Fourth Plant PlantFour
        Document plantFour = new Document();
        plantFour.append("_id", new ObjectId("58d1c36efb0cac4e15afd278"));
        plantFour.append("uploadId", "second uploadId");

        plantFour.append("commonName", "PlantFour");
        plantFour.append("cultivar", "Some Plant");
        plantFour.append("gardenLocation", "12");
        plantFour.append("id", "16040.0");

        Document metadataDoc3 = new Document();
        metadataDoc3.append("pageViews", 0);
        metadataDoc3.append("ratings", new BsonArray());

        plantFour.append("metadata", metadataDoc3);
        plants.insertOne(plantFour);
    }
}




