package umm3601.plant;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import umm3601.digitalDisplayGarden.PlantController;

import java.io.IOException;

import static junit.framework.TestCase.assertEquals;

public class TestGardenLocations {

    private final static String databaseName = "data-for-testing-only";
    private PlantController plantController;

    @Before
    public void populateDB() throws IOException {
        PopulateMockDatabase db = new PopulateMockDatabase();
        db.clearAndPopulateDBAgain();
        plantController = new PlantController(databaseName);
    }

    @Test
    public void GetGardenLocationAsJsonRemovesDuplicates() throws IOException {
        GardenLocation[] gardenLocations;
        Gson gson = new Gson();
        GardenLocation dummy = new GardenLocation();
        dummy._id = "10.0";

        String json = plantController.getGardenLocationsAsJson("first uploadId");
        gardenLocations = gson.fromJson(json, GardenLocation[].class);
        assertEquals("Incorrect number of unique garden locations", 1, gardenLocations.length);
        assertEquals("Incorrect zero index", dummy, gardenLocations[0]);
        assertEquals("Incorrect value for index 0", "10.0", gardenLocations[0]._id);
    }

    @Test
    public void GetGardenLocationsRemovesDuplicates() throws IOException {
        String[] gardenLocations;
        Gson gson = new Gson();
        String[] dummy = {"10.0"};

        gardenLocations = plantController.getGardenLocations("first uploadId");
        assertEquals("Incorrect number of unique garden locations", 1, gardenLocations.length);
        assertArrayEquals("Incorrect zero index", dummy, gardenLocations);
        assertEquals("Incorrect value for index 0", "10.0", gardenLocations[0]);
    }
}
