package com.ardam.admin2d3d;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.ardam.admin2d3d.adapter.TodayAdapter;
import com.ardam.admin2d3d.models.Today;
import com.ardam.admin2d3d.ui.TodayformbottomSheet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db;
    RecyclerView rcvToday;
    Context context;
    ArrayList<Today> todayArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rcvToday = findViewById(R.id.rcv_today);
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TodayformbottomSheet bottomSheet = new TodayformbottomSheet();
                bottomSheet.show(getSupportFragmentManager(),bottomSheet.getTag());
            }
        });

        rcvToday.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        rcvToday.setLayoutManager(manager);

        db = FirebaseFirestore.getInstance();
        db.collection("today")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                        if (error != null)
                        {
                            Toast.makeText(context, "Listen Failed", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        todayArrayList.clear();
                        for (QueryDocumentSnapshot doc: queryDocumentSnapshots)
                        {

                            Today today = doc.toObject(Today.class);
                            todayArrayList.add(today);
                        }

                        TodayAdapter adapter = new TodayAdapter(todayArrayList,getSupportFragmentManager(),context);
                        rcvToday.setAdapter(adapter);
                    }

                });

    }
}
