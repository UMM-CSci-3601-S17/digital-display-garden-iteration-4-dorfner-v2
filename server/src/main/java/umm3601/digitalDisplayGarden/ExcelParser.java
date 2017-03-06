package umm3601.digitalDisplayGarden;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bson.Document;

public class ExcelParser {
    private static final String FILE_NAME = "/home/Dogxx000/IdeaProjects/digital-display-garden-iteration-1-claudearabo/server/src/main/java/umm3601/digitalDisplayGarden/AccessionList2016.xlsx";

    public static void main(String[] args) {
        String[][] arrayRepresentation = extractFromXLSX();
        String[][] horizontallyCollapsed = collapseHorizontally(arrayRepresentation);
        String[][] verticallyCollapsed = collapseVertically(horizontallyCollapsed);
        replaceNulls(verticallyCollapsed);
        populateDatabase(verticallyCollapsed);


        //FOR TESTING
        /*
        System.out.println("---------------------- Raw XLSX data -------------------");
        printDoubleArray(extractFromXLSX());
        System.out.println("---------------------- Raw XLSX data -------------------");
        for(int i = 0; i < 10; i++) {
            System.out.println();
        }

        System.out.println("---------------------- Horizontally Collapsed Array -------------------");
        printDoubleArray(horizontallyCollapsed);
        System.out.println("---------------------- Horizontally Collapsed Array -------------------");
        for(int i = 0; i < 10; i++) {
            System.out.println();
        }

        System.out.println("---------------------- Vertically (Fully) Collapsed Array -------------------");
        printDoubleArray(verticallyCollapsed);
        System.out.println("---------------------- Vertically (Fully) Collapsed Array -------------------");
        for(int i = 0; i < 10; i++) {
            System.out.println();
        }

        // to make work, you should really copy past the replaceNulls & populateDataBase lines from above, to below this print statement
        System.out.println("---------------------- Replaced Null's with EmptyString -------------------");
        replaceNulls(verticallyCollapsed);
        printDoubleArray(verticallyCollapsed);
        System.out.println("---------------------- Replaced Null's with EmptyString -------------------");
        */
    }

    /*
    Uses Apache POI to extract information from xlsx file into a 2D array.
    Here is where we got our starter code: http://www.mkyong.com/java/apache-poi-reading-and-writing-excel-file-in-java/
    Look at example number 4, Apache POI library – Reading an Excel file.

    This file originally just printed data, that is why there are several commented out lines in the code.
    We have repurposed this method to put all data into a 2D String array and return it.
     */
    private static String[][] extractFromXLSX() {
        try {

            // Did this print to find where the root of the filepath was.
            //System.out.println("Attempting to read from file in: "+ new File(FILE_NAME).getCanonicalPath());
            FileInputStream excelFile = new FileInputStream(new File(FILE_NAME));

            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = datatypeSheet.iterator();

            // ---------- our stuff -----------
            String[][] cellValues = new String[datatypeSheet.getLastRowNum() + 1][];
            Row currentRow = datatypeSheet.getRow(1);
            // ---------- our stuff -----------

            Cell firstCell = iterator.next().getCell(1);
            while (iterator.hasNext() && !firstCell.equals(null)) {

                currentRow = iterator.next();
                Iterator<Cell> cellIterator = currentRow.iterator();

                cellValues[currentRow.getRowNum()] = new String[currentRow.getLastCellNum() - 1];

                while (cellIterator.hasNext()) {

                    Cell currentCell = cellIterator.next();


                    //getCellTypeEnum shown as deprecated for version 3.15
                    //getCellTypeEnum ill be renamed to getCellType starting from version 4.0
                    if (currentCell.getCellTypeEnum() == CellType.STRING) {
                        //System.out.print(currentCell.getStringCellValue() + "--");
                        cellValues[currentCell.getRowIndex()][currentCell.getColumnIndex()] = currentCell.getStringCellValue();
                    } else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                        //System.out.print(currentCell.getNumericCellValue() + "--");
                        cellValues[currentCell.getRowIndex()][currentCell.getColumnIndex()] = ("" + currentCell.getNumericCellValue());
                    }

                }
                //System.out.println();


            }

            /* Our print statements to understand what this 2D array looks like.
            System.out.println(cellValues.length); //This is how tall the double array is
            System.out.println(cellValues[1].length); //This is how wide it is
            printDoubleArray(cellValues); //Double array representation.
             */

            return cellValues;

        } catch (FileNotFoundException e) {
            System.out.println("EVERYTHING BLEW UP STOP STOP STOP");
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            System.out.println("EVERYTHING BLEW UP STOP STOP STOP");
            e.printStackTrace();
            return null;
        }
    }

    // removes all columns that are completely null.
    // Identify how much to shrink the array and calls a helper function to shrink it
    private static String[][] collapseHorizontally(String[][] cellValues){
        // scanning over columns
        for(int j = cellValues[1].length - 1; j > 0; j--){
            //scanning over the three rows of our "key row"
            for(int i = 1; i <= 3; i++) {
                if(!(cellValues[i][j] == null)){
                    return trimArrayHorizontally(cellValues, j);
                }
            }
        }
        return null;
    }

    // removes all completely null rows
    // Identify how much to shrink the array and calls a helper function to shrink it
    private static String[][] collapseVertically(String[][] cellValues){
        for(int i = cellValues.length - 1; i > 0; i--) {
            if(!(cellValues[i][0] == null)) {
                return trimArrayVertically(cellValues, i);
            }
        }
        return null;
    }

    // helper function for collapseHorizontally() decreases the number of columns in the array
    private static String[][] trimArrayHorizontally(String[][] cellValues, int horizontalBound){
        String[][] trimmedArray = new String[cellValues.length][];
        for(int j = 1; j < cellValues.length; j++) {
            trimmedArray[j] = new String[horizontalBound + 1];
            for (int i = 0; i <= horizontalBound; i++) {
                trimmedArray[j][i] = cellValues[j][i];
            }
        }
        return trimmedArray;
    }

    // helper function for collapseHorizontally() decreases the number of rows in the array
    private static String[][] trimArrayVertically(String[][] cellValues, int verticalBound){
        String[][] trimmedArray = new String[verticalBound][];
        for(int i = 1; i <= verticalBound; i++) {
            trimmedArray[i - 1] = new String[cellValues[i].length];
            trimmedArray[i - 1] = cellValues[i];
        }
        return trimmedArray;
    }

    // replaces all cells with null values with an empty string (avoids future null pointer exceptions)
    private static void replaceNulls(String[][] cellValues) {
        for(int i = 0; i < cellValues.length; i++) {
            for(int j = 0; j < cellValues[i].length; j++) {
                if(cellValues[i][j] == null) {
                    cellValues[i][j] = "";
                }
            }
        }
    }

    /* Helper method for populateDatabase().
    looks at rows 2 through 4 (xlsx file is 1 based) and:
    1. Concatenates all content in rows 2 through 4 for a given column
    2. Adds all keys into a 1D string array
    3. Replaces certain key words so they match with the standard committee's requirements
     */
    private static String[] getKeys(String[][] cellValues){
        String[] keys = new String[cellValues[0].length];


        for(int i = 0; i < cellValues[0].length; i++){
            keys[i] = cellValues[0][i];
            for(int j = 1; j < 3; j++){
                keys[i] = keys[i] + cellValues[j][i];
            }
        }

        for (int i = 0; i < keys.length; i++){
            if(keys[i].equals("#")) keys[i] = "id";
            if(keys[i].equals("Common Name")) keys[i] = "commonName";
            if(keys[i].equals("Cultivar")) keys[i] = "cultivar";
            if(keys[i].equals("Source")) keys[i] = "source";
            if(keys[i].equals("Garden  Location")) keys[i] = "gardenLocation";
        }

        return keys;
    }

    // Moves row by row through the 2D array and adds content for every flower paired with keys into a document
    // Uses the document to one at a time, add flower information into the database.
    private static void populateDatabase(String[][] cellValues){
        MongoClient mongoClient = new MongoClient();
        MongoDatabase test = mongoClient.getDatabase("test");
        MongoCollection plants = test.getCollection("plants");

        for (int i = 4; i < cellValues.length; i++){
            String[] keys = getKeys(cellValues);
            Map<String, String> map = new HashMap<String, String>();
            for(int j = 0; j < cellValues[i].length; j++){
                map.put(keys[j], cellValues[i][j]);
            }

            Document doc = new Document();
            doc.putAll(map);
            plants.insertOne(doc);
        }
    }

    /*
    ------------------------------- UTILITIES -----------------------------------
     */

    // prints a 1D array
    private static void printArray(String[] input){
        System.out.print("[");
        for(String str : input){
            System.out.print(str + ", ");
        }
        System.out.println("]");
    }

    /*
    Prints a 2D array.
    NOTE: If you uncomment the commented out line, and comment the one beneath it,
    you will also see the indexes for every element as well as their content.
     */
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
