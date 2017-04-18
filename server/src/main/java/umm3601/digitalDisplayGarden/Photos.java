package umm3601.digitalDisplayGarden;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;


public class Photos {

    private final MongoCollection<Document> plantCollection;

    public Photos(String databaseName) {


        MongoClient mongoClient = new MongoClient();

        MongoDatabase db = mongoClient.getDatabase(databaseName);

        plantCollection = db.getCollection("plants");
    }

    public void saveImage(String plantId, RenderedImage photo){
        try {
            File outputFile = new File(".photos" + '/' + plantId + ".png");
            ImageIO.write(photo, "png", outputFile);
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
            System.err.println("Could not write some Images to disk, exiting.");
        }
    }
}
