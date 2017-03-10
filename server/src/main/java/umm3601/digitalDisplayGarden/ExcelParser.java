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
import java.util.HashMap;
import java.util.Map;
import static java.lang.Math.max;
import static java.lang.Math.round;

import org.bson.Document;
//import sun.text.normalizer.UTF16;

public class ExcelParser {
    public static String FILE_NAME = "/home/Dogxx000/IdeaProjects/digital-display-garden-iteration-1-claudearabo/server/src/main/java/umm3601/digitalDisplayGarden/AccessionList2016.xlsx";

    public static void main(String[] args) {
        parseExel();
    }

    public ExcelParser(boolean testing){
        if (testing){
            FILE_NAME = "/home/benek020/Downloads/IDPH_STD_Illinois_By_County_By_Sex.xlsx";
        }
    }

    public static void parseExel() {

        String[][] arrayRepresentation = extractFromXLSX();

        String[][] horizontallyCollapsed = collapseHorizontally(arrayRepresentation);
        String[][] verticallyCollapsed = collapseVertically(horizontallyCollapsed);
        replaceNulls(verticallyCollapsed);
        populateDatabase(verticallyCollapsed);

    }

    /*
    Uses Apache POI to extract information from xlsx file into a 2D array.
    Here is where we got our starter code: http://www.mkyong.com/java/apache-poi-reading-and-writing-excel-file-in-java/
    Look at example number 4, Apache POI library – Reading an Excel file.

    This file originally just printed data, that is why there are several commented out lines in the code.
    We have repurposed this method to put all data into a 2D String array and return it.
     */
    public static String[][] extractFromXLSX() {
        try {
            FileInputStream excelFile = new FileInputStream(new File(FILE_NAME));

            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);

            String[][] cellValues = new String[datatypeSheet.getLastRowNum() + 1]
                    [max(max(datatypeSheet.getRow(1).getLastCellNum(), datatypeSheet.getRow(2).getLastCellNum()),
                    datatypeSheet.getRow(3).getLastCellNum())];

            for(Row currentRow : datatypeSheet) {
                //cellValues[currentRow.getRowNum()] = new String[currentRow.getLastCellNum()];

                for (Cell currentCell : currentRow) {

                    //getCellTypeEnum shown as deprecated for version 3.15
                    //getCellTypeEnum ill be renamed to getCellType starting from version 4.0
                    if (currentCell.getCellTypeEnum() == CellType.STRING) {
                        cellValues[currentCell.getRowIndex()][currentCell.getColumnIndex()] = currentCell.getStringCellValue();
                    } else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                        cellValues[currentCell.getRowIndex()][currentCell.getColumnIndex()] =
                                Integer.toString((int)Math.round(currentCell.getNumericCellValue()));
                    }

                }

            }
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
    public static String[][] collapseHorizontally(String[][] cellValues){
        // scanning over columns
        for(int j = cellValues[1].length - 1; j > 0; j--){
            //scanning over the three rows of our "key row"
            for(int i = 1; i <= 3; i++) {
                if(cellValues[i][j] != null){
                    return trimArrayHorizontally(cellValues, j);
                }
            }
        }
        return null;
    }

    // removes all completely null rows
    // Identify how much to shrink the array and calls a helper function to shrink it
    public static String[][] collapseVertically(String[][] cellValues){
        for(int i = cellValues.length - 1; i > 0; i--) {
            if(!(cellValues[i][0] == null)) {
                return trimArrayVertically(cellValues, i + 1);
            }
        }
        return null;
    }

    // helper function for collapseHorizontally() decreases the number of columns in the array
    public static String[][] trimArrayHorizontally(String[][] cellValues, int horizontalBound){
        String[][] trimmedArray = new String[cellValues.length][];
        for(int j = 0; j < cellValues.length; j++) {
            trimmedArray[j] = new String[horizontalBound + 1];
            for (int i = 0; i < horizontalBound + 1; i++) {
                trimmedArray[j][i] = cellValues[j][i];
            }
        }
        return trimmedArray;
    }

    // helper function for collapseHorizontally() decreases the number of rows in the array
    public static String[][] trimArrayVertically(String[][] cellValues, int verticalBound){
        String[][] trimmedArray = new String[verticalBound][];
        for(int i = 0; i < verticalBound; i++) {
            trimmedArray[i] = new String[cellValues[i].length];
            trimmedArray[i] = cellValues[i];
        }
        return trimmedArray;
    }

    // replaces all cells with null values with an empty string (avoids future null pointer exceptions)
    public static void replaceNulls(String[][] cellValues) {
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
    public static String[] getKeys(String[][] cellValues){
        String[] keys = new String[cellValues[0].length];

        for(int i = 0; i < cellValues[0].length; i++){
            keys[i] = cellValues[1][i];
            for(int j = 2; j < 4; j++){
                keys[i] = keys[i] + cellValues[j][i];
            }
        }

        for (int i = 0; i < keys.length; i++){
            if(keys[i].equals("#")) keys[i] = "id";
            if(keys[i].equals("Common Name")) keys[i] = "commonName";
            if(keys[i].equals("Cultivar")) keys[i] = "cultivar";
            if(keys[i].equals("Source")) keys[i] = "source";
            if(keys[i].equals("Garden  Location")) keys[i] = "gardenLocation";
            if(keys[i].contains(" ")) keys[i] = keys[i].replace(" ","");
            if(keys[i].contains("=")) keys[i] = keys[i].replace("=", "");
            //if(keys[i].contains((UTF16.valueOf(0x00AE)))) keys[i].replaceAll(UTF16.valueOf(0x00AE), "");

        }

        return keys;
    }

    // Moves row by row through the 2D array and adds content for every flower paired with keys into a document
    // Uses the document to one at a time, add flower information into the database.
    public static void populateDatabase(String[][] cellValues){
        MongoClient mongoClient = new MongoClient();
        MongoDatabase test = mongoClient.getDatabase("test");
        MongoCollection plants = test.getCollection("plants");
        plants.drop();

        String[] keys = getKeys(cellValues);

        for (int i = 4; i < cellValues.length; i++){
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
    public static void printArray(String[] input){
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
    public static void printDoubleArray(String[][] input){
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
