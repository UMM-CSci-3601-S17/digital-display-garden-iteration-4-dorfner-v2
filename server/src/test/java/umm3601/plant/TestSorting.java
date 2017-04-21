package umm3601.plant;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import org.junit.Before;
import org.junit.Test;
import umm3601.digitalDisplayGarden.ExcelParser;
import umm3601.digitalDisplayGarden.Plant;
import umm3601.digitalDisplayGarden.PlantController;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

public class TestSorting {
    private final static String databaseName = "data-for-testing-only";
    private PlantController plantController;

    public MongoClient mongoClient = new MongoClient();
    public ExcelParser parser;
    public InputStream fromFile;

    @Before
    public void clearAndPopulateDatabase() throws IOException{
        mongoClient.dropDatabase(databaseName);
        fromFile = this.getClass().getResourceAsStream("/AccessionList2016.xlsx");
        parser = new ExcelParser(fromFile, databaseName);

        try {
            String id = "160210";
            parser.parseExcel(id);
        } catch (FileNotFoundException e) {
            System.err.println("Something wrong when importing excel in sorting test");
        }

        plantController = new PlantController(databaseName);
    }

    @Test
    public void testFirstAndLastBed() {
        Map<String, String[]> empty = new HashMap<>();
        Gson gson = new Gson();

        String sortedPlantsStr = plantController.listPlants(empty, "160210");
        Plant[] sortedPlants = gson.fromJson(sortedPlantsStr, Plant[].class);

        assertEquals("Incorrect number of plants in the database", 286, sortedPlants.length);
        assertEquals("Incorrect accession number for the first plant", "16338", sortedPlants[0].id);
        assertEquals("Incorrect accession number for the last plant", "16223", sortedPlants[sortedPlants.length-1].id);
    }

    @Test
    public void testMiddleBedNames() {
        Map<String, String[]> empty = new HashMap<>();
        Gson gson = new Gson();

        String sortedPlantsStr = plantController.listPlants(empty, "160210");
        Plant[] sortedPlants = gson.fromJson(sortedPlantsStr, Plant[].class);

        assertEquals("Incorrect second bed name", "1S", sortedPlants[36].gardenLocation);
        assertEquals("Incorrect third bed name", "2N", sortedPlants[70].gardenLocation);
        assertEquals("Incorrect second last bed name", "13", sortedPlants[255].gardenLocation);
    }

    @Test
    public void testGardenLocations() {
        Gson gson = new Gson();

        String bedsStr = plantController.getGardenLocationsAsJson("160210");
        GardenLocation[] beds = gson.fromJson(bedsStr, GardenLocation[].class);

        assertEquals("Incorrect first bed name for getGardenLocationsAsJson", "1N", beds[0]._id);
        assertEquals("Incorrect second bed name for getGardenLocationsAsJson", "1S", beds[1]._id);
        assertEquals("Incorrect third last bed name for getGardenLocationsAsJson", "2N", beds[2]._id);
        assertEquals("Incorrect last bed name for getGardenLocationsAsJson", "LG", beds[beds.length-1]._id);
    }

    @Test
    public void testSortByCommonNamesAndCultivar() {
        Map<String, String[]> LG = new HashMap<>();
        String[] location = {"LG"};
        LG.put("gardenLocation", location);
        Gson gson = new Gson();

        String LGPlantsStr = plantController.listPlants(LG, "160210");
        Plant[] LGPlants = gson.fromJson(LGPlantsStr, Plant[].class);

        assertEquals("Incorrect first plant's common name", "Begonia semperflorens", LGPlants[0].commonName);
        assertEquals("Incorrect first plant's cultivar name", "Sprint Plus Orange", LGPlants[0].cultivar);
        assertEquals("Incorrect last plant's common name", "Vinca", LGPlants[LGPlants.length-1].commonName);
        assertEquals("Incorrect last plant's cultivar name", "Soiree™ Crown Rose", LGPlants[LGPlants.length-1].cultivar);
    }
}
