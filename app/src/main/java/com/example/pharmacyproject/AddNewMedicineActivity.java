package com.example.pharmacyproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;

import android.widget.Toast;

import com.example.pharmacyproject.Models.Medicine;
import com.example.pharmacyproject.Models.Spenner;
import com.example.pharmacyproject.Views.SpennerAdapter;
import com.example.pharmacyproject.databinding.ActivityAddNewMedicineBinding;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

public class AddNewMedicineActivity extends AppCompatActivity {
    private static final int PICK_IMG_REQ_CODE = 2;
    ActivityAddNewMedicineBinding binding;
    FirebaseFirestore fb ;
    String currentUserId ;
    SharedPreferences sp;
    FirebaseStorage storage;
    Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding =ActivityAddNewMedicineBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        MyDatabase db =new MyDatabase(this);
        ArrayList<Spenner> spenners =db.getAllSpenner();
        SpennerAdapter spinnerAdapter =new SpennerAdapter(spenners,this);
        binding.spinner.setAdapter(spinnerAdapter);
        fb=FirebaseFirestore.getInstance();
        storage= FirebaseStorage.getInstance();
        sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
      selectedImageUri= Uri.parse("https://www.annahar.com/ContentFiles/85451Image1-1180x677_d.jpg?version=737107");
        binding.addIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, PICK_IMG_REQ_CODE);
            }
        });


        binding.ButtonAddMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // int id = Integer.parseInt( binding..getText().toString().trim());
                String name = binding.editTextAddName.getText().toString().trim();
                String desc = binding.editTextAddDescription.getText().toString().trim();
                String spinnerItem =(( Spenner) binding.spinner.getSelectedItem()).getName();

                if (TextUtils.isEmpty(name))
                    binding.editTextAddName.setError("Please enter your name");
                else if (TextUtils.isEmpty( binding.editTextAddTheCost.getText().toString().trim()))
                    binding.editTextAddTheCost.setError("Please enter your the Cost");
                else if (TextUtils.isEmpty(binding.editTextAddSalary.getText().toString().trim()))
                    binding.editTextAddSalary.setError("Please enter your salary");
                else if (TextUtils.isEmpty(desc))
                    binding.editTextAddDescription.setError("Please enter your desc");
                else {
                    double theCost = Double.parseDouble( binding.editTextAddTheCost.getText().toString().trim());
                    double salary = Double.parseDouble( binding.editTextAddSalary.getText().toString().trim());
                    String medicineId = fb.collection("Medicine").document().getId();//هين بجيب اي دي الدوكيومنت
                    currentUserId = sp.getString(Constants.USER_Uid_KEY, "noValue");
                    Medicine item = new Medicine(medicineId, currentUserId, name, spinnerItem, theCost, salary, desc, "https://www.annahar.com/ContentFiles/85451Image1-1180x677_d.jpg?version=737107", false);
                    uploadImageIntoFirebaseStorage(item);
                }

            }
        });


    }
    private void uploadImageIntoFirebaseStorage(Medicine item) {
        Calendar calendar = Calendar.getInstance();
        storage.getReference().child("items/"+calendar.getTimeInMillis()).putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // another request to get image url
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        item.setImage(uri.toString());
                       addItemToFirestore(item);

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                addItemToFirestore(item);
           //     Toast.makeText(getBaseContext(), "Failed to upload item image", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void addItemToFirestore(Medicine item) {
        fb.collection("Medicine").document(item.getMedicineId()).set(item).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getBaseContext(), "Success!!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getBaseContext(), e.getMessage() ,Toast.LENGTH_SHORT).show();

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMG_REQ_CODE && resultCode == RESULT_OK && data != null) {
            Uri u = data.getData();
            int flag = data.getFlags() & (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            getContentResolver().takePersistableUriPermission(u,flag);

            binding.addIv.setImageURI(u);
            selectedImageUri = u;
        }
    }

}