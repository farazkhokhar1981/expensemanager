package com.hrptech.expensemanager;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.hrptech.expensemanager.ui.home.HomeActivity;

public class email_registration extends AppCompatActivity {

    EditText txtName;
    EditText txtEmail;
    EditText txtPassword;
    Button btnSignUp;
    TextView txtLinkLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_registration);

        txtName = (EditText) findViewById(R.id.txtName);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        txtLinkLogin = (TextView) findViewById(R.id.txtLinkLogin);

        txtLinkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(email_registration.this, email_signin.class));
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(txtEmail.getText().toString())){
                    Toast.makeText(email_registration.this, "Please Enter Email Address",Toast.LENGTH_SHORT).show();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(txtEmail.getText().toString()).matches()){
                    Toast.makeText(email_registration.this, "Please Enter Valid Email Address",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(txtPassword.getText().toString())){
                    Toast.makeText(email_registration.this, "Please Enter Password",Toast.LENGTH_SHORT).show();
                }
                else if(txtPassword.getText().toString().length() < 6){
                    Toast.makeText(email_registration.this, "Please Enter 6 or More than 6 digits password",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(txtName.getText().toString())){
                    Toast.makeText(email_registration.this, "Please Enter Name",Toast.LENGTH_SHORT).show();
                }
                else{
                    registration();
                }


            }

            private void registration() {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(txtEmail.getText().toString(),txtPassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Users users = new Users();
                                users.setUserName(txtName.getText().toString());
                                users.setUserEmail(txtEmail.getText().toString());
                                users.setUserPassword(txtPassword.getText().toString());

                                FirebaseDatabase.getInstance().getReference()
                                        .child("Users")
                                        .child(FirebaseAuth.getInstance().getUid())
                                        .setValue(users)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(email_registration.this,"Registration Succesfull",Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(email_registration.this, HomeActivity.class));
                                                finish();
                                            }
                                        });

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(email_registration.this, "Failed: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });


    }
}