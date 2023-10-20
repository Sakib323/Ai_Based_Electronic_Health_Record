package com.itsolution.electronichealthrecord;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    Boolean home_click=true,add_click=false,settings_click=false;
    CardView add_paper,home,settings;
    ImageView navigation_drawer;
    NavigationView navigationView;
    RelativeLayout main;
    TextView view_all_report;
    String user_name;
    HorizontalScrollView history;
    Bitmap bitmap;
    TextView name,email,phn_nmbr,address;
    CircleImageView profile_image;
    List<String> stringList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.hide();

        SharedPreferences sharedPref = getSharedPreferences("user_info", MODE_PRIVATE);
        user_name=sharedPref.getString("name","");



        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.top_bar_of_main_activity));
        }

        view_all_report=findViewById(R.id.view_all_report);
        view_all_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MainActivity.this,view_all_report.class);
                intent.putExtra("user",user_name);
                startActivity(intent);
            }
        });


        add_paper=findViewById(R.id.add_paper);
        home=findViewById(R.id.home);
        settings=findViewById(R.id.settings);
        navigation_drawer=findViewById(R.id.navigation_drawer);
        navigationView=findViewById(R.id.nav_view);
        add_paper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_paper.setCardBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                check();
                Intent intent =new Intent(MainActivity.this,add_report_document.class);
                startActivity(intent);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                home.setCardBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                check();
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settings.setCardBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                check();
                Intent intent =new Intent(MainActivity.this,settings.class);
                startActivity(intent);
            }
        });

        navigation_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationView.setVisibility(View.VISIBLE);
            }
        });

        View headerView = navigationView.getHeaderView(0);

        name=headerView.findViewById(R.id.name);
        email=headerView.findViewById(R.id.email);
        phn_nmbr=headerView.findViewById(R.id.phn_nmbr);
        address=headerView.findViewById(R.id.address);
        profile_image=headerView.findViewById(R.id.profile_image);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId()==R.id.Share){
                    generate_qr();
                }
                if(item.getItemId()==R.id.scanner){
                    Intent intent =new Intent(MainActivity.this,ScannerActivity.class);
                    startActivity(intent);
                }

                if(item.getItemId()==R.id.shared_report){
                    Intent intent =new Intent(MainActivity.this,others_report.class);
                    startActivity(intent);
                }

                return false;
            }
        });

        DatabaseReference db= FirebaseDatabase.getInstance().getReference().child("owner").child(user_name);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                name.setText(""+snapshot.child("name").getValue(String.class));
                email.setText(""+snapshot.child("email").getValue(String.class));
                address.setText(""+snapshot.child("location").getValue(String.class));
                phn_nmbr.setText(""+snapshot.child("phone").getValue(String.class));

                Picasso.get().load(snapshot.child("profile_img").getValue(String.class)).into(profile_image);


            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });



        main=findViewById(R.id.main_window);
        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationView.setVisibility(View.INVISIBLE);
            }
        });


    }


    private void check(){

        if(home_click==true){
            home.setCardBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.nav_bg));
            home_click=false;
            add_click=true;
        }

        if(add_click==true){
            home.setCardBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.nav_bg));
            home_click=true;
            add_click=false;
        }
        if(settings_click==true){
            home.setCardBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.nav_bg));
            home_click=true;
            add_click=false;
        }

    }

    private void generate_qr(){

        Dialog dialog_1=new Dialog(MainActivity.this);
        dialog_1.setContentView(R.layout.qr_code_show);
        dialog_1.getWindow().setBackgroundDrawableResource(R.drawable.bacground_for_dialog);
        dialog_1.setCancelable(true);
        dialog_1.show();
        ImageView qr_img=dialog_1.findViewById(R.id.idIVQrcode);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(user_name, BarcodeFormat.QR_CODE, 200, 200);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

            qr_img.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }


    }

