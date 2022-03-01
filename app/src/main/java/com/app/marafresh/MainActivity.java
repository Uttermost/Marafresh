package com.app.marafresh;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.app.marafresh.Adapter.CartAdapter;
import com.app.marafresh.model.Cart;
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


public class MainActivity extends AppCompatActivity implements  BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener
{

    private ProgressBar progressBar;


    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;




    private List<Cart> foodList = new ArrayList<>();
    private List<Cart>ordersList= new ArrayList<>();



    ProgressDialog pDialog;

    SharedPreferences Msosiprefferences;

    protected String Strl_PaybillNumber;

    private RecyclerView recyclerView;
    private CartAdapter adapter;
    Cart notificationItem;
    ArrayList< Cart> allReports;
    private NotificationManager mNotificationManager;
    private int notificationID = 100;
    private int numMessages = 0;
    private LinearLayoutManager llm;

    String path;
    private SliderLayout mDemoSlider;
    //EditText search_bar;
    AutoCompleteTextView ACTV;
    TextView view_all;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        //setSupportActionBar(toolbar);
        // Get ax
        // support ActionBar corresponding to this toolbar
       // ActionBar ab = getSupportActionBar();

        // Enable the Up button
        //ab.setDisplayHomeAsUpEnabled(true);




        auth = FirebaseAuth.getInstance();


        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth auth) {
                FirebaseUser user = auth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity

                    //getCashInHand();
                    startActivity(new Intent(MainActivity.this, OtpVerification.class));
                    finish();
                }



            }
        };

        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance().getReference();
        }




        // CreatCart();
        filldata();



        recyclerView = (RecyclerView) findViewById(R.id.product_recycler);
        view_all= (TextView) findViewById(R.id.view_all);
        view_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ShoppingActivity.class);
                startActivity(i);
            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        mDemoSlider = findViewById(R.id.slider);
        ArrayList<String> listUrl = new ArrayList<>();
        ArrayList<String> listName = new ArrayList<>();

        listUrl.add("https://firebasestorage.googleapis.com/v0/b/mara-fresh.appspot.com/o/fruits-g59090e25f_640.jpg?alt=media&token=277861ad-ce24-48f4-a74a-9cc7faf817ac");
        listName.add("Fruits");

        listUrl.add("https://firebasestorage.googleapis.com/v0/b/mara-fresh.appspot.com/o/vegetables-g849a92e16_640.jpg?alt=media&token=135b57df-543a-402b-8d79-53f8e8d59471");
        listName.add("Vegetables");

        listUrl.add("https://firebasestorage.googleapis.com/v0/b/mara-fresh.appspot.com/o/flesh-geba22575f_640.jpg?alt=media&token=b7b0e451-7da5-4cd7-96a1-f1f849733407f");
        listName.add("Meat");

       

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop();
        //.diskCacheStrategy(DiskCacheStrategy.NONE)
        //.placeholder(R.drawable.placeholder)
        //.error(R.drawable.placeholder);

        for (int i = 0; i < listUrl.size(); i++) {
            TextSliderView sliderView = new TextSliderView(this);
            // if you want show image only / without description text use DefaultSliderView instead

            // initialize SliderLayout
            sliderView
                    .image(listUrl.get(i))
                    .description(listName.get(i))
                    .setRequestOption(requestOptions)
                    .setProgressBarVisible(true)
                    .setOnSliderClickListener(this);

            //add your extra information
            sliderView.bundle(new Bundle());
            sliderView.getBundle().putString("extra", listName.get(i));
            mDemoSlider.addSlider(sliderView);
        }
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

        // set Slider Transition Animation
        // mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);

        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);
        mDemoSlider.stopCyclingWhenTouch(false);
        //Nothing special, create database reference.
        //Nothing special, create database reference.
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        //Create a new ArrayAdapter with your context and the simple layout for the dropdown menu provided by Android
        final ArrayAdapter<String> autoComplete = new ArrayAdapter<>(this,R.layout.item_list);
        //Child the root before all the push() keys are found and add a ValueEventListener()

        database.child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Basically, this says "For each DataSnapshot *Data* in dataSnapshot, do what's inside the method.
                for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()){
                    //Get the suggestion by childing the key of the string you want to get.
                    String suggestion = suggestionSnapshot.child("Title").getValue(String.class);
                    //Add the retrieved string to the list
                    autoComplete.add(suggestion);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ACTV= (AutoCompleteTextView)findViewById(R.id.search_bar);
        ACTV.setAdapter(autoComplete);

        //edtSeach = (EditText)action.getCustomView().findViewById(R.id.edtSearch); //the text editor

        //this is a listener to do a search when the user clicks on search button
        ACTV.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    doSearch(v);
                    return true;
                }
                return false;
            }
        });

        ACTV.requestFocus();

        //open the keyboard focused in the edtSearch
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(ACTV, InputMethodManager.SHOW_IMPLICIT);



        foodList = new ArrayList<Cart>();






        adapter = new CartAdapter(getApplicationContext(), foodList);




    }


    private void filldata() {



        Query queryReftwo = mDatabase.child("Products");


        queryReftwo.keepSynced(true);

        queryReftwo.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String s) {

                System.out.println("snapshot:" + snapshot.toString());

//                 pDialog = new ProgressDialog(ShopperActivity.this);
//                 pDialog.setMessage("Please wait");
//                pDialog.setCancelable(false);
//                pDialog.show();

                Map<String, Object> newPost = (Map<String, Object>) snapshot.getValue();


                if (newPost != null) {

                    if (newPost.get("Title") != null & newPost.get("Title") != "") {
                        foodList.add(new Cart

                                (

                                        newPost.get("Title").toString(),
                                        newPost.get("vDescription").toString(),
                                        newPost.get("Price").toString(),

                                        newPost.get("Quantity").toString(),
                                        newPost.get("Category").toString(),

                                        newPost.get("create_ImagedownloadURL").toString(),
                                        newPost.get("BusinessName").toString(),
                                        newPost.get("BusinessEmail").toString(),

                                        newPost.get("Location").toString(),
                                        newPost.get("Paybill").toString(),
                                        newPost.get("Telephone").toString(),
                                        newPost.get("CreateDate").toString(),


                                        snapshot.getKey().toString()));

                    }

                } else {
                    foodList.add(new Cart

                            (

                                    newPost.get("Title").toString(),
                                    newPost.get("vDescription").toString(),
                                    newPost.get("Price").toString(),

                                    newPost.get("Quantity").toString(),
                                    newPost.get("Category").toString(),

                                    newPost.get("create_ImagedownloadURL").toString(),
                                    newPost.get("BusinessName").toString(),
                                    newPost.get("BusinessEmail").toString(),

                                    newPost.get("Location").toString(),
                                    newPost.get("Paybill").toString(),
                                    newPost.get("Telephone").toString(),
                                    newPost.get("CreateDate").toString(),


                                    snapshot.getKey().toString()));


                }
                recyclerView.setAdapter(adapter);
                // pDialog.hide();
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
//        for(int arr=allBrews.size();arr<0;arr--){
//            allReports.add((allBrews.get(arr)));
//
//        }



    }
    @Override
    public boolean onSupportNavigateUp(){
        Intent intent = new Intent();
        intent.putExtra("PaybillNumber", Strl_PaybillNumber);
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
        mDemoSlider.stopAutoCycle();
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }



    private void doSearch(TextView v) {
        String type=v.getText().toString();
        Query queryRef =  mDatabase.child("Products").
                orderByChild("Title").
                equalTo(type);



        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String s) {
                Map<String, Object> newPost = (Map<String, Object>) snapshot.getValue();
                if (newPost != null) {
                    //allBrews.clear();
                    //allReports.clear();
                    foodList.clear();
                    pDialog = new ProgressDialog(MainActivity.this);
                    pDialog.setMessage("Fetching data");
                    pDialog.show();

                    if (newPost.get("Title") != null & newPost.get("Title") != "") {
                        foodList.add(new Cart(


                                newPost.get("Title").toString(),
                                newPost.get("vDescription").toString(),
                                newPost.get("Price").toString(),

                                newPost.get("Quantity").toString(),
                                newPost.get("Category").toString(),

                                newPost.get("create_ImagedownloadURL").toString(),
                                newPost.get("BusinessName").toString(),
                                newPost.get("BusinessEmail").toString(),

                                newPost.get("Location").toString(),
                                newPost.get("Paybill").toString(),
                                newPost.get("Telephone").toString(),
                                newPost.get("CreateDate").toString(),


                                snapshot.getKey().toString()));
                    }

                } else {
                    foodList.add(new Cart(


                            newPost.get("Title").toString(),
                            newPost.get("vDescription").toString(),
                            newPost.get("Price").toString(),

                            newPost.get("Quantity").toString(),
                            newPost.get("Category").toString(),

                            newPost.get("create_ImagedownloadURL").toString(),
                            newPost.get("BusinessName").toString(),
                            newPost.get("BusinessEmail").toString(),

                            newPost.get("Location").toString(),
                            newPost.get("Paybill").toString(),
                            newPost.get("Telephone").toString(),
                            newPost.get("CreateDate").toString(),


                            snapshot.getKey().toString()));
                }


//
//                    listAllBrews.setAdapter(adapters);
//                    for(int arr=allBrews.size();arr<0;arr--){
//                        allReports.add((allBrews.get(arr)));
//
//                    }
                pDialog.hide();
                recyclerView.setAdapter(adapter);


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



//
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}



