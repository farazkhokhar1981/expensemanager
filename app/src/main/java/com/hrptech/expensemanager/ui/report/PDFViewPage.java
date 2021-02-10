package com.hrptech.expensemanager.ui.report;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.RequiresApi;

import com.hrptech.expensemanager.R;
import com.hrptech.expensemanager.beans.PDFHeaderBeans;
import com.hrptech.expensemanager.beans.ReportInfoBeans;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class PDFViewPage {

    private BaseFont bfBold;
    private BaseFont bf;
    private int pageNumber = 0;
    Font f_urdu = FontFactory.getFont("assets/NotoNaskhArabic-Regular.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    Font f_english = FontFactory.getFont("assets/FreeSans.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);


    Activity activity;
String fileName;
    ArrayList<PDFHeaderBeans> headerBeansList = new ArrayList<>();
    ArrayList<ArrayList<PDFHeaderBeans>> rowBeansList = new ArrayList<>();
    float columnWidths[];
    ReportInfoBeans reportInfoBeans;
    ArrayList<PDFHeaderBeans> totalBeans = new ArrayList<>();
    public PDFViewPage(Activity activity, String fileName, float columnWidths[], ReportInfoBeans reportInfoBeans, ArrayList<PDFHeaderBeans> headerBeansList, ArrayList<ArrayList<PDFHeaderBeans>> rowBeansList){
        String pdfFilename = "";
        this.activity = activity;
        this.fileName = fileName;
        this.headerBeansList = headerBeansList;
        this.rowBeansList = rowBeansList;
        double qty = 0;
        double amount = 0;
        for(int index=0; index<rowBeansList.size(); index++){
            ArrayList<PDFHeaderBeans> beansList = rowBeansList.get(index);
            if(beansList.size()>0){
                for(int indexInner=0; indexInner<beansList.size(); indexInner++) {
                    PDFHeaderBeans beansQty = beansList.get(indexInner);
                    if(indexInner==2){
                        try{
                            qty+= Double.parseDouble(beansQty.getName());
                        }catch(Exception e){

                        }
                    }else if(indexInner==3){
                        try{
                            amount+= Double.parseDouble(beansQty.getName());
                        }catch(Exception e){

                        }
                    }

                }
            }
        }
        totalBeans.add(getTableValues("", Element.ALIGN_LEFT,BaseColor.WHITE,BaseColor.GRAY,BaseColor.BLACK,13, Font.BOLD));
        totalBeans.add(getTableValues("Total", Element.ALIGN_RIGHT,BaseColor.WHITE,BaseColor.GRAY,BaseColor.BLACK,13, Font.BOLD));
        totalBeans.add(getTableValues(new DecimalFormat("0").format(qty), Element.ALIGN_RIGHT,BaseColor.WHITE,BaseColor.GRAY,BaseColor.BLACK,13, Font.BOLD));
        totalBeans.add(getTableValues(new DecimalFormat("0").format(amount), Element.ALIGN_RIGHT,BaseColor.WHITE,BaseColor.GRAY,BaseColor.BLACK,13, Font.BOLD));
        this.columnWidths = columnWidths;
        this.reportInfoBeans = reportInfoBeans;


    }

    public PDFHeaderBeans getTableValues(String name, int align, BaseColor fColor, BaseColor bgColor, BaseColor brColor, int fontSize, int fontStyle) {
        PDFHeaderBeans beans = new PDFHeaderBeans();
        beans.setName(name);
        beans.setAlign(align);
        beans.setFontSize(fontSize);
        beans.setFontStyle(fontStyle);
        beans.setfColor(fColor);
        beans.setBgColor(bgColor);
        beans.setBrColor(brColor);
        return beans;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean isCreated(){

        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File newFile = new File(directory.getAbsolutePath()+""+ File.separator+"hrptechexpense"+ File.separator+"doc");
        if(!newFile.exists()){
            newFile.mkdirs();
        }

        if (newFile.canWrite()) {
            File backupDB = new File(newFile, fileName+".pdf");
            return createPDF(backupDB);
        }
        return false;
    }




    int yAxis = 0;


    private boolean createPDF(File backupDB) {
        boolean isTrue = false;
        Document doc = new Document();
        PdfWriter docWriter = null;
        initializeFonts();

        try {
            docWriter = PdfWriter.getInstance(doc, new FileOutputStream(backupDB));
            doc.addAuthor("betterThanZero");
            doc.addCreationDate();
            doc.addProducer();
            doc.addCreator("MySampleCode.com");
            doc.addTitle("Invoice");
            doc.setPageSize(PageSize.A4);

            doc.open();
            PdfContentByte cb = docWriter.getDirectContent();

            cb.setLineWidth(1f);
            cb.stroke();
            boolean beginPage = true;
            int y = 0;

            int headerIndexInt = 0;
            PdfPTable table = null;

            for (int i = 0; i < rowBeansList.size(); i++) {

                if (beginPage) {
                    generateTital(doc, reportInfoBeans,BaseColor.WHITE,BaseColor.BLACK);
                    beginPage = false;
                    headerIndexInt = 0;
                    table = new PdfPTable(columnWidths);
                    table.setTotalWidth(PageSize.A4.getWidth() - 40);
                    table.setLockedWidth(true);
                    generateTableHeader(table, headerBeansList);
                    y = 615;
                }
                generateTableHeader(table, rowBeansList.get(i));
                headerIndexInt++;
                //generateRow(cb);
//                generateDetail(doc, cb, i, y);
                y = y - 20;
                if (y < 50) {
                    ColumnText.showTextAligned(docWriter.getDirectContent(), Element.ALIGN_CENTER, footer(), 300, 30, 0);

                    doc.add(table);
                    table = null;
                    printPageNumber(cb);
                    doc.newPage();
                    beginPage = true;
                }
            }

            if (table != null) {
                if (headerIndexInt < 25) {
                    generateTableHeader(table, totalBeans);
                    doc.add(table);
                    table = new PdfPTable(columnWidths);
                    table.setTotalWidth(PageSize.A4.getWidth() - 40);
                    table.setLockedWidth(true);
                    generateTableHeader(table, totalBeans);
                    ColumnText.showTextAligned(docWriter.getDirectContent(), Element.ALIGN_CENTER, footer(), 300, 30, 0);

                } else {
                    ColumnText.showTextAligned(docWriter.getDirectContent(), Element.ALIGN_CENTER, footer(), 300, 30, 0);
                    generateTableHeader(table, totalBeans);
                    doc.add(table);

                }

            } else {
                if (headerIndexInt == 25) {
                    table = new PdfPTable(columnWidths);
                    table.setTotalWidth(PageSize.A4.getWidth() - 40);
                    table.setLockedWidth(true);
                    generateTableHeader(table, totalBeans);
                    doc.add(table);
                    ColumnText.showTextAligned(docWriter.getDirectContent(), Element.ALIGN_CENTER, footer(), 300, 30, 0);

                } else {
                    ColumnText.showTextAligned(docWriter.getDirectContent(), Element.ALIGN_CENTER, footer(), 300, 30, 0);
                    if(table==null){
                        table = new PdfPTable(columnWidths);
                        table.setTotalWidth(PageSize.A4.getWidth() - 40);
                        table.setLockedWidth(true);
                    }
                    generateTableHeader(table, totalBeans);
                    doc.add(table);

                }
            }
            printPageNumber(cb);
            isTrue = true;
        } catch (DocumentException dex) {
            dex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (doc != null) {
                doc.close();
            }
            if (docWriter != null) {
                docWriter.close();
            }
        }
        return isTrue;
    }

    private void generateTital(Document document, ReportInfoBeans reportInfoBeans,BaseColor bgColor,BaseColor fgColor) {
        try {
            // get input stream

            Bitmap bmp  = BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_logo);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image image = Image.getInstance(stream.toByteArray());
            image.scaleToFit(46, 46);

            Chunk chunk = new Chunk(image, 0, -36);
            document.add(chunk);
            Paragraph paragraph = new Paragraph();
            paragraph.setAlignment(Element.ALIGN_CENTER);

            PdfPTable table = new PdfPTable(1);
            table.addCell(getPdfCell(f_urdu,f_english,bgColor,fgColor,getCharactersEnglish(reportInfoBeans.getCompanyName()),false));



            if(!reportInfoBeans.getAddress().equalsIgnoreCase("")) {
                table.addCell(getPdfCell(f_urdu,f_english,bgColor,fgColor,getCharactersEnglish(reportInfoBeans.getAddress()),false));
            }
            if(!reportInfoBeans.getContact().equalsIgnoreCase("")) {
                table.addCell(getPdfCell(f_urdu,f_english,bgColor,fgColor,getCharactersEnglish(reportInfoBeans.getContact()),false));
            }
            if(!reportInfoBeans.getCustomerName().equalsIgnoreCase("")) {
                table.addCell(getPdfCell(f_urdu,f_english,bgColor,fgColor,getCharactersEnglish(reportInfoBeans.getCustomerName()),false));
            }
            if(!reportInfoBeans.getReportName().equalsIgnoreCase("")) {
                table.addCell(getPdfCell(f_urdu,f_english,bgColor,fgColor,getCharactersEnglish(reportInfoBeans.getReportName()),false));
            }
            if(!reportInfoBeans.getDateTime().equalsIgnoreCase("")) {
                table.addCell(getPdfCell(f_urdu,f_english,bgColor,fgColor,getCharactersEnglish(reportInfoBeans.getDateTime()),false));
            }

            document.add(table);

            LineSeparator separator = new LineSeparator();
            separator.setPercentage(59500f / 523f);
            Chunk linebreak = new Chunk(separator);
            paragraph.add(linebreak);
            document.add(paragraph);
            Paragraph paragraph1 = new Paragraph();
            paragraph1.add(new Chunk("\n"));
            document.add(paragraph1);



        } catch (Exception e) {
e.printStackTrace();
        }
    }

    String characters = "()ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-[]1234567890*+#@%&!";
    String charactersHindi = "ोे्िुपरकतचटॉंमनवलस,.यौैाीूबहगदजड़ॉृऍॅऋऔऐआईऊभङघधझढञऑठछथखऱफउइअएओऑँणळशष।य़ण";
    public String getCharactersEnglish(String title){

        int count = 0;
        int countNon = 0;

        byte[] list = new byte[0];
        try {
            list = title.getBytes("UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        char[] c = new char[0];
        try {
            c = new String(list, "UTF-8").toCharArray();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String english = "";
        String nonEnglish = "";
        for(int index=0; index<c.length; index++){
            String cht = c[index]+"";
            if(isEnglishCharacters(cht,characters) || isHindiCharacters(cht,charactersHindi)){
                if(count==1){
                    english+= "#";
                    count=0;
                }
                english+=cht;

            }else {
                if(count==0){
                    english+= "|";
                    count=1;
                }
                english+=cht;


            }
        }
    return english;
    }

    public boolean isEnglishCharacters(String c, String title){
        boolean val = false;
        Charset charset = Charset.forName("UTF8");
        byte chr[] = title.getBytes(charset);
        for(int index=0; index<chr.length; index++){
            String cht = (char)chr[index]+"";
            if(cht.contains(c)){
                return true;
            }
        }
        return val;
    }

    public boolean isHindiCharacters(String c, String title){
        boolean val = false;

        char chr[] = title.toCharArray();
        for(int index=0; index<chr.length; index++){
            String cht = chr[index]+"";
            if(cht.contains(c)){
                return true;
            }
        }
        return val;
    }

    public boolean isSindhiCharacters(String c, String title){
        boolean val = false;
        Charset charset = Charset.forName("UTF8");
        byte chr[] = title.getBytes(charset);
        for(int index=0; index<chr.length; index++){
            String cht = (char)chr[index]+"";
            if(cht.contains(c)){
                return true;
            }
        }
        return val;
    }



    public Chunk getChunk(Font f, BaseColor fColor, int Size, String title){


        f.setSize(Size);
        f.setColor(fColor);
        return new Chunk(title, f);
    }
    public PdfPCell getPdfCellPhrase(Phrase phrase){

        PdfPCell cell = new PdfPCell(phrase);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setUseDescender(true);
        cell.setColspan(2);
        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        return cell;
    }

    private Chunk getChunk(String title, int align, BaseColor color, int size, int style) {
        Chunk paragraph = new Chunk(title);
        BaseFont urName = null;
        try {
            urName = BaseFont.createFont("assets/feefonts.ttf", "UTF-8",BaseFont.EMBEDDED);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Font urFontName = new Font(urName, 12);
      //  paragraph.setFont(getFont("assets/OpenSans-Regular.ttf",size,style,color));
        paragraph.setFont(urFontName);
        return paragraph;
    }
public Font getFont(String fontFace, int fontSize, int fontStyle, BaseColor textColor){
    try {
        // Try to embed the font.
        // This doesn't work for type 1 fonts.
        return FontFactory.getFont(fontFace, BaseFont.IDENTITY_H,
                true, fontSize, fontStyle, textColor);
    } catch (ExceptionConverter e) {
        return FontFactory.getFont(fontFace, "UTF-8", true,
                fontSize, fontStyle, textColor);
    }
}
    private Phrase footer() {
        Font ffont = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL);
        Phrase p;
        p = new Phrase("Powered by hrptech.com for contact # +92332-2815822 / email : khokhar.faraz.a@gmail.com/info@hrptech.com", ffont);
        return p;
    }

    public void generateTableHeader(PdfPTable table, ArrayList<PDFHeaderBeans> headerList) {

        for (int headerIndex = 0; headerIndex < headerList.size(); headerIndex++) {
            PDFHeaderBeans beans = headerList.get(headerIndex);
            String nameVal = beans.getName();
            int align = beans.getAlign();
            int fontSize = beans.getFontSize();
            int fontStyle = beans.getFontStyle();
            BaseColor fColor = beans.getfColor();
            BaseColor bgColor = beans.getBgColor();
            BaseColor brColor = beans.getBrColor();


                table.addCell(getPdfCell(f_urdu,f_english,bgColor,fColor,getCharactersEnglish(nameVal),true));
           //  table.addCell(getCell(f_urdu,f_english,getCharactersEnglish(nameVal), align, fColor, bgColor, brColor, fontSize, fontStyle));
        }
    }
    public PdfPCell getPdfCell(Font f, Font f1, BaseColor bgColor, BaseColor fColor, String listVal, boolean isSetWithBoarder){
        Phrase phrase = new Phrase();
        String[]list = listVal.split("\\#");
        if(list.length>0) {

            for (int index = 0; index < list.length; index++) {
                String lng[] = new String[0];
                String english = "";
                String nonEnglish = "";
                if(list[index].contains("|")){
                    lng = list[index].split("\\|");
                    english =lng[0];
                    nonEnglish = lng[1];
                }else {
                    english = list[index];
                    nonEnglish="";
                }
                if(!english.equalsIgnoreCase("")){
                    phrase.add(getChunk(f1,fColor,20,english));
                }
                if(!nonEnglish.equalsIgnoreCase("")){
                    phrase.add(getChunk(f,fColor,20,nonEnglish));
                }
            }


        }

        PdfPCell cell = new PdfPCell(phrase);

        if(!isSetWithBoarder){
            cell.setBorder(Rectangle.NO_BORDER);
        }
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setUseDescender(true);
        cell.setBackgroundColor(bgColor);
        //cell.setColspan(2);
        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        return cell;
    }
    private PdfPCell getCell(Font f, Font f1, String name, int align, BaseColor fColor, BaseColor bgColor, BaseColor brColor, int fontSize, int style) {
        Phrase phrase = new Phrase();
        f1.setColor(fColor);
       // f1.setStyle(style);
        f.setColor(fColor);
       // f.setStyle(style);
        String[]list = name.split("\\#");
        if(list.length>0) {

            for (int index = 0; index < list.length; index++) {
                String lng[] = new String[0];
                String english = "";
                String nonEnglish = "";
                if(list[index].contains("|")){
                    lng = list[index].split("\\|");
                    english = lng[0];
                    nonEnglish = lng[1];
                }else {
                    english = list[index];
                    nonEnglish="";
                }
                if(!english.equalsIgnoreCase("")){
                    phrase.add(getChunk(f1,fColor,20,english));
                }
                if(!nonEnglish.equalsIgnoreCase("")){
                    phrase.add(getChunk(f,fColor,20,nonEnglish));
                }
            }


        }

        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(align);
        cell.setBackgroundColor(bgColor);
        cell.setBorderColor(brColor);
        cell.setFixedHeight(20);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setUseDescender(true);
      //  cell.setColspan(4);
        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        return cell;
    }

    private void printPageNumber(PdfContentByte cb) {

        cb.beginText();
        cb.setFontAndSize(bfBold, 8);
        cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Page No. " + (pageNumber + 1), 570, 25, 0);
        cb.endText();

        pageNumber++;

    }

    private void initializeFonts() {

        try {
            bfBold = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
