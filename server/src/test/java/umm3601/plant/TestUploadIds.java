package umm3601.plant;

import org.junit.Before;
import org.junit.Test;
import umm3601.digitalDisplayGarden.PlantController;

import java.io.IOException;

import static junit.framework.TestCase.assertEquals;

public class TestUploadIds {

    private final static String databaseName = "data-for-testing-only";
    private PlantController plantController;

    @Before
    public void populateDB() throws IOException {
        PopulateMockDatabase db = new PopulateMockDatabase();
        db.clearAndPopulateDBAgain();
        plantController = new PlantController(databaseName);
    }

    @Test
    public void testListingOfUploadIds() throws IOException {
        String uploadid = plantController.getLiveUploadId();
        assertEquals("Incorrect uploadID in config", "second uploadId", uploadid);
    }

    @Test
    public void testGetLiveUploadId() throws IOException {
        String expect = "[ \"first uploadId\" , \"second uploadId\"]";
        String uploadArr = plantController.listUploadIds();

        assertEquals("Incorrect distinct uploadIDs", expect, uploadArr);
    }
}
