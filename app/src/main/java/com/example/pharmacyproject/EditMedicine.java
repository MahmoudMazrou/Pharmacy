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
import com.example.pharmacyproject.Models.UserProfail;
import com.example.pharmacyproject.databinding.ActivityEditMedicineBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

public class EditMedicine extends AppCompatActivity {
    ActivityEditMedicineBinding binding;
    private static final int PICK_IMG_REQ_CODE = 2;
    FirebaseFirestore fb;
    FirebaseStorage storage;
    Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityEditMedicineBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
       fb = FirebaseFirestore.getInstance();
        storage= FirebaseStorage.getInstance();
        Intent intent =getIntent();
        Bundle mArgs = intent.getBundleExtra("medicine");
        String MedicineId = mArgs.getString("MedicineId");
        String uid = mArgs.getString("uid");
        String name = mArgs.getString("name");
        String type = mArgs.getString("type");
        Double theCost = mArgs.getDouble("theCost");
        Double price = mArgs.getDouble("price");
        String description = mArgs.getString("description");
        String image = mArgs.getString("image");
        Boolean isAdd = mArgs.getBoolean("isAdd");
        //Medicine medicine = new Medicine(MedicineId, uid, name, type, theCost, price, description, image, isAdd);

        selectedImageUri= Uri.parse(image);

        binding.editTextName.setText(name);
        binding.editTextTaype.setText(type);
        binding.editTextTheCost.setText(String.valueOf(theCost));
        binding.editTextSalary.setText(String.valueOf(price));
        binding.editTextDescription.setText(description);
        Picasso.get().load(image).into(binding.addIv);
        binding.editTextName.setEnabled(false);
        binding.editTextTaype.setEnabled(false);
        binding.editTextTheCost.setEnabled(false);
        binding.editTextSalary.setEnabled(false);
        binding.editTextDescription.setEnabled(false);
        binding.addIv.setEnabled(false);

        binding.editIvEdit2.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              binding.editTextName.setEnabled(true);
              binding.editTextTaype.setEnabled(true);
              binding.editTextTheCost.setEnabled(true);
              binding.editTextSalary.setEnabled(true);
              binding.editTextDescription.setEnabled(true);
              binding.addIv.setEnabled(true);
          }
      });

        binding.ButtonEditMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Nname =binding.editTextName.getText().toString().trim();
                String Ntype =binding.editTextTaype.getText().toString().trim();
                String Ndescription = binding.editTextDescription.getText().toString().trim();
                if (TextUtils.isEmpty(Nname))
                    binding.editTextName.setError("Please enter your name");
                else if (TextUtils.isEmpty(Ntype))
                    binding.editTextTaype.setError("Please enter your type");
                else if (TextUtils.isEmpty(Ndescription))
                    binding.editTextDescription.setError("Please enter your description");
                else if (TextUtils.isEmpty(binding.editTextTheCost.getText().toString().trim()))
                    binding.editTextDescription.setError("Please enter your cost number");
                else if (TextUtils.isEmpty(binding.editTextSalary.getText().toString().trim()))
                    binding.editTextDescription.setError("Please enter your salary number");
                else {
                    Double NtheCost = Double.parseDouble(binding.editTextTheCost.getText().toString().trim());
                    Double Nprice = Double.parseDouble(binding.editTextSalary.getText().toString().trim());
                    Medicine Nmedicine = new Medicine(MedicineId, uid, Nname, Ntype, NtheCost, Nprice, Ndescription, image, isAdd);
                    uploadImageIntoFirebaseStorage(Nmedicine);
                }
            }
        });
        binding.addIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                galleryIntent.setType("image/*");

                startActivityForResult(galleryIntent, PICK_IMG_REQ_CODE);
            }
        });

    }
    private void uploadImageIntoFirebaseStorage(Medicine item) {
        Calendar calendar = Calendar.getInstance();
        storage.getReference().child("itemsM/"+calendar.getTimeInMillis()).putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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
                Toast.makeText(EditMedicine.this, "Modified", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditMedicine.this, e.toString(), Toast.LENGTH_SHORT).show();

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