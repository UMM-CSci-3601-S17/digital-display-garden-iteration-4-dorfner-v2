package umm3601.digitalDisplayGarden;

import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Sorts;
import com.mongodb.util.JSON;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import static com.mongodb.client.model.Filters.eq;

public class PlantController {

    private final MongoCollection<Document> plantCollection;

    public PlantController() throws IOException {
        // Set up our server address
        // (Default host: 'localhost', default port: 27017)
        // ServerAddress testAddress = new ServerAddress();

        // Try connecting to the server
        //MongoClient mongoClient = new MongoClient(testAddress, credentials);
        MongoClient mongoClient = new MongoClient(); // Defaults!

        // Try connecting to a database
        MongoDatabase db = mongoClient.getDatabase("test");

        plantCollection = db.getCollection("plants");
    }

    // List plants
    public String listPlants(Map<String, String[]> queryParams) {
        Document filterDoc = new Document();

        /*
        if (queryParams.containsKey("age")) {
            int targetAge = Integer.parseInt(queryParams.get("age")[0]);
            filterDoc = filterDoc.append("age", targetAge);
        }*/

        FindIterable<Document> matchingPlants = plantCollection.find(filterDoc);

        return JSON.serialize(matchingPlants);
    }

    // Get a single plant
    public String getPlant(String id) {
        FindIterable<Document> jsonPlants
                = plantCollection
                .find(eq("_id", new ObjectId(id)));

        Iterator<Document> iterator = jsonPlants.iterator();

        Document plant = iterator.next();

        return plant.toJson();
    }

    // Get the average age of all plants by company
    /*
    public String getAverageAgeByCompany() {
        AggregateIterable<Document> documents
                = plantCollection.aggregate(
                Arrays.asList(
                        Aggregates.group("$company",
                                Accumulators.avg("averageAge", "$age")),
                        Aggregates.sort(Sorts.ascending("_id"))
                ));
        System.err.println(JSON.serialize(documents));
        return JSON.serialize(documents);
    }*/

}