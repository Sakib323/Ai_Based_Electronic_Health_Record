package com.itsolution.electronichealthrecord;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class register extends AppCompatActivity {

    FusedLocationProviderClient fusedLocationProviderClient;
    private final static int REQUEST_CODE=100;
    FirebaseDatabase rootNode;
    AutoCompleteTextView gender,location1;
    EditText name,email,phone,password;
    FirebaseStorage mStorage;

    String str_name="",str_email="",str_phone="",str_location="",str_gender="",str_password="",latitude,longitude1,city1;
    private static final int Gallery_Code=1;
    Uri image_uri=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_register);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar actionBar;
        actionBar=getSupportActionBar();
        actionBar.hide();
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(register.this);
        gender=findViewById(R.id.gender);
        String[] GENDERLIST={"Male","Female"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, GENDERLIST);
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.gender);
        textView.setAdapter(adapter);
        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
                textView.showDropDown();
                textView.requestFocus();
                return false;
            }
        });



        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        phone=findViewById(R.id.number);
        password=findViewById(R.id.password);


        CardView register_user=findViewById(R.id.submit);


        register_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                str_name=name.getText().toString();
                str_email=email.getText().toString();
                str_phone=phone.getText().toString();
                str_gender=gender.getText().toString();
                str_password=password.getText().toString();

                if(!str_name.equals("") && !str_email.equals("") && !str_phone.equals("") && !str_password.equals("") && str_location.equals("") && !str_gender.equals("")){

                    DatabaseReference reference= FirebaseDatabase.getInstance().getReference("owner");
                    Query checkUser=reference.orderByChild("name").equalTo(str_name);
                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()) {
                                Toast.makeText(register.this, "This name already exist", Toast.LENGTH_SHORT).show();

                            }
                            else {

                                Dialog dialog_=new Dialog(register.this);
                                dialog_.setContentView(R.layout.dialog_temp);
                                dialog_.getWindow().setBackgroundDrawableResource(R.drawable.bacground_for_dialog);
                                dialog_.setCancelable(false);
                                dialog_.show();
                                CardView ok=dialog_.findViewById(R.id.submit_btn);
                                ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog_.dismiss();
                                        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                                        intent.setType("image/*");
                                        startActivityForResult(intent,Gallery_Code);
                                    }
                                });



                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                }
                else {

                    Toast.makeText(register.this, "Empty field", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Gallery_Code && resultCode==RESULT_OK){
            image_uri=data.getData();
            upload_to_server();

            Dialog dialog_=new Dialog(register.this);
            dialog_.setContentView(R.layout.location);
            dialog_.getWindow().setBackgroundDrawableResource(R.drawable.bacground_for_dialog);
            dialog_.setCancelable(false);
            dialog_.show();
            TextView txt=dialog_.findViewById(R.id.textview_text);
            txt.setText("Uploading your information to the server");
            CardView ok=dialog_.findViewById(R.id.submit_btn);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });


        }
    }




    private void upload_to_server() {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference filepath = storage.getReference().child("owner_profile_picture").child(image_uri.getLastPathSegment());
        filepath.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        DatabaseReference reference;
                        rootNode = FirebaseDatabase.getInstance();
                        reference = rootNode.getReference("owner");
                        SharedPreferences sharedPref = getSharedPreferences("user_info", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putBoolean("state", true);
                        editor.putBoolean("verified", false);
                        editor.putString("name", str_name);
                        editor.putString("email", str_email);
                        editor.putString("phone", str_phone);
                        editor.putString("gender", str_gender);
                        editor.putString("location", str_location);
                        editor.putString("password", str_password);
                        editor.putString("balance","0");
                        editor.apply();
                        HashMap<String, String> UserMap = new HashMap<>();
                        UserMap.put("name", str_name);
                        UserMap.put("email", str_email);
                        UserMap.put("phone", str_phone);
                        UserMap.put("password", str_password);
                        UserMap.put("gender", str_gender);
                        UserMap.put("location", str_location);
                        UserMap.put("lat",latitude);
                        UserMap.put("long",longitude1);
                        UserMap.put("city",city1);
                        UserMap.put("verified", "false");
                        UserMap.put("profile_img", task.getResult().toString());
                        UserMap.put("balance","0");
                        reference.child(str_name).setValue(UserMap);
                        Intent intent = new Intent(register.this, login.class);
                        startActivity(intent);
                    }
                });
            }
        });


    }


}
