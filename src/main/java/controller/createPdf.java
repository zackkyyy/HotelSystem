package controller;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javax.print.*;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.*;
import java.text.DecimalFormat;

/**
 * Created by IntelliJ IDEA.
 *
 * @User: Zacky Kharboutli
 * @Date: 2018-05-09
 * @Project : HotelSystem
 */


public class createPdf  {

    public static void main(String []args) throws IOException, DocumentException, PrintException, PrinterException {
        createPdf createPdf = new createPdf();
        createPdf.createPdfRec();
        createPdf.Print();
    }
    public void createPdfRec() throws FileNotFoundException, DocumentException {
       com.itextpdf.text.Document layoutDocument = new com.itextpdf.text.Document(PageSize.A6);
        PdfWriter.getInstance(layoutDocument, new FileOutputStream("file.pdf"));
        layoutDocument.open();
        layoutDocument.add(new Paragraph("INVOICE"));
        layoutDocument.add(new Paragraph("Lineaus Hotel"));
        layoutDocument.add(new Paragraph("Växjö/Kalmar"));
        layoutDocument.add(new Paragraph("   "));
        layoutDocument.add(new Paragraph("   "));
        layoutDocument.add(new Paragraph(" Customer :" ));
        layoutDocument.add(new Paragraph("   "));
        layoutDocument.add(new Paragraph("   "));

        PdfPTable table1 = new PdfPTable(2);
        DecimalFormat df = new DecimalFormat("200");
        double total = 0;
        table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table1.setWidthPercentage(110f);
        table1.getDefaultCell().setPadding(3);
        table1.addCell("Arrival");
        table1.addCell("dsadsa");
        table1.addCell("Departure");
        table1.addCell("$");
        table1.addCell("Number of nights");
        table1.addCell("$0.00");
        table1.addCell("Number of guest");
        table1.addCell("$0.00");
        table1.addCell("Number of nights");
        table1.addCell("$0.00");
        table1.addCell("Total");
        table1.addCell(df.format(total));
        layoutDocument.add(table1);

        layoutDocument.close();


    }
public void Print() throws PrintException, IOException, PrinterException {

   /* boolean showPrintDialog=true;
    PrinterJob printJob = PrinterJob.getPrinterJob ();
    printJob.setJobName ("file.pdf");
    //printJob.setPrintable();
    try {
        if (showPrintDialog) {
            if (printJob.printDialog()) {
                printJob.print();
            }
        }
        else
            printJob.print ();
    } catch (Exception PrintException) {
        PrintException.printStackTrace();
    }
*/
    PrinterJob printerJob = PrinterJob.getPrinterJob();

    PrintService printService = null;
    if(printerJob.printDialog())
    {
        printService = printerJob.getPrintService();
    }
    DocFlavor docType = DocFlavor.INPUT_STREAM.AUTOSENSE;
    File file = new File("file.pdf");
    FileInputStream fis = new FileInputStream(file);


    DocPrintJob printJob = printService.createPrintJob();
    final byte[] byteStream = fis.toString().getBytes();
    Doc documentToBePrinted = new SimpleDoc(new ByteArrayInputStream(byteStream), docType, null);
    printJob.print(documentToBePrinted, null);



}
}



