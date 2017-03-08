package umm3601.plant;

import com.google.gson.Gson;
import org.junit.Test;
import sun.text.normalizer.UTF16;
import umm3601.digitalDisplayGarden.Plant;
import umm3601.digitalDisplayGarden.PlantController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

public class FilterByCommonName {
    @Test
    public void filterPlantsByCommonName() throws IOException {
        PlantController plantController = new PlantController();
        Plant[] filteredPlants;
        Gson gson = new Gson();

        Map<String, String[]> queryParams = new HashMap<>();
        queryParams.put("commonName", new String[]{"Angelonia"});
        String rawPlants = plantController.listPlants(queryParams);

//        if(rawPlants.contains(UTF16.valueOf(0x00AE))){
//            rawPlants = rawPlants.replaceAll(UTF16.valueOf(0x00AE), "");
//        }

        filteredPlants = gson.fromJson(rawPlants, Plant[].class);
        assertEquals("Incorrect number of plants with commonName Angelonia", 6, filteredPlants.length);

    }
}
