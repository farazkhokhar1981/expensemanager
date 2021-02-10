package com.hrptech.expensemanager.moreapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.hrptech.expensemanager.MoreActivity;
import com.hrptech.expensemanager.R;
import com.hrptech.expensemanager.utility.Utilities;

import java.util.ArrayList;

public class MoreAppsForm extends Activity {
    RecyclerView gridView=null;
    InterstitialAd mInterstitialAd;
    MoreRecyclerViewAdapter moreRecyclerViewAdapter;
    ImageView btnBack = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.moreapp);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        btnBack = (ImageView)findViewById(R.id.back_btn);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadMainScreen();
            }
        });
        mInterstitialAd = new InterstitialAd(this);

        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

        AdRequest adRequest = new AdRequest.Builder().build();

        // Load ads into Interstitial Ads
//        mInterstitialAd.loadAd(adRequest);
//        mInterstitialAd.setAdListener(new AdListener() {
//            public void onAdLoaded() {
//                if (mInterstitialAd.isLoaded()) {
//                    mInterstitialAd.show();
//                    AdView mAdView = (AdView) findViewById(R.id.adView);
//                    AdRequest adRequest = new AdRequest.Builder().build();
//                    mAdView.loadAd(adRequest);
//                }
//            }
//        });

        AdView mAdView = (AdView) findViewById(R.id.adView);
            AdRequest adRequest1 = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest1);
        gridView = (RecyclerView) findViewById(R.id.gridView);
        gridView.setLayoutManager(new GridLayoutManager(this, 2));
        gridView.setAdapter(null);
        ArrayList<NoteBookBean> noteBookBeans = new ArrayList<>();
        NoteBookBean beans = new NoteBookBean();
        beans.setDescription("com.hrptech.ledgerbook");
        beans.setResImage(R.drawable.ledgerbook_logo);
        beans.setTitle("Ledger Book - Cash Book");
        noteBookBeans.add(beans);
        NoteBookBean beans1 = new NoteBookBean();
        beans1.setDescription("notebook.com.notebook");
        beans1.setResImage(R.drawable.notebook);
        beans1.setTitle("Note Book");
        noteBookBeans.add(beans1);


        moreRecyclerViewAdapter = new MoreRecyclerViewAdapter(this,this,noteBookBeans);
        gridView.setAdapter(moreRecyclerViewAdapter);

        //UpdateNewVersion();

        FrameLayout frameLayout =
                findViewById(R.id.fl_adplaceholder);
        Utilities.refreshAd(this,frameLayout);
    }
    public void LoadMainScreen(){
        LoadMoreActivity(new Intent(MoreAppsForm.this, MoreActivity.class));
    }

    SharedPreferences.Editor editor=null;







    /**
     * Event Handling for Individual menu item selected
     * Identify single menu item by it's id
     * */


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
        LoadMoreActivity(new Intent(MoreAppsForm.this,MoreActivity.class));

    }

    public void LoadMoreActivity(Intent intent){
        startActivity(intent);
        finish();
    }
}
