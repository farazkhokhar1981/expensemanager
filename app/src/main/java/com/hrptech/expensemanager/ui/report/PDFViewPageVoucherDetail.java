package com.hrptech.expensemanager.ui.report;

import android.app.Activity;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.RequiresApi;

import com.hrptech.expensemanager.R;
import com.hrptech.expensemanager.beans.TransactionBeans;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class PDFViewPageVoucherDetail {

    private BaseFont bfBold;
    private BaseFont bf;
    private int pageNumber = 0;
    Font f_urdu = FontFactory.getFont("assets/NotoNaskhArabic-Regular.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    Font f_english = FontFactory.getFont("assets/FreeSans.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

    Activity activity;
String fileName;
    TransactionBeans beans;
    public PDFViewPageVoucherDetail(Activity activity, String fileName, TransactionBeans beans){
        this.activity = activity;
        this.fileName = fileName;
        this.beans = beans;


    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean isCreated(){

        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File newFile = new File(directory.getAbsolutePath()+""+ File.separator+"hrptechKhata"+ File.separator+"doc");
        if(!newFile.exists()){
            newFile.mkdirs();
        }

        if (newFile.canWrite()) {
            File backupDB = new File(newFile, fileName + ".pdf");
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

            generateTital(doc, beans,BaseColor.WHITE,BaseColor.BLACK);
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

    private void generateTital(Document document, TransactionBeans beans,BaseColor bgColor,BaseColor fgColor) {
        try {
            // get input stream

//            Bitmap bmp  = BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_logo);
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
//            Image image = Image.getInstance(stream.toByteArray());
//            image.scaleToFit(40, 40);
//
//            Chunk chunk = new Chunk(image, 0, -36);
           // document.add(chunk);
            Paragraph paragraph = new Paragraph();
            paragraph.setAlignment(Element.ALIGN_CENTER);


            PdfPTable table = new PdfPTable(1);
            table.setWidthPercentage(100);
            table.addCell(getPdfCell(f_urdu,f_english,bgColor,fgColor,46,Font.BOLD,Element.ALIGN_MIDDLE,getCharactersEnglish(beans.getCashType()).toUpperCase(),true));
            document.add(table);


            float colum[] = {300,300};
            PdfPTable table2 = new PdfPTable(colum);
            table2.setWidthPercentage(100);

            table2.addCell(getPdfCell(f_urdu,f_english,bgColor,fgColor,22,Font.BOLD,Element.ALIGN_LEFT,getCharactersEnglish(activity.getResources().getString(R.string.transaction_no)),false));
            table2.addCell(getPdfCell(f_urdu,f_english,bgColor,fgColor,20,Font.NORMAL,Element.ALIGN_RIGHT,getCharactersEnglish(beans.getId()),false));

                table2.addCell(getPdfCell(f_urdu,f_english,bgColor,fgColor,22,Font.BOLD,Element.ALIGN_LEFT,getCharactersEnglish(activity.getResources().getString(R.string.customer_name)),false));
                table2.addCell(getPdfCell(f_urdu,f_english,bgColor,fgColor,20,Font.NORMAL,Element.ALIGN_RIGHT,getCharactersEnglish(beans.getName()),false));

                table2.addCell(getPdfCell(f_urdu,f_english,bgColor,fgColor,22,Font.BOLD,Element.ALIGN_LEFT,getCharactersEnglish(activity.getResources().getString(R.string.transaction_date)),false));
                table2.addCell(getPdfCell(f_urdu,f_english,bgColor,fgColor,20,Font.NORMAL,Element.ALIGN_RIGHT,getCharactersEnglish(beans.getDate()),false));

                table2.addCell(getPdfCell(f_urdu,f_english,bgColor,fgColor,22,Font.BOLD,Element.ALIGN_LEFT,getCharactersEnglish(activity.getResources().getString(R.string.transaction_Description)),false));
                table2.addCell(getPdfCell(f_urdu,f_english,bgColor,fgColor,20,Font.BOLD,Element.ALIGN_RIGHT,getCharactersEnglish(beans.getDescription()),false));

                table2.addCell(getPdfCell(f_urdu,f_english,bgColor,fgColor,32,Font.BOLD,Element.ALIGN_LEFT,getCharactersEnglish(activity.getResources().getString(R.string.balance)),true));
                String type = beans.getType();
                if(type.equalsIgnoreCase("dr")){
                    table2.addCell(getPdfCell(f_urdu,f_english,bgColor,fgColor,30,Font.NORMAL,Element.ALIGN_RIGHT,getCharactersEnglish(beans.getBalance()),true));
                }else {
                    table2.addCell(getPdfCell(f_urdu,f_english,bgColor,fgColor,30,Font.NORMAL,Element.ALIGN_RIGHT,getCharactersEnglish(beans.getBalance()),true));
                }

            document.add(table2);


            paragraph.add(getLineSeparator());
            document.add(paragraph);
            Paragraph paragraph1 = new Paragraph();
            paragraph1.add(new Chunk("\n"));
            document.add(paragraph1);



        } catch (Exception e) {
e.printStackTrace();
        }
    }

    public Chunk getLineSeparator(){
        LineSeparator separator = new LineSeparator();
        separator.setPercentage(59500f / 523f);
        Chunk linebreak = new Chunk(separator);
        return linebreak;
    }

    String characters = "()ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-[]1234567890*+#@%&!";
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
            if(isEnglishCharacters(cht,characters)){
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



    public Chunk getChunk(Font f, BaseColor fColor, int Size, int style, String title){


        f.setSize(Size);
        f.setColor(fColor);
        f.setStyle(style);
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

    public PdfPCell getPdfCell(Font f, Font f1, BaseColor bgColor, BaseColor fColor, int size, int alignment, int style, String listVal, boolean isSetWithBoarder){
        Phrase phrase = new Phrase();
        String[]list = listVal.split("\\#");
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
                    phrase.add(getChunk(f1,fColor,size,style,english));
                }
                if(!nonEnglish.equalsIgnoreCase("")){
                    phrase.add(getChunk(f,fColor,size,style,nonEnglish));
                }
            }


        }
        PdfPCell cell = new PdfPCell(phrase);

        if(!isSetWithBoarder){
            cell.setBorder(Rectangle.NO_BORDER);
        }
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setHorizontalAlignment(alignment);
        cell.setUseDescender(true);
        cell.setBackgroundColor(bgColor);
        //cell.setColspan(2);
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
