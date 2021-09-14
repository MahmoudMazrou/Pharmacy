package com.example.pharmacyproject.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pharmacyproject.AddNewMedicineActivity;
import com.example.pharmacyproject.Constants;
import com.example.pharmacyproject.CustoemDialog;
import com.example.pharmacyproject.EditMedicine;
import com.example.pharmacyproject.Models.Medicine;
import com.example.pharmacyproject.R;
import com.example.pharmacyproject.Views.AddToOrderListener;
import com.example.pharmacyproject.Views.EditListener;
import com.example.pharmacyproject.Views.MedicineAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class MedicineFragment extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "model"; // متغير ثابت زي مفتاح يعني بستخدمهم لمن بدي ارسل بياات للفراقمنت


    // TODO: Rename and change types of parameters
    private String model;

    public MedicineFragment() {//هاد لازم بكون فاضي قاعدة في الفراقمنت
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment MyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MedicineFragment newInstance(String param1) {//كل متنشأ اوبجكت جديد بستدعيها يعني بعمل تجميع للعمليات هي مثوود
        MedicineFragment fragment = new MedicineFragment();
        Bundle args = new Bundle();//حزمة يعني ابوجكت بتحط في البيانات مفتاح وقيمة
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);//خزنت الحزمة في الفراقمنت
        return fragment;//رجعها !
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            model = getArguments().getString(ARG_PARAM1);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        //   final TextView textView = root.findViewById(R.id.text_home);
        RecyclerView rv =root.findViewById(R.id.medicineRv);
        FloatingActionButton  floa  =root.findViewById(R.id.fab);
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String  OrderUserId = sp.getString(Constants.USER_Uid_KEY,"noValue");
        floa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(requireContext(), AddNewMedicineActivity.class);
                startActivity(intent);
            }
        });

        fb.collection("Medicine").addSnapshotListener(new EventListener<QuerySnapshot>() {//addSnapshotListener هايبتحدث البيانات تلقائي
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.size() > 0) {  //هاد الكودد للاضافة العناصر ع الاراي
                    ArrayList<Medicine> items = new ArrayList<>();
                    for (QueryDocumentSnapshot q : value) {
                        Medicine i = q.toObject(Medicine.class);
                        items.add(i);
                    }
                    MedicineAdapter adapter =new MedicineAdapter(items,requireContext());
                    adapter.setListener(new AddToOrderListener() {
                        @Override
                        public void onCarAddToOrder(Medicine medicine) {
                            if(medicine!=null) {
                                String MedicineId = medicine.getMedicineId();
                                String uid = medicine.getUid();
                                String name = medicine.getName();
                                String type = medicine.getType();
                                double theCost = medicine.getTheCost();
                                double price = medicine.getPrice();
                                String description = medicine.getDescription();
                                String image = medicine.getImage();
                                boolean isAdd = medicine.isAdd();
                                Bundle args = new Bundle();
                                args.putString("MedicineId", MedicineId);
                                args.putString("uid", uid);
                                args.putString("name", name);
                                args.putString("type", type);
                                args.putDouble("theCost", theCost);
                                args.putDouble("price", price);
                                args.putString("description", description);
                                args.putString("image", image);
                                args.putBoolean("isAdd", isAdd);
                                DialogFragment newFragment = new CustoemDialog();
                                newFragment.setArguments(args);
                                newFragment.show(requireFragmentManager(), "Medicine");
                                //new CustoemDialog().show(requireFragmentManager(),null);
                            }else {
                                Toast.makeText(requireContext(), "medicen Null", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                    adapter.setEditlistener(new EditListener() {
                        @Override
                        public void onEditListener(Medicine medicine) {
                            String MedicineId = medicine.getMedicineId();
                            String uid = medicine.getUid();
                            String name = medicine.getName();
                            String type = medicine.getType();
                            double theCost = medicine.getTheCost();
                            double price = medicine.getPrice();
                            String description = medicine.getDescription();
                            String image = medicine.getImage();
                            boolean isAdd = medicine.isAdd();
                            Bundle args = new Bundle();
                            args.putString("MedicineId", MedicineId);
                            args.putString("uid", uid);
                            args.putString("name", name);
                            args.putString("type", type);
                            args.putDouble("theCost", theCost);
                            args.putDouble("price", price);
                            args.putString("description", description);
                            args.putString("image", image);
                            args.putBoolean("isAdd", isAdd);
                            Intent intent =new Intent(requireContext(), EditMedicine.class);
                            intent.putExtra("medicine",args);
                            startActivity(intent);


                        }
                    });
                    rv.setAdapter(adapter);
                    rv.setHasFixedSize(true);
                  rv.setLayoutManager(new LinearLayoutManager(requireContext()));
                }
            }
        });

   return root;
    }
}