package umm3601.plant;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import umm3601.digitalDisplayGarden.Plant;
import umm3601.digitalDisplayGarden.PlantController;
import umm3601.plant.PopulateMockDatabase;

import java.io.IOException;

import static junit.framework.TestCase.assertEquals;

public class FilterByUniqueGardenLocations {

    private final static String databaseName = "data-for-testing-only";
    private PlantController plantController;

    @Before
    public void populateDB() throws IOException {
        PopulateMockDatabase db = new PopulateMockDatabase();
        db.clearAndPopulateDBAgain();
        plantController = new PlantController(databaseName);
    }

    @Test
    public void findByGardenLocation() throws IOException {
        GardenLocation[] filteredPlants;
        Gson gson = new Gson();
        //System.out.println();
        String rawPlants = plantController.getPlantsByGardenLocations("first uploadId");
        filteredPlants = gson.fromJson(rawPlants, GardenLocation[].class);
        assertEquals("Incorrect number of unique garden locations", 1, filteredPlants.length);
        assertEquals("Incorrect zero index", "10.0", filteredPlants[0]._id);
        assertEquals("Incorrect value for index 0", "10.0", filteredPlants[0]._id);
    }
}
