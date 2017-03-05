package umm3601.digitalDisplayGarden;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


public class ExcelParser {

    public static void returnsAnyThing() {
        try {
            FileInputStream file = new FileInputStream(new File("Accession list 2016.xls.xlsx"));

            HSSFWorkbook workbook = new HSSFWorkbook(file);

            HSSFSheet sheet = workbook.getSheetAt(0);

            System.out.println(sheet.getRow(1));
        } catch (Exception E) {
            System.out.println(E);
        }
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
