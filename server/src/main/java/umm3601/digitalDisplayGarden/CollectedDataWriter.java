package umm3601.digitalDisplayGarden;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CollectedDataWriter {

    OutputStream outputStream;
    XSSFWorkbook workbook;
    XSSFSheet commentsSheet;
    XSSFSheet ratingsSheet;
    XSSFSheet timesSheet;
    int commentCount;
    int ratingCount;

    public CollectedDataWriter(OutputStream outputStream) throws IOException{
        this.outputStream = outputStream;

        this.workbook = new XSSFWorkbook();
        this.commentsSheet = workbook.createSheet("Comments");
        this.ratingsSheet = workbook.createSheet("Counts");
        this.timesSheet = workbook.createSheet("TimeStamps");

        Row commentRow = commentsSheet.createRow(0);
        Cell cell = commentRow.createCell(0);
        cell.setCellValue("#");

        cell = commentRow.createCell(1);
        cell.setCellValue("common name");

        cell = commentRow.createCell(2);
        cell.setCellValue("cultivar");

        cell = commentRow.createCell(3);
        cell.setCellValue("garden location");

        cell = commentRow.createCell(4);
        cell.setCellValue("comment");

        cell = commentRow.createCell(5);
        cell.setCellValue("timestamp");

        commentCount = 1;

        Row ratingRow = ratingsSheet.createRow(0);
        cell = ratingRow.createCell(0);
        cell.setCellValue("#");

        cell = ratingRow.createCell(1);
        cell.setCellValue("common name");

        cell = ratingRow.createCell(2);
        cell.setCellValue("cultivar");

        cell = ratingRow.createCell(3);
        cell.setCellValue("garden location");

        cell = ratingRow.createCell(4);
        cell.setCellValue("likes");

        cell = ratingRow.createCell(5);
        cell.setCellValue("dislikes");

        cell = ratingRow.createCell(6);
        cell.setCellValue("visits");

        cell = ratingRow.createCell(7);
        cell.setCellValue("comments");

        ratingCount = 1;

        Row timeRow = timesSheet.createRow(0);
        cell = timeRow.createCell(0);
        cell.setCellValue("Hour");

        cell = timeRow.createCell(1);
        cell.setCellValue("Visits");

    }

    /**
     * Adds the given information as a new row to the sheet.
     * @param id: plant ID number
     * @param comment: comment left by visitor
     * @param timestamp: time the user left the comment
     */
    public void writeComment(String id, String commonName, String cultivar, String gardenLocation, String comment, Date timestamp){
        Row row = commentsSheet.createRow((short) commentCount);

        Cell cell = row.createCell(0);
        cell.setCellValue(id);

        cell = row.createCell(1);
        cell.setCellValue(commonName);

        cell = row.createCell(2);
        cell.setCellValue(cultivar);

        cell = row.createCell(3);
        cell.setCellValue(gardenLocation);

        cell = row.createCell(4);

        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);
        cell.setCellStyle(style);
        cell.setCellValue(comment);

        cell = row.createCell(5);
        cell.setCellValue(timestamp.toString());

        commentCount++;
    }
    /**
     * Adds the given information as a new row to the sheet.
     * @param id: plant ID number
     * @param commonName: common name of flower
     * @param cultivar: cultivar name of flower
     * @param gardenLocation: bed number of flower
     * @param likes: number of likes on the flower
     * @param dislikes: number of dislikes on the flower
     * @param visits: number of visits on the flower
     * @param comments: number of comments on the flower
     */
    public void writeRating(String id, String commonName, String cultivar, String gardenLocation, int likes, int dislikes, int visits, int comments){
        Row row = ratingsSheet.createRow(ratingCount);

        Cell cell = row.createCell(0);
        cell.setCellValue(id);

        cell = row.createCell(1);
        cell.setCellValue(commonName);

        cell = row.createCell(2);
        cell.setCellValue(cultivar);

        cell = row.createCell(3);
        cell.setCellValue(gardenLocation);

        cell = row.createCell(4);
        cell.setCellValue(likes);

        cell = row.createCell(5);
        cell.setCellValue(dislikes);

        cell = row.createCell(6);
        cell.setCellValue(visits);

        cell = row.createCell(7);
        cell.setCellValue(comments);

        ratingCount++;
    }

    public void writeTimes(int[] timeCounts) {
        for (int i = 0; i < timeCounts.length; i++) {
            Row row = timesSheet.createRow(i + 1);
            Cell cell = row.createCell(0);
            cell.setCellValue(i + " - " + (i + 1));

            cell = row.createCell(1);
            cell.setCellValue(timeCounts[i]);
        }
    }

    /**
     * Writes the spreadsheet to the outputstream, then closes it.
     * @throws IOException
     */
    public void complete() throws IOException{

        for(int i=0; i<6; i++) {
            String header = commentsSheet.getRow(0).getCell(i).getStringCellValue();
            if(!header.equals("comment")){
                commentsSheet.autoSizeColumn(i);}
            else {
                commentsSheet.setColumnWidth(i,100*256);
            }
        }
        for(int i=0; i<8; i++){
            ratingsSheet.autoSizeColumn(i);
        }

        for(int i=0; i<2; i++){
            timesSheet.autoSizeColumn(i);
        }

        workbook.write(outputStream);
        outputStream.close();
    }
}
