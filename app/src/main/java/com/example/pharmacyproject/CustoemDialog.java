package com.example.pharmacyproject;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pharmacyproject.Models.Medicine;
import com.example.pharmacyproject.Models.OrderItem;
import com.example.pharmacyproject.Views.AddToOrderListener;
import com.example.pharmacyproject.Views.MedicineAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class CustoemDialog extends androidx.fragment.app.DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder =new AlertDialog.Builder(requireContext());
        View v = LayoutInflater.from(requireContext()).inflate(R.layout.activity_custoem_dialog,null,false);
        EditText et_note = v.findViewById(R.id.customOrderNote);
        EditText et_order = v.findViewById(R.id.customOrderNumber);
        Button bt_add = v.findViewById(R.id.customBtAdd);
        Button bt_cancel = v.findViewById(R.id.customBtCancel);
      // String x = builder.setMessage(getArguments().getString("msg"));//get Mesg here

        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String note = et_note.getText().toString().trim();
                double order = Double.parseDouble(et_order.getText().toString().trim());
                if (TextUtils.isEmpty(note))
                    et_note.setError("Please enter your note");
                else if (TextUtils.isEmpty(et_order.getText().toString().trim()))
                    et_order.setError("Please enter your order");
                else{
                FirebaseFirestore fb = FirebaseFirestore.getInstance();
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(requireContext());
                String OrderUserId = sp.getString(Constants.USER_Uid_KEY, "noValue");
                Bundle mArgs = getArguments();
                String MedicineId = mArgs.getString("MedicineId");
                String uid = mArgs.getString("uid");
                String name = mArgs.getString("name");
                String type = mArgs.getString("type");
                Double theCost = mArgs.getDouble("theCost");
                Double price = mArgs.getDouble("price");
                String description = mArgs.getString("description");
                String image = mArgs.getString("image");
                Boolean isAdd = mArgs.getBoolean("isAdd");
                Medicine medicine = new Medicine(MedicineId, uid, name, type, theCost, price, description, image, isAdd);
                String OrderId = fb.collection("Order").document().getId();//هين بجيب اي دي الدوكيومنت

                OrderItem orderItem = new OrderItem(medicine, OrderId, OrderUserId, order, note, false);
                fb.collection("Order").document(orderItem.getOrderItemId()).set(orderItem).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(requireContext(), "addٍSecc", Toast.LENGTH_SHORT).show();
                        dismiss();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   Toast.makeText(requireContext(), "cancel", Toast.LENGTH_SHORT).show();
                dismiss();

            }
        });
        builder.setView(v);

        return builder.create();

    }
}