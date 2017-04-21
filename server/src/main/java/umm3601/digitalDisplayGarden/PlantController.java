package umm3601.digitalDisplayGarden;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.*;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Sorts;
import com.mongodb.util.JSON;
import org.bson.BsonInvalidOperationException;
import org.bson.Document;
import org.bson.types.ObjectId;

import org.bson.conversions.Bson;
import org.joda.time.DateTime;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.nio.Buffer;
import java.util.Iterator;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.exists;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Updates.*;
import static com.mongodb.client.model.Projections.fields;

import java.io.IOException;
import java.util.*;

import static com.mongodb.client.model.Updates.push;

public class PlantController {

    private final MongoCollection<Document> plantCollection;
    private final MongoCollection<Document> configCollection;

    public PlantController(String databaseName) throws IOException {
        // Set up our server address
        // (Default host: 'localhost', default port: 27017)
        // ServerAddress testAddress = new ServerAddress();

        // Try connecting to the server
        //MongoClient mongoClient = new MongoClient(testAddress, credentials);
        MongoClient mongoClient = new MongoClient(); // Defaults!

        // Try connecting to a database
        MongoDatabase db = mongoClient.getDatabase(databaseName);

        plantCollection = db.getCollection("plants");
        configCollection = db.getCollection("config");
    }

    /**
     * Returns a string representation of uploadId in the config collection.
     * Assumes there is only one liveUploadId in the config collection for any given time.
     * @return a string representation of uploadId in the config collection
     */
    public String getLiveUploadId() {
        try
        {
            FindIterable<Document> findIterable = configCollection.find(exists("liveUploadId"));
            Iterator<Document> iterator = findIterable.iterator();
            Document doc = iterator.next();

            return doc.getString("liveUploadId");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(" [hint] Database might be empty? Couldn't getLiveUploadId");
            throw e;
        }
    }

    // List plants
    public String listPlants(Map<String, String[]> queryParams, String uploadId) {
        Document filterDoc = new Document();
        filterDoc.append("uploadId", uploadId);

        if (queryParams.containsKey("gardenLocation")) {
            String location =(queryParams.get("gardenLocation")[0]);
            filterDoc = filterDoc.append("gardenLocation", location);
        }


        if (queryParams.containsKey("commonName")) {
            String commonName =(queryParams.get("commonName")[0]);
            filterDoc = filterDoc.append("commonName", commonName);
        }

        FindIterable<Document> matchingPlants = plantCollection.find(filterDoc);

        return JSON.serialize(matchingPlants);
    }

    /**
     * Takes a String representing an ID number of a plant
     * and when the ID is found in the database returns a JSON document
     * as a String of the following form
     *
     * <code>
     * {
     *  "_id"        : ObjectId,
     *  "commonName" : String,
     *  "cultivar"   : String
     * }
     * </code>
     *
     * If the ID is invalid or not found, the following JSON value is
     * returned
     *
     * <code>
     *  null
     * </code>
     *
     * @param plantID an ID number of a plant in the DB
     * @param uploadID Dataset to find the plant
     * @return a string representation of a JSON value
     */
    public String getPlantByPlantID(String plantID, String uploadID) {

        FindIterable<Document> jsonPlant;
        String returnVal;
        try {

            jsonPlant = plantCollection.find(and(eq("id", plantID),
                    eq("uploadId", uploadID)))
                    .projection(fields(include("commonName", "cultivar", "photoLocation")));

            Iterator<Document> iterator = jsonPlant.iterator();

            if (iterator.hasNext()) {
                addVisit(plantID, uploadID);
                returnVal = iterator.next().toJson();
            } else {
                returnVal = "null";
            }

        } catch (IllegalArgumentException e) {
            returnVal = "null";
        }

        return returnVal;

    }

    /**
     *
     * @param plantID The plant to get feedback of
     * @param uploadID Dataset to find the plant
     *
     * @return JSON for the number of interactions of a plant (likes + dislikes + comments)
     * Of the form:
     * {
     *     interactionCount: number
     * }
     */

    public String getFeedbackForPlantByPlantID(String plantID, String uploadID) {
        long comments = 0;
        long likes = 0;
        long dislikes = 0;
        long interactions = 0;

        Document out = new Document();

        FindIterable document = plantCollection.find(new Document().append("id", plantID).append("uploadId", uploadID));

        Iterator iterator = document.iterator();
        if(iterator.hasNext()){
            Document result = (Document) iterator.next();

            //Get metadat.comments array
            List<Document> plantComments = (List<Document>) ((Document) result.get("metadata")).get("comments");

            comments = plantComments.size();

            //Get metadata.rating array
            List<Document> ratings = (List<Document>) ((Document) result.get("metadata")).get("ratings");

            //Loop through all of the entries within the array, counting like=true(like) and like=false(dislike)
            for(Document rating : ratings)
            {
                if(rating.get("like").equals(true))
                    likes++;
                else if(rating.get("like").equals(false))
                    dislikes++;
            }
        }

        interactions = likes + dislikes + comments;

        out.put("interactionCount", interactions);
        return JSON.serialize(out);
    }

    // Used in the garden website
    /**
     * Takes `uploadID` and returns all bed names as a json format string
     * @param uploadID - the year that the data was uploaded
     * @return String representation of json with all bed names
     */
    public String getGardenLocationsAsJson(String uploadID){
        AggregateIterable<Document> documents
                = plantCollection.aggregate(
                Arrays.asList(
                        Aggregates.match(eq("uploadId", uploadID)), //!! Order is important here
                        Aggregates.group("$gardenLocation"),
                        Aggregates.sort(Sorts.ascending("_id"))
                ));
        return JSON.serialize(documents);
    }

    // Used in the QR code generation
    /**
     * Takes `uploadID` and returns all bed names as an array of Strings
     * @param uploadID - - the year that the data was uploaded
     * @return an array of strings
     */
    public String[] getGardenLocations(String uploadID){
        Document filter = new Document();
        filter.append("uploadId", uploadID);
        DistinctIterable<String>  bedIterator = plantCollection.distinct("gardenLocation", filter, String.class);
        List<String> beds = new ArrayList<String>();
        for(String s : bedIterator)
        {
            beds.add(s);
        }
        return beds.toArray(new String[beds.size()]);
    }

    /**
     * Adds a comment to the specified plant.
     *
     * @param id a hexstring specifiying the oid
     * @param comment true if this is a like, false if this is a dislike
     * @param uploadID Dataset to find the plant
     * @return true iff the operation succeeded.
     */
    public boolean addComment(String id, String comment, String uploadID) {
        Document filterDoc = new Document();
        ObjectId objectId;

        try {
            objectId = new ObjectId(id);
        } catch (IllegalArgumentException e) {
            return false;
        }

        filterDoc.append("_id", new ObjectId(id));
        filterDoc.append("uploadId", uploadID);

        Document comments = new Document();
        comments.append("comment", comment);
        comments.append("_id", new ObjectId());

        return null != plantCollection.findOneAndUpdate(filterDoc, push("metadata.comments", comments));
    }
    /**
     * Accepts string representation of JSON object containing
     * at least the following.
     * <code>
     *     {
     *         plantId: String,
     *         comment: String
     *     }
     * </code>
     * If either of the keys are missing or the types of the values are
     * wrong, false is returned.
     * @param json string representation of JSON object
     * @param uploadID Dataset to find the plant
     * @return true iff the comment was successfully submitted
     */
    public boolean addComment(String json, String uploadID) {
        String comment;
        String id;

        try {
            Document parsedDocument = Document.parse(json);

            if (parsedDocument.containsKey("comment") && parsedDocument.get("comment") instanceof String) {
                comment = parsedDocument.getString("comment");
            } else {
                return false;
            }
            if(parsedDocument.containsKey("plantId") && parsedDocument.get("plantId") instanceof String){
                id = parsedDocument.getString("plantId");
            } else {
                return false;
            }

        } catch (BsonInvalidOperationException e){
            e.printStackTrace();
            return false;
        } catch (org.bson.json.JsonParseException e){
            return false;
        } catch (IllegalArgumentException e){
            return false;
        }

    return addComment(id, comment, uploadID);

    }

    /**
     * Writes the comments on plants to an excel file with the following columns;
     * Id, common name, cultivar, garden location, comment, and timestamp
     *
     * outputStream is closed when this method exits
     *
     * @param outputStream stream to which the excel file is written
     * @param uploadId Dataset to find a plant
     */
    public void exportCollectedData(OutputStream outputStream, String uploadId) throws IOException {

        CollectedDataWriter collectedDataWriter = new CollectedDataWriter(outputStream);

        FindIterable<Document> plantFindIterable = plantCollection.find(new Document().append("uploadId", uploadId));
        Iterator<Document> plantIterator = plantFindIterable.iterator();

        //for each plant, get a list of comments and write each comment to the excel
        while(plantIterator.hasNext()) {
            Document plant = plantIterator.next();

            String plantID = plant.getString("id");

            List<Document> plantComments = (List<Document>) ((Document) plant.get("metadata")).get("comments");
            for(Document plantComment : plantComments) {
                String strPlantComment = plantComment.getString("comment");
                collectedDataWriter.writeComment(plantID,
                        plant.getString("commonName"),
                        plant.getString("cultivar"),
                        plant.getString("gardenLocation"),
                        strPlantComment,
                        plantComment.getObjectId("_id").getDate());
            }

            long comments = plantComments.size();
            long likes = 0;
            long dislikes = 0;

            //Get metadata.rating array
            List<Document> ratings = (List<Document>) ((Document) plant.get("metadata")).get("ratings");

            //Loop through all of the entries within the array, counting like=true(like) and like=false(dislike)
            for(Document rating : ratings)
            {
                if(rating.get("like").equals(true))
                    likes++;
                else if(rating.get("like").equals(false))
                    dislikes++;
            }

            List<Document> visits = (List<Document>) ((Document) plant.get("metadata")).get("visits");
            long numVisits = visits.size();

            collectedDataWriter.writeRating(plantID,
                    plant.getString("commonName"),
                    plant.getString("cultivar"),
                    plant.getString("gardenLocation"),
                    (int) likes,
                    (int) dislikes,
                    (int) numVisits,
                    (int) comments);
        }
        collectedDataWriter.complete();
    }

    public void getImage(OutputStream outputStream, String plantId, String uploadID) {
        try {
            String filePath = ".photos" + '/' + plantId + ".png";
            File file = new File(filePath);
            BufferedImage photo = ImageIO.read(file);
            ImageIO.write(photo,"PNG",outputStream);

        }
        catch (IOException ioe) {
            ioe.printStackTrace();
            System.err.println("Could not write some Images to disk, exiting.");
        }
    }
    /**
     * Deletes all data associated with the specified uploadID
     * in the database.
     * Returns a Document of the following form
     * <code>
     *     {
     *         success: boolean,
     *         uploadIds: [ String, ... ]
     *     }
     * </code>
     * Success will be true if the uploadID was found and false if it wasn't.
     * <br/>
     * uploadIDs contains a list of the remaining uploadIds in the database
     * @param uploadID the uploadID to delete
     * @return A Document specifying the status of the operation
     * @throws IllegalStateException if the specified uploadID is currently the liveUploadID
     */
    public Document deleteUploadID (String uploadID) {

        if (getLiveUploadId().equals(uploadID)) {
            throw new IllegalStateException("The uploadID cannot be deleted because it is the liveUploadID");
        }
        Document filterDoc = new Document();
        Document returnDoc = new Document();

        filterDoc.append("uploadId", uploadID);
        long deleted = plantCollection.deleteMany(filterDoc).getDeletedCount();
        returnDoc.append("success", deleted != 0);
        returnDoc.append("uploadIds", listUploadIds());
        return returnDoc;
    }

    /**
     * Adds a like or dislike to the specified plant.
     *
     * @param id a hexstring specifiying the oid
     * @param like true if this is a like, false if this is a dislike
     * @param uploadID Dataset to find the plant
     * @return true iff the operation succeeded.
     */

    public boolean addFlowerRating(String id, boolean like, String uploadID) {

        Document filterDoc = new Document();

        ObjectId objectId;

        try {
            objectId = new ObjectId(id);
        } catch (IllegalArgumentException e) {
            return false;
        }

        filterDoc.append("_id", new ObjectId(id));
        filterDoc.append("uploadId", uploadID);

        Document rating = new Document();
        rating.append("like", like);
        rating.append("ratingOnObjectOfId", objectId);

        return null != plantCollection.findOneAndUpdate(filterDoc, push("metadata.ratings", rating));
    }

    /**
     * Accepts string representation of JSON object containing
     * at least the following:
     * <code>
     *     {
     *         id: String,
     *         like: boolean
     *     }
     * </code>
     *
     * @param json string representation of a JSON object
     * @param uploadID Dataset to find the plant
     * @return true iff the operation succeeded.
     */

    public boolean addFlowerRating(String json, String uploadID){
        boolean like;
        String id;

        try {

            Document parsedDocument = Document.parse(json);

            if(parsedDocument.containsKey("id") && parsedDocument.get("id") instanceof String){
                id = parsedDocument.getString("id");
            } else {
                return false;
            }

            if(parsedDocument.containsKey("like") && parsedDocument.get("like") instanceof Boolean){
                like = parsedDocument.getBoolean("like");
            } else {
                return false;
            }

        } catch (BsonInvalidOperationException e){
            e.printStackTrace();
            return false;
        } catch (org.bson.json.JsonParseException e){
            return false;
        }

        return addFlowerRating(id, like, uploadID);
    }

    /**
     *
     * @return a sorted JSON array of all the distinct uploadIds in plant collection of the DB
     */
    public List<String> listUploadIds() {
        AggregateIterable<Document> documents
                = plantCollection.aggregate(
                Arrays.asList(
                        Aggregates.group("$uploadId"),
                        Aggregates.sort(Sorts.ascending("_id"))
                ));
        List<String> lst = new LinkedList<>();
        for(Document d: documents) {
            lst.add(d.getString("_id"));
        }
        return lst;
//        return JSON.serialize(plantCollection.distinct("uploadId","".getClass()));
    }

    public boolean addVisit(String plantID, String uploadID) {

        Document filterDoc = new Document();
        filterDoc.append("id", plantID);
        filterDoc.append("uploadId", uploadID);

        Document visit = new Document();
        visit.append("visit", new ObjectId());

        return null != plantCollection.findOneAndUpdate(filterDoc, push("metadata.visits", visit));
    }

}