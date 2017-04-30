package umm3601.plant;

import com.mongodb.util.JSON;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import umm3601.digitalDisplayGarden.PlantController;

import java.io.IOException;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestUploadIDs {

    private final static String databaseName = "data-for-testing-only";
    private PlantController plantController;

    @Before
    public void populateDB() throws IOException {
        PopulateMockDatabase db = new PopulateMockDatabase();
        db.clearAndPopulateDBAgain();
        plantController = new PlantController(databaseName);
    }

    @Test
    public void testListingOfUploadIDs() throws IOException {
        String uploadID = plantController.getLiveUploadID();
        assertEquals("Incorrect uploadID in config", "second uploadId", uploadID);
    }

    @Test
    public void testGetLiveUploadID() throws IOException {
        String expect = "[ \"first uploadId\" , \"second uploadId\"]";
        String uploadArr = JSON.serialize(plantController.listUploadIDs());

        assertEquals("Incorrect distinct uploadIDs", expect, uploadArr);
    }

    @Test
    public void testDeleteUploadFailsProperly() {
        Document result = plantController.deleteUploadID("foobar doesn't exist");
        assertFalse(result.getBoolean("success"));
        String expectedIDs = "[ \"first uploadId\" , \"second uploadId\"]";
        assertEquals(expectedIDs, JSON.serialize(result.get("uploadIds")));
    }

    @Test(expected = IllegalStateException.class)
    public void testDeleteUploadPreventsRemovalOfLiveUploadID() {
        plantController.deleteUploadID("second uploadId");
        assertTrue(false); // this line shouldn't be reached
    }

    @Test
    public void testDeleteUploadIDSucceedsProperly() {
        Document result = plantController.deleteUploadID("first uploadId");
        assertTrue(result.getBoolean("success"));
        String expectedIDs = "[ \"second uploadId\"]";
        assertEquals(expectedIDs, JSON.serialize(result.get("uploadIds")));
    }
}
