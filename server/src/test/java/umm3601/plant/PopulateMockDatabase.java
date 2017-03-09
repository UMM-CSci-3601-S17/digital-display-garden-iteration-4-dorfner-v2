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

    public PopulateMockDatabase(){

    }

    private PlantController plantController;
    private String begoniaIdString;
    public String hexAlternantheraID;

    public void clearAndPopulateDB() throws IOException {
//        MongoClient mongoClient = new MongoClient();
//        MongoDatabase db = mongoClient.getDatabase("test");
//        MongoCollection<Document> plantDocuments = db.getCollection("plants");
//        plantDocuments.drop();
//        List<Document> testPlants = new ArrayList<>();
//        testPlants.add(Document.parse("{\n" +
//                "                    commonName: \"Angelonia\",\n" +
//                "                    cultivar: \"Angelface® Perfectly Pink,\n" +
//                "                    gardenLocation: \"1S\",\n" +
//                "                    Comments: \"20x12\", \n" +
//                "                    S=SeedV=Veg: \"V\", \n" +
//                "                    id: \"16280.0\", \n" +
//                "                    source: \"PW\", \n" +
//                "                    HB=Hang BasketC=ContainerW=Wall: \"\", \n" +
//                "                }"));
//        testPlants.add(Document.parse("{\n" +
//                "                    commonName: \"Angelonia\",\n" +
//                "                    cultivar: \"Angelface® Super Blue,\n" +
//                "                    gardenLocation: \"1S\",\n" +
//                "                    Comments: \"\", \n" +
//                "                    S=SeedV=Veg: \"V\", \n" +
//                "                    id: \"16281.0\", \n" +
//                "                    source: \"PW\", \n" +
//                "                    HB=Hang BasketC=ContainerW=Wall: \"\", \n"+
//                "                }"));
//        testPlants.add(Document.parse("{\n" +
//                "                    commonName: \"Angelonia\",\n" +
//                "                    cultivar: \"Angelface® Super Pink,\n" +
//                "                    gardenLocation: \"1S\",\n" +
//                "                    Comments: \"\", \n" +
//                "                    S=SeedV=Veg: \"V\", \n" +
//                "                    id: \"16282.0\", \n" +
//                "                    source: \"PW\", \n" +
//                "                    HB=Hang BasketC=ContainerW=Wall: \"\", \n"+
//                "                }"));
//        ObjectId begoniaId = new ObjectId("58bc8252a84aab6cbed02cea");
//        BasicDBObject begonia = new BasicDBObject("_id", begoniaId);
//        begonia = begonia.append("commonName", "Begonia")
//                .append("cultivar", "Bossa Nova Ivory")
//                .append("gardenLocation", "")
//                .append("Comments", "Container only")
//                .append("S=SeedV=Veg", "V")
//                .append("id", "16235.0")
//                .append("source", "FL")
//                .append("HB=Hang BasketC=ContainerW=Wall", "");
//        begoniaIdString = begoniaId.toHexString();
//        plantDocuments.insertMany(testPlants);
//        plantDocuments.insertOne(Document.parse(begonia.toJson()));
//
//        // It might be important to construct this _after_ the DB is set up
//        // in case there are bits in the constructor that care about the state
//        // of the database.
//        plantController = new PlantController();
    }

    public void clearAndPopulateDBAgain() throws IOException {
        MongoClient mongoClient = new MongoClient();
        MongoDatabase db = mongoClient.getDatabase("test");
        MongoCollection plants = db.getCollection("plants");
        db.drop();

        //First Plant Alternanthera
        Map<String, String > alternanthera = new HashMap<>();
        alternanthera.put("commonName", "Alternanthera");
        alternanthera.put("cultivar", "Experimental");
        alternanthera.put("gardenLocation", "10.0");
        alternanthera.put("Comments", "Name change from Purple Prince   14x18 spreader");
        alternanthera.put("HBHangBasketCContainerWWall", "");
        alternanthera.put("id", "16001.0");
        alternanthera.put("source", "PA");
        alternanthera.put("SSeedVVeg", "S");
        //alternanthera.put("garden", "hello!");
        Document doc = new Document();
        doc.putAll(alternanthera);
        plants.insertOne(doc);

        //Second Plant Begonia
        Map<String, String> begonia = new HashMap<>();
        begonia.put("commonName", "Begonia");
        begonia.put("cultivar", "Megawatt Rose Green Leaf");
        begonia.put("gardenLocation", "10.0");
        begonia.put("Comments", "Grow in same sun or shade area; grow close proximity to each other for comparison");
        begonia.put("HBHangBasketCContainerWWall", "");
        begonia.put("id", "16008.0");
        begonia.put("source", "PA");
        begonia.put("SSeedVVeg", "S");
        doc = new Document();
        doc.putAll(begonia);
        plants.insertOne(doc);

        //Third Plant Dianthus
        Map<String, String> dianthus = new HashMap<>();
        dianthus.put("commonName", "Dianthus");
        dianthus.put("cultivar", "Jolt™ Pink F1");
        dianthus.put("gardenLocation", "7.0");
        dianthus.put("Comments", "");
        dianthus.put("HBHangBasketCContainerWWall", "");
        dianthus.put("id", "16040.0");
        dianthus.put("source", "AAS");
        dianthus.put("SSeedVVeg", "S");
        doc = new Document();
        doc.putAll(dianthus);
        plants.insertOne(doc);

//        //First Plant Alternanthera
//        ObjectId alternantheraId = new ObjectId();
//        BasicDBObject alternanthera = new BasicDBObject();
//        alternanthera = alternanthera.append("commonName", "Alternanthera")
//                .append("cultivar", "Experimental")
//                .append("gardenLocation", "13.0")
//                .append("Comments", "Name change from Purple Prince   14x18 spreader")
//                .append("HBHangBasketCContainerWWall", "")
//                .append("id", "16001.0")
//                .append("source", "PA")
//                .append("SSeedVVeg", "S");
//        hexAlternantheraID = alternantheraId.toHexString();
//        plantDocuments.insertOne(Document.parse(alternanthera.toJson()));


    }
}




