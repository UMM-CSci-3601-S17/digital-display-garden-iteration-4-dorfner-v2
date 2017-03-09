package umm3601.digitalDisplayGarden;


import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.junit.Before;
import org.junit.Test;

import static com.mongodb.client.model.Filters.eq;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by benek020 on 3/6/17.
 */
public class TestExcelParser {

    public MongoClient mongoClient = new MongoClient();
    public MongoDatabase testDB;
    public ExcelParser parser;

    @Before
    public void clearAndPopulateDatabase(){
        mongoClient.dropDatabase("test");
        testDB = mongoClient.getDatabase("test");
        parser = new ExcelParser(true);
    }



    @Test
    public void testSpeadsheetToDoubleArray(){
        String[][] plantArray = parser.extractFromXLSX();
        //printDoubleArray(plantArray);

        assertEquals(1668, plantArray.length);
        assertEquals(plantArray[40].length, plantArray[1234].length);
        assertEquals("ALEXANDER", plantArray[5][2]);

    }

    @Test
    public void testCollapse(){
        String[][] plantArray = parser.extractFromXLSX();
        //System.out.println(plantArray.length);
        //printDoubleArray(plantArray);

        plantArray = parser.collapseHorizontally(plantArray);
        plantArray = parser.collapseVertically(plantArray);

        //printDoubleArray(plantArray);

        assertEquals(1668, plantArray.length);
        assertEquals(10, plantArray[30].length);
        assertEquals(10, plantArray[0].length);
        assertEquals(10, plantArray[3].length);
    }

    @Test
    public void testReplaceNulls(){
        String[][] plantArray = parser.extractFromXLSX();
        plantArray = parser.collapseHorizontally(plantArray);
        plantArray = parser.collapseVertically(plantArray);
        parser.replaceNulls(plantArray);

        for (String[] row : plantArray){
            for (String cell : row){
                assertNotNull(cell);
            }
        }
    }

    @Test
    public void testPopulateDatabase(){
        String[][] plantArray = parser.extractFromXLSX();
        plantArray = parser.collapseHorizontally(plantArray);
        plantArray = parser.collapseVertically(plantArray);
        parser.replaceNulls(plantArray);

        parser.populateDatabase(plantArray);
        MongoCollection plants = testDB.getCollection("plants");


        assertEquals(1664, plants.count());
        assertEquals(16, plants.count(eq("Sort", "104")));
    }



    private static void printDoubleArray(String[][] input){
        for(int i = 0; i < input.length; i++){
            if (!(input[i] == (null))) {
                for (int j = 0; j < input[i].length; j++) {
                    //System.out.print(" | " + "i: " + i + " j: " + j + " value: " + input[i][j] );
                    System.out.print(" | " + input[i][j]);
                }
                System.out.println();
                System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            }
        }
    }
}
