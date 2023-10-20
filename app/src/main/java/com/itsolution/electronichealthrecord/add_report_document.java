package com.itsolution.electronichealthrecord;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class add_report_document extends AppCompatActivity {



    FirebaseDatabase rootNode;
    AutoCompleteTextView record_type;
    EditText Physician_Name,Note,Description,Specialization,Diseases,Date,Contact_info;
    ImageView add_dr_img;
    String user_name;
    String str_Physician_Name="",str_Note="",str_Description="",str_record_type,str_specialization, str_diseases="",str_date="",str_contact_info;
    private static final int Gallery_Code=1;
    Uri image_uri=null,dr_img=null;

    Task<Uri> task_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_report_document);



        ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.hide();

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.top_bar_of_add_health_record));
        }



        SharedPreferences sharedPref = getSharedPreferences("user_info", MODE_PRIVATE);
        user_name=sharedPref.getString("name","");


        record_type=findViewById(R.id.record_type);
        String[] report_type={"X-ray","MRI","CT scans","Ultrasound","ECG","Pathology","Blood test","Urine test","Fecal test","Microbiology","Radiology","EEG","Other's"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, report_type);
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.record_type);
        textView.setAdapter(adapter);
        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
                textView.showDropDown();
                textView.requestFocus();
                return false;
            }
        });



        Physician_Name=findViewById(R.id.physician_name);
        Note=findViewById(R.id.note);
        Description=findViewById(R.id.description);
        Specialization=findViewById(R.id.specialization);
        Diseases=findViewById(R.id.diseases);
        Date=findViewById(R.id.date_of_visit);
        Contact_info=findViewById(R.id.contact_info);
        add_dr_img=findViewById(R.id.add_dr_img);



        CardView register_user=findViewById(R.id.submit);


        register_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                str_Physician_Name=Physician_Name.getText().toString();
                str_Note=Note.getText().toString();
                str_Description=Description.getText().toString();
                str_record_type=record_type.getText().toString();
                str_specialization=Specialization.getText().toString();
                str_diseases=Diseases.getText().toString();
                str_date=Date.getText().toString();
                str_contact_info=Contact_info.getText().toString();


                if(!str_Physician_Name.equals("") && !str_Note.equals("") && !str_Description.equals("") && !str_record_type.equals("") && !str_specialization.equals("") && !str_diseases.equals("")&& !str_date.equals("") && !str_contact_info.equals("")){

                    DatabaseReference reference= FirebaseDatabase.getInstance().getReference("owner");
                    Query checkUser=reference.orderByChild("name").equalTo(user_name);
                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()) {
                                Dialog dialog_=new Dialog(add_report_document.this);
                                dialog_.setContentView(R.layout.add_report_img);
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

                    Toast.makeText(add_report_document.this, "Empty field", Toast.LENGTH_SHORT).show();

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


            Dialog dialog_=new Dialog(add_report_document.this);
            dialog_.setContentView(R.layout.location);
            dialog_.getWindow().setBackgroundDrawableResource(R.drawable.bacground_for_dialog);
            dialog_.setCancelable(false);
            dialog_.show();
            TextView txt=dialog_.findViewById(R.id.textview_text);
            txt.setText("Uploading health record to the server");
            CardView ok=dialog_.findViewById(R.id.submit_btn);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(add_report_document.this, "wait for a while!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void upload_to_server() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference filepath = storage.getReference().child("report_images").child(image_uri.getLastPathSegment());
        filepath.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        DatabaseReference reference;
                        rootNode = FirebaseDatabase.getInstance();
                        reference = rootNode.getReference("owner");
                        HashMap<String, String> UserMap = new HashMap<>();
                        UserMap.put("Physician_Name", str_Physician_Name);
                        UserMap.put("Specialization", str_specialization);
                        UserMap.put("Note", str_Note);
                        UserMap.put("Description", str_Description);
                        UserMap.put("Record_Type", str_record_type);
                        UserMap.put("report_img", task.getResult().toString());
                        UserMap.put("dr_img",task.getResult().toString());
                        UserMap.put("visit_date",str_date);
                        UserMap.put("diseases",str_diseases);
                        UserMap.put("contact_info",str_contact_info);
                        reference.child(user_name).child("reports").push().setValue(UserMap);
                        Intent intent = new Intent(add_report_document.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });


    }



}