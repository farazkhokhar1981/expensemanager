package com.hrptech.expensemanager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.audiofx.BassBoost;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hrptech.expensemanager.beans.CATEGORY;
import com.hrptech.expensemanager.beans.SettingBeans;
import com.hrptech.expensemanager.db.SettingDB;
import com.hrptech.expensemanager.ui.home.HomeActivity;
import com.hrptech.expensemanager.utility.Utilities;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class SettingsActivity extends Activity {

    Spinner currency_spr;
    Spinner dateformat_spr;

    FirebaseDatabase database =null;
    DatabaseReference databaseReference = null;
    ImageView back_btn;
    SettingDB settingDB;
    SettingBeans settingBeans;
    LinearLayout save_btn;
    int onStartCount = 0;


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

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SettingBeans beans = dataSnapshot.getValue(SettingBeans.class);
                selectedCurrecy = beans.getCurrency();
                selectedDateFormat = beans.getDateformat();
                currency_spr.setSelection(getArrayPosition(currencyArray,selectedCurrecy));
                dateformat_spr.setSelection(getArrayPosition(dateFormatArray,selectedDateFormat));
            }
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
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
                    HomeActivity.currency = selectedCurrecy;
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
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("EXPENSEMANAGER/SETTINGS");
        back_btn = (ImageView) findViewById(R.id.back_btn);
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
