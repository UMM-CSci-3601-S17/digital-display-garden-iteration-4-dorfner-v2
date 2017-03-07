package umm3601.plant;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import umm3601.digitalDisplayGarden.Plant;
import umm3601.digitalDisplayGarden.PlantController;
import umm3601.plant.PopulateMockDatabase;

import java.io.IOException;

import static junit.framework.TestCase.assertEquals;

public class FilterPlantsByID {
//    @Before
//    public void setUpDB() throws IOException{
//        PopulateMockDatabase mockDatabase = new PopulateMockDatabase();
//        mockDatabase.clearAndPopulateDB();
//    }

    @Test
    public void findByID() throws IOException {
        PlantController plantController = new PlantController();
        Plant[] filteredPlants;
        Gson gson = new Gson();

        String rawPlants = plantController.getPlant("16280.0");
        filteredPlants = gson.fromJson(rawPlants, Plant[].class);
        assertEquals("Incorrect", 1, filteredPlants.length);
    }
}
