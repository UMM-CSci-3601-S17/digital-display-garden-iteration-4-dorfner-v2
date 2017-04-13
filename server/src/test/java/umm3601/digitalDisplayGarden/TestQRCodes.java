package umm3601.digitalDisplayGarden;

import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import java.awt.image.BufferedImage;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.Buffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestQRCodes {
    static String fileName = "/tmp/testingtestcollecteddatawriter.xlsx";
    Map hintMap;

    public static String readQRCode(BufferedImage qr, Map hintMap)
            throws FileNotFoundException, IOException, NotFoundException {
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(qr)));
        Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap, hintMap);
        return qrCodeResult.getText();
    }

    @Before
    public void clearAndPopulateDatabase(){
        hintMap = new HashMap();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
    }

    @Test
    public void createQRCodeTest() throws IOException, WriterException, NotFoundException {
        BufferedImage qr =  QRCodes.createQRCode("https://www.google.com/", "UTF-8", hintMap, 300,300);
        BufferedImage qr2 =  QRCodes.createQRCode("https://www.amazon.com/", "UTF-8", hintMap, 150,150);
        assertEquals("The generated QR code has an incorrect URL", "https://www.google.com/", readQRCode(qr, hintMap));
        assertEquals("The generated QR code has an incorrect URL", "https://www.amazon.com/", readQRCode(qr2, hintMap));
    }

    @Test
    public void createQRFromBedURLTest() throws IOException, WriterException, NotFoundException {
        BufferedImage qr =  QRCodes.createQRFromBedURL("http://dorfner.congrue.xyz:2538/bed/1N");
        BufferedImage qr2 =  QRCodes.createQRFromBedURL("http://dorfner.congrue.xyz:2538/bed/2S");
        assertEquals("The 1N bed QR code has an incorrect URL",
                     "http://dorfner.congrue.xyz:2538/bed/1N",
                              readQRCode(qr, hintMap));
        assertEquals("The 2S bed QR code has an incorrect URL",
                     "http://dorfner.congrue.xyz:2538/bed/2S",
                             readQRCode(qr2, hintMap));
    }


//    @AfterClass
//    public static void cleanUp() throws IOException {
//        Files.delete(Paths.get(fileName));
//    }
}
