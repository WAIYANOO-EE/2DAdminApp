package com.ardam.admin2d3d.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ardam.admin2d3d.R;
import com.ardam.admin2d3d.models.Today;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class TodayformbottomSheet extends BaseBottomSheet {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference todayRef;
    Today today = null;
    Button etdate;
    EditText morning;
    EditText evening;
     Button addToday;
    DatePickerDialog datePickerDialog;


    public TodayformbottomSheet() {
    }

    public TodayformbottomSheet(Today today) {
        this.today = today;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.today_form, container,false);
        etdate = view.findViewById(R.id.btnDate);
        morning = view.findViewById(R.id.et_morning);
        evening = view.findViewById(R.id.et_evening);
        addToday = view.findViewById(R.id.btn_add);
        if (today != null) {
            addToday.setText("Edit");
        }

        etdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               final Calendar calendar = Calendar.getInstance();
               final int year = calendar.get(Calendar.YEAR);
               final int month = calendar.get(Calendar.MONTH);
               final int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);



                datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int dayOfMonth, int month, int year) {
                        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                            etdate.setText(year +" / "+(month+1) +" / "+dayOfMonth);
                    }
                }, year, month, dayOfMonth);

                                boolean isDateExist = false;
                                isDateExist = true;

                datePickerDialog.show();

            }
        });
        addToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String date = etdate.getText().toString().trim();
                final ArrayList<Today> todayArrayList = new ArrayList<>();
                db.collection("today")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                todayArrayList.clear();

                                ArrayList<String> ids = new ArrayList<>();
                                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots)
                                    {

                                        ids.add(documentSnapshot.getId());
                                        todayArrayList.add(documentSnapshot.toObject(Today.class));
                                    }
                                    for (int i = 0; i < todayArrayList.size(); i++) {
                                        Today today1 = new Today();
                                        today1 = todayArrayList.get(i);
                                            if (today1.equals(date)) {
                                                today1.morning = morning.getText().toString().trim();
                                                today1.evening = evening.getText().toString().trim();
                                                db.collection("today")
                                                        .document(ids.get(i))
                                                        .set(today1)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });

                                            } else {
                                                today1.setDate(etdate.getText().toString().trim());
                                                today1.morning = morning.getText().toString().trim();
                                                today1.evening = evening.getText().toString().trim();
                                                db.collection("today")
                                                        .add(today1)
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                            @Override
                                                            public void onSuccess(DocumentReference documentReference) {

                                                            }
                                                        });
                                            }
                                        }





                if (!etdate.equals(date))
                {
                    String etMorning = morning.getText().toString().trim();
                    String etEvening = evening.getText().toString().trim();
                    String id;
                    if (today != null) {
                        id = today.getId();
                    } else {
                        id = UUID.randomUUID().toString();
                    }
                    final Today today = new Today(id, etMorning,etEvening);
                    db.collection("today").document(id)
                            .set(today)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    if (TodayformbottomSheet.this.today != null) {
                                        dismiss();
                                        return;
                                    }
                                    morning.setText("");
                                    evening.setText("");

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Today today1=new Today();
                        today1.setDate(etdate.getText().toString().trim());
                        today1.morning = morning.getText().toString().trim();
                        today1.evening = evening.getText().toString().trim();
                        db.collection("today")
                                .add(today1)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {

                                    }
                                });
                    }
                });
            }
        });
        return view;
    }
}
