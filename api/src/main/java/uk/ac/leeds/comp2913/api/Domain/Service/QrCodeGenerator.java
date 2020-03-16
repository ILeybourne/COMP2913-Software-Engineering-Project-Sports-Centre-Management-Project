package uk.ac.leeds.comp2913.api.Domain.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class QrCodeGenerator {

    private int width, height;

    public QrCodeGenerator(int w, int h) {
        width = w;
        height = h;
    }

    public void generateQRCodeImage(String qrCodeText, String outputPath) {
        // Create bit matrix of barcode
        BitMatrix bitMatrix;
        try {
            bitMatrix = new QRCodeWriter().encode(qrCodeText, BarcodeFormat.QR_CODE, width, height);
        } catch (WriterException e) {
            System.err.println("Could not encode qr code\n" + e);
            return;
        }

        // Write image to file
        File outputFile = new File(outputPath);
        try {
            ImageIO.write(MatrixToImageWriter.toBufferedImage(bitMatrix), "jpg", outputFile);
        } catch (IOException e) {
            System.err.println("File could not be generates\n" + e);
        }
    }
}