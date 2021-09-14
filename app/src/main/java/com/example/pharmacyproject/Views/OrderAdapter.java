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


import com.example.pharmacyproject.Models.OrderItem;
import com.example.pharmacyproject.R;


import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter <OrderAdapter.OrderHolder>{
    ArrayList<OrderItem> orderItems;
    Context context ;
    DeleteOrderListener listener ;
    IncreaseDecreaseOrderListener increaseDecreaseOrderListener;
    IsShowOrderListener isShowOrderListener;

    public IsShowOrderListener getIsShowOrderListener() {
        return isShowOrderListener;
    }

    public void setIsShowOrderListener(IsShowOrderListener isShowOrderListener) {
        this.isShowOrderListener = isShowOrderListener;
    }

    public IncreaseDecreaseOrderListener getIncreaseDecreaseOrderListener() {
        return increaseDecreaseOrderListener;
    }

    public void setIncreaseDecreaseOrderListener(IncreaseDecreaseOrderListener increaseDecreaseOrderListener) {
        this.increaseDecreaseOrderListener = increaseDecreaseOrderListener;
    }

    public ArrayList<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(ArrayList<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public DeleteOrderListener getListener() {
        return listener;
    }

    public void setListener(DeleteOrderListener listener) {
        this.listener = listener;
    }

    public OrderAdapter(ArrayList<OrderItem>orderItems, Context context){
        this.orderItems=orderItems;
        this.context=context;
    }


    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate to card
        View v = LayoutInflater.from(context).inflate(R.layout.card_order,parent,false);
        OrderHolder holder =new OrderHolder(v);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull OrderHolder holder, int position) {
        //
        OrderItem c =orderItems.get(position);
        holder.tv_name.setText(c.getMedicine().getName());
        holder.tv_order_des.setText(c.getDescriptionOrder());
        holder.tv_order.setText(String.valueOf(c.getOrder()));
        if(c.isReceipt()==true){
            holder.OrderIsShoYes.setVisibility(View.VISIBLE);}
        else if (c.isReceipt()==false) {
            holder.OrderIsShoYes.setVisibility(View.INVISIBLE);}


        holder.OrderDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDeleteOrder(c);
            }
        });
        holder.OrderAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                increaseDecreaseOrderListener.onIncreaseOrder(c);
            }
        });
        holder.OrderSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseDecreaseOrderListener.onDecreaseOrder(c);
            }
        });
        holder.OrderIsShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isShowOrderListener.onIsShowOrder(c);
                if(c.isReceipt()==true){
                    holder.OrderIsShoYes.setVisibility(View.VISIBLE);}
                else if (c.isReceipt()==false) {
                    holder.OrderIsShoYes.setVisibility(View.INVISIBLE);}
                }

        });

    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }


    public class OrderHolder extends RecyclerView.ViewHolder { //بعرف هين بلأولوبعمل inflate للعناصر
        TextView tv_name,tv_order,tv_order_des;
        CardView card ;
        ImageView OrderDelete,OrderAdd,OrderSub,OrderIsShow,OrderIsShoYes;
        public OrderHolder(@NonNull View itemView) {
            super(itemView);
            tv_name=itemView.findViewById(R.id.cardOrderName);
            tv_order=itemView.findViewById(R.id.cardOrderOrder);
            tv_order_des=itemView.findViewById(R.id.cardOrderNote);
            OrderDelete=itemView.findViewById(R.id.cardOrderDelete);
            OrderAdd=itemView.findViewById(R.id.cardOrderAdd);
            OrderSub=itemView.findViewById(R.id.cardOrderSub);
            OrderIsShow=itemView.findViewById(R.id.cardOrderIsShow);
            OrderIsShoYes=itemView.findViewById(R.id.cardOrderIsShowyes);


            //card=itemView.findViewById(R.id.cardVi);
        }
    }
}
