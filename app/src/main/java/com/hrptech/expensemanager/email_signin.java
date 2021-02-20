package com.hrptech.expensemanager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.hrptech.expensemanager.ui.home.HomeActivity;

public class email_signin extends AppCompatActivity {

    TextView txtLinkAccSignup;
    Button btnSignIn;
    EditText txtEmail;
    EditText txtPassword;

    ProgressDialog nDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_signin);

        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);


        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(txtEmail.getText().toString())){
                    Toast.makeText(email_signin.this, "Please Enter Email Address", Toast.LENGTH_SHORT).show();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(txtEmail.getText().toString()).matches()){
                    Toast.makeText(email_signin.this, "Please Enter Valid Email Address",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(txtPassword.getText().toString())){
                    Toast.makeText(email_signin.this, "Please Enter Password",Toast.LENGTH_SHORT).show();
                }
                else if(txtPassword.getText().toString().length() < 6){
                    Toast.makeText(email_signin.this, "Please Enter 6 or More than 6 digits password",Toast.LENGTH_SHORT).show();
                }
                else{
                    LogIn();
                }
            }

            private void LogIn() {

                nDialog = new ProgressDialog(email_signin.this);
                nDialog.setMessage("");
                nDialog.setTitle("");
                nDialog.setIndeterminate(false);
                nDialog.setCancelable(false);
                nDialog.show();


                FirebaseAuth.getInstance().signInWithEmailAndPassword(txtEmail.getText().toString(),txtPassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(email_signin.this,"Login Successful...", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(email_signin.this, HomeActivity.class));
                                nDialog.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(email_signin.this, "Failed: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                nDialog.dismiss();
                            }
                        });
            }
        });


        txtLinkAccSignup = (TextView) findViewById(R.id.txtLinkAccSignup);
        txtLinkAccSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(email_signin.this, email_registration.class));
                finish();
            }
        });

    }
}