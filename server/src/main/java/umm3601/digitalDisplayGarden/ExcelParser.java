package umm3601.digitalDisplayGarden;

import java.io.FileInputStream;
import java.util.Iterator;
import org.apache.poi.*;


public class ExcelParser {

    public static String returnsAnyThing() {
        System.out.println("I return anything!");
        return "I return anything!";
    }
//    FileInputStream inp = new FileInputStream( file );
//    Workbook workbook = WorkbookFactory.create( inp );
//
//    // Get the first Sheet.
//    Sheet sheet = workbook.getSheetAt( 0 );
//
//    // Start constructing JSON.
//    JSONObject json = new JSONObject();
//
//    // Iterate through the rows.
//    JSONArray rows = new JSONArray();
//    for ( Iterator<Row> rowsIT = sheet.rowIterator(); rowsIT.hasNext(); )
//    {
//        Row row = rowsIT.next();
//        JSONObject jRow = new JSONObject();
//
//        // Iterate through the cells.
//        JSONArray cells = new JSONArray();
//        for ( Iterator<Cell> cellsIT = row.cellIterator(); cellsIT.hasNext(); )
//        {
//            Cell cell = cellsIT.next();
//            cells.put( cell.getStringCellValue() );
//        }
//        jRow.put( "cell", cells );
//        rows.put( jRow );
//    }

    // Create the JSON.
//    json.put( "rows", rows );

// Get the JSON text.
//return json.toString();
}
