package com.hrptech.expensemanager.language;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.hrptech.expensemanager.MoreActivity;
import com.hrptech.expensemanager.R;
import com.hrptech.expensemanager.beans.SettingBeans;
import com.hrptech.expensemanager.db.LockDB;
import com.hrptech.expensemanager.db.SettingDB;
import com.hrptech.expensemanager.utility.Utilities;

import java.util.Locale;

public class ChangeFromMainLangugeActivity extends Activity {

    LockDB lockDB;


    LinearLayout english_btn;
    LinearLayout urdu_btn;
    LinearLayout hindi_btn;
    LinearLayout sindhi_btn;
    LinearLayout bangla_btn;
    LinearLayout back_btn;

    TextView txtEnglish1;
    TextView txtEnglish2;

    TextView txtUrdu1;
    TextView txtUrdu2;

    TextView txtHindi1;
    TextView txtHindi2;

    TextView txtSindhi1;
    TextView txtSindhi2;

    TextView txtBangla1;
    TextView txtBangla2;
    String languge = "en";
    SettingDB settingDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_languges);
        init(savedInstanceState);

    }
    SettingBeans beans = null;
    public Activity getActivity(){
        return this;
    }
    public void init(Bundle savedInstanceState){
        settingDB = new SettingDB(this);
        txtEnglish1  = (TextView) findViewById(R.id.english1_txt);
        txtEnglish2  = (TextView) findViewById(R.id.english2_txt);
        txtUrdu1  = (TextView) findViewById(R.id.urdu1_txt);
        txtUrdu2  = (TextView) findViewById(R.id.urdu2_txt);
        txtHindi1  = (TextView) findViewById(R.id.hindi_txt);
        txtHindi2  = (TextView) findViewById(R.id.hindi2_txt);
        txtSindhi1  = (TextView) findViewById(R.id.sindhi_txt);
        txtSindhi2 = (TextView) findViewById(R.id.sindhi2_txt);
        txtBangla1  = (TextView) findViewById(R.id.bangla_txt);
        txtBangla2 = (TextView) findViewById(R.id.bangla2_txt);
        back_btn = (LinearLayout) findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                LoadMoreActivity(new Intent(com.hrptech.expensemanager.language.ChangeFromMainLangugeActivity.this,MoreActivity.class));
            }
        });
        english_btn = (LinearLayout) findViewById(R.id.english_btn);
        english_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("en",new Intent(com.hrptech.expensemanager.language.ChangeFromMainLangugeActivity.this, MoreActivity.class));

            }
        });
        urdu_btn = (LinearLayout) findViewById(R.id.urdu_btn);
        urdu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("ur",new Intent(com.hrptech.expensemanager.language.ChangeFromMainLangugeActivity.this, MoreActivity.class));
            }
        });

        hindi_btn = (LinearLayout) findViewById(R.id.endi_btn);
        hindi_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("hi",new Intent(com.hrptech.expensemanager.language.ChangeFromMainLangugeActivity.this, MoreActivity.class));
            }
        });
        sindhi_btn = (LinearLayout) findViewById(R.id.sindhi_btn);
        sindhi_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("si",new Intent(com.hrptech.expensemanager.language.ChangeFromMainLangugeActivity.this, MoreActivity.class));
            }
        });
        bangla_btn = (LinearLayout) findViewById(R.id.bangla_btn);
        bangla_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("bn",new Intent(com.hrptech.expensemanager.language.ChangeFromMainLangugeActivity.this, MoreActivity.class));
            }
        });
      //  beans = settingDB.getSettings();
        if(beans!=null){
            languge = beans.getLanguge();

            if(languge.equalsIgnoreCase("en")){
                english_btn.setBackground(getResources().getDrawable(R.drawable.button_rounded));
                txtEnglish1.setTextColor(ContextCompat.getColor(this,R.color.colorWhite));
                txtEnglish2.setTextColor(ContextCompat.getColor(this,R.color.colorWhite));
                urdu_btn.setBackground(getResources().getDrawable(R.drawable.button_rounded_white));
                txtUrdu1.setTextColor(ContextCompat.getColor(this,R.color.colorlightRed));
                txtUrdu2.setTextColor(ContextCompat.getColor(this,R.color.colorlightRed));
                hindi_btn.setBackground(getResources().getDrawable(R.drawable.button_rounded_white));
                txtHindi1.setTextColor(ContextCompat.getColor(this,R.color.colorlightRed));
                txtHindi2.setTextColor(ContextCompat.getColor(this,R.color.colorlightRed));
                sindhi_btn.setBackground(getResources().getDrawable(R.drawable.button_rounded_white));
                txtSindhi1.setTextColor(ContextCompat.getColor(this,R.color.colorlightRed));
                txtSindhi2.setTextColor(ContextCompat.getColor(this,R.color.colorlightRed));
                bangla_btn.setBackground(getResources().getDrawable(R.drawable.button_rounded_white));
                txtBangla1.setTextColor(ContextCompat.getColor(this,R.color.colorlightRed));
                txtBangla2.setTextColor(ContextCompat.getColor(this,R.color.colorlightRed));
            }else if(languge.equalsIgnoreCase("ur")){
                english_btn.setBackground(getResources().getDrawable(R.drawable.button_rounded_white));
                txtEnglish1.setTextColor(ContextCompat.getColor(this,R.color.colorlightRed));
                txtEnglish2.setTextColor(ContextCompat.getColor(this,R.color.colorlightRed));
                urdu_btn.setBackground(getResources().getDrawable(R.drawable.button_rounded));
                txtUrdu1.setTextColor(ContextCompat.getColor(this,R.color.colorWhite));
                txtUrdu2.setTextColor(ContextCompat.getColor(this,R.color.colorWhite));
                hindi_btn.setBackground(getResources().getDrawable(R.drawable.button_rounded_white));
                txtHindi1.setTextColor(ContextCompat.getColor(this,R.color.colorlightRed));
                txtHindi2.setTextColor(ContextCompat.getColor(this,R.color.colorlightRed));
                sindhi_btn.setBackground(getResources().getDrawable(R.drawable.button_rounded_white));
                txtSindhi1.setTextColor(ContextCompat.getColor(this,R.color.colorlightRed));
                txtSindhi2.setTextColor(ContextCompat.getColor(this,R.color.colorlightRed));
                bangla_btn.setBackground(getResources().getDrawable(R.drawable.button_rounded_white));
                txtBangla1.setTextColor(ContextCompat.getColor(this,R.color.colorlightRed));
                txtBangla2.setTextColor(ContextCompat.getColor(this,R.color.colorlightRed));
            }else if(languge.equalsIgnoreCase("hi")){
                english_btn.setBackground(getResources().getDrawable(R.drawable.button_rounded_white));
                txtEnglish1.setTextColor(ContextCompat.getColor(this,R.color.colorlightRed));
                txtEnglish2.setTextColor(ContextCompat.getColor(this,R.color.colorlightRed));
                urdu_btn.setBackground(getResources().getDrawable(R.drawable.button_rounded_white));
                txtUrdu1.setTextColor(ContextCompat.getColor(this,R.color.colorlightRed));
                txtUrdu2.setTextColor(ContextCompat.getColor(this,R.color.colorlightRed));
                hindi_btn.setBackground(getResources().getDrawable(R.drawable.button_rounded));
                txtHindi1.setTextColor(ContextCompat.getColor(this,R.color.colorWhite));
                txtHindi2.setTextColor(ContextCompat.getColor(this,R.color.colorWhite));
                sindhi_btn.setBackground(getResources().getDrawable(R.drawable.button_rounded_white));
                txtSindhi1.setTextColor(ContextCompat.getColor(this,R.color.colorlightRed));
                txtSindhi2.setTextColor(ContextCompat.getColor(this,R.color.colorlightRed));
                bangla_btn.setBackground(getResources().getDrawable(R.drawable.button_rounded_white));
                txtBangla1.setTextColor(ContextCompat.getColor(this,R.color.colorlightRed));
                txtBangla2.setTextColor(ContextCompat.getColor(this,R.color.colorlightRed));
            }else if(languge.equalsIgnoreCase("si")){
                english_btn.setBackground(getResources().getDrawable(R.drawable.button_rounded_white));
                txtEnglish1.setTextColor(ContextCompat.getColor(this,R.color.colorlightRed));
                txtEnglish2.setTextColor(ContextCompat.getColor(this,R.color.colorlightRed));
                urdu_btn.setBackground(getResources().getDrawable(R.drawable.button_rounded_white));
                txtUrdu1.setTextColor(ContextCompat.getColor(this,R.color.colorlightRed));
                txtUrdu2.setTextColor(ContextCompat.getColor(this,R.color.colorlightRed));
                sindhi_btn.setBackground(getResources().getDrawable(R.drawable.button_rounded));
                txtSindhi1.setTextColor(ContextCompat.getColor(this,R.color.colorWhite));
                txtSindhi2.setTextColor(ContextCompat.getColor(this,R.color.colorWhite));
                hindi_btn.setBackground(getResources().getDrawable(R.drawable.button_rounded_white));
                txtHindi1.setTextColor(ContextCompat.getColor(this,R.color.colorlightRed));
                txtHindi2.setTextColor(ContextCompat.getColor(this,R.color.colorlightRed));
                bangla_btn.setBackground(getResources().getDrawable(R.drawable.button_rounded_white));
                txtBangla1.setTextColor(ContextCompat.getColor(this,R.color.colorlightRed));
                txtBangla2.setTextColor(ContextCompat.getColor(this,R.color.colorlightRed));
            }else if(languge.equalsIgnoreCase("bn")){
                english_btn.setBackground(getResources().getDrawable(R.drawable.button_rounded_white));
                txtEnglish1.setTextColor(ContextCompat.getColor(this,R.color.colorlightRed));
                txtEnglish2.setTextColor(ContextCompat.getColor(this,R.color.colorlightRed));
                urdu_btn.setBackground(getResources().getDrawable(R.drawable.button_rounded_white));
                txtUrdu1.setTextColor(ContextCompat.getColor(this,R.color.colorlightRed));
                txtUrdu2.setTextColor(ContextCompat.getColor(this,R.color.colorlightRed));
                bangla_btn.setBackground(getResources().getDrawable(R.drawable.button_rounded));
                txtBangla1.setTextColor(ContextCompat.getColor(this,R.color.colorWhite));
                txtBangla2.setTextColor(ContextCompat.getColor(this,R.color.colorWhite));
                hindi_btn.setBackground(getResources().getDrawable(R.drawable.button_rounded_white));
                txtHindi1.setTextColor(ContextCompat.getColor(this,R.color.colorlightRed));
                txtHindi2.setTextColor(ContextCompat.getColor(this,R.color.colorlightRed));
                sindhi_btn.setBackground(getResources().getDrawable(R.drawable.button_rounded_white));
                txtSindhi1.setTextColor(ContextCompat.getColor(this,R.color.colorlightRed));
                txtSindhi2.setTextColor(ContextCompat.getColor(this,R.color.colorlightRed));
            }


        }
        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.fl_adplaceholder);
        Utilities.refreshAd(this,frameLayout);
        AdsInit();
        //LoadBanner();
    }
    public void AdsInit(){


        LoadBanner();
    }



    public void LoadBanner(){
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Utilities.isLoadAdsWhenOpened = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Utilities.isLoadAdsWhenOpened = false;
    }





    Locale myLocale;
    String currentLanguage = "en", currentLang;
    public void setLocale(String localeName, Intent intent) {
            myLocale = new Locale(localeName);
            currentLanguage = localeName;
            Resources res = getResources();
            if(beans!=null){
               // settingDB.UpdateRecord(beans.getCurrency(),localeName,beans.getDate(),beans.getCompanyId());
            }else {
               // settingDB.InsertRecord("Rs",localeName,new SimpleDateFormat("yyyy-MM-dd").format(new Date()),"1");
            }
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            startActivity(intent);
            res.updateConfiguration(conf, dm);
            finish();


    }





    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            onBackPressed();
        }
        return true;
    }
    String prodName = "MEDICINA";
    @Override
    public void onBackPressed() {
        LoadMoreActivity(new Intent(com.hrptech.expensemanager.language.ChangeFromMainLangugeActivity.this,MoreActivity.class));

    }
    public void LoadMoreActivity(Intent intent){
        SettingBeans settingBeans =null;// settingDB.getSettings();
        if(settingBeans!=null) {
            String localeName = settingBeans.getLanguge();
            myLocale = new Locale(localeName);
            Resources res = getResources();

            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            startActivity(intent);
            res.updateConfiguration(conf, dm);
            finish();
        }
    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }





    public Dialog dialog = null;
    public Button no_btn= null;
    public Button yes_btn= null;
    public Button secure_btn= null;
    LinearLayout expenseManagerBtn;
    LinearLayout notebookBtn;

    @SuppressLint("NewApi")
    public void showDialogClose(){
        dialog = new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.closedialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        expenseManagerBtn = (LinearLayout) dialog.findViewById(R.id.expenseManager_btn);
        expenseManagerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.hrptech.dailyexampenseapp")));
            }
        });


        notebookBtn = (LinearLayout)dialog.findViewById(R.id.noteBook_btn);
        notebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=notebook.com.notebook")));
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

                System.exit(0);
                dialog.dismiss();
            }
        });

        secure_btn = (Button)dialog.findViewById(R.id.secure_No_btn);
        secure_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String filePath = Utilities.exportDBZipReturn(com.hrptech.expensemanager.language.ChangeFromMainLangugeActivity.this,"","SystemCreatedBackup.DB");
                if(!filePath.equalsIgnoreCase("")){
                    Utilities.ShareImageToWhatsappBackup(com.hrptech.expensemanager.language.ChangeFromMainLangugeActivity.this,filePath);
                }
                dialog.dismiss();
            }
        });

        FrameLayout frameLayout = dialog.findViewById(R.id.fl_adplaceholder);
        Utilities.refreshAd(this,frameLayout);
        dialog.show();

    }



}
