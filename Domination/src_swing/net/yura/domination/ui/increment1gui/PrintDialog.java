// Yura Mamyrin, Group D

package net.yura.domination.ui.increment1gui;

import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.awt.Graphics;
import java.awt.print.PrinterException;
import net.yura.domination.engine.RiskUtil;

/**
 * <p> Print Dialog </p>
 * @author Yura Mamyrin
 */

public class PrintDialog {
    private static String printJobName = "Print Job";
    // number of pages
    private static int numberOfPages = 1;

    public static void print() {
        PrinterJob pJob = PrinterJob.getPrinterJob();
        if (pJob.printDialog()) {
            // if user have pressed OK in native print dialog
            pJob.setJobName(printJobName);
            Printer printer = new Printer();
            pJob.setPageable(printer);
            pJob.setPrintable(printer);
            try {
                pJob.print();
            }
            catch (PrinterException ex) {
                RiskUtil.printStackTrace(ex);
            }
        }
    }

    /** Provides printing support */
    private static class Printer implements Pageable, Printable {
        public int getNumberOfPages() {
            return numberOfPages;
        }

        public PageFormat getPageFormat(int pageIndex) throws IndexOutOfBoundsException {
            // the same format for all pages
            PageFormat pf = new PageFormat();
            pf.setPaper(new java.awt.print.Paper());
            return pf;
        }

        public Printable getPrintable(int pageIndex) throws IndexOutOfBoundsException {
            return this;
        }

        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
            if (pageIndex >= numberOfPages) {
                return Printable.NO_SUCH_PAGE;
            }
            graphics.translate((int)pageFormat.getImageableX(), (int)pageFormat.getImageableY());
            int paperWidth = (int)pageFormat.getImageableWidth();
            int paperHeight = (int)pageFormat.getImageableHeight();
            // Write your paint code here
            return Printable.PAGE_EXISTS;
        }
    }
}
