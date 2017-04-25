package umm3601.digitalDisplayGarden;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.AfterClass;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TestCollectedDataWriter {

    static String fileName = "/tmp/testingtestcollecteddatawriter.xlsx";

    @Test
    public void CollectedDataWriterProducesCorrectFiles() throws IOException, FileNotFoundException {

            // required to make the tests pass reliably across various boxes
            TimeZone.setDefault(TimeZone.getTimeZone("CST"));

            OutputStream outputStream = new FileOutputStream(fileName);
            CollectedDataWriter collectedDataWriter = new CollectedDataWriter(outputStream);

            collectedDataWriter.writeComment(
                    "16001",
                    "Rose",
                    "Boy Yellow",
                    "13",
                    "This flower is amazing",
                    new Date(0)
            );

            collectedDataWriter.writeComment(
                    "16003",
                    "Marigold",
                    "Kong Red",
                    "5",
                    "LOVE IT",
                    new Date(1000000)
            );

            collectedDataWriter.writeRating(
                    "16025",
                    "Pentunia",
                    "Angelface Perfectly Pink",
                    "6",
                    25,
                    2,
                    100,
                    15
            );

            collectedDataWriter.writeRating(
                    "16282",
                    "Angelonia",
                    "Aloha Raspberry",
                    "2N",
                    60,
                    62,
                    35,
                    115
            );

            int[] hourlyVisits = new int[] {4, 5, 6, 7, 8, 9, 1, 2, 3, 10, 11, 12, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
            collectedDataWriter.writeHourlyVisits(
                    hourlyVisits
            );

            int[][] dailyVisits = new int[12][31];
            for (int i = 0; i < 12; i++) {
                for (int j = 0; j < 31; j++) {
                    dailyVisits[i][j] = i + j;
                }
            }

            collectedDataWriter.writeDailyVisits( dailyVisits );

            collectedDataWriter.complete();

            InputStream inputStream = new FileInputStream(fileName);

            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet commentsSheet = workbook.getSheetAt(0);
            Sheet ratingsSheet = workbook.getSheetAt(1);
            Sheet hourlyVisitSheet = workbook.getSheetAt(2);
            Sheet dailyVisitSheet = workbook.getSheetAt(3);
//
//        assertEquals("#", commentsSheet.getRow(0).getCell(0));
//        assertEquals("common name", commentsSheet.getRow(0).getCell(1))

            Row row = commentsSheet.getRow(0);
            for (int j = 0; j < 7; j++) {
                Cell cell = row.getCell(j);
                switch (j) {
                    case 0:
                        assertEquals("#", cell.getStringCellValue());
                        break;
                    case 1:
                        assertEquals("common name", cell.getStringCellValue());
                        break;
                    case 2:
                        assertEquals("cultivar", cell.getStringCellValue());
                        break;
                    case 3:
                        assertEquals("garden location", cell.getStringCellValue());
                        break;
                    case 4:
                        assertEquals("comment", cell.getStringCellValue());
                        break;
                    case 5:
                        assertEquals("timestamp", cell.getStringCellValue());
                        break;
                    case 6:
                        assertNull(cell);
                        break;
                }
            }

            Row row1 = commentsSheet.getRow(1);
            for (int j = 0; j < 7; j++) {
                Cell cell = row1.getCell(j);
                switch (j) {
                    case 0:
                        assertEquals("16001", cell.getStringCellValue());
                        break;
                    case 1:
                        assertEquals("Rose", cell.getStringCellValue());
                        break;
                    case 2:
                        assertEquals("Boy Yellow", cell.getStringCellValue());
                        break;
                    case 3:
                        assertEquals("13", cell.getStringCellValue());
                        break;
                    case 4:
                        assertEquals("This flower is amazing", cell.getStringCellValue());
                        break;
                    case 5:
                        assertEquals("Wed Dec 31 18:00:00 CST 1969", cell.getStringCellValue());
                        break;
                    case 6:
                        assertNull(cell);
                        break;

                }
            }

            Row row2 = commentsSheet.getRow(2);
            for (int j = 0; j < 7; j++) {
                Cell cell = row2.getCell(j);
                switch (j) {
                    case 0:
                        assertEquals("16003", cell.getStringCellValue());
                        break;
                    case 1:
                        assertEquals("Marigold", cell.getStringCellValue());
                        break;
                    case 2:
                        assertEquals("Kong Red", cell.getStringCellValue());
                        break;
                    case 3:
                        assertEquals("5", cell.getStringCellValue());
                        break;
                    case 4:
                        assertEquals("LOVE IT", cell.getStringCellValue());
                        break;
                    case 5:
                        assertEquals("Wed Dec 31 18:16:40 CST 1969", cell.getStringCellValue());
                        break;
                    case 6:
                        assertNull(cell);
                        break;

                }
            }

            Row row3 = commentsSheet.getRow(3);
            assertNull(row3);

            Row ratingRow = ratingsSheet.getRow(0);
            for (int j = 0; j < 7; j++) {
                Cell cell = ratingRow.getCell(j);
                switch (j) {
                    case 0:
                        assertEquals("#", cell.getStringCellValue());
                        break;
                    case 1:
                        assertEquals("common name", cell.getStringCellValue());
                        break;
                    case 2:
                        assertEquals("cultivar", cell.getStringCellValue());
                        break;
                    case 3:
                        assertEquals("garden location", cell.getStringCellValue());
                        break;
                    case 4:
                        assertEquals("likes", cell.getStringCellValue());
                        break;
                    case 5:
                        assertEquals("dislikes", cell.getStringCellValue());
                        break;
                    case 6:
                        assertEquals("visits", cell.getStringCellValue());
                        break;
                    case 7:
                        assertEquals("comments", cell.getStringCellValue());
                        break;
                    case 8:
                        assertNull(cell);
                        break;

                }
            }

            Row ratingRow1 = ratingsSheet.getRow(1);
            for (int j = 0; j < 7; j++) {
                Cell cell = ratingRow1.getCell(j);
                switch (j) {
                    case 0:
                        assertEquals("16025", cell.getStringCellValue());
                        break;
                    case 1:
                        assertEquals("Pentunia", cell.getStringCellValue());
                        break;
                    case 2:
                        assertEquals("Angelface Perfectly Pink", cell.getStringCellValue());
                        break;
                    case 3:
                        assertEquals("6", cell.getStringCellValue());
                        break;
                    case 4:
                        assertEquals(25, cell.getNumericCellValue(), 0);
                        break;
                    case 5:
                        assertEquals(2, cell.getNumericCellValue(), 0);
                        break;
                    case 6:
                        assertEquals(100, cell.getNumericCellValue(), 0);
                        break;
                    case 7:
                        assertEquals(15, cell.getNumericCellValue(), 0);
                        break;
                    case 8:
                        assertNull(cell);
                        break;

                }
            }

            Row ratingRow2 = ratingsSheet.getRow(2);
            for (int j = 0; j < 7; j++) {
                Cell cell = ratingRow2.getCell(j);
                switch (j) {
                    case 0:
                        assertEquals("16282", cell.getStringCellValue());
                        break;
                    case 1:
                        assertEquals("Angelonia", cell.getStringCellValue());
                        break;
                    case 2:
                        assertEquals("Aloha Raspberry", cell.getStringCellValue());
                        break;
                    case 3:
                        assertEquals("2N", cell.getStringCellValue());
                        break;
                    case 4:
                        assertEquals(60, cell.getNumericCellValue(), 0);
                        break;
                    case 5:
                        assertEquals(62, cell.getNumericCellValue(), 0);
                        break;
                    case 6:
                        assertEquals(35, cell.getNumericCellValue(), 0);
                        break;
                    case 7:
                        assertEquals(115, cell.getNumericCellValue(), 0);
                        break;
                    case 8:
                        assertNull(cell);
                        break;

                }
            }

            Row ratingRow3 = ratingsSheet.getRow(3);
            assertNull(ratingRow3);

            // Test hourly data
            Row hourlyVisitHeadRow = hourlyVisitSheet.getRow(0);
            for (int j = 0; j < 2; j++) {
                Cell cell = hourlyVisitHeadRow.getCell(j);
                switch (j) {
                    case 0:
                        assertEquals("Hour", cell.getStringCellValue());
                        break;
                    case 1:
                        assertEquals("Visits", cell.getStringCellValue());
                        break;
                    case 2:
                        assertNull(cell);
                        break;
                }
            }

            for (int i = 0; i < hourlyVisits.length; i++) {
                Row currentRow = hourlyVisitSheet.getRow(i + 1);
                Cell cell = currentRow.getCell(1);
                assertEquals(hourlyVisits[i], (int) cell.getNumericCellValue());
            }

            // Test daily data
            Row dailyVisitHeadRow = dailyVisitSheet.getRow(0);
            String[] months = new String[]{"Jan","Feb","Mar","Apr","May","June","July","Aug","Sep","Oct","Nov","Dec"};
            for (int i = 0; i < 12; i++) {
                Cell cell = dailyVisitHeadRow.getCell(i + 1);
                assertEquals(months[i], cell.getStringCellValue());
            }

            for (int day = 0; day < 31; day++) {
                Row currentRow = dailyVisitSheet.getRow(day + 1);
                for (int month = 0; month < 12; month++) {
                    Cell cell = currentRow.getCell(month + 1);
                    assertEquals(dailyVisits[month][day], (int) cell.getNumericCellValue());
                }
            }
    }

    @AfterClass
    public static void cleanUp() throws IOException {
        Files.delete(Paths.get(fileName));
    }
}
