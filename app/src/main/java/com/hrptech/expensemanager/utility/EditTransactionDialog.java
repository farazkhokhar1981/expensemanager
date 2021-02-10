package com.hrptech.expensemanager.utility;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.material.textfield.TextInputEditText;
import com.hrptech.expensemanager.R;
import com.hrptech.expensemanager.beans.TransactionBeans;
import com.hrptech.expensemanager.db.ModificationDB;
import com.hrptech.expensemanager.db.TransactionDB;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class EditTransactionDialog {
    Activity activity;
    TransactionBeans transactionBeans_;
    TransactionDB transactionDB;
    ModificationDB modificationDB;
    String types = "";
    public EditTransactionDialog(Activity activity, TransactionBeans transactionBeans, String types){
        this.activity = activity;
        this.types = types;
        transactionDB = new TransactionDB(activity);
        modificationDB = new ModificationDB(activity);
        transactionBeans_ = transactionDB.getTransactionRecordSingle(transactionBeans.getId());
        if(transactionBeans_!=null){

        }
    }
    TextView id_txt;
    TextView name_txt;
    TextView date_txt;
    ImageView calender_btn;
    TextInputEditText amount_txt;
    EditText description_txt;
    public Dialog dialog = null;

    public void LoadDialog(){
        dialog = new Dialog(activity,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.fragment_transaction_edit);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        id_txt = (TextView)dialog.findViewById(R.id.id_txt);
        id_txt.setText(transactionBeans_.getId());
        selectedParties_Id = transactionBeans_.getCid();
        name_txt = (TextView)dialog.findViewById(R.id.customer_txt);
        name_txt.setText(transactionBeans_.getName());
        date_txt = (TextView)dialog.findViewById(R.id.date_txt);
        date_txt.setText(transactionBeans_.getDate());
        amount_txt = (TextInputEditText)dialog.findViewById(R.id.txtBudgetAmount);
        amount_txt.setText(transactionBeans_.getBalance());
        description_txt = (EditText)dialog.findViewById(R.id.txtDescription);
        description_txt.setText(transactionBeans_.getDescription());
        description_txt.requestFocus();
        Button debit_btn = (Button)dialog.findViewById(R.id.save_btn);
        debit_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                UpdateRecord("dr");
            }
        });
        Button credit_btn = (Button)dialog.findViewById(R.id.add_btn);
        credit_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                UpdateRecord("cr");
            }
        });
        Button cancel_btn = (Button)dialog.findViewById(R.id.close_btn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        calender_btn = (ImageView) dialog.findViewById(R.id.cander_btn);
        calender_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadDatePicker();
            }
        });

        FrameLayout frameLayout = (FrameLayout)dialog.findViewById(R.id.fl_adplaceholder);
        refreshAd(activity,frameLayout);
        dialog.show();
    }

    private static final String ADMOB_AD_UNIT_ID = "ca-app-pub-6624065608358661/5792882344";


    private static UnifiedNativeAd nativeAd;
    private static void refreshAd(final Activity activity, final FrameLayout frameLayout) {
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

    private static void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
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

    String selectedParties_Id = "";
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void UpdateRecord(String type){

        String id = id_txt.getText().toString();
        String date = date_txt.getText().toString();
        String amount = amount_txt.getText().toString();
        if(amount.equalsIgnoreCase("")){
            Toast.makeText(activity,"Enter Amount greater then 0",Toast.LENGTH_LONG).show();
            return;
        }
        String description = description_txt.getText().toString();
        String tDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String tTime = new SimpleDateFormat("hh:mm:ss a").format(Calendar.getInstance().getTime());
        TransactionBeans tBeans = transactionDB.getTransactionRecordSingle(id);
        String lastAmount = "";
        double dr = 0;
        try {
            dr = Double.parseDouble(tBeans.getIncome());
        }catch (Exception e){
            dr = 0;
        }
        double cr = 0;
        try {
            cr = Double.parseDouble(tBeans.getExpense());
        }catch (Exception e){
            cr = 0;
        }
        if(dr>0){
            lastAmount=tBeans.getIncome()+"(dr)";
        }else if(cr>0){
            lastAmount=tBeans.getExpense()+"(cr)";
        }

//        if(type.equalsIgnoreCase("cr")) {
//            modificationDB.InsertRecord(id,date,tBeans.getDate(),tDate,tTime,MainActivity.getInstance().selectedCompanyName,selectedParties_Id,description,lastAmount,tBeans.getType(),"0.0",amount);
//                transactionDB.UpdateRecord(id, MainActivity.getInstance().selectedCompanyName, selectedParties_Id, date, type, description, "0.0", amount, amount);
//
//            selectedParties_Id = "";
//        }else if(type.equalsIgnoreCase("dr")) {
//            modificationDB.InsertRecord(id,date,tBeans.getDate(),tDate,tTime,MainActivity.getInstance().selectedCompanyName,selectedParties_Id,description,lastAmount,tBeans.getType(),amount,"0.0");
//                transactionDB.UpdateRecord(id, MainActivity.getInstance().selectedCompanyName, selectedParties_Id, date, type, description, amount, "0.0", amount);
//            selectedParties_Id = "";
//
//        }
//        Utilities.exportDB(activity,"","SystemCreatedBackup");
//        if(types.equalsIgnoreCase("home")) {
//            if (HomeFragment.getHomeIns() != null) {
//                Utilities.CustomerLedgerTransaction(activity);
//                HomeFragment.getHomeIns().CustomerLedgerTransaction();
//            }
//        }else if(types.equalsIgnoreCase("detail")) {
//            if (DailyTransactionActivity.getInstance() != null) {
//                DailyTransactionActivity.getInstance().UpdateListBySortandSearch();
//            }
//        }
        dialog.dismiss();

    }


    Calendar calendar = null;
    int year,month,dayOfMonth;
    DatePickerDialog datePickerDialog;
    public void LoadDatePicker(){
        calendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(activity,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year_, int month_, int day) {
                        date_txt.setText(year + "-" + Utilities.getIndexZero((month + 1)) + "-" + Utilities.getIndexZero(day));
                        year = year_;
                        month = month_;
                        dayOfMonth = day;
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

}
