package com.itsolution.electronichealthrecord;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.vision.CameraSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;
import java.util.Map;


public class ScannerActivity extends AppCompatActivity {

    String user_name,profile_img;
    private CameraSource cameraSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        SharedPreferences sharedPref = getSharedPreferences("user_info", MODE_PRIVATE);
        user_name=sharedPref.getString("name","");
        IntentIntegrator intentIntegrator = new IntentIntegrator(ScannerActivity.this);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setPrompt("Scan a QR Code");
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        intentIntegrator.initiateScan();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(intentResult != null){
            String contents = intentResult.getContents();
            if(contents != null){
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                if (vibrator != null) {
                    vibrator.vibrate(200);
                }


                DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("owner");
                db.child(contents).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        profile_img= snapshot.child(contents).child("profile_img").getValue(String.class);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });

                Map<String, Object> name = new HashMap<>();
                name.put("name", contents);
                name.put("profile_img",profile_img);
                db.child(user_name).child("other's report").push().setValue(name);
                Intent intent =new Intent(ScannerActivity.this,view_all_report.class);
                intent.putExtra("user",contents);
                startActivity(intent);

            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
