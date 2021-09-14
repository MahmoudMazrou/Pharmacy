package com.example.pharmacyproject;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pharmacyproject.Models.UserProfail;
import com.example.pharmacyproject.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore fb;
    SharedPreferences sp;
    private void getUserDataFromFirestore(String uid) {//مثوود

        fb.collection("User").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot!= null){
                    UserProfail userProfail = documentSnapshot.toObject(UserProfail.class);// جبنا ملفات اليوزر المحدد
                   sp= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    SharedPreferences.Editor editor =sp.edit();
                    editor.putString(Constants.USER_Uid_KEY,userProfail.getUid());
                    editor.putString(Constants.USER_NAME_KEY,userProfail.getName());
                    editor.putString(Constants.USER_EMAIL_KEY,userProfail.getEmail());
                    editor.putString(Constants.USER_MOBILE_NUMBER_KEY,userProfail.getMobileNumber());
                    editor.putString(Constants.USER_IMAGE_KEY,userProfail.getImage());
                    editor.commit();
                    Toast.makeText(getBaseContext(), "Success!!", Toast.LENGTH_SHORT).show();
                    binding.progressBar.setVisibility(View.GONE);
                    binding.Login.setEnabled(true);
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                binding.progressBar.setVisibility(View.GONE);
                binding.Login.setEnabled(true);

            }
        });

    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        if(auth!=null && auth.getCurrentUser()!=null && sp.getString(Constants.USER_Uid_KEY,null)!=null){
//            Intent intent = new Intent(getBaseContext(), MainActivity.class);
//            startActivity(intent);
//            finish();
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        auth =FirebaseAuth.getInstance();
        fb =FirebaseFirestore.getInstance();


        binding.Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.usernameInput.getText().toString().trim();
                String pass = binding.pass.getText().toString().trim();
                if (TextUtils.isEmpty(email))
                    binding.usernameInput.setError("Please enter your email");
                else if (TextUtils.isEmpty(pass))
                    binding.pass.setError("Please enter your password");
                    else{
                    binding.progressBar.setVisibility(View.VISIBLE);//اظهر  البروقريس بار قبل ميعمل تسجيل بضبط
                    binding.Login.setEnabled(false);//وقف الزر لونو بوتون
                    auth.signInWithEmailAndPassword(email, pass)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    if(authResult!=null)//تجنباً للاخطاء
                                    {
                                        getUserDataFromFirestore(authResult.getUser().getUid());// مثود انا عملتها بتستقبل يوزر اي دي وبتجيب بياناتو من fiarstore

                                    }
                                }


                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            binding.progressBar.setVisibility(View.GONE);
                            binding.Login.setEnabled(true);

                        }
                    });
                }
            }
        });



        binding.NewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getBaseContext(),RegisterActivity.class);
                startActivity(intent);

            }
        });
    }


}
