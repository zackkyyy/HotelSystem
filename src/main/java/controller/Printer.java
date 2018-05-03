package controller;

import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

public class Printer implements Printable {
    public static void main(String[] args) {

        Printer example1 = new Printer();
        example1.doProint();
    }

    private final double INCH = 72;



    public void doProint(){
        PrinterJob printJob = PrinterJob.getPrinterJob();

        //--- Set the printable class to this one since we
        //--- are implementing the Printable interface
        printJob.setPrintable(this);

        //--- Show a print dialog to the user. If the user
        //--- click the print button, then print otherwise
        //--- cancel the print job
        if (printJob.printDialog()) {
            try {
                printJob.print();
            } catch (Exception PrintException) {
                PrintException.printStackTrace();
            }
        }
    }

    /**
     * Method: print
     * <p>
     *
     * This class is responsible for rendering a page using the provided
     * parameters. The result will be a grid where each cell will be half an
     * inch by half an inch.
     *
     * @param g
     *            a value of type Graphics
     * @param pf
     *            a value of type PageFormat
     * @param page
     *            a value of type int
     * @return a value of type int
     */
    public int print(Graphics g, PageFormat pf, int page )
            throws PrinterException {

        // We have only one page, and 'page'
        // is zero-based
        if (page > 0) {
            return NO_SUCH_PAGE;
        }

        // User (0,0) is typically outside the
        // imageable area, so we must translate
        // by the X and Y values in the PageFormat
        // to avoid clipping.
        Graphics2D g2d = (Graphics2D)g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());

        // Now we perform our rendering

        g.drawString("hello World", 100, 100);

        // tell the caller that this page is part
        // of the printed document
        return PAGE_EXISTS;
    }

} //Example1