package com.itsolution.electronichealthrecord;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class view_all_report extends AppCompatActivity {


    String user_name;
    RecyclerView recyclerView;
    adapter_to_get_data_OwnerDashboard adapter;
    List<model_for_owner_dash> model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_report);


        ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.hide();

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.top_bar_of_add_health_record));
        }


        Intent intent = getIntent();

        if (intent != null) {
            user_name = intent.getStringExtra("user");
        }



        DatabaseReference firebaseDatabase_for_list_data= FirebaseDatabase.getInstance().getReference().child("owner").child(user_name).child("reports");
        recyclerView=findViewById(R.id.list_of_report);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        model=new ArrayList<model_for_owner_dash>();
        adapter=new adapter_to_get_data_OwnerDashboard(view_all_report.this,model);
        recyclerView.setAdapter(adapter);

        firebaseDatabase_for_list_data.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                model_for_owner_dash student_model=snapshot.getValue(model_for_owner_dash.class);
                model.add(student_model);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }
}