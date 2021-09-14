package com.example.pharmacyproject.Views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pharmacyproject.Models.Spenner;
import com.example.pharmacyproject.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SpennerAdapter extends BaseAdapter {
    ArrayList<Spenner> spenners ;
    Context context ;

    public SpennerAdapter(ArrayList<Spenner> spenners, Context context) {
        this.spenners = spenners;
        this.context = context;
    }

    @Override
    public int getCount() {
        return  spenners.size();
    }

    @Override
    public Object getItem(int position) {
        return spenners.get(position);
    }

    @Override
    public long getItemId(int position) {
        return  spenners.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v=convertView;
        if(v==null) {
            v = LayoutInflater.from(parent.getContext()).inflate//عملنا inflate ل
                    (R.layout.card_spenner, null, false);
        }
        TextView tv = v.findViewById(R.id.cardSpennerTextVName);
        ImageView iv = v.findViewById(R.id.cardSpennerImageV);

        Spenner spenner = (Spenner) getItem(position);
        Picasso.get().load(spenner.getImage()).into(iv);


        tv.setText(spenner.getName());//تعبئة البيانات



        return v;
    }

    public ArrayList<Spenner> getModels() {
        return spenners;
    }

    public void setModels(ArrayList<Spenner> models) {
        this.spenners = models;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
