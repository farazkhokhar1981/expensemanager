package com.hrptech.expensemanager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hrptech.expensemanager.beans.SettingBeans;
import com.hrptech.expensemanager.ui.home.HomeActivity;
import com.hrptech.expensemanager.ui.home.LoginActivity;
import com.hrptech.expensemanager.utility.Utilities;

import java.io.IOException;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_splash);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("EXPENSEMANAGER/SETTINGS");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SettingBeans beans = dataSnapshot.getValue(SettingBeans.class);
                HomeActivity.currency = beans.getCurrency();
            }
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        // check if user logged in ---> it will be redirected to Dashboard,else it will remain on Login Page
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                if(isOnline()){
                    if(FirebaseAuth.getInstance().getCurrentUser() == null){
                        startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                        finish();
                    }
                    else{

                            startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                            finish();
                    }
                }else{
                    Utilities.showDialogClose(SplashActivity.this,"close","close","","");
                }
            }
        },3000);
    }

    public boolean isOnline() {
        System.out.println("executeCommand");
        Runtime localRuntime = Runtime.getRuntime();
        try {
            int i = localRuntime.exec("/system/bin/ping -c 1 8.8.8.8")
                    .waitFor();
            System.out.println(" mExitValue " + i);
            boolean bool = false;
            if (i == 0) {
                bool = true;
            }
            return bool;
        } catch (InterruptedException localInterruptedException) {
            localInterruptedException.printStackTrace();
            System.out.println(" Exception:" + localInterruptedException);
            return false;
        } catch (IOException localIOException) {
            localIOException.printStackTrace();
            System.out.println(" Exception:" + localIOException);
        }
        return false;
    }


}
