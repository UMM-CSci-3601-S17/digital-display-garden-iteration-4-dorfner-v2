package umm3601.digitalDisplayGarden;


import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
        parser = new ExcelParser();
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
        System.out.println(plantArray.length);
        printDoubleArray(plantArray);

        plantArray = parser.collapseHorizontally(plantArray);
        plantArray = parser.collapseVertically(plantArray);

        printDoubleArray(plantArray);

        assertEquals(1668, plantArray.length);
        assertEquals(10, plantArray[30].length);
        assertEquals(10, plantArray[0].length);
        assertEquals(10, plantArray[3].length);
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
