package com.hrptech.expensemanager.utility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.hrptech.expensemanager.R;
import com.hrptech.expensemanager.beans.CATEGORY;
import com.hrptech.expensemanager.beans.TransactionBeans;
import com.hrptech.expensemanager.db.SettingDB;
import com.hrptech.expensemanager.ui.backup.BackupRestoreFragment;
import com.hrptech.expensemanager.ui.budget.BudgetFragment;
import com.hrptech.expensemanager.ui.category.CategoryActivity;
import com.hrptech.expensemanager.ui.dailytran.DailyTransactionActivity;
import com.hrptech.expensemanager.ui.home.HomeActivity;
import com.hrptech.expensemanager.ui.report.PDFViewPageVoucherDetail;
import com.hrptech.expensemanager.ui.report.PdfView;
import com.hrptech.expensemanager.ui.transaction.TransactionExpenseActivity;
import com.hrptech.expensemanager.ui.transaction.TransactionFragment;
import com.hrptech.expensemanager.ui.transaction.TransactionIncomeActivity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Utilities {
    public static boolean isLoadAdsWhenOpened = true;

    public static String DBNAME="DAILYEXPENSE1.DB";


    public static String lock_tbl="LOCK_TBL";
    public static String lock_password="LOCK_PASSWORD";
    public static String lock_question="LOCK_QUESTION";
    public static String lock_answer="LOCK_ANSWER";
    public static String lock_password_hint="LOCK_PASSWORD_HINT";
    public static String lock_contact="LOCK_CONTACT";


    public static String budget_tbl="BUDGET_TBL";
    public static String budget_id="BUDGET_ID";
    public static String budget_cid="BUDGET_CID";
    public static String budget_year="BUDGET_YEAR";
    public static String budget_month="BUDGET_MONTH";
    public static String budget_amount="BUDGET_AMOUNT";

    public static String category_tbl="CATEGORY_TBL";
    public static String category_id="CATEGORY_ID";
    public static String category_name="CATEGORY_NAME";
    public static String category_type="CATEGORY_type";

    public static String setting_tbl="SETTING_TBL";
    public static String setting_id="SETTING_ID";
    public static String setting_currency="SETTING_CURRENCY";
    public static String setting_language="SETTING_LANGUAGE";
    public static String setting_dateformat="SETTING_DATEFORMAT";

    public static String transaction_tbl="TRANSACTION_TBL";
    public static String transaction_id="TRANSACTION_ID";
    public static String transaction_cid="TRANSACTION_CID";
    public static String transaction_date="TRANSACTION_DATE";
    public static String transaction_type="TRANSACTION_TYPE";
    public static String transaction_description="TRANSACTION_DESCRIPTION";
    public static String transaction_income="TRANSACTION_INCOME";
    public static String transaction_expense="TRANSACTION_EXPENSE";
    public static String transaction_balance="TRANSACTION_BALANCE";


    public static String modification_tbl="MODIFICATION_TBL";
    public static String modification_id="MODIFICATION_ID";
    public static String modification_modi_tr_id="MODIFICATION_MODI_TR_ID"; // kis date key kam ko chara heay
    public static String modification_modi_date="MODIFICATION_MODI_DATE"; // kis date key kam ko chara heay
    public static String modification_modi_tr_date="MODIFICATION_MODI_TR_DATE"; // transaction date
    public static String modification_date="MODIFICATION_DATE";
    public static String modification_time="MODIFICATION_TIME";
    public static String modification_customer_id="MODIFICATION_CUSTOMER_ID";
    public static String modification_companyname_id="MODIFICATION_COMPANY_ID";
    public static String modification_description="MODIFICATION_DESCRIPTION"; // change
    public static String modification_last_amount="MODIFICATION_LAST_AMOUNT"; // change
    public static String modification_last_payment_type="MODIFICATION_PAYMENT_TYPE"; // change
    public static String modification_debit="MODIFICATION_DEBIT"; // change
    public static String modification_credit="MODIFICATION_CREDIT"; // change

    public static String lastChanges = "";
    public static String lastType = "";
    public static String lastName = "";
    public static String lastDate = "";
    public static ArrayList<CATEGORY> catNameList = new ArrayList<>();



    public static int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }

    public static int getIndexCategory(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){

            if (((CATEGORY)spinner.getItemAtPosition(i)).getName().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }


    public static void showDialogCategory(final CategoryActivity categoryFragment, final String id, Activity activity, final String type, String msg1, String msg2){
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.alertdialog);

        TextView text = (TextView) dialog.findViewById(R.id.title1_txt);
        text.setText(msg1);
        TextView text2 = (TextView) dialog.findViewById(R.id.title2_txt);
        text2.setText(msg2);

        Button dialogButton = (Button) dialog.findViewById(R.id.yes_btn);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.equalsIgnoreCase("delete")){
                    categoryFragment.categoryDB.DeleteRecord(id);
                    categoryFragment.RefreshRecord();
                    dialog.dismiss();
                }else {
                    categoryFragment.saveRecord();
                    dialog.dismiss();
                }
            }
        });

        Button noButton = (Button) dialog.findViewById(R.id.no_btn);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public static void showDialogBudget(final BudgetFragment budgetFragment,final String id, Activity activity, final String type, String msg1, String msg2){
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.alertdialog);

        TextView text = (TextView) dialog.findViewById(R.id.title1_txt);
        text.setText(msg1);
        TextView text2 = (TextView) dialog.findViewById(R.id.title2_txt);
        text2.setText(msg2);

        Button dialogButton = (Button) dialog.findViewById(R.id.yes_btn);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.equalsIgnoreCase("delete")){
                    budgetFragment.budgetDB.DeleteRecord(id);
                    budgetFragment.RefreshRecord();
                    dialog.dismiss();
                }else {
                    budgetFragment.SaveRecord();
                    dialog.dismiss();
                }
            }
        });

        Button noButton = (Button) dialog.findViewById(R.id.no_btn);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }


    public static void showDialogTransaction(final TransactionFragment transactionFragment, final String id,final String cid, Activity activity, final String type, String msg1, String msg2){
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.alertdialog);

        TextView text = (TextView) dialog.findViewById(R.id.title1_txt);
        text.setText(msg1);
        TextView text2 = (TextView) dialog.findViewById(R.id.title2_txt);
        text2.setText(msg2);

        Button dialogButton = (Button) dialog.findViewById(R.id.yes_btn);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.equalsIgnoreCase("delete")){
                    transactionFragment.transactionDB.DeleteRecord(id);
                    transactionFragment.RefreshRecord();
                    dialog.dismiss();
                }else {
                    transactionFragment.SaveRecord();
                    dialog.dismiss();
                }
            }
        });

        Button noButton = (Button) dialog.findViewById(R.id.no_btn);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }


    public static void showIncomeActivityDialogTransaction(final TransactionIncomeActivity transactionFragment, final String id, final String cid, Activity activity, final String type, String msg1, String msg2){
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.alertdialog);

        TextView text = (TextView) dialog.findViewById(R.id.title1_txt);
        text.setText(msg1);
        TextView text2 = (TextView) dialog.findViewById(R.id.title2_txt);
        text2.setText(msg2);

        Button dialogButton = (Button) dialog.findViewById(R.id.yes_btn);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.equalsIgnoreCase("delete")){
                    transactionFragment.transactionDB.DeleteRecord(id);
                    transactionFragment.RefreshRecord();
                    dialog.dismiss();
                }else {
                    transactionFragment.SaveRecord();
                    dialog.dismiss();
                }
            }
        });

        Button noButton = (Button) dialog.findViewById(R.id.no_btn);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public static void showExpenseActivityDialogTransaction(final TransactionExpenseActivity transactionFragment, final String id, final String cid, Activity activity, final String type, String msg1, String msg2){
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.alertdialog);

        TextView text = (TextView) dialog.findViewById(R.id.title1_txt);
        text.setText(msg1);
        TextView text2 = (TextView) dialog.findViewById(R.id.title2_txt);
        text2.setText(msg2);

        Button dialogButton = (Button) dialog.findViewById(R.id.yes_btn);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.equalsIgnoreCase("delete")){
                    transactionFragment.transactionDB.DeleteRecord(id);
                    transactionFragment.RefreshRecord();
                    dialog.dismiss();
                }else {
                    transactionFragment.SaveRecord();
                    dialog.dismiss();
                }
            }
        });

        Button noButton = (Button) dialog.findViewById(R.id.no_btn);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public static void hideKeyboardFrom(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

    }

    public static String getIndexZero(int num){
        if(num<10){
            return "0"+num;
        }
        return ""+num;
    }

    public static String getMonth(String m[],String month){
        for(int index=0; index<m.length; index++){
            String mName = m[index];
            if(mName.equalsIgnoreCase(month)){
                return getIndexZero(index+1);
            }
        }
        return "";
    }

    public static String getDayFromDate(String date,String format){
        Date mDate = null;
        try{
            mDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            return new SimpleDateFormat(format).format(mDate);
        }catch(Exception e){

        }
        return "";
    }


    private static final String ADMOB_AD_UNIT_ID = "ca-app-pub-6624065608358661/4230928477";


    private static UnifiedNativeAd nativeAd;
    public static void refreshAd(final Activity activity,final FrameLayout frameLayout) {
        //refresh.setEnabled(false);

        AdLoader.Builder builder = new AdLoader.Builder(activity, ADMOB_AD_UNIT_ID);

        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            // OnUnifiedNativeAdLoadedListener implementation.
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                // You must call destroy on old ads when you are done with them,
                // otherwise you will have a memory leak.
                if (nativeAd != null) {
                    nativeAd.destroy();
                }
                nativeAd = unifiedNativeAd;

                try {
                    UnifiedNativeAdView adView = (UnifiedNativeAdView) activity.getLayoutInflater()
                            .inflate(R.layout.ad_unified, null);
                    populateUnifiedNativeAdView(unifiedNativeAd, adView);
                    frameLayout.removeAllViews();
                    frameLayout.addView(adView);
                }catch(Exception e){

                }
            }

        });

        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(true)
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                Toast.makeText(activity, "Failed to load native ad: "
                        + errorCode, Toast.LENGTH_SHORT).show();
            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());


    }



    public static void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
        // Set the media view. Media content will be automatically populated in the media view once
        // adView.setNativeAd() is called.
        MediaView mediaView = adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline is guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad. The SDK will populate the adView's MediaView
        // with the media content from this native ad.
        adView.setNativeAd(nativeAd);

        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        VideoController vc = nativeAd.getVideoController();

        // Updates the UI to say whether or not this ad has a video asset.

    }

    public static void LoadBanner(AdView mAdView){
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String exportDBZipReturn(Context context,String typeDB,String fileName) {
        try {
            File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File newFile = new File(directory.getAbsolutePath()+""+File.separator+"hrptechexpense");
            if(!newFile.exists()){
                newFile.mkdir();
            }

            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String time = new SimpleDateFormat("hh:mm:ss").format(Calendar.getInstance().getTime());
            String path = newFile.getAbsolutePath();
            File sd = new File(path);

            if(!sd.mkdir()){
                sd.mkdirs();
            }
            if (sd.canWrite()) {
                String backupDBPath = String.format("%s.bak", Utilities.DBNAME);
                File currentDB = context.getDatabasePath(Utilities.DBNAME);
                File backupDB = null;
                String []files = new String[1];
                String zipFileName = "";
                if(fileName.equalsIgnoreCase("")){
                    backupDB = new File(sd, date+"_"+time+"_"+backupDBPath);
                    files[0] = path+"/"+date+"_"+time+"_"+backupDBPath;
                    zipFileName = path+"/"+date+"_"+time+"_"+backupDBPath+".zip";
                } else {
                    backupDB = new File(sd, fileName+"_"+backupDBPath);
                    files[0] = path+"/"+fileName+"_"+backupDBPath;
                    zipFileName = path+"/"+fileName+"_"+backupDBPath+".zip";
                }
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                Utilities.zip(files,zipFileName);
                src.close();
                backupDB.delete();
                dst.close();

                return zipFileName;


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void zip(String[] _files, String zipFileName) {
        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(zipFileName);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
                    dest));
            byte data[] = new byte[1024];

            for (int i = 0; i < _files.length; i++) {
                Log.v("Compress", "Adding: " + _files[i]);
                FileInputStream fi = new FileInputStream(_files[i]);
                origin = new BufferedInputStream(fi, 1024);

                ZipEntry entry = new ZipEntry(_files[i].substring(_files[i].lastIndexOf("/") + 1));
                out.putNextEntry(entry);
                int count;

                while ((count = origin.read(data, 0, 1024)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void ShareImageToWhatsappBackup(Activity activity,String fileName){
        File filePath = new File(fileName);


        Intent share = new Intent(Intent.ACTION_SEND);
        Uri uri = FileProvider.getUriForFile(activity, "com.hrptech.dailyexampenseapp", filePath);
        share.setDataAndType(uri, "application/x-wav");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        activity.startActivity(share);
    }

    public static String imageFileName="";
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void ShareImageToWhatsapp(Activity activity, String fileName){

        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File newFile = new File(directory.getAbsolutePath()+""+File.separator+"hrptechexpense"+File.separator+"doc");
        File filePath = new File(newFile,fileName);
        if (fileName.contains(".pdf")) {
            filePath = new File(newFile,fileName);
        }else {
            filePath = new File(newFile,fileName+".pdf");
        }
        Intent share = new Intent();
        share.setType("application/pdf");
        //share.setType("text/plain");
        Uri uri = Uri.fromFile(filePath);
        share.setPackage("com.whatsapp");
        share.putExtra(Intent.EXTRA_STREAM, uri);

        activity.startActivity(share);
    }



    public static Dialog dialog = null;
    public static Button no_btn= null;
public static TextView confirmTxt;
    public static Button yes_btn= null;
    public static Button secure_btn= null;
    public static LinearLayout expenseManagerBtn;
    public static LinearLayout notebookBtn;
    public static LinearLayout sec_lay;

    @SuppressLint("NewApi")
    public static void showDialogClose(final Activity activity, final String dialogType, final String trType, final String id, final String cid){
        dialog = new Dialog(activity,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.closedialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        confirmTxt = (TextView) dialog.findViewById(R.id.confirm_txt);
        sec_lay = (LinearLayout) dialog.findViewById(R.id.sec_lay);
        sec_lay.setVisibility(View.GONE);
        if(dialogType.equalsIgnoreCase("delete")){
            confirmTxt.setText(activity.getResources().getString(R.string.confirmDeleteRec));
        }else if(dialogType.equalsIgnoreCase("Update")){
            confirmTxt.setText(activity.getResources().getString(R.string.confirmUpdateRec));
        }
        expenseManagerBtn = (LinearLayout) dialog.findViewById(R.id.expenseManager_btn);
        expenseManagerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMoreAppActivity(activity,Uri.parse("https://play.google.com/store/apps/details?id=com.hrptech.ledgerbook"));
            }
        });


        notebookBtn = (LinearLayout)dialog.findViewById(R.id.noteBook_btn);
        notebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMoreAppActivity(activity,Uri.parse("https://play.google.com/store/apps/details?id=notebook.com.notebook"));
            }
        });
        no_btn = (Button)dialog.findViewById(R.id.no_btn);
        no_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        yes_btn = (Button)dialog.findViewById(R.id.yes_btn);
        yes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(trType.equalsIgnoreCase("Income")){
                    if(TransactionIncomeActivity.getTransactionFragment()!=null){
                        if(dialogType.equalsIgnoreCase("delete")){
                            TransactionIncomeActivity.getTransactionFragment().transactionDB.DeleteRecord(id);
                        }  else if(dialogType.equalsIgnoreCase("update")){
                            TransactionIncomeActivity.getTransactionFragment().SaveRecord();

                        }
                        TransactionIncomeActivity.getTransactionFragment().RefreshRecord();
                    }else if(DailyTransactionActivity.getInstance()!=null){
                        if(dialogType.equalsIgnoreCase("delete")){

                                DailyTransactionActivity.getInstance().transactionDB.DeleteRecord(id);
                                DailyTransactionActivity.getInstance().RefreshData();


                        }  else if(dialogType.equalsIgnoreCase("update")){
//                            TransactionIncomeActivity.getTransactionFragment().SaveRecord();
//                            TransactionIncomeActivity.getTransactionFragment().RefreshRecord();
                        }

                    }
                }else if(trType.equalsIgnoreCase("Expense")){
                    if(TransactionIncomeActivity.getTransactionFragment()!=null){
                        if(dialogType.equalsIgnoreCase("delete")){
                            if(TransactionIncomeActivity.getTransactionFragment()!=null){
                                TransactionIncomeActivity.getTransactionFragment().transactionDB.DeleteRecord(id);
                                TransactionIncomeActivity.getTransactionFragment().RefreshRecord();
                            }else if(DailyTransactionActivity.getInstance()!=null){
                                DailyTransactionActivity.getInstance().transactionDB.DeleteRecord(id);
                                DailyTransactionActivity.getInstance().RefreshData();
                            }

                        }  else if(dialogType.equalsIgnoreCase("update")){
                            TransactionIncomeActivity.getTransactionFragment().SaveRecord();
                            TransactionIncomeActivity.getTransactionFragment().RefreshRecord();
                        }

                    }else if(DailyTransactionActivity.getInstance()!=null){
                        if(dialogType.equalsIgnoreCase("delete")){
                                DailyTransactionActivity.getInstance().transactionDB.DeleteRecord(id);
                                DailyTransactionActivity.getInstance().RefreshData();
                        }  else if(dialogType.equalsIgnoreCase("update")){
//                            TransactionExpenseActivity.getTransactionFragment().SaveRecord();
//                            TransactionIncomeActivity.getTransactionFragment().RefreshRecord();
                        }

                    }
                }else if(trType.equalsIgnoreCase("Category")){
                    if(CategoryActivity.getCategoryFragment()!=null){

                        CategoryActivity.getCategoryFragment().saveRecord();

                        CategoryActivity.getCategoryFragment().RefreshRecord();
                    }
                }
                dialog.dismiss();
            }
        });

        secure_btn = (Button)dialog.findViewById(R.id.secure_No_btn);
        secure_btn.setVisibility(View.GONE);

        FrameLayout frameLayout = dialog.findViewById(R.id.fl_adplaceholder);
        Utilities.refreshAd(activity,frameLayout);
        dialog.show();

    }


    @SuppressLint("NewApi")
    public static void showDialogForBudget(final Activity activity, final String dialogType, final String id){
        dialog = new Dialog(activity,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.closedialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        confirmTxt = (TextView) dialog.findViewById(R.id.confirm_txt);
        sec_lay = (LinearLayout) dialog.findViewById(R.id.sec_lay);
        sec_lay.setVisibility(View.GONE);
        if(dialogType.equalsIgnoreCase("delete")){
            confirmTxt.setText(activity.getResources().getString(R.string.confirmDeleteRec));
        }
        expenseManagerBtn = (LinearLayout) dialog.findViewById(R.id.expenseManager_btn);
        expenseManagerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMoreAppActivity(activity,Uri.parse("https://play.google.com/store/apps/details?id=com.hrptech.ledgerbook"));
            }
        });


        notebookBtn = (LinearLayout)dialog.findViewById(R.id.noteBook_btn);
        notebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMoreAppActivity(activity,Uri.parse("https://play.google.com/store/apps/details?id=notebook.com.notebook"));
            }
        });
        no_btn = (Button)dialog.findViewById(R.id.no_btn);
        no_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        yes_btn = (Button)dialog.findViewById(R.id.yes_btn);
        yes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(BudgetFragment.getInit()!=null){


                            BudgetFragment.getInit().budgetDB.DeleteRecord(id);
                            BudgetFragment.getInit().RefreshRecord();




                    }

                dialog.dismiss();
            }
        });

        secure_btn = (Button)dialog.findViewById(R.id.secure_No_btn);
        secure_btn.setVisibility(View.GONE);

        FrameLayout frameLayout = dialog.findViewById(R.id.fl_adplaceholder);
        Utilities.refreshAd(activity,frameLayout);
        dialog.show();

    }


    public static void loadMoreAppActivity(Activity activity,Uri uri){
        activity.startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    public static void importDB(Activity context,String path) {
        try {
            File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File newFile = new File(directory.getAbsolutePath()+""+File.separator+"hrptechexpense");
            String pathLocation = newFile.getAbsolutePath();
            Utilities.unzip(path,pathLocation+"/");
            if(path.contains("zip")){
                path = path.replace(".zip","");
            }
            File sd = new File(path);
            if (sd.canWrite()) {
                File backupDB = context.getDatabasePath(Utilities.DBNAME);

                FileChannel src = new FileInputStream(sd).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
//4254
                sd.delete();
                Toast.makeText(context,"Backup successfully restored",Toast.LENGTH_LONG).show();
                if (BackupRestoreFragment.getCategoryFragment() != null) {
                    Intent intent = new Intent(BackupRestoreFragment.getCategoryFragment(),HomeActivity.class);
                    BackupRestoreFragment.getCategoryFragment().startActivity(intent);
                    BackupRestoreFragment.getCategoryFragment().finish();
                }
//                else if(CompanyRegistrationActivity.getInitiate()!=null){
//                    CompanyRegistrationActivity.getInitiate().MainScreen();
//                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unzip(String _zipFile, String _targetLocation) {

        //create target location folder if not exist

        try {
            FileInputStream fin = new FileInputStream(_zipFile);
            ZipInputStream zin = new ZipInputStream(fin);
            ZipEntry ze = null;
            while ((ze = zin.getNextEntry()) != null) {

                //create dir if required while unzipping
                FileOutputStream fout = new FileOutputStream(_targetLocation + ze.getName());
                for (int c = zin.read(); c != -1; c = zin.read()) {
                    fout.write(c);
                }

                zin.closeEntry();
                fout.close();

            }
            zin.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public static void zipU(String[] _files, String zipFileName) {
        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(zipFileName);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
                    dest));
            byte data[] = new byte[1024];

            for (int i = 0; i < _files.length; i++) {
                Log.v("Compress", "Adding: " + _files[i]);
                FileInputStream fi = new FileInputStream(_files[i]);
                origin = new BufferedInputStream(fi, 1024);

                ZipEntry entry = new ZipEntry(_files[i].substring(_files[i].lastIndexOf("/") + 1));
                out.putNextEntry(entry);
                int count;

                while ((count = origin.read(data, 0, 1024)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getValue(Activity activity, Bundle savedInstanceState, String key){
        String newString = "";
        if (savedInstanceState == null) {
            Bundle extras = activity.getIntent().getExtras();
            if(extras == null) {
                newString= null;
            } else {
                newString= extras.getString(key);
            }
        } else {
            newString = (String) savedInstanceState.getSerializable(key);
        }
        return newString;
    }

    private static TextView transactionNoTxt = null;
    private static TextView customerNameTxt = null;
    private static TextView dateTxt = null;
    private static TextView tran_descrptionTxt = null;
    private static TextView tran_amountTxt = null;
    private static TextView tran_FinalAmountTxt = null;
    private static LinearLayout share_btn;
    private static LinearLayout close_btn;
    private static SettingDB settingDB;
    @SuppressLint("NewApi")
    public static void showDialogCustomerTransactionVoucherDetail(final TransactionBeans beans, final Activity activity){
        dialog = new Dialog(activity,android.R.style.Theme_Black_NoTitleBar);
        dialog.setContentView(R.layout.dialog_trans_voucher_detail);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        settingDB = new SettingDB(activity);
        share_btn = (LinearLayout) dialog.findViewById(R.id.share_btn);
        share_btn.setTag(beans);
        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TransactionBeans beans = (TransactionBeans)v.getTag();
                beans.setCashType(activity.getResources().getString(R.string.voucherdetail));
                if(beans!=null) {
                    String report = "" + activity.getResources().getString(R.string.voucherdetail);
                    LoadWhatsPdf(activity, report,beans);

                }


            }
        });
        close_btn = (LinearLayout) dialog.findViewById(R.id.close_btn);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        transactionNoTxt = (TextView)dialog.findViewById(R.id.transactionNo_txt);
        transactionNoTxt.setText(beans.getId());
        customerNameTxt = (TextView)dialog.findViewById(R.id.pName_txt);
        customerNameTxt.setText(beans.getName());
        dateTxt = (TextView)dialog.findViewById(R.id.transactionDate_txt);
        dateTxt.setText(beans.getDate());
        tran_descrptionTxt = (TextView)dialog.findViewById(R.id.tranDescr_txt);
        tran_descrptionTxt.setText(beans.getDescription());
        tran_amountTxt = (TextView)dialog.findViewById(R.id.tran_amountTxt);
        String type = beans.getType();
        double income = 0;
        double expense = 0;
        double balance= 0;
        if(type==null){
            income = Double.parseDouble(beans.getIncome());
            expense = Double.parseDouble(beans.getExpense());
            if(income>expense){
                balance = income;
                type = "dr";
            }else if(expense>income){
                balance = expense;
                type = "cr";
            }
        }else {
            balance = Double.parseDouble(beans.getBalance());
        }
        tran_FinalAmountTxt = (TextView)dialog.findViewById(R.id.total_amount_txt);
        try {
            if (type.equalsIgnoreCase("dr")) {
                tran_amountTxt.setText(balance + " (" + activity.getResources().getString(R.string.debit) + ")");
                tran_FinalAmountTxt.setText(balance + " (" + activity.getResources().getString(R.string.debit) + ")");
            } else if (type.equalsIgnoreCase("cr")) {
                tran_amountTxt.setText(balance + " (" + activity.getResources().getString(R.string.credit) + ")");
                tran_FinalAmountTxt.setText(balance + " (" + activity.getResources().getString(R.string.credit) + ")");
            }
        }catch(Exception e){
            tran_amountTxt.setText(beans.getBalance());
            tran_FinalAmountTxt.setText(beans.getBalance());
        }


        FrameLayout frameLayout =dialog.
                findViewById(R.id.fl_adplaceholder);
        refreshAd(activity,frameLayout);
        AdView mAdViews = (AdView) dialog.findViewById(R.id.adViews);
        AdRequest adRequests = new AdRequest.Builder().build();
        mAdViews.loadAd(adRequests);
        dialog.show();

    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static  void LoadWhatsPdf(Activity activity, String reportName, TransactionBeans beans){
        PdfView pdfView = new PdfView(activity);
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String time = new SimpleDateFormat("hh:mm:ss a").format(Calendar.getInstance().getTime());
        String name = reportName+"_"+date+"_"+time;
        PDFViewPageVoucherDetail pdfViewPage = new PDFViewPageVoucherDetail(activity,name,beans);
        // boolean isCreated = pdfView.write("testPdf3",reportInfoBeans,beans,transactionBeans);
        boolean isCreated = pdfViewPage.isCreated();
        if(isCreated) {

            pdfView.SharePdfToWhatsapp(name);
            dialog.dismiss();
        }
    }
}
