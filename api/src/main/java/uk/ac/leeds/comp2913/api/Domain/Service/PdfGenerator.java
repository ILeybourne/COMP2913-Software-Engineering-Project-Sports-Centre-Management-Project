package uk.ac.leeds.comp2913.api.Domain.Service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PdfGenerator {

    private Document document;
    private String outputPath;

    public PdfGenerator(String oP) {
        document = new Document();
        if (oP.endsWith(".pdf"))
            outputPath = oP;
        else
            outputPath = oP + ".pdf";
    }

    public static void main(String[] args) throws IOException {

    }

    public void createDocument() {
        // Create pdf writer instance
        try {
            PdfWriter.getInstance(document, new FileOutputStream(outputPath));
        } catch (DocumentException e) {
            System.err.println("Invalid document\n" + e);
        } catch (FileNotFoundException e) {
            System.err.println("Invalid output path\n" + e);
        }

        // Open document
        document.open();
    }

    public void addText(String font, int size, BaseColor color, String text) {
        try {
            document.add(new Paragraph(new Chunk(text, FontFactory.getFont(font, size, color))));
        } catch (DocumentException e) {
            System.err.println("Chunk could not be added\n" + e);
        }
    }

    public void addImage(String imagePath) {
        try {
            document.add(Image.getInstance(imagePath));
        } catch (DocumentException e) {
            System.err.println("Image could not be added to the document\n" + e);
        } catch (IOException e) {
            System.err.println("Image path does not exist\n" + e);
        }
    }

    public void closeDocument() {
        document.close();
    }
}