package com.itsolution.electronichealthrecord;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class splash_screen extends AppCompatActivity {

    CardView main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.hide();

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.black));
        }

        main=findViewById(R.id.main);
        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPref = getSharedPreferences("user_info", MODE_PRIVATE);
                Boolean register=sharedPref.getBoolean("state",false);

                if(register==true){
                    Intent intent =new Intent(splash_screen.this,MainActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent =new Intent(splash_screen.this,login_or_register.class);
                    startActivity(intent);
                }

            }
        });



    }
}