package com.app.marafresh;

import android.os.Bundle;

import com.app.marafresh.Adapter.MyOrdersAdapter;
import com.app.marafresh.model.Orders;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;


import androidx.appcompat.widget.Toolbar;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.iid.FirebaseInstanceId;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.ArrayList;
import java.util.List;


public class OrdersActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private DatabaseReference mDatabase;

    private final List<Orders> ordersList = new ArrayList<>();
    private MyOrdersAdapter mAdapter;
    SweetAlertDialog pDialog;
    private RecyclerView mRecycler;
    private LinearLayoutManager mLayout;
    private Toolbar toolbar;
    String firekey,shopkey;
    String shopname,shopimage,shopphonenumber,shopmail;
    private Query mQuery;

    private FirebaseAuth firebaseAuth;


    private FirebaseAuth.AuthStateListener mAuthListener;





    private FirebaseAuth auth;

    private String mCurrentUser,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Previous Orders");

        Intent intent = getIntent();
        auth = FirebaseAuth.getInstance();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();





        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    mCurrentUser = firebaseAuth.getCurrentUser().toString();

                    email = firebaseAuth.getCurrentUser().getPhoneNumber().toString();
                } else {
                    startActivity(new Intent(OrdersActivity.this, OtpVerification.class));
                }
            }
        };


        firekey = intent.getStringExtra("firekey");
        shopkey = intent.getStringExtra("shopkey");
        shopmail= intent.getStringExtra("shopmail");
        Log.e("OrdersActivity", "ShopMail "+ shopmail);
        shopname = intent.getStringExtra("shopname");
        shopimage = intent.getStringExtra("image");
        shopphonenumber = intent.getStringExtra("phonenumber");
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitleText("Fetching Orders");
        pDialog.setContentText("Please wait while we load your orders. This may take a few seconds");
        pDialog.setCancelable(true);
        pDialog.show();
        email = auth.getCurrentUser().getPhoneNumber().toString();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("CheckoutData");
        mQuery = mDatabase.orderByChild("usermail").equalTo(email);
        mRecycler = findViewById(R.id.list_orders);
        mLayout = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(mLayout);
        mAdapter = new MyOrdersAdapter(ordersList, this, mDatabase);
        mRecycler.setHasFixedSize(false);
        mRecycler.setAdapter(mAdapter);




        ChildEventListener postListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Orders orders = dataSnapshot.getValue(Orders.class);
                ordersList.add(orders);
                mAdapter.notifyDataSetChanged();
                pDialog.dismissWithAnimation();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } ;
        mQuery.addChildEventListener(postListener);
    }
    @Override
    public boolean onSupportNavigateUp(){
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
        return true;
    }
}
