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
    public MongoCollection plants;

    @Before
    public void clearAndPopulateDatabase(){
        mongoClient.dropDatabase("test2");


        testDB = mongoClient.getDatabase("test2");
        plants = testDB.getCollection("plants");
    }



    @Test
    public void testSpeadsheetToDoubleArray(){
        plants.drop();

        ExcelParser parser = new ExcelParser();

        String[][] doubleArray = parser.extractFromXLSX();
        printDoubleArray(doubleArray);

        assertEquals(1668, doubleArray.length);
        assertEquals(doubleArray[40].length, doubleArray[1234].length);
        assertEquals("ALEXANDER", doubleArray[5][2]);

    }

    private static void printDoubleArray(String[][] input){
        for(int i = 1; i < input.length; i++){
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
