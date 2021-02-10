package com.hrptech.expensemanager.ui.report;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.hrptech.expensemanager.R;
import com.hrptech.expensemanager.beans.ReportHeaderBeans;
import com.hrptech.expensemanager.beans.ReportInfoBeans;
import com.hrptech.expensemanager.beans.ReportRowBeans;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class PdfView {
    Activity activity;
    private BaseFont bfBold;
    public PdfView(Activity activity){
        this.activity = activity;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public PdfDocument getPdfView(String sometext){
        // create a new document
        // create a new document
        PdfDocument document = new PdfDocument();
        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        canvas.drawCircle(50, 50, 30, paint);
        paint.setColor(Color.BLACK);
        canvas.drawText(sometext, 80, 50, paint);
        //canvas.drawt
        // finish the page
        document.finishPage(page);
// draw text on the graphics object of the page
        // Create Page 2
        pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 2).create();
        page = document.startPage(pageInfo);
        canvas = page.getCanvas();
        paint = new Paint();
        paint.setColor(Color.BLUE);
        canvas.drawCircle(100, 100, 100, paint);
        document.finishPage(page);
        // write the document content
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/mypdf/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path+"test-2.pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(activity, "Done", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e("main", "error "+e.toString());
            Toast.makeText(activity, "Something wrong: " + e.toString(),  Toast.LENGTH_LONG).show();
        }
        // close the document
        document.close();
        return document;
    }

    public int getColor(int color){
        return activity.getResources().getColor(color);
    }
    public Boolean write(String fname, ReportInfoBeans reportInfoBeans, ArrayList<ReportHeaderBeans> headerBeans, ArrayList<ReportRowBeans> transactionRow) {
        try {
            PdfWriter docWriter = null;
                    String fpath = "/sdcard/" + fname + ".pdf";
            String path = activity.getExternalFilesDir(null).getAbsolutePath()+"\\hrptechexpense";
            File sd = new File(path);
            File data = Environment.getDataDirectory();

            if(!sd.mkdir()){
                sd.mkdirs();
            }
            if (sd.canWrite()) {
                File backupDB = new File(sd, fname + ".pdf");
                initializeFonts();

                Document document = new Document(PageSize.A4);

                try {
                    docWriter = PdfWriter.getInstance(document,
                            new FileOutputStream(backupDB));
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
                PdfContentByte cb = new PdfContentByte(docWriter);
                document.open();

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

                paragraph.add(getChunk(reportInfoBeans.getCompanyName(),Element.ALIGN_CENTER,getColor(R.color.colorBlack),22,Font.BOLD));
                paragraph.add(new Chunk("\n"));
                paragraph.add(getChunk("",Element.ALIGN_CENTER,getColor(R.color.colorGray),3,Font.NORMAL));
                paragraph.add(getChunk(reportInfoBeans.getAddress(),Element.ALIGN_CENTER,getColor(R.color.colorGray),14,Font.NORMAL));
                paragraph.add(new Chunk("\n"));
                paragraph.add(getChunk("",Element.ALIGN_CENTER,getColor(R.color.colorGray),3,Font.NORMAL));
                paragraph.add(getChunk("Contact # ",Element.ALIGN_CENTER,getColor(R.color.colorLiteGray),10,Font.BOLD));
                paragraph.add(getChunk(""+reportInfoBeans.getContact(),Element.ALIGN_CENTER,getColor(R.color.colorLiteGray),9,Font.NORMAL));
                paragraph.add(getChunk(" / Email : ",Element.ALIGN_CENTER,getColor(R.color.colorLiteGray),10,Font.BOLD));
                paragraph.add(getChunk(""+reportInfoBeans.getEmail(),Element.ALIGN_CENTER,getColor(R.color.colorLiteGray),9,Font.NORMAL));

                paragraph.add(new Chunk("\n"));


                LineSeparator separator = new LineSeparator();
                separator.setPercentage(59500f / 523f);
                Chunk linebreak = new Chunk(separator);
                paragraph.add(new Chunk("\n"));
                paragraph.add(linebreak);
                document.add(paragraph);
                Paragraph paragraphForReportInfo = new Paragraph();
                paragraphForReportInfo.setAlignment(Element.ALIGN_LEFT);

                paragraphForReportInfo.add(new Chunk("\n"));
                paragraphForReportInfo.add(getChunk("Report Name : ",Element.ALIGN_LEFT,getColor(R.color.colorLiteGray),11,Font.BOLD));
                paragraphForReportInfo.add(getChunk(""+reportInfoBeans.getReportName(),Element.ALIGN_LEFT,getColor(R.color.colorLiteGray),10,Font.NORMAL));
                paragraphForReportInfo.add(new Chunk("\n"));
                paragraphForReportInfo.add(getChunk("Print Date/Time : ",Element.ALIGN_LEFT,getColor(R.color.colorLiteGray),11,Font.BOLD));
                paragraphForReportInfo.add(getChunk(""+reportInfoBeans.getDateTime(),Element.ALIGN_LEFT,getColor(R.color.colorLiteGray),10,Font.NORMAL));
                paragraphForReportInfo.add(new Chunk("\n"));
                paragraphForReportInfo.add(getChunk("",Element.ALIGN_CENTER,getColor(R.color.colorGray),3,Font.NORMAL));
                if(!reportInfoBeans.getCustomerName().equalsIgnoreCase("")) {
                    paragraphForReportInfo.add(getChunk("Customer Name : ", Element.ALIGN_LEFT, getColor(R.color.colorLiteGray), 11, Font.BOLD));
                    paragraphForReportInfo.add(getChunk("" + reportInfoBeans.getCustomerName(), Element.ALIGN_LEFT, getColor(R.color.colorLiteGray), 10, Font.NORMAL));
                    paragraphForReportInfo.add(new Chunk("\n"));
                    paragraphForReportInfo.add(getChunk("", Element.ALIGN_CENTER, getColor(R.color.colorGray), 3, Font.NORMAL));
                }
                document.add(paragraphForReportInfo);




                float[] columnWidths = new float[headerBeans.size()];
                for(int headerIndex = 0; headerIndex<headerBeans.size(); headerIndex++){
                    ReportHeaderBeans hBeans = headerBeans.get(headerIndex);
                    float size = hBeans.getColumnSize();
                    columnWidths[headerIndex] = size;
                }
                //create PDF table with the given widths
                PdfPTable table = new PdfPTable(columnWidths);
                // set table width a percentage of the page width
                table.setTotalWidth(500f);



                for(int headerIndex = 0; headerIndex<headerBeans.size(); headerIndex++) {
                    ReportHeaderBeans hBeans = headerBeans.get(headerIndex);
                    String name = hBeans.getName();
                    int align = hBeans.getAlign();
                    table.addCell(getCell(name, align, activity.getResources().getColor(R.color.colorWhite), activity.getResources().getColor(R.color.colorGray), activity.getResources().getColor(R.color.colorWhite), 14,Font.BOLD));
                }
                //                table.addCell(getCell(headerBeans.getDescription(),Element.ALIGN_LEFT,activity.getResources().getColor(R.color.colorWhite),activity.getResources().getColor(R.color.colorGray),activity.getResources().getColor(R.color.colorWhite),11));
  //              table.addCell(getCell(headerBeans.getBalance(),Element.ALIGN_RIGHT,activity.getResources().getColor(R.color.colorWhite),activity.getResources().getColor(R.color.colorGray),activity.getResources().getColor(R.color.colorWhite),11));

                table.setHeaderRows(1);

                for(int rowIndex=0; rowIndex < transactionRow.size(); rowIndex++ ){
                    ReportRowBeans tBeans = transactionRow.get(rowIndex);
                    ArrayList<ReportHeaderBeans> listOfStr = tBeans.getListOfStr();
                    String type = tBeans.getType();
                    for(int row=0; row<listOfStr.size(); row++){
                        ReportHeaderBeans hBeans = listOfStr.get(row);
                        String name = hBeans.getName();
                        int align = hBeans.getAlign();
                        int color = hBeans.getColor();
                        table.addCell(getCell(name, align, activity.getResources().getColor(R.color.colorGray), activity.getResources().getColor(R.color.colorWhite), activity.getResources().getColor(color), 14, Font.NORMAL));
                    }
                    //table.addCell(getCell(tBeans.getDescription(),Element.ALIGN_LEFT,activity.getResources().getColor(R.color.colorGray),activity.getResources().getColor(R.color.colorWhite),activity.getResources().getColor(R.color.colorBlack),10));
//                    if(type.equalsIgnoreCase("cr")){
//                        table.addCell(getCell(tBeans.getBalance(),Element.ALIGN_RIGHT,activity.getResources().getColor(R.color.colorGray),activity.getResources().getColor(R.color.colorWhite),activity.getResources().getColor(R.color.colorGreens),10));
//                    }else {
//                        table.addCell(getCell(tBeans.getBalance(),Element.ALIGN_RIGHT,activity.getResources().getColor(R.color.colorGray),activity.getResources().getColor(R.color.colorWhite),activity.getResources().getColor(R.color.colorRed),10));
//                    }

                }

                //absolute location to print the PDF table from
                table.writeSelectedRows(0, -1, document.leftMargin(), 650, docWriter.getDirectContent());
                ColumnText.showTextAligned(docWriter.getDirectContent(), Element.ALIGN_CENTER, footer(), 300, 30, 0);

                //print the signature image along with the persons name
//                Drawable drawable1 = activity.getResources().getDrawable(R.drawable.ic_logo);
//                BitmapDrawable bitmapDrawable1 = ((BitmapDrawable) drawable1);
//                Bitmap bmp = bitmapDrawable1.getBitmap();
//                stream = new ByteArrayOutputStream();
//                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                Image signature = Image.getInstance(stream.toByteArray());
//                signature.setAbsolutePosition(400f, 150f);
//                signature.scalePercent(25f);
//                document.add(signature);


                document.close();

                return true;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return false;
    }

    private Phrase footer() {
        Font ffont = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL);
        Phrase p = new Phrase("Powered by hrptech.com for contact # +923322815822 / email : khokhar.faraz.a@gmail.com",ffont);
        return p;
    }

    private Paragraph getParagraph(String title, int align, int color, int size, int style){
        Paragraph paragraph = new Paragraph(title);
        paragraph.setAlignment(align);
        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, size);
        font.setStyle(style);
        font.setColor(new BaseColor(color));
        paragraph.setFont(font);
        return paragraph;
    }

    private Chunk getChunk(String title, int align, int color, int size, int style){
        Chunk paragraph = new Chunk(title);
        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, size);
        font.setStyle(style);
        font.setColor(new BaseColor(color));
        paragraph.setFont(font);
        return paragraph;
    }

    private PdfPCell getCell(String name, int align, int brColor, int bgColor, int fgColor, int fontSize, int style){
        Font f1 = FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize);
        f1.setColor(new BaseColor(fgColor));
        f1.setStyle(style);
        PdfPCell cell = new PdfPCell(new Phrase(name,f1));
        cell.setHorizontalAlignment(align);
        cell.setBackgroundColor(new BaseColor(bgColor));
        cell.setBorderColor(new BaseColor(brColor));
        cell.setFixedHeight(25);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }
    private void createHeadings(PdfContentByte cb, float x, float y, String text){

        cb.beginText();
        cb.setFontAndSize(bfBold, 8);
        cb.setTextMatrix(x,y);
        cb.showText(text.trim());
        cb.endText();

    }

    private void initializeFonts(){


        try {
            bfBold = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void imageToPDF(String fname) throws FileNotFoundException {
        try {
            String path = activity.getExternalFilesDir(null).getAbsolutePath()+"\\\\hrptechKhata";
            File sd = new File(path);
            File backupDB = new File(sd, fname + ".pdf");
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(backupDB), "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            activity.startActivity(intent);
        } catch (Exception e) {

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void SharePdfToWhatsapp(String fileName){


        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File newFile = new File(directory.getAbsolutePath()+""+ File.separator+"hrptechKhata"+ File.separator+"doc");
        File[]  files = newFile.listFiles();
        File filePath = null;
        if(files!=null) {
            for (File file : files) {
                if (file.getName().contains(".pdf")) {
                    filePath = file;
                }
            }

            File backupDB = new File(newFile, fileName + ".pdf");

            Intent share = new Intent();
            //share.setAction(Intent.ACTION_SEND);
            share.setType("application/pdf");
            //share.setType("text/plain");
            Uri uri = Uri.fromFile(filePath);
            share.setPackage("com.whatsapp");
            //share.putExtra(Intent.EXTRA_SUBJECT, "Subject");
            //share.putExtra(Intent.EXTRA_TEXT, "Hello Tetx");
            share.putExtra(Intent.EXTRA_STREAM, uri);
try{
    activity.startActivity(share);
}catch (Exception e){
    Toast.makeText(activity,""+e.getMessage(), Toast.LENGTH_LONG).show();
}
        }
    }

    private List<File> getListFiles(File parentDir)
    {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files = parentDir.listFiles();
        try {
            for (File file : files) {

                //if (file.getName().endsWith(".txt"))

                if (file.isDirectory()) {
                    inFiles.addAll(getListFiles(file));
                } else {
                    if (file.getName().endsWith(".bak")) {
                        inFiles.add(file);
                    }
                }
            }
        }catch(Exception e){
            return inFiles;
        }
        return inFiles;
    }
}
