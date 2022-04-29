package com.app.marafresh;



import android.app.ProgressDialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.app.marafresh.Adapter.CartAdapter;
import com.app.marafresh.model.Cart;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.lang.reflect.Field;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.firebase.database.ValueEventListener;


public class Cereals extends AppCompatActivity {





    private List<Cart> foodList = new ArrayList<>();

    //ArrayList<FoodItem> allBrews;
    //ArrayList<FoodItem> allReports;

    static final int SET_SOUNDS = 1;


    private MenuItem mSearchAction;
    private boolean isSearchOpened = false;
    private EditText edtSeach;


//    MaterialProgressBar loadbar;


    ProgressDialog pDialog;


    private RecyclerView recyclerView;
    private CartAdapter adapter;

    private int numMessages = 0;
    private LinearLayoutManager llm;
    DatabaseReference myFirebaseRef;
    private DatabaseReference mDatabase;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    private Toolbar toolbar;

    private String mCurrentUserId;

    /// private FirebaseAuth auth;
    AutoCompleteTextView ACTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);





        //setToolBar();
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);


//        auth = FirebaseAuth.getInstance();
//
//
//
//        FirebaseUser user = auth.getCurrentUser();
//
//
//        if (user == null) {
//            mCurrentUserId = user.getUid();
//
//
//        }else{
//            mCurrentUserId=auth.getCurrentUser().getUid().toString();
//
//        }
//

        mDatabase = FirebaseDatabase.getInstance().getReference();


        // allBrews=new ArrayList<FoodItem>();
        //allReports=new ArrayList<FoodItem>();
        foodList=new ArrayList<Cart>();
        recyclerView = (RecyclerView) findViewById(R.id.product_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));




        adapter = new CartAdapter(getApplicationContext(),foodList);



        filldata();

    }

    private void filldata(){


        //Query queryReftwo = myFirebaseRef.child("REPORTS").orderByChild("VOTES").startAt("0");
        Query queryReftwo = mDatabase.child("Products").orderByChild("Category").equalTo("Cereals");
        queryReftwo.keepSynced(true);

        queryReftwo.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String s) {
//                pDialog = new ProgressDialog(ProductsActivity.this);
//                pDialog.setMessage("Fetching data");




                // pDialog.show();

                Map<String, Object> newPost = (Map<String, Object>) snapshot.getValue();
                if (newPost != null) {

                    if (newPost.get("Title") != null & newPost.get("Title") != "") {
                        foodList.add(new
                                Cart
                                (

                                        newPost.get("Title").toString(),
                                        newPost.get("vDescription").toString(),
                                        newPost.get("Price").toString(),

                                        newPost.get("Quantity").toString(),
                                        newPost.get("Category").toString(),
                                        newPost.get("SubCategory").toString(),

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
                    foodList.add(new
                            Cart
                            (
                                    newPost.get("Title").toString(),
                                    newPost.get("vDescription").toString(),
                                    newPost.get("Price").toString(),

                                    newPost.get("Quantity").toString(),
                                    newPost.get("Category").toString(),
                                    newPost.get("SubCategory").toString(),

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
                //pDialog.hide();


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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.shopper_menu, menu);
        mSearchAction = menu.findItem(R.id.action_search);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_search) {
            handleMenuSearch();
            return true;
        }
        if (id == R.id.action_cart) {
            Intent xbrew = new Intent(getApplicationContext(),CartActivity.class);

            startActivity(xbrew);
        }



        return super.onOptionsItemSelected(item);
    }
    protected void handleMenuSearch(){
        ActionBar action = getSupportActionBar(); //get the actionbar

        if(isSearchOpened){ //test if the search is open

            action.setDisplayShowCustomEnabled(false); //disable a custom view inside the actionbar
            action.setDisplayShowTitleEnabled(true); //show the title in the action bar

            //hides the keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(ACTV.getWindowToken(), 0);

            //add the search icon in the action bar
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_baseline_search_24));
            //allBrews.clear();
            //allReports.clear();
            foodList.clear();
            filldata();



            isSearchOpened = false;
        } else { //open the search entry

            action.setDisplayShowCustomEnabled(true); //enable it to display a
            // custom view in the action bar.
            action.setCustomView(R.layout.searchbar);//add the custom view
            action.setDisplayShowTitleEnabled(false); //hide the title

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
            ACTV= (AutoCompleteTextView)findViewById(R.id.edtSearch);
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

            //add the close icon
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_baseline_close_24));

            isSearchOpened = true;
        }
    }




    @Override
    public void onBackPressed() {
        if(isSearchOpened) {
            handleMenuSearch();
            return;
        }
        super.onBackPressed();
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
                    pDialog = new ProgressDialog(Cereals.this);
                    pDialog.setMessage("Fetching data");
                    pDialog.show();

                    if (newPost.get("Title") != null & newPost.get("Title") != "") {
                        foodList.add(new Cart(

                                newPost.get("Title").toString(),
                                newPost.get("vDescription").toString(),
                                newPost.get("Price").toString(),

                                newPost.get("Quantity").toString(),
                                newPost.get("Category").toString(),
                                newPost.get("SubCategory").toString(),

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
                            newPost.get("SubCategory").toString(),

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
    public boolean onSupportNavigateUp(){
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
        return true;
    }
}
