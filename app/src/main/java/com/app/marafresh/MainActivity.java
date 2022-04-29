package com.app.marafresh;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.app.marafresh.Adapter.CartAdapter;
import com.app.marafresh.model.Cart;
import com.app.marafresh.model.Profile;
import com.bumptech.glide.request.RequestOptions;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.animations.DescriptionAnimation;
import com.glide.slider.library.slidertypes.BaseSliderView;
import com.glide.slider.library.slidertypes.TextSliderView;
import com.glide.slider.library.tricks.ViewPagerEx;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity
{

    private RelativeLayout topArea;
    private TextView myDashboard;
    private TextView myName;
    private TextView TodaysPrompt;
    private CardView Fruit;
    private CardView vegetables;
    private CardView meat;
    private CardView Cereals;
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    Profile reportSingle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        //setSupportActionBar(toolbar);
        // Get ax
        // support ActionBar corresponding to this toolbar
       // ActionBar ab = getSupportActionBar();

        // Enable the Up button
        //ab.setDisplayHomeAsUpEnabled(true);


        setTitle(R.id.inc_toolbar,R.id.iv_title, "Awash", R.drawable.ic_baseline_home_24, R.color.white, R.color.black);

        auth = FirebaseAuth.getInstance();


        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance().getReference();

        }


        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth auth) {
                FirebaseUser user = auth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity

                    //getCashInHand();
                    startActivity(new Intent(MainActivity.this, EmailSignUp.class));
                    finish();
                }else{
                    String uid = mDatabase.child("UserProfile").push().getKey();
//
                    System.out.println("uid: " + uid.toString());

                    String key = auth.getCurrentUser().getEmail().toString();
                    //String key = mDatabase.push().getKey();
                    System.out.println("key_email:" + key.toString());

                    Query queryReftwo = mDatabase.child("UserProfile").orderByChild("phonenumber").
                            equalTo(key);
                    queryReftwo.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Map<String, Object> newPost = (Map<String, Object>) dataSnapshot.getValue();
                            if (newPost != null) {

                                reportSingle=new Profile(
                                        newPost.get("email").toString(),
                                        newPost.get("Displayname").toString(),
                                        newPost.get("LastName").toString(),
                                        newPost.get("phonenumber").toString(),
                                        newPost.get("uid").toString(),
                                        newPost.get("key").toString(),
                                        newPost.get("Image").toString(),
                                        newPost.get("timestamp").toString(),

                                        dataSnapshot.getKey().toString());

                            }
                            topArea = (RelativeLayout) findViewById(R.id.topArea);
                            myDashboard = (TextView) findViewById(R.id.myDashboard);
                            myName = (TextView) findViewById(R.id.myName);
                            TodaysPrompt = (TextView) findViewById(R.id.TodaysPrompt);
                            myName.setText(reportSingle.getFullName());
                            TodaysPrompt.setText(auth.getCurrentUser().getEmail());






                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }


                    });




                }
            }
        };

        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance().getReference();
        }






        Fruit = (CardView) findViewById(R.id.Fruit);
        Fruit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent xbrew = new Intent(getApplicationContext(),Fruits.class);

                xbrew.putExtra("category","fruits");
                startActivity(xbrew);

            }
        });
        vegetables = (CardView) findViewById(R.id.vegetables);
        vegetables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent xbrew = new Intent(getApplicationContext(),vegetablesCategories.class);

                xbrew.putExtra("category","vegs");
                startActivity(xbrew);
            }
        });
        meat = (CardView) findViewById(R.id.meat);
        meat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent xbrew = new Intent(getApplicationContext(), MeatCategories.class);

                xbrew.putExtra("category","meat");
                startActivity(xbrew);
            }
        });
        Cereals = (CardView) findViewById(R.id.Cereals);
        Cereals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent xbrew = new Intent(getApplicationContext(), Cereals.class);

                xbrew.putExtra("category","meat");
                startActivity(xbrew);
            }
        });
        BottomNavigationView bnve = (BottomNavigationView)
                findViewById(R.id.bnve);


        bnve.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {


                            case R.id.home:
                                startActivity(new Intent(MainActivity.this, MainActivity.class));
                                break;
                            case R.id.cart:

                                startActivity(new Intent(MainActivity.this, CartActivity.class));
                                break;
                            case R.id.order:

                                startActivity(new Intent(MainActivity.this,OrdersActivity.class));
                                break;

                            case R.id.setting:

                                startActivity(new Intent(MainActivity.this,MyProfile.class));
                                break;


//                            case R.id.Call:
//
//                                //numberCalls.setText(reportSingle.getVotes()+" have called this");
//                                //lastCalls.setText(currentDateTimeString);
//                                Intent intent = new Intent(Intent.ACTION_DIAL);
//                                //intent.setData(Uri.parse((txtPhonenumber.getText().toString())));
//                                intent.setData(Uri.parse(("tel:")+"0729092897"));
//
//
//
//                                //videoLikes.setText(reportSingle.getVotes()+"Like this");
//                                startActivity(intent);
//                                break;

                        }
                        return false;
                    }
                });



    }
    @Override
    public boolean onSupportNavigateUp(){
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
        return true;
    }


    private void shareTextUrl() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,

                "\n \n" +"Shop from MyDuka"+
                        " https://play.google.com/store/apps/details?id=com.graph.origicheck.prittworld");

        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Via"));
        startActivity(sendIntent);
    }




    private void signOut() {

        auth.signOut();
    }
    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {

        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }

    public void setTitle(int toolbarId,int titleId, String title, int btnDrawable, int colorBg, int titleColor){
        Toolbar toolbar = (Toolbar) findViewById(toolbarId);
        toolbar.setBackgroundResource(colorBg);
        setSupportActionBar(toolbar);
        //ImageView pageTitle = (ImageView) toolbar.findViewById(titleId);
//        pageTitle.setText(title);
//        pageTitle.setTextColor(getResources().getColor(titleColor));
        getSupportActionBar().setTitle("MaraFresh");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(btnDrawable);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_logout) {

            auth.signOut();

            return true;
        }







        return super.onOptionsItemSelected(item);
    }



}



