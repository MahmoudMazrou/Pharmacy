package com.example.pharmacyproject;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pharmacyproject.Models.UserProfail;
import com.example.pharmacyproject.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;


public class RegisterActivity extends AppCompatActivity {

     ActivityRegisterBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore fb ;
    FirebaseStorage storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding =ActivityRegisterBinding.inflate(getLayoutInflater());

        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        auth =FirebaseAuth.getInstance();
        fb =FirebaseFirestore.getInstance();


        binding.cirRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email =binding.editTextEmail.getText().toString().trim();
                String pass = binding.editTextPassword.getText().toString().trim();
                String name = binding.editTextName.getText().toString().trim();
                String mobileNember = binding.editTextMobile.getText().toString().trim();
                String image = "https://juhaina.in/media/lib/pics/1389844881.jpg";
                if(TextUtils.isEmpty(email))
                    binding.editTextEmail.setError("Please enter your email");
                else if(TextUtils.isEmpty(pass))
                    binding.editTextPassword.setError("Please enter your password");
                else if(TextUtils.isEmpty(name))
                    binding.editTextName.setError("Please enter your name");
                else if(TextUtils.isEmpty(mobileNember))
                    binding.editTextMobile.setError("Please enter your mobile nember");
                else {
                     binding.redesterProgressBar.setVisibility(View.VISIBLE);//اظهر  البروقريس بار قبل ميعمل تسجيل بضبط
                     binding.cirRegisterButton.setEnabled(false);//وقف الزر لونو بوتون
                auth.createUserWithEmailAndPassword(email,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        UserProfail userProfail =new UserProfail(auth.getUid(),name,email,mobileNember,image);
//عملنا هايdocument(userProfail.getUid())  عشان اصل لليوزر  بسهولة اريح بليوزر يعني اوحد الاي دي يكن نفسو لمسجل عنا
                        fb.collection("User").document(userProfail.getUid()).set(userProfail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                binding.redesterProgressBar.setVisibility(View.GONE);//اظهر  البروقريس بار قبل ميعمل تسجيل بضبط
                                binding.cirRegisterButton.setEnabled(true);//وقف الزر
                                Toast.makeText(getBaseContext(), "Success!!", Toast.LENGTH_SHORT).show();
                                Intent intent =new Intent(getBaseContext(),LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                binding.redesterProgressBar.setVisibility(View.GONE);//اظهر  البروقريس بار قبل ميعمل تسجيل بضبط
                                binding.cirRegisterButton.setEnabled(true);//وقف الزر

                            }
                        });


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        binding.redesterProgressBar.setVisibility(View.GONE);//اظهر  البروقريس بار قبل ميعمل تسجيل بضبط
                        binding.cirRegisterButton.setEnabled(true);//وقف الزر
                    }
                });
                }
            }
        });
        binding.bakToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getBaseContext(),LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        binding.AlreadyHave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getBaseContext(),LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }





}
