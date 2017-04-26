package umm3601.plant;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;

import umm3601.digitalDisplayGarden.PlantController;

import java.io.IOException;

import static junit.framework.TestCase.assertEquals;

public class TestPlantByPlantId {
    private final static String databaseName = "data-for-testing-only";
    private PlantController plantController;

    @Before
    public void populateDB() throws IOException {
        PopulateMockDatabase db = new PopulateMockDatabase();
        db.clearAndPopulateDBAgain();
        plantController = new PlantController(databaseName);
    }

    @Test
    public void GetPlantByPlantIdExist() throws IOException {
//        Gson gson = new Gson();
        String expect1 = "{ \"_id\" : { \"$oid\" : \"58d1c36efb0cac4e15afd202\" }, \"commonName\" : \"Alternanthera\", \"cultivar\" : \"Experimental\" }";
        String plant1 = plantController.getPlantByPlantID("16001.0", "first uploadId",false);
        String expect2 = "{ \"_id\" : { \"$oid\" : \"58d1c36efb0cac4e15afd203\" }, \"commonName\" : \"Begonia\", \"cultivar\" : \"Megawatt Rose Green Leaf\" }";
        String plant2 = plantController.getPlantByPlantID("16008.0", "first uploadId",false);
        String expect3 = "{ \"_id\" : { \"$oid\" : \"58d1c36efb0cac4e15afd204\" }, \"commonName\" : \"Dianthus\", \"cultivar\" : \"Jolt™ Pink F1\" }";
        String plant3 = plantController.getPlantByPlantID("16040.0", "second uploadId",false);

        // For some reason, this creates a new object id for the plant even though there is already one.
        // plant = gson.fromJson(json, Plant.class);
        assertEquals("Incorrect plant", expect1, plant1);
        assertEquals("Incorrect plant", expect2, plant2);
        assertEquals("Incorrect plant", expect3, plant3);
    }

    @Test
    public void GetPlantByPlantIdDE() throws IOException {
        String plant1 = plantController.getPlantByPlantID("16001.0", "second uploadId",false);
        String plant2 = plantController.getPlantByPlantID("16008.0", "second uploadId",false);
        String plant3 = plantController.getPlantByPlantID("16040.0", "first uploadId",false);

        assertEquals("Incorrect plant", "null", plant1);
        assertEquals("Incorrect plant", "null", plant2);
        assertEquals("Incorrect plant", "null", plant3);
    }
}
