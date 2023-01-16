package com.example.deneme.activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.deneme.R;
import com.example.deneme.Tools;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    Button signup;
    EditText name,mail,password;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        signup=(Button) findViewById(R.id.buttonsingUp);
        name=(EditText) findViewById(R.id.name);
        mail=(EditText) findViewById(R.id.emailRegister);
        password=(EditText) findViewById(R.id.passwordRegister);
        firebaseAuth= FirebaseAuth.getInstance();
        progressBar=(ProgressBar) findViewById(R.id.progressBarRegister);
        progressBar.setVisibility(View.INVISIBLE);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                signupClick();
            }
        });


    }

    private void signupClick() {
        String userName=name.getText().toString();
        String userMail=mail.getText().toString();
        String userPass=password.getText().toString();
        if (userName.isEmpty()){
            Tools.showMessage("User name can't be empty");
        }
        if (userMail.isEmpty()){
            Tools.showMessage("User E-Mail can't be empty");
        }
        if (userPass.isEmpty()||userPass.length()<6){
            Tools.showMessage("Inaviled Password");
        }

        firebaseAuth.createUserWithEmailAndPassword(userMail,userPass).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.INVISIBLE);
                if(task.isSuccessful()){
                    Tools.showMessage("Registration Successful");
                    startActivity(new Intent(RegisterActivity.this, SplashActivity.class));
                }else{
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Tools.showMessage("Failed");
                }
            }
        });
    }

    public void loginClick(View view) {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        this.finish();
    }
}