package umm3601.digitalDisplayGarden;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CommentWriter {

    OutputStream outputStream;
    XSSFWorkbook workbook;
    XSSFSheet sheet;
    int rowCount;

    public CommentWriter(OutputStream outputStream) throws IOException{
        this.outputStream = outputStream;

        this.workbook = new XSSFWorkbook();
        this.sheet = workbook.createSheet("Comments");

        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("#");

        cell = row.createCell(1);
        cell.setCellValue("comment");

        cell = row.createCell(2);
        cell.setCellValue("timestamp");

        rowCount = 1;
    }

    /**
     * Adds the given information as a new row to the sheet.
     * @param id: plant ID number
     * @param comment: comment left by visitor
     * @param timestamp: time the user left the comment
     */
    public void writeComment(String id, String comment, Date timestamp){
        Row row = sheet.createRow(rowCount);

        Cell cell = row.createCell(0);
        cell.setCellValue(id);

        cell = row.createCell(1);
        cell.setCellValue(comment);

        cell = row.createCell(2);
        cell.setCellValue(timestamp.toString());

        rowCount++;
    }

    /**
     * Writes the spreadsheet to the outputstream, then closes it.
     * @throws IOException
     */
    public void complete() throws IOException{
        workbook.write(outputStream);
        outputStream.close();
    }
}
