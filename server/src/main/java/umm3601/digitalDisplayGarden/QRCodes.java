package umm3601.digitalDisplayGarden;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.Buffer;
import java.nio.file.Files;
import java.util.*;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

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
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Sorts;
import org.bson.Document;

/**
 * Created by carav008 on 3/25/17.
 */
public class QRCodes {
    //http://javapapers.com/core-java/java-qr-code/

    private PlantController plantController;

    public QRCodes(PlantController plantController)
    {
        this.plantController = plantController;
    }

    public static void main(String[] args)
            throws WriterException, IOException, NotFoundException {
        String qrCodeData = "Hello World!";
        String filePath = "QRCode.png";
        String charset = "UTF-8"; // or "ISO-8859-1"
        Map hintMap = new HashMap();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

        BufferedImage img = createQRCode(qrCodeData, charset, hintMap, 200, 200);
        //Files.write(filePath, img.getData());
        System.out.println("QR Code image created successfully!");

    }

    public static BufferedImage createQRFromBedURL(String url) throws IOException,WriterException{

        Map hintMap = new HashMap();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        return createQRCode(url, "UTF-8", hintMap, 300,300);
    }


    public void CreateQRCodesFromAllBeds(){
        //Get all unique beds from Database
        //Create URLs for all unique beds
        //Create QRCode BufferedImages for all URLs
        //Zip up all BufferedImages (We have to tar/zip them up because we don't want to spam the WCROC admin with QRCode file for every bed)
            //http://www.mkyong.com/java/how-to-convert-bufferedimage-to-byte-in-java/
            //http://www.oracle.com/technetwork/articles/java/compress-1565076.html
        //create Post request to client to download zip.

        String bedNames[] = plantController.getGardenLocations();
        final int numBeds = bedNames.length;

        String bedURLs[] = new String [numBeds];
        List<BufferedImage> qrCodeImages = new ArrayList<BufferedImage>();

        for(int i = 0; i < numBeds; i++) {
            bedURLs[i] = "http://localhost:9000/bed/" + bedNames[i];
            try {
                qrCodeImages.add(createQRFromBedURL(bedURLs[i]));
            }
            catch(IOException ioe)
            {
                //It would be really bad for a qrCodeImage to crash out and be null.
                //TODO: We should handle this better, what should we do?
                ioe.printStackTrace();
            }
            catch(WriterException we)
            {
                we.printStackTrace();
            }
        }

        //Archive and Compress into Zip

        final int BUFFER_SIZE = 1024;

        //FileOutputStream dest = new FileOutputStream("somefile"); //TODO: should not go to file
        CheckedOutputStream checksum = new CheckedOutputStream(null, new Adler32()); //null should be replaced with an OutputStream
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(checksum));

        //out.setMethod(ZipOutputStream.DEFLATED);

        byte dataBuffer[] = new byte[BUFFER_SIZE];

        //Get bytes from images and write them into the zipOutputStream
        byte imageData[];
        for(int i = 0; i < qrCodeImages.size(); i++)
        {
            ByteArrayOutputStream toByteStream = new ByteArrayOutputStream();
            try {
                ImageIO.write(qrCodeImages.get(i), "png", toByteStream); //formatName was jpg -- does changing it to png just _rename_ it a png or actually make it png?
                toByteStream.flush();
                imageData = toByteStream.toByteArray();
                //imageData contains the bytes for the qr images, now we just need to put it in the ZipOutputStream
                //and then send the zip stream to the client in a way it will recognize



            }
            catch(IOException ioe)
            {
                //TODO: handle better
                ioe.printStackTrace();
            }


        }


    }

    public static BufferedImage createQRCode(String qrCodeData, String charset, Map hintMap, int qrCodeheight, int qrCodewidth)
            throws WriterException, IOException {
        //Create the BitMatrix representing the QR code
        BitMatrix matrix = new MultiFormatWriter().encode(
                new String(qrCodeData.getBytes(charset), charset),
                BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, hintMap);

        //Create BufferedImages from the QRCode BitMatricies
        return MatrixToImageWriter.toBufferedImage(matrix);
    }

}
