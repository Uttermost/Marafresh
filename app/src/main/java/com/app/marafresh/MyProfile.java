package com.app.marafresh;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.app.marafresh.model.Profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Map;

public class MyProfile extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    SharedPreferences SHTAKIprefferences;


    Profile reportSingle;
    String currentuserId;

    ProgressDialog pDialog;



    DatabaseReference mDatabase;
    private EditText Displayname;
    private EditText emailaddess;
    private EditText phonenumber;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);



        auth = FirebaseAuth.getInstance();

        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance().getReference();
        }
        mDatabase.keepSynced(true);



        currentuserId = auth.getCurrentUser().getUid();


        Displayname = (EditText) findViewById(R.id.Displayname);
        emailaddess = (EditText) findViewById(R.id.emailaddess);
        phonenumber = (EditText) findViewById(R.id.phonenumber);
        pDialog = new ProgressDialog(MyProfile.this);
        pDialog.setMessage("Fetching data");
        pDialog.show();

//        String uid = mDatabase.child("UserProfile").push().getKey();
//
//        System.out.println("uid: " + uid.toString());

        String key = auth.getCurrentUser().getPhoneNumber().toString();
        //String key = mDatabase.push().getKey();
        System.out.println("key: " + key.toString());

        Query queryReftwo = mDatabase.child("UserProfile").orderByChild("phonenumber").
                equalTo(key).limitToFirst(1);
        queryReftwo.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // Profile newPost = dataSnapshot.getValue(Profile.class);
                Map<String, Object> newPost = (Map<String, Object>) dataSnapshot.getValue();
                if (newPost != null) {

                    reportSingle = new Profile(
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

                Displayname.setText(reportSingle.getFullName());
                emailaddess.setText(reportSingle.getEmail());
                //  toolbar.setTitle(Displayname);
                phonenumber.setText(auth.getCurrentUser().getPhoneNumber());
                //Picasso.get().load(reportSingle.getImage().toString());




                pDialog.dismiss();

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



    private void Update() {



        String txt_name = Displayname.getText().toString();

       // String txt_p_number = phonenumber.getText().toString();
        String txt_email =emailaddess.getText().toString();

        mDatabase.child("UserProfile").child(currentuserId).child("FullName").setValue(txt_name);
       // mDatabase.child("UserProfile").child(currentuserId).child("phonenumber").setValue(txt_p_number);
        mDatabase.child("UserProfile").child(currentuserId).child("email").setValue(txt_email);




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_save) {

            Update();
            return true;
        }







        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp(){
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
        return true;
    }


}
