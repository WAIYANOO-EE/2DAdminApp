package com.ardam.admin2d3d.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.ardam.admin2d3d.MainActivity;
import com.ardam.admin2d3d.R;
import com.ardam.admin2d3d.models.Today;
import com.ardam.admin2d3d.ui.TodayformbottomSheet;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class TodayAdapter extends RecyclerView.Adapter<TodayAdapter.TodayViewHolder> {
    private ArrayList<Today> todayArrayList;
    private Context context;
    FragmentManager fm;

    public TodayAdapter(ArrayList<Today> todayArrayList,FragmentManager fm, Context context) {
        this.todayArrayList = todayArrayList;
        this.fm =fm;
        this.context = context;
    }

    @NonNull
    @Override
    public TodayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.today_items, parent, false);
        return new TodayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodayViewHolder holder, int position) {
        holder.txtDate.setText(todayArrayList.get(position).getDate());
        holder.txtMorning.setText(todayArrayList.get(position).getMorning());
        holder.txtEvening.setText(todayArrayList.get(position).getEvening());
    }

    @Override
    public int getItemCount() {
        return todayArrayList.size();
    }

    class TodayViewHolder extends RecyclerView.ViewHolder {
        TextView txtDate;
        TextView txtMorning;
        TextView txtEvening;


        public TodayViewHolder(@NonNull final View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtMorning = itemView.findViewById(R.id.txtMorning);
            txtEvening = itemView.findViewById(R.id.txtEvening);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    String[] items = {"Edit", "Delete"};

                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            final FirebaseFirestore db = FirebaseFirestore.getInstance();

                            final int position = getAdapterPosition();


                            switch (which) {
                                case 0:
                                    TodayformbottomSheet bottomSheet = new TodayformbottomSheet(todayArrayList.get(position));
                                    bottomSheet.show(fm,"Edit ");
                                    break;
                                case 1:
                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                                    builder1.setTitle("Are you sure you want to delete?");

                                    builder1.setPositiveButton(context.getResources().getString(R.string.alert_dialog_ok), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            db.collection("today")
                                                    .document(todayArrayList.get(position).getId())
                                                    .delete()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(context, "Delete Success!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Delete Failed! Please try again!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    });

                                    builder1.setNegativeButton(context.getResources().getString(R.string.alert_dialog_cancel), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });

                                    builder1.show();
                            }
                        }
                    });
                    builder.show();
                    return true;
                }

            });

        }
    }
}







