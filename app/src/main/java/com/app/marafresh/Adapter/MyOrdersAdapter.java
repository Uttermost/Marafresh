package com.app.marafresh.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.RecyclerView;


import com.app.marafresh.R;
import com.app.marafresh.model.Orders;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class MyOrdersAdapter extends RecyclerView.Adapter<com.app.marafresh.Adapter.MyOrdersAdapter.OrdersClassViewHolder> {


    private List<Orders> mOrdersList;
    private Context context;
    private DatabaseReference mDatabase;


    public MyOrdersAdapter(List<Orders> mOrdersList, Context context, DatabaseReference mDatabase) {

        this.mOrdersList = mOrdersList;
        this.context = context;
        this.mDatabase = mDatabase;


    }

    @Override
    public com.app.marafresh.Adapter.MyOrdersAdapter.OrdersClassViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_orders_row, parent, false);

        return new com.app.marafresh.Adapter.MyOrdersAdapter.OrdersClassViewHolder(v);

    }


    static class OrdersClassViewHolder extends RecyclerView.ViewHolder {


        TextView post_time;
        TextView post_date;
        TextView post_name;
        TextView post_mobile;
        TextView post_orderId;
        TextView post_items;
        TextView post_amount;


        OrdersClassViewHolder(View view) {
            super(view);

            view = itemView;
            post_time = view.findViewById(R.id.time);
            post_date = view.findViewById(R.id.placedOn);
            post_name = view.findViewById(R.id.placedBy);

            post_mobile = view.findViewById(R.id.mobile);
            post_orderId = view.findViewById(R.id.orderId);
            post_items = view.findViewById(R.id.items);
            post_amount = view.findViewById(R.id.amount);


        }
    }

    @Override
    public void onBindViewHolder(final com.app.marafresh.Adapter.MyOrdersAdapter.OrdersClassViewHolder holder, int i) {

        final Orders c = mOrdersList.get(i);
        Long tStamp = c.getTimestamp();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = formatter.format(new Date(tStamp));
        SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm:ss");
        String timeString = formatterTime.format(new Date(tStamp));
        holder.post_date.setText(dateString);
        holder.post_time.setText(timeString);
        holder.post_name.setText(c.getFirstName());
        holder.post_mobile.setText(c.getMobileNumber());
        holder.post_orderId.setText(c.getKey());
        holder.post_amount.setText(c.getTotal());

        Gson gson = new Gson();
        String json = gson.toJson(c.getItems());
        Log.e("Items", json);
        holder.post_items.setText(json);




    }


    @Override
    public int getItemCount() {
        return mOrdersList.size();
    }

}

