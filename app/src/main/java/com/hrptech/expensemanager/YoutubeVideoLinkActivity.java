package com.hrptech.expensemanager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.hrptech.expensemanager.utility.Utilities;

import java.util.Locale;

public class YoutubeVideoLinkActivity extends Activity {




    LinearLayout intro_btn;
    LinearLayout backupAndRestore_btn;
    LinearLayout howToCreateCompany_btn;
    LinearLayout howWeCreateCustomerAccount_btn;
    LinearLayout howWeMakeTransaction_btn;
    LinearLayout howWeCreateReports_btn;


    int onStartCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);

        onStartCount = 1;
        if (savedInstanceState == null) // 1st time
        {
            this.overridePendingTransition(R.anim.anim_slide_in_left,
                    R.anim.anim_slide_out_left);
        } else // already created so reverse animation
        {
            onStartCount = 2;
        }
        init();
        Utilities.LoadBanner((AdView)findViewById(R.id.adView));
    }


    public void init(){
        intro_btn = (LinearLayout)findViewById(R.id.intro_btn);
        intro_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=us2wR06uXs0")));
            }
        });

        backupAndRestore_btn = (LinearLayout)findViewById(R.id.backupAndRestore_btn);
        backupAndRestore_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=ddnPgeMgLAc")));
            }
        });



        howToCreateCompany_btn = (LinearLayout)findViewById(R.id.howwe_create_company_btn);
        howToCreateCompany_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=dgParJCh0jY")));
            }
        });













        LoadBanner();
    }
    public void LoadBanner(){
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    public Dialog dialog = null;
    public Button no_btn= null;

    public Button yes_btn= null;
    public Button secure_btn= null;
    LinearLayout expenseManagerBtn;
    LinearLayout notebookBtn;



    public void LoadMoreActivity(Intent intent){

            startActivity(intent);
            finish();

    }






    @Override
    protected void onStart() {
        super.onStart();
        if (onStartCount > 1) {
            this.overridePendingTransition(R.anim.anim_slide_in_right,
                    R.anim.anim_slide_out_right);

        } else if (onStartCount == 1) {
            onStartCount++;
        }
        Utilities.isLoadAdsWhenOpened = true;


        //updateUI(account);
    }


    @Override
    protected void onStop() {
        super.onStop();
        Utilities.isLoadAdsWhenOpened = false;
    }









    Locale myLocale;
    String currentLanguage = "en", currentLang;
    public void setLocale(String localeName) {
            myLocale = new Locale(localeName);
            currentLanguage = localeName;
//            UpdateSetting();
//            Refresh();
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);


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
                LoadMoreActivity(new Intent(YoutubeVideoLinkActivity.this,MoreActivity.class));

    }







}
