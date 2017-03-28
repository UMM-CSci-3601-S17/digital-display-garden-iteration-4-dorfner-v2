package umm3601.plant;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import umm3601.digitalDisplayGarden.Plant;
import umm3601.digitalDisplayGarden.PlantController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

public class FilterByGardenLocation {

    private final static String databaseName = "data-for-testing-only";
    private PlantController plantController;

    @Before
    public void populateDB() throws IOException {
        PopulateMockDatabase db = new PopulateMockDatabase();
        db.clearAndPopulateDBAgain();
        plantController = new PlantController(databaseName);
    }

    @Test
    public void findDataForGardenTen() throws  IOException {
        Plant[] filteredPlants;
        Gson gson = new Gson();

        Map<String, String[]> queryParams = new HashMap<>();
        queryParams.put("gardenLocation", new String[]{"10.0"});
        String rawPlants = plantController.listPlants(queryParams, "first uploadId");
        filteredPlants = gson.fromJson(rawPlants, Plant[].class);

        assertEquals("Incorrect number of flowers for gardenLocation 10.0", 2, filteredPlants.length);
        assertEquals("Incorrect contents for index 0", "Alternanthera", filteredPlants[0].commonName);
        assertEquals("Incorrect contents for index 1", "Begonia", filteredPlants[1].commonName);
    }

    @Test
    public void gardenOneHundred() throws IOException {
        Plant[] filteredPlants;
        Gson gson = new Gson();

        Map<String, String[]> queryParams = new HashMap<>();
        queryParams.put("gardenLocation", new String[]{"100.0"});
        String rawPlants = plantController.listPlants(queryParams, "second uploadId");
        filteredPlants = gson.fromJson(rawPlants, Plant[].class);

        assertEquals("Incorrect number of plants for gardenLocation 100", 0, filteredPlants.length);
    }
}
