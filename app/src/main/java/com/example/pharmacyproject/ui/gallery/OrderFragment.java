package com.example.pharmacyproject.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pharmacyproject.Models.Medicine;
import com.example.pharmacyproject.Models.OrderItem;
import com.example.pharmacyproject.R;
import com.example.pharmacyproject.Views.DeleteOrderListener;
import com.example.pharmacyproject.Views.IncreaseDecreaseOrderListener;
import com.example.pharmacyproject.Views.IsShowOrderListener;
import com.example.pharmacyproject.Views.OrderAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class OrderFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "model"; // متغير ثابت زي مفتاح يعني بستخدمهم لمن بدي ارسل بياات للفراقمنت


    // TODO: Rename and change types of parameters
    private String model;

    public OrderFragment() {//هاد لازم بكون فاضي قاعدة في الفراقمنت
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
    public static OrderFragment newInstance(String param1) {//كل متنشأ اوبجكت جديد بستدعيها يعني بعمل تجميع للعمليات هي مثوود
        OrderFragment fragment = new OrderFragment();
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

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        //   final TextView textView = root.findViewById(R.id.text_home);
        RecyclerView rv =root.findViewById(R.id.orderRv);
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
setHasOptionsMenu(true);
        fb.collection("Order").addSnapshotListener(new EventListener<QuerySnapshot>() {//addSnapshotListener هايبتحدث البيانات تلقائي
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.size() > 0) {  //هاد الكودد للاضافة العناصر ع الاراي
                    ArrayList<OrderItem> items = new ArrayList<>();
                    for (QueryDocumentSnapshot q : value) {
                        OrderItem i = q.toObject(OrderItem.class);
                        items.add(i);
                    }
                    OrderAdapter adapter =new OrderAdapter(items,requireContext());

                    adapter.setListener(new DeleteOrderListener() {
                        @Override
                        public void onDeleteOrder(OrderItem orderItem) {
                            fb.collection("Order").document(orderItem.getOrderItemId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(requireContext(), "secssDelete", Toast.LENGTH_SHORT).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    adapter.setIncreaseDecreaseOrderListener(new IncreaseDecreaseOrderListener() {
                        @Override
                        public void onIncreaseOrder(OrderItem orderItem) {
                            Medicine medicine = orderItem.getMedicine();
                             String OrderItemId =orderItem.getOrderItemId();
                             String uid =orderItem.getUid();
                             double order =orderItem.getOrder();
                             String descriptionOrder =orderItem.getDescriptionOrder();
                             boolean isReceipt =orderItem.isReceipt();
                           order=order+1.0;
                             OrderItem newOrd = new OrderItem(medicine,OrderItemId,uid,order,descriptionOrder,isReceipt);
                            fb.collection("Order").document(orderItem.getOrderItemId()).set(newOrd).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Toast.makeText(requireContext(), "+1", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();


                                }
                            });
                        }

                        @Override
                        public void onDecreaseOrder(OrderItem orderItem) {
                            Medicine medicine = orderItem.getMedicine();
                            String OrderItemId =orderItem.getOrderItemId();
                            String uid =orderItem.getUid();
                            double order =orderItem.getOrder();
                            String descriptionOrder =orderItem.getDescriptionOrder();
                            boolean isReceipt =orderItem.isReceipt();
                            if(order>0){
                            order=order-1.0;
                            OrderItem newOrd = new OrderItem(medicine,OrderItemId,uid,order,descriptionOrder,isReceipt);
                            fb.collection("Order").document(orderItem.getOrderItemId()).set(newOrd).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                        Toast.makeText(requireContext(), "-1", Toast.LENGTH_SHORT).show();
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
                    adapter.setIsShowOrderListener(new IsShowOrderListener() {
                        @Override
                        public void onIsShowOrder(OrderItem orderItem) {  Medicine medicine = orderItem.getMedicine();
                            String OrderItemId =orderItem.getOrderItemId();
                            String uid =orderItem.getUid();
                            double order =orderItem.getOrder();
                            String descriptionOrder =orderItem.getDescriptionOrder();
                            boolean isReceipt =orderItem.isReceipt();
                            isReceipt=!isReceipt;
                            OrderItem newOrd = new OrderItem(medicine,OrderItemId,uid,order,descriptionOrder,isReceipt);
                            fb.collection("Order").document(orderItem.getOrderItemId()).set(newOrd).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Toast.makeText(requireContext(), "ok", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();


                                }
                            });
                        }
                    });
                    rv.setAdapter(adapter);
                    rv.setHasFixedSize(true);
                    rv.setLayoutManager(new LinearLayoutManager(requireContext()));                }
            }
        });

        return root;
    }
}