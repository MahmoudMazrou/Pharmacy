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
import com.example.pharmacyproject.databinding.ActivityEditProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;


public class EditProfile extends AppCompatActivity {
    private static final int PICK_IMG_REQ_CODE = 2;
    ActivityEditProfileBinding binding ;
       SharedPreferences sp;
       FirebaseFirestore fb ;
     FirebaseStorage storage;
    Uri selectedImageUri;
    String uId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        fb = FirebaseFirestore.getInstance();
        storage= FirebaseStorage.getInstance();

        sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        uId =sp.getString(Constants.USER_Uid_KEY,"1");
        String name = sp.getString(Constants.USER_NAME_KEY,"Dr");
        String imgD = sp.getString(Constants.USER_IMAGE_KEY,"https://juhaina.in/media/lib/pics/1389844881.jpg");
        String mobile = sp.getString(Constants.USER_MOBILE_NUMBER_KEY,"Dr");
        String email = sp.getString(Constants.USER_EMAIL_KEY,"Dr");
        selectedImageUri= Uri.parse("https://juhaina.in/media/lib/pics/1389844881.jpg");

        binding.editTextNameEdit.setText(name);
        Picasso.get().load(imgD).into(binding.editIv);
        binding.editTextMobileEdit.setText(mobile);
        binding.editTextEmailEdit.setText(email);
        //

    //
        binding.editTextNameEdit.setEnabled(false);
        binding.editIv.setEnabled(false);
        binding.editTextMobileEdit.setEnabled(false);
        binding.editTextEmailEdit.setEnabled(false);
        binding.cirEditButton.setEnabled(false);

        binding.editIvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.editTextNameEdit.setEnabled(true);
                binding.editIv.setEnabled(true);
                binding.editTextMobileEdit.setEnabled(true);
                binding.editTextEmailEdit.setEnabled(true);
                binding.cirEditButton.setEnabled(true);

            }
        });
        binding.editIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, PICK_IMG_REQ_CODE);
            }
        });
        binding.cirEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.editTextNameEdit.setEnabled(false);
                binding.editIv.setEnabled(false);
                binding.editTextMobileEdit.setEnabled(false);
                binding.editTextEmailEdit.setEnabled(false);
                String nName = binding.editTextNameEdit.getText().toString().trim();
                String nEmail = binding.editTextEmailEdit.getText().toString().trim();
                String nMopileNem = binding.editTextMobileEdit.getText().toString().trim();
                if (TextUtils.isEmpty(nName))
                    binding.editTextNameEdit.setError("Please enter your name");
                else if (TextUtils.isEmpty(nEmail))
                    binding.editTextEmailEdit.setError("Please enter your email");
                else if (TextUtils.isEmpty(nMopileNem))
                    binding.editTextMobileEdit.setError("Please enter your mobile number");
                else {
                    UserProfail userProfail = new UserProfail(uId,nName,nEmail,nMopileNem,imgD);
                    binding.EditProgressBar.setVisibility(View.VISIBLE);
                    uploadImageIntoFirebaseStorage(userProfail);

                }

            }
        });


    }
    private void uploadImageIntoFirebaseStorage(UserProfail item) {
        Calendar calendar = Calendar.getInstance();
        storage.getReference().child("itemsP/"+calendar.getTimeInMillis()).putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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
    private void addItemToFirestore(UserProfail item) {
        fb.collection("User").document(uId).set(item).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                  // جبنا ملفات اليوزر المحدد
                sp= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                SharedPreferences.Editor editor =sp.edit();
                editor.putString(Constants.USER_Uid_KEY,item.getUid());
                editor.putString(Constants.USER_NAME_KEY,item.getName());
                editor.putString(Constants.USER_EMAIL_KEY,item.getEmail());
                editor.putString(Constants.USER_MOBILE_NUMBER_KEY,item.getMobileNumber());
                editor.putString(Constants.USER_IMAGE_KEY,item.getImage());
                editor.commit();
                Toast.makeText(getBaseContext(), "Success!!", Toast.LENGTH_SHORT).show();
                Intent intent =new Intent(getBaseContext(),MainActivity.class);
                startActivity(intent);
                finish();
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

            binding.editIv.setImageURI(u);
            selectedImageUri = u;
        }
    }
}