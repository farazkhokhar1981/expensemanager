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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.hrptech.expensemanager.beans.SettingBeans;
import com.hrptech.expensemanager.db.SettingDB;
import com.hrptech.expensemanager.ui.home.HomeActivity;
import com.hrptech.expensemanager.utility.Utilities;

import java.util.Locale;

public class SettingsActivity extends Activity {




    Spinner currency_spr;
    Spinner dateformat_spr;

    SettingDB settingDB;
    SettingBeans settingBeans;
    LinearLayout save_btn;
    int onStartCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

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
        settingBeans = settingDB.getSettingRecordsSingle();
        if(settingBeans!=null){
            selectedCurrecy = settingBeans.getCurrency();
            selectedDateFormat = settingBeans.getDateformat();
            currency_spr.setSelection(getArrayPosition(currencyArray,selectedCurrecy));
            dateformat_spr.setSelection(getArrayPosition(dateFormatArray,selectedDateFormat));
        }
        currency_spr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCurrecy = currencyArray[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });

        dateformat_spr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDateFormat = dateFormatArray[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int record = 0;
                SettingBeans settingBeans = new SettingBeans();
                settingBeans.setDateformat(selectedDateFormat);
                settingBeans.setCurrency(selectedCurrecy);
                settingBeans.setLanguge("en");
                    record = settingDB.InsertRecord(settingBeans);
                if(record>0){
                    LoadMoreActivity(new Intent(SettingsActivity.this,HomeActivity.class));
                }

            }
        });
    }
    String selectedCurrecy = "";
    String selectedDateFormat = "";
    String currencyArray[]={"$","€","£"};
    String dateFormatArray[]={"yyyy-MM-dd","dd-MM-yyyy","dd-M-yyyy hh:mm:ss","dd MMMM yyyy","E, dd MMM yyyy HH:mm:ss z"};
    public void init(){
        currency_spr = (Spinner) findViewById(R.id.currency_spr);
        dateformat_spr = (Spinner) findViewById(R.id.dateformat_spr);
        settingDB = new SettingDB(this);
        save_btn = (LinearLayout)findViewById(R.id.save_btn);

    }

    //using for get array index position of currency and dateformat array
    public int getArrayPosition(String array[],String name){
        for(int index=0; index<array.length; index++){
            String cName = array[index];
            if(cName.equalsIgnoreCase(name)){
                return index;
            }
        }
        return -1;
    }






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
                LoadMoreActivity(new Intent(SettingsActivity.this, HomeActivity.class));

    }







}
