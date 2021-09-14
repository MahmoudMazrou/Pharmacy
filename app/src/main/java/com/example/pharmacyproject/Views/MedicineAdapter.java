package com.example.pharmacyproject.Views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pharmacyproject.Models.Medicine;
import com.example.pharmacyproject.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class  MedicineAdapter extends RecyclerView.Adapter <MedicineAdapter.MedicinesHolder>{
    ArrayList<Medicine> medicines;
    Context context ;
    private AddToOrderListener listener;
    private EditListener editlistener;

    public EditListener getEditlistener() {
        return editlistener;
    }

    public void setEditlistener(EditListener editlistener) {
        this.editlistener = editlistener;
    }

    public MedicineAdapter() {

    }

    public ArrayList<Medicine> getMedicines() {
        return medicines;
    }

    public void setMedicines(ArrayList<Medicine> medicines) {
        this.medicines = medicines;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public AddToOrderListener getListener() {
        return listener;
    }

    public void setListener(AddToOrderListener listener) {
        this.listener = listener;
    }

    public MedicineAdapter(ArrayList<Medicine>medicines, Context context){
        this.medicines=medicines;
        this.context=context;
    }


    @NonNull
    @Override
    public MedicinesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate to card
        View v = LayoutInflater.from(context).inflate(R.layout.card_medicines,parent,false);
        MedicinesHolder holder =new MedicinesHolder(v);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull MedicinesHolder holder, int position) {
        //
        Medicine c =medicines.get(position);
        holder.tv_name.setText(c.getName());
        holder.tv_cost.setText(String.valueOf(c.getTheCost()));
        holder.tv_salary.setText(String.valueOf(c.getPrice()));
        Picasso.get().load(c.getImage()).into(holder.imageM);

        holder.tv_add_to_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               listener.onCarAddToOrder(c);
            }
        });
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editlistener.onEditListener(c);

            }
        });


    }

    @Override
    public int getItemCount() {
        return medicines.size();
    }


    public class MedicinesHolder extends RecyclerView.ViewHolder { //بعرف هين بلأولوبعمل inflate للعناصر
        TextView tv_name,tv_cost,tv_salary,tv_add_to_order;
        CardView card ;
        ImageView imageM;
        public MedicinesHolder(@NonNull View itemView) {
            super(itemView);
            tv_name=itemView.findViewById(R.id.cardMedicineName);
            tv_cost=itemView.findViewById(R.id.cardMedicineCost);
            tv_salary=itemView.findViewById(R.id.cardMedicineSalary);
            tv_add_to_order=itemView.findViewById(R.id.cardAddToOrder);
            imageM=itemView.findViewById(R.id.cardMedicineimage);

            card=itemView.findViewById(R.id.card);
        }
    }
}
