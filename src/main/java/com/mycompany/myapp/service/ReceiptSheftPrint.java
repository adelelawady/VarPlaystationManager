package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Product;
import com.mycompany.myapp.domain.Record;
import com.mycompany.myapp.domain.Sheft;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import javax.imageio.ImageIO;
import javax.swing.table.TableModel;

public class ReceiptSheftPrint implements Printable {

    public Sheft currentSheft = new Sheft();

    public ReceiptSheftPrint(Sheft rec) {
        currentSheft = rec;
    }

    SimpleDateFormat df = new SimpleDateFormat();
    String receiptDetailLine;
    public static final String pspace = "               "; // 15-spaces

    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        String lineDot = "----------------------------------------------------------------------------------";
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        int line = 10;

        Font font = new Font("", Font.BOLD, 10);
        // Font font = new Font("MS Gothic", Font.PLAIN, 10);

        if (pageIndex < 0 || pageIndex >= 1) {
            return Printable.NO_SUCH_PAGE;
        }

        g2d.drawString(String.format("%-25s", "	-   VAR   Sheft -"), 1, line);
        line += 13;
        int imagewidth = 160;
        int imageheight = 50;
        BufferedImage read;

        try {
            read = ImageIO.read(getClass().getResource("/image/img.jpeg"));
            g2d.drawImage(read, 5, line, imagewidth, imageheight, null); // draw image
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        g2d.setFont(font);
        line += 70;
        // font = new Font("Arial", Font.PLAIN, 9);
        // g2d.setFont(font);

        g2d.drawString(String.format("%-25s", "SHEFT  :  " + currentSheft.getUser().getFirstName()), 1, line);
        line += 13;
        g2d.drawString(String.format("%-25s", "START : " + currentSheft.getStart().toString()), 1, line);

        line += 13;
        g2d.drawString(String.format("%-25s", "END : " + currentSheft.getEnd().toString()), 1, line);

        font = new Font("", Font.BOLD, 12);
        line += 13;
        g2d.drawString(String.format("%-25s", "SUBTOTAL : " + currentSheft.getTotal_net_price().toString()), 1, line);

        line += 13;
        g2d.drawString(String.format("%-25s", "DISCOUNT : " + currentSheft.getTotal_discount().toString()), 1, line);

        line += 13;
        g2d.drawString(
            String.format("%-25s", "SYSTEM TOTAL : " + currentSheft.getTotal_net_price_after_discount_system().toString()),
            1,
            line
        );

        line += 13;
        g2d.drawString(String.format("%-25s", " TOTAL : " + currentSheft.getTotal_net_price_after_discount().toString()), 1, line);

        return Printable.PAGE_EXISTS;
    }

    String toCodedString(String strValue) {
        return new String(strValue.getBytes(), StandardCharsets.UTF_8);
    }
}
