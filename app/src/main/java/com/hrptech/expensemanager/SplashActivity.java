package com.hrptech.expensemanager;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.hrptech.expensemanager.ui.home.HomeActivity;
import com.hrptech.expensemanager.ui.home.LoginActivity;

public class SplashActivity extends AppCompatActivity implements Runnable {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_splash);
        new Thread(this).start();
    }

    private int startTime = 2000;
    @Override
    public void run() {
        try{

            Thread.sleep(startTime);
            Intent intent = new Intent();
            intent.setClass(this, LoginActivity.class);
            intent.putExtra("check","new");
            startActivity(intent);
            finish();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
