package umm3601.digitalDisplayGarden;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.BSON;
import org.bson.Document;
import org.bson.types.ObjectId;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.mongodb.client.model.Updates.push;


public class Photos {

    private final MongoCollection<Document> plantCollection;

    public Photos(String databaseName) {


        MongoClient mongoClient = new MongoClient();

        MongoDatabase db = mongoClient.getDatabase(databaseName);

        plantCollection = db.getCollection("plants");
    }

    public boolean saveImage(String plantId, RenderedImage photo, String uploadId){
        try {
            if (Files.notExists(Paths.get(".photos"))) {
                Files.createDirectory(Paths.get(".photos"));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        try {
            String filePath = ".photos" + '/' + plantId + ".png";
            File outputFile = new File(filePath);
            String relPath = outputFile.getPath();
            ImageIO.write(photo, "png", outputFile);


            Document filterDoc = new Document();
            filterDoc.append("id", plantId);
            filterDoc.append("uploadId", uploadId);

            Document photoLocation = new Document();
            photoLocation.append("photoLocation", relPath);

            plantCollection.findOneAndUpdate(filterDoc,new Document("$set", photoLocation));
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
            System.err.println("Could not write some Images to disk, exiting.");
            return false;
        }
        return true;
    }



}
