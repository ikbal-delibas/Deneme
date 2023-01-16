package com.example.deneme.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.deneme.MainActivity;
import com.example.deneme.R;
import com.example.deneme.Tools;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {
    Thread wait;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Tools.context=getApplicationContext();
        firebaseAuth=FirebaseAuth.getInstance();
        progressBar=(ProgressBar) findViewById(R.id.progressBarSplash);
        progressBar.setVisibility(View.INVISIBLE);
        SplashThread();
        if(firebaseAuth.getCurrentUser()!=null){
            progressBar.setVisibility(View.VISIBLE);
           Tools.showMessage("You are already login. Redirect to main page.");
           wait.start();
        }
        else{
            Tools.showMessage("Login or SignUp please.");
        }
    }
    public void singupClick(View view) {
        startActivity(new Intent(SplashActivity.this, RegisterActivity.class));

    }
    public void loginClick(View view) {
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));

    }
    public void SplashThread(){
         wait =new Thread(){
            @Override
            public void run() {
                try {
                    sleep(2000);
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

    }


}