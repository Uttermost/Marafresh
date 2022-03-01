package com.app.marafresh;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.app.marafresh.Adapter.CartItemsAdapter;
import com.app.marafresh.model.Cart;
import com.app.marafresh.model.CartItems;
import com.app.marafresh.model.TotalCart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Map;




public class CartActivity extends AppCompatActivity {

    private ProgressBar progressBar;


    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;


    private ArrayList<CartItems> foodList;
    private ArrayList<CartItems> ordersList ;
    private CartItems cartItems;

    private StorageReference mStorageRef;


    static final int SET_SOUNDS = 1;


    private MenuItem mSearchAction;
    private boolean isSearchOpened = false;
    private EditText edtSeach;
    private Button checkout,keep_shopping;
    protected ImageButton remove;


    ProgressDialog pDialog;

    SharedPreferences Msosiprefferences;

    String useremail;


    TextView user_name, user_email, total;
    ImageView user_picture, imPhoto;

    Cart reportSingle;

    private RecyclerView recyclerView;
    private CartItemsAdapter adapter;

    ArrayList<Cart> allReports;

    private LinearLayoutManager llm;


    String shopname,shop_email,shop_address,shop_location,shop_paybill,shop_telephone;
    private String mCurrentUserId;
    int totalAmount;
    private TextView ShopName;
    private TextView ShopMail;
    private TextView Address;
    private TextView Location;
    private TextView Paybill;
    private TextView Telephone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            mCurrentUserId = user.getUid();



        }


        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance().getReference();
        }
        mDatabase.keepSynced(true);
        total = (TextView) findViewById(R.id.total);
        ShopName = (TextView)findViewById( R.id.ShopName );
        ShopMail = (TextView)findViewById( R.id.ShopMail );
        Address = (TextView)findViewById( R.id.Address );
        Location = (TextView)findViewById( R.id.Location );
        Paybill = (TextView)findViewById( R.id.Paybill );
        Telephone = (TextView)findViewById( R.id.Telephone );
        checkout = (Button) findViewById(R.id.proceed_checkout);
        keep_shopping= (Button) findViewById(R.id.keep_shopping);

        //remove = (ImageButton) findViewById(R.id.remove);
        recyclerView = (RecyclerView) findViewById(R.id.listReports);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));

        foodList = new ArrayList<CartItems>();

        ordersList = new ArrayList<CartItems>();


        adapter = new CartItemsAdapter(getApplicationContext(), foodList);


        filldata();


        DatabaseReference mRef = mDatabase.child("Cart").child(mCurrentUserId);

        mRef.keepSynced(true);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                totalAmount = 0;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    TotalCart totalCart = snapshot.getValue(TotalCart.class);

                    totalAmount += totalCart.getTotalPerITEM();

                    shopname=snapshot.child("ShopName").getValue(String.class);
                    shop_email=snapshot.child("ShopMail").getValue(String.class);
                    shop_address=snapshot.child("Address").getValue(String.class);
                    shop_location=snapshot.child("Location").getValue(String.class);
                    shop_paybill=snapshot.child("Paybill").getValue(String.class);
                    shop_telephone=snapshot.child("Telephone").getValue(String.class);
                    total.setText(String.valueOf(totalAmount));
                    ShopName.setText(String.valueOf(shopname));
                    ShopMail.setText(String.valueOf(shop_email));
                    Address.setText(String.valueOf(shop_address));
                    Location.setText(String.valueOf(shop_location));
                    Paybill.setText(String.valueOf(shop_paybill));
                    Telephone.setText(String.valueOf(shop_telephone));

                    checkout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent xbrew = new Intent(getApplicationContext(), UserCheckOutActivity.class);
                            xbrew.putExtra("AmountTotal", String.valueOf(totalAmount));
                            xbrew.putExtra("ShopName",ShopName.getText().toString());
                            xbrew.putExtra("ShopMail",ShopMail.getText().toString());
                            xbrew.putExtra("Address",Address.getText().toString());
                            xbrew.putExtra("Location",Location.getText().toString());
                            xbrew.putExtra("Paybill",Paybill.getText().toString());
                            xbrew.putExtra("Telephone",Telephone.getText().toString());
                            xbrew.putExtra("productList", ordersList);
                            //mDatabase.child("CartAmount").removeValue();
                            startActivity(xbrew);

                        }
                    });
                    keep_shopping.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent xbrew = new Intent(getApplicationContext(),ShoppingActivity.class);
                            xbrew.putExtra("PaybillNumber", Paybill.getText().toString());
                            startActivity(xbrew);

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = recyclerView.getChildPosition(v);

                Intent xbrew = new Intent(getApplicationContext(), ProductDetailActivity.class);
                xbrew.putExtra("firekey",
                        foodList.get(position).getFirekey().toString());
                startActivity(xbrew);

            }
        });



        adapter = new CartItemsAdapter(getApplicationContext(), foodList);

    }


    private void filldata() {


        //Query queryReftwo = myFirebaseRef.child("REPORTS").orderByChild("VOTES").startAt("0");
        Query queryReftwo = mDatabase.child("Cart").child(mCurrentUserId);
        queryReftwo.keepSynced(true);

        queryReftwo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Map<String, Object> newPost = (Map<String, Object>) snapshot.getValue();
                    if (newPost != null) {

                        if (newPost.get("name") != null & newPost.get("name") != "") {
                            foodList.add(new CartItems

                                    (

                                            newPost.get("name").toString(),
                                            newPost.get("price").toString(),
                                            newPost.get("ImageUrl").toString(),
                                            newPost.get("Quantity").toString(),
                                            newPost.get("ShopName").toString(),
                                            newPost.get("ShopMail").toString(),

                                            newPost.get("Location").toString(),
                                            newPost.get("Paybill").toString(),
                                            newPost.get("Telephone").toString(),


                                            snapshot.getKey().toString())

                            );
                            ordersList.add(new CartItems

                                    (

                                            newPost.get("name").toString(),
                                            newPost.get("Quantity").toString()

                                    )

                            );

                            // cartItems = new CartItems(newPost.get("name").toString(), newPost.get("Quantity").toString());
                        }

                    } else {
                        foodList.add(new CartItems
                                (


                                        newPost.get("name").toString(),
                                        newPost.get("price").toString(),
                                        newPost.get("ImageUrl").toString(),
                                        newPost.get("Quantity").toString(),
                                        newPost.get("ShopName").toString(),
                                        newPost.get("ShopMail").toString(),

                                        newPost.get("Location").toString(),
                                        newPost.get("Paybill").toString(),
                                        newPost.get("Telephone").toString(),

                                        snapshot.getKey().toString())


                        );
                        ordersList.add(new CartItems

                                (

                                        newPost.get("name").toString(),
                                        newPost.get("Quantity").toString()

                                )

                        );
                        //cartItems = new CartItems(newPost.get("name").toString(), newPost.get("Quantity").toString());
                    }


                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    // pDialog.hide();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //pDialog = new ProgressDialog(CartActivity.this);
        //pDialog.setMessage("Fetching data");
//                pDialog.show();


    }




    @Override
    public boolean onSupportNavigateUp(){
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
        return true;
    }

//    @Override
//    public void onBackPressed() {
//
//        Intent xbrew = new Intent(getApplicationContext(),CategoriesActivity.class);
//
//        startActivity(xbrew);
//
//    }
}

