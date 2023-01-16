package com.example.deneme.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.deneme.MainActivity;
import com.example.deneme.R;
import com.example.deneme.Tools;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    Button login;
    EditText email,pass;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login=(Button) findViewById(R.id.loginbutton);
        email=(EditText) findViewById(R.id.emailLogin);
        pass=(EditText) findViewById(R.id.passwordLogin);
        firebaseAuth= FirebaseAuth.getInstance();
        progressBar=(ProgressBar) findViewById(R.id.progressBarLogin);
        progressBar.setVisibility(View.INVISIBLE);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                loginUser();
            }
        });

    }
    private void loginUser(){


        String userMail=email.getText().toString();
        String userPass=pass.getText().toString();
        if (userMail.isEmpty()){
            Tools.showMessage("User E-Mail can't be empty");
        }
        else if (userPass.isEmpty()){
            Tools.showMessage("Inaviled Password");
        }


        firebaseAuth.signInWithEmailAndPassword(userMail,userPass).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.INVISIBLE);
                if(task.isSuccessful()){
                    Tools.showMessage("Welcome");
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }else{

                    Tools.showMessage("Failed");
                }
            }
        });

    }
    public void singupClick(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        this.finish();
    }
}