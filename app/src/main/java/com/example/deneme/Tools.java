package com.example.deneme;

import android.content.Context;
import android.widget.Toast;

public class Tools {
    public static Context context;
    public static void showMessage(String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
}
