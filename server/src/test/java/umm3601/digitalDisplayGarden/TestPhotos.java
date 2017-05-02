package umm3601.digitalDisplayGarden;


import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestPhotos {
    Photos photos = new Photos("data-for-testing-only");
    MongoClient mongoClient = new MongoClient();
    MongoDatabase db = mongoClient.getDatabase("data-for-testing-only");

    boolean photosExist;

    @Before
    public void flagDeletePos() {
        if (Files.notExists(Paths.get(".photos/"))) {
            photosExist = false;
        } else {
            photosExist = true;
        }
    }

    @After
    public void delFolders() {
        deleteDirectory(new File(".photos/"+"second uploadId"));
        if (!photosExist) {
            deleteDirectory(new File(".photos/"));
        }
    }

    @Test
    public void smallImageTest() throws IOException {

        BufferedImage test1 = new BufferedImage(80,80,BufferedImage.TYPE_BYTE_BINARY);

        photos.saveImage("one",test1,"second uploadId");

        File foo = new File (".photos/second uploadId/one.jpeg");

        assertTrue(foo.exists());
    }

    @Test
    public void giantLargeTest() throws IOException {

        BufferedImage test1 = new BufferedImage(20000,15000,BufferedImage.TYPE_BYTE_BINARY);

        photos.saveImage("three",test1,"second uploadId");

        File foo = new File (".photos/second uploadId/three.jpeg");

        assertTrue(foo.exists());
    }


    public static void deleteDirectory(File directory) {
        if(directory.exists()){
            File[] files = directory.listFiles();
            if(null!=files){
                for(int i=0; i<files.length; i++) {
                    if(files[i].isDirectory()) {
                        deleteDirectory(files[i]);
                    }
                    else {
                        files[i].delete();
                    }
                }
            }
        }
        directory.delete();
    }
}
