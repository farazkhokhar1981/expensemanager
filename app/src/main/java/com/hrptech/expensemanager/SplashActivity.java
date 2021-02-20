package com.hrptech.expensemanager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.hrptech.expensemanager.ui.home.HomeActivity;
import com.hrptech.expensemanager.ui.home.LoginActivity;

public class SplashActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_splash);
        // check if user logged in ---> it will be redirected to Dashboard,else it will remain on Login Page
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(FirebaseAuth.getInstance().getCurrentUser() == null){
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                    finish();
                }
                else{
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    finish();
                }

            }
        },3000);

    }


}
