package com.hrptech.expensemanager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;


import com.hrptech.expensemanager.utility.Utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class OpenPdfActivity extends Activity {
    private ParcelFileDescriptor fileDescriptor;
    private PdfRenderer pdfRenderer;
    private PdfRenderer.Page currentPage;
    private ImageView image;
    private Button btnPrevious;
    private Button btnNext;
    private ImageView btnBack;
    private ImageView btnShare;
    private ImageView btnDelete;
    String path = "";
    String name = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_open_pdf);
        image = (ImageView) findViewById(R.id.image);
        btnPrevious = (Button)findViewById(R.id.btn_previous);
        btnNext = (Button)findViewById(R.id.btn_next);
        btnBack = (ImageView) findViewById(R.id.back_btn);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadBack();
            }
        });
        btnShare = (ImageView) findViewById(R.id.share_btn);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                Utilities.ShareImageToWhatsapp(OpenPdfActivity.this,name);
            }
        });
        btnDelete = (ImageView) findViewById(R.id.delete_btn);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new File(path).delete();
                LoadBack();
            }
        });
        //set buttons event
        btnPrevious.setOnClickListener(onActionListener(-1)); //previous button clicked
        btnNext.setOnClickListener(onActionListener(1)); //next button clicked

        int index = 0;
        // If there is a savedInstanceState (screen orientations, etc.), we restore the page index.
        if (null != savedInstanceState) {
            index = savedInstanceState.getInt("current_page", 0);
        }

        path = getBundleName(savedInstanceState,"path");
        name = getBundleName(savedInstanceState,"name");

        try {
            openRenderer(this,path);
            showPage(index);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getBundleName(Bundle savedInstanceState, String key){
        String valueName = "";
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                valueName= null;
            } else {
                valueName= extras.getString(key);
            }
        } else {
            valueName= (String) savedInstanceState.getSerializable(key);
        }
        return valueName;
    }

    @Override
    public void onDestroy() {
        try {
            closeRenderer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @SuppressLint("NewApi")
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (null != currentPage) {
            outState.putInt("current_page", currentPage.getIndex());
        }
    }

    /**
     * Create a PDF renderer
     * @param activity
     * @throws IOException
     */
    @SuppressLint("NewApi")
    private void openRenderer(Activity activity, String path) throws IOException {
        // Reading a PDF file from the assets directory.

        File file = new File(path);

        try {
            fileDescriptor= ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        }
        catch (  FileNotFoundException e) {
            e.printStackTrace();
        }



        // This is the PdfRenderer we use to render the PDF.
        pdfRenderer = new PdfRenderer(fileDescriptor);
    }

    /**
     * Closes PdfRenderer and related resources.
     */
    @SuppressLint("NewApi")
    private void closeRenderer() throws IOException {
        if (null != currentPage) {
            currentPage.close();
        }
        pdfRenderer.close();
        fileDescriptor.close();
    }

    /**
     * Shows the specified page of PDF file to screen
     * @param index The page index.
     */
    @SuppressLint("NewApi")
    private void showPage(int index) {
        if (pdfRenderer.getPageCount() <= index) {
            return;
        }
        // Make sure to close the current page before opening another one.
        if (null != currentPage) {
            currentPage.close();
        }
        //open a specific page in PDF file
        currentPage = pdfRenderer.openPage(index);
        // Important: the destination bitmap must be ARGB (not RGB).
        Bitmap bitmap = Bitmap.createBitmap(currentPage.getWidth(), currentPage.getHeight(),
                Bitmap.Config.ARGB_8888);
        // Here, we render the page onto the Bitmap.
        currentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
        // showing bitmap to an imageview
        image.setImageBitmap(bitmap);
        updateUIData();
    }

    /**
     * Updates the state of 2 control buttons in response to the current page index.
     */
    @SuppressLint("StringFormatInvalid")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void updateUIData() {
        int index = currentPage.getIndex();
        int pageCount = pdfRenderer.getPageCount();
        btnPrevious.setEnabled(0 != index);
        btnNext.setEnabled(index + 1 < pageCount);
        setTitle(getString(R.string.app_name2, index + 1, pageCount));
    }

    private View.OnClickListener onActionListener(final int i) {
        return new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {

                if (i < 0) {//go to previous page
                    showPage(currentPage.getIndex() - 1);
                } else {
                    showPage(currentPage.getIndex() + 1);
                }
            }
        };
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            LoadBack();
        }
        return true;
    }

    public void LoadBack(){
        startActivity(new Intent(this,MoreActivity.class));
        finish();
    }
}
