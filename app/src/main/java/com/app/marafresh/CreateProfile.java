package com.app.marafresh;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hbb20.CountryCodePicker;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CreateProfile extends AppCompatActivity  implements View.OnClickListener{

    Toolbar toolbar;

    TextView txtSelect1;


    ProgressDialog pDialog;

    private DatabaseReference mDatabase;
    private FirebaseAuth auth;


    private ProgressDialog mProgress;

    private TextView txt_link;

    private FirebaseAuth.AuthStateListener authListener;

    String currentUserId;



    private static final String DATE_FORMAT = "MM-dd-yyyy";

    private EditText Displayname;
    private EditText LastName;
    private LinearLayout ResidenceLinear;
    private TextView ResidenceTxt;
    private EditText Residence;
    private EditText emailaddess;
    private EditText Phonenumber;
    private TextView Gender;
    private Spinner gender;
    private MaterialButton btn_sub;

    String apiKey = "AIzaSyCAawMnC6vfUa40ZNFsLN-ov7Pa4DjcUrM";

    PlacesClient placesClient;

    private final int PLACE_AUTOCOMPLETE_REQUEST_CODE_FROM = 201;

    Place place;

    String picklong, strlphoneNumber;
    String picklat, placeName, pickup_address;

    Double maplt, maplng;
    boolean isAllFieldsChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        mProgress = new ProgressDialog(this);


        Intent i = getIntent();



        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance().getReference();
        }





        auth = FirebaseAuth.getInstance();
        currentUserId= FirebaseAuth.getInstance().getCurrentUser().getUid();

        if(auth.getCurrentUser() == null){
            startActivity(new Intent(CreateProfile.this,EmailSignUp.class));
            finish();
        }else{
            // Toast.makeText(this, "Already logged in", Toast.LENGTH_LONG).show();
            currentUserId=auth.getCurrentUser().getUid();

        }


        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        placesClient = Places.createClient(this);


        Displayname = (EditText) findViewById(R.id.Displayname);
        LastName = (EditText) findViewById(R.id.LastName);
        ResidenceLinear = (LinearLayout) findViewById(R.id.ResidenceLinear);
        ResidenceTxt = (TextView) findViewById(R.id.ResidenceTxt);
        Residence = (EditText) findViewById(R.id.Residence);
        emailaddess = (EditText) findViewById(R.id.emailaddess);
        Phonenumber = (EditText) findViewById(R.id.Phonenumber);
        Gender = (TextView) findViewById(R.id.Gender);
        gender = (Spinner) findViewById(R.id.gender);
        btn_sub = (MaterialButton) findViewById(R.id.btn_sub);

        Residence.setOnClickListener(this);
        ResidenceLinear.setOnClickListener(this);
        ResidenceTxt.setOnClickListener(this);


        emailaddess = (EditText) findViewById(R.id.emailaddess);
        emailaddess.setText(auth.getCurrentUser().getEmail().toString());
//        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> zone_adapter = ArrayAdapter.createFromResource(this,
                R.array.gender, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        zone_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        gender.setAdapter(zone_adapter);
        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                String item = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        pDialog = new ProgressDialog(CreateProfile.this);



        btn_sub=(MaterialButton) findViewById(R.id.btn_sub);
        btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"Hell",Toast.LENGTH_SHORT).show();
                isAllFieldsChecked = CheckAllFields();

                // the boolean variable turns to be true then
                // only the user must be proceed to the activity2
                if (isAllFieldsChecked) {
                    startPosting();
                }


            }
        });




    }

    private boolean CheckAllFields() {


        if (Displayname.length() == 0) {
            Displayname.setError("Kindly Enter Your FirstName");
            return false;
        }

        if (LastName.length() == 0) {
            LastName.setError("Kindly Enter your LastName");
            return false;
        }



        if (Phonenumber.length() == 0) {
            Phonenumber.setError("Phonenumber required");
            return false;
        }

        if (Residence.length() == 0) {
            Residence.setError("Kindly pick your location");
            return false;
        }

        if (gender.getSelectedItem().toString().trim().equals("--Kindly Select --")) {
            Gender.setError("Kindly select your gender");
        }

        // after all validation return true.
        return true;
    }


    private void startPosting() {

        mProgress.setMessage("Posting... ");
        mProgress.getMax();
        mProgress.setCancelable(false);

        Displayname = (EditText) findViewById(R.id.Displayname);
        emailaddess = (EditText) findViewById(R.id.emailaddess);
        Phonenumber = (EditText) findViewById(R.id.Phonenumber);

        final String a = Displayname.getText().toString();
        final String b = emailaddess.getText().toString();
        final String cc = Phonenumber.getText().toString();



        if (!TextUtils.isEmpty(a) && !TextUtils.isEmpty(b) && !TextUtils.isEmpty(cc)) {
            mProgress.show();



            Map<String, Object> postBrew = new HashMap<String, Object>();

            CharSequence dateFormat = android.text.format.DateFormat.format("dd/MM/yyyy", new java.util.Date());

            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String strDate = sdf.format(c.getTime());



            postBrew.put("Displayname",
                    Displayname.getText().toString());
            postBrew.put("LastName",
                    LastName.getText().toString());


            postBrew.put("email",
                    emailaddess.getText().toString());

            postBrew.put("Area",
                    pickup_address);

            postBrew.put("Residence",
                    Residence.getText().toString());


            postBrew.put("phonenumber",
                    Phonenumber.getText().toString());

            postBrew.put("Fullnumber",
                    strlphoneNumber);


            postBrew.put("gender",
                    gender.getSelectedItem().toString());


            postBrew.put("Views",
                    "0");
            postBrew.put("Likes",
                    "0");


            postBrew.put("date", strDate );

            postBrew.put("status",
                    "pending");
            postBrew.put("Image", "https://firebasestorage.googleapis.com/v0/b/myhealth-3d307.appspot.com/o/android-profile-icon-2.jpg?alt=media&token=26ab8cce-ca6e-4f47-86e6-642aa0bd078d");

            postBrew.put("timestamp", ServerValue.TIMESTAMP);

            postBrew.put("phonenumber",
                    Phonenumber.getText().toString());
            postBrew.put("uid",
                    auth.getCurrentUser().getUid().toString());

            String key = mDatabase.child("Profile").push().getKey();
            postBrew.put("key", key);

            if (key != null) {
                mDatabase.child("UserProfile").child(auth.getCurrentUser().getUid()).setValue(postBrew);
                mProgress.dismiss();

                Intent xbrew = new Intent(getApplicationContext(), MainActivity.class);
                Toast.makeText(getApplicationContext(), "Profile Created  Successfully", Toast.LENGTH_LONG).show();
                xbrew.putExtra("key",key);
                startActivity(xbrew);
                finish();

            }



        }


//        Intent i = new Intent(getApplicationContext(),MyHardware.class);
//        startActivity(i);
//        finish();
    }

    public String loadJSONFromAsset(String file) {
        try {
            InputStream is = getAssets().open(file);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            return json;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public boolean onSupportNavigateUp(){
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Residence:
                pickLocation(PLACE_AUTOCOMPLETE_REQUEST_CODE_FROM);
                break;
            case R.id.ResidenceLinear:
                pickLocation(PLACE_AUTOCOMPLETE_REQUEST_CODE_FROM);
                break;
            case R.id.ResidenceTxt:
                pickLocation(PLACE_AUTOCOMPLETE_REQUEST_CODE_FROM);
                break;

        }
    }
    private void pickLocation(int requestCode) {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)//NIGERIA
                .build(this);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // If user take photo and click "accept" save photo to file and upload it to firebase using filename and uri

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            place = Autocomplete.getPlaceFromIntent(data);
            //  Log.i(TAG, "Place: " + place.getName());
            placeName = place.getName();
            setResultText(place, requestCode);
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(data);

            showMessage(status.getStatusMessage());
        }
    }


    private void setResultText(Place place, int requestCode) {
        switch (requestCode) {
            case PLACE_AUTOCOMPLETE_REQUEST_CODE_FROM:

                Residence.setText(place.getAddress());
                pickup_address = place.getName();
                picklong = String.valueOf(place.getLatLng().longitude);
                picklat = String.valueOf(place.getLatLng().latitude);


                maplt = place.getLatLng().latitude;
                maplng = place.getLatLng().longitude;


                break;

        }

    }
    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}

