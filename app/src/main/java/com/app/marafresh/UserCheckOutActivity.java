package com.app.marafresh;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import com.app.marafresh.model.CartItems;
import com.app.marafresh.model.Profile;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;


public class UserCheckOutActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth firebaseAuth;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    SharedPreferences SHTAKIprefferences;

    DatabaseReference delivery;

    Boolean isDriverFound = false;
    int radius = 1;
    String driverId = "";
    int distance = 1;
    private static int LIMIT = 5;


    String path;

    ProgressDialog pDialog;
    ArrayList<CartItems> mainLine;

    private TextView total, total1, coord;


    private String fire_key;

    private String userId;


    DatabaseReference mDatabase;
    Query mRootRef;

    private EditText note;

    String datasnap;


    private String strlamount, shop_name, shop_email, shop_address, shop_location, shop_paybill, shop_telephone;
    ArrayList<CartItems> product_list;
    String lat, lng;
    Double shopLat, shopLng;


    private final int REQUEST_CODE_PLACEPICKER = 1;

    private Button paymentConfirm, btncpy;
    private ImageButton locButton;
    private Button Edt;


    private EditText FullName;
    private EditText ValidEmail;
    private EditText MobileNumber;

    private EditText DeliveryAddress;
    private EditText Landmark;
    private Spinner spinZone;

    private TextView showSelectedLocation;

    private TextView ShopName;
    private TextView ShopMail;
    private TextView Location;
    private TextView Paybill;
    private TextView Telephone;


    private String mCurrentUserId;
    private String key, mobileNumber, shopname;
    //IFCMService mService;
    String apiKey = "AIzaSyB1KYMTAbNxOpGAN8Qw8Fprdr4SbGxBoG8";

    PlacesClient placesClient;

    private final int PLACE_AUTOCOMPLETE_REQUEST_CODE_FROM = 201;

    Place place;

    String picklong, tillnumber;
    String picklat, placeName, pickup_address;

    Double maplt, maplng;

    private ClipboardManager clipboardManager;
    private ClipData clipData;
    Profile reportSingle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_check_out);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        // mService = Common.getFCMService();
        ab.setTitle("User Checkout");


        auth = FirebaseAuth.getInstance();


        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            mCurrentUserId = user.getUid();


        }


        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        placesClient = Places.createClient(this);


        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance().getReference();
            key = mDatabase.child("CheckoutData").push().getKey();
        }


        mainLine = new ArrayList<CartItems>();
//

        total1 = (TextView) findViewById(R.id.total1);
        FullName = (EditText) findViewById(R.id.FullName);
        ValidEmail = (EditText) findViewById(R.id.ValidEmail);
        MobileNumber = (EditText) findViewById(R.id.MobileNumber);

        DeliveryAddress = (EditText) findViewById(R.id.DeliveryAddress);
        Landmark = (EditText) findViewById(R.id.Landmark);
        spinZone = (Spinner) findViewById(R.id.spin_Zone);
        coord = (TextView) findViewById(R.id.coord);
        note = (EditText) findViewById(R.id.note);
        showSelectedLocation = (TextView) findViewById(R.id.show_selected_location);
        total = (TextView) findViewById(R.id.total);
        ShopName = (TextView) findViewById(R.id.ShopName);
        ShopMail = (TextView) findViewById(R.id.ShopMail);
        Location = (TextView) findViewById(R.id.Location);
        Paybill = (TextView) findViewById(R.id.Paybill);
        Telephone = (TextView) findViewById(R.id.Telephone);
        paymentConfirm = (Button) findViewById(R.id.payment_confirm);
        btncpy = (Button) findViewById(R.id.btnCopy);

        clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);



        coord = (TextView) findViewById(R.id.coord);
        coord.setVisibility(View.GONE);


        Intent intent = getIntent();
        strlamount = intent.getStringExtra("AmountTotal");
        shop_name = "Mara Fresh";
        shop_email = "marafresh@gmail.com";
        shop_address = "Nairobi";
        shop_location = "Nairobi Kenya";
        shop_paybill = "76756";
        shop_telephone = "0729092897";
        product_list = (ArrayList<CartItems>) intent.getSerializableExtra("productList");
       /* CartItems main = new CartItems();
        main.setTitle(product_list.getTitle());
        main.setQuantity(product_list.getQuantity());
        mainLine.add(main);*/

        total.setText("Total Amount: " + strlamount + " KES");
        total1.setText("Total Amount: " + strlamount + " KES");


//        Common.totalAmount = strlamount;
//        Common.paybillShop = shop_paybill;

        ValidEmail = (EditText) findViewById(R.id.ValidEmail);
        MobileNumber = (EditText) findViewById(R.id.MobileNumber);

        DeliveryAddress = (EditText) findViewById(R.id.DeliveryAddress);
        DeliveryAddress.setOnClickListener(this);
        Landmark = (EditText) findViewById(R.id.Landmark);
        spinZone = (Spinner) findViewById(R.id.spin_Zone);

        btncpy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtcopy = "65656";
                clipData = ClipData.newPlainText("text", txtcopy);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getApplicationContext(), "Data Copied to Clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        paymentConfirm = (Button) findViewById(R.id.payment_confirm);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> zone_adapter = ArrayAdapter.createFromResource(this,
                R.array.area_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        zone_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinZone.setAdapter(zone_adapter);
        spinZone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                String item = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        paymentConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(MobileNumber.getText().toString()) || MobileNumber.getText().toString().length() < 9) {
                    Toasty.error(UserCheckOutActivity.this, "Enter a valid phone number", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(FullName.getText().toString()) || FullName.getText().toString().length() < 2) {
                    Toasty.error(UserCheckOutActivity.this, "Enter full name", Toast.LENGTH_LONG).show();
                    return;
                }


                if (maplt == null && maplng == null) {

                    Toast.makeText(getApplicationContext(), "Delivery Location is empty", Toast.LENGTH_SHORT).show();
                } else {
                    //AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserCheckOutActivity.this);
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserCheckOutActivity.this);

                    // Setting Dialog Title
                    alertDialog.setTitle("Confirming Payment!");

                    // Setting Dialog Message
                    alertDialog.setMessage("Hello " + FullName.getText().toString() + " Kindly Confirm your payment of" + " " + strlamount + " to" +
                            shopname + " to be delivered at "
                            + placeName +
                            "");

                    // Setting Icon to Dialog
                    alertDialog.setIcon(R.drawable.ic_baseline_check_circle_outline_24);

                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("Confirm Payment", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                                 /*   String json_latlng = new Gson().toJson(new LatLng(shopLat,shopLng));
                                    Notification notification = new Notification("New  Order Placed",json_latlng);
                                    Sender sender = new Sender(notification);

                                    mService.sendMessage(sender).enqueue(new Callback<FCMResponse>() {
                                        @Override
                                        public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                                            if (response.body().success == 1){
                                                Toast.makeText(UserCheckOutActivity.this, "Request Sent Successfully", Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                Toast.makeText(UserCheckOutActivity.this, "Request Failed", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<FCMResponse> call, Throwable t) {
                                            Log.e("Error", t.getMessage());

                                        }
                                    });*/


                            Bitmap icon1 = BitmapFactory.decodeResource(getResources(),
                                    R.mipmap.ic_launcher);

                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                                    getApplicationContext()).setAutoCancel(true)
                                    .setContentTitle("Tots and Glass")
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setLargeIcon(icon1).setContentText("Payment Confirmation");

                            NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                            bigText.bigText("Hello " + FullName.getText().toString() + " Kindly Confirm your payment of" + " " + strlamount + " to" + " paybill " + shop_paybill + " of " +
                                    ShopName.getText().toString() + " located at "
                                    + placeName +
                                    "");
                            bigText.setBigContentTitle("Payment Confirmation");
                            bigText.setSummaryText("Tots and Glass");
                            mBuilder.setStyle(bigText);
                            mBuilder.setPriority(NotificationCompat.PRIORITY_MAX);

                            // Creates an explicit intent for an Activity in your app
                            Intent resultIntent = new Intent(getApplicationContext(),
                                    MainActivity.class);

                            // The stack builder object will contain an artificial back
                            // stack for
                            // the
                            // started Activity.
                            // getApplicationContext() ensures that navigating backward from
                            // the Activity leads out of
                            // your application to the Home screen.
                            TaskStackBuilder stackBuilder = TaskStackBuilder
                                    .create(getApplicationContext());

                            // Adds the back stack for the Intent (but not the Intent
                            // itself)
                            stackBuilder.addParentStack(MainActivity.class);

                            // Adds the Intent that starts the Activity to the top of the
                            // stack
                            stackBuilder.addNextIntent(resultIntent);
                            PendingIntent resultPendingIntent = stackBuilder
                                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                            mBuilder.setContentIntent(resultPendingIntent);

                            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                            // mId allows you to update the notification later on.
                            mNotificationManager.notify(100, mBuilder.build());



                       /* Intent xbrew = new Intent(getApplicationContext(), PayActivity.class);
                        xbrew.putExtra("FirstName", FullName.getText().toString());
                        xbrew.putExtra("AmountTotal", strlamount);
                        xbrew.putExtra("ShopName", shop_name);
                        xbrew.putExtra("amount","300");
                        xbrew.putExtra("key", key);
                        xbrew.putExtra("ShopMail", ShopMail.getText().toString());
                        xbrew.putExtra("Location", Location.getText().toString());
                        xbrew.putExtra("Paybill", shop_paybill);
                        xbrew.putExtra("Telephone", Telephone.getText().toString());
                        startActivity(xbrew);*/
                            mobileNumber = MobileNumber.getText().toString().trim();
                            mobileNumber = "0" + mobileNumber.substring(mobileNumber.length() - 9);


                            if (TextUtils.isEmpty(mobileNumber)) {
                                Toast.makeText(UserCheckOutActivity.this, "Mobile number cannot be empty", Toast.LENGTH_LONG).show();
                                return;

                            }


                            CopyData();
                            SavePayments();
                            StoreData();
                            Update();

                            mDatabase.child("Cart").child(mCurrentUserId).removeValue();


                            //startActivity(xbrew);

                        }


                    });

                    //Setting Negative "NO" Button
                    alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event
                            Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                            dialog.cancel();

                            // MainActivity.this.finish();

                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();


                }
            }
        });





        String uid = mDatabase.child("UserProfile").push().getKey();
//
        System.out.println("uid: " + uid.toString());

        String key = auth.getCurrentUser().getPhoneNumber().toString();
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
                    userId=dataSnapshot.getKey().toString();

                    System.out.println("userId: " + userId);
                }

                FullName.setText(reportSingle.getFullName());
                ValidEmail.setText(auth.getCurrentUser().getEmail());
                MobileNumber.setText(auth.getCurrentUser().getPhoneNumber());





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

    private void SavePayments() {

        Map<String, Object> postBrew = new HashMap<String, Object>();


        postBrew.put("FirstName",
                FullName.getText().toString());

        postBrew.put("TransporterId",
                "N/A");

        postBrew.put("vehicleregnumber",
                "N/A");
        postBrew.put("Transportername",
                "N/A");

        postBrew.put("Type",
                "Delivery");



        postBrew.put("MobileNumber",
                MobileNumber.getText().toString());
        postBrew.put("items", product_list);

        postBrew.put("total",
                total.getText().toString());

        postBrew.put("ShopName",
                shop_name);

        postBrew.put("ShopMail",
                shop_email);
        postBrew.put("Location",
                Location.getText().toString());
        postBrew.put("Paybill",
                tillnumber);
        postBrew.put("Phonenumber",
                Telephone.getText().toString());


        postBrew.put("timestamp", ServerValue.TIMESTAMP);

        postBrew.put("usermail",
                auth.getCurrentUser().getPhoneNumber());


        String paybill = Paybill.getText().toString();


        mDatabase.child("Payments").setValue(postBrew);
    }


    private void CopyData() {
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            mCurrentUserId = user.getUid();


        }
        moveFirebaseRecord(mDatabase.child("Cart").child(mCurrentUserId),

                mDatabase.child("History").child(mCurrentUserId));
    }

    public void moveFirebaseRecord(DatabaseReference fromPath, final DatabaseReference toPath) {
        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toPath.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            Toast.makeText(getApplicationContext(), "COPY FAILED", Toast.LENGTH_LONG).show();
                        } else {




                        }
                    }

                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "onCancelled- copy fail", Toast.LENGTH_LONG).show();

            }
        });


    }


    private void StoreData() {


        Map<String, Object> postBrew = new HashMap<String, Object>();


        postBrew.put("FirstName",
                FullName.getText().toString());

        postBrew.put("TransporterId",
                "N/A");

        postBrew.put("vehicleregnumber",
                "N/A");
        postBrew.put("Transportername",
                "N/A");

        postBrew.put("Type",
                "Delivery");


        postBrew.put("MobileNumber",
                MobileNumber.getText().toString());
        postBrew.put("Landmark",
                Landmark.getText().toString());
        postBrew.put("note",
                note.getText().toString());
        postBrew.put("items", product_list);

        postBrew.put("total",
                strlamount);

        postBrew.put("ShopName",
                shop_name);

        postBrew.put("ShopMail",
                shop_email);

        postBrew.put("Location",
                placeName);
        postBrew.put("Paybill",
                Paybill.getText().toString());

        postBrew.put("Phonenumber",
                Telephone.getText().toString());
        postBrew.put("latitude", maplt);
        postBrew.put("longitude", maplng);

        postBrew.put("PlaceName",
                placeName);


        postBrew.put("timestamp", ServerValue.TIMESTAMP);

        postBrew.put("usermail",
                auth.getCurrentUser().getPhoneNumber().toString());

        postBrew.put("userId", auth.getCurrentUser().getUid());
        postBrew.put("shopId", "shopKey");
        postBrew.put("Status", "Processing Order");
        postBrew.put("key", key);
        postBrew.put("DeliveryLat", lat);
        postBrew.put("DeliveryLng", lng);


        //postBrew.put("timestamp", currentDateTimeString);


        mDatabase.child("CheckoutData").child(key).setValue(postBrew);

        Toast.makeText(getApplicationContext(), "Order Placed Successfully", Toast.LENGTH_LONG).show();

        Intent xbrew = new Intent(getApplicationContext(), PaymentActivity.class);

        xbrew.putExtra("amount",strlamount);
        xbrew.putExtra("fullname",FullName.getText().toString());
        xbrew.putExtra("phonenumber",MobileNumber.getText().toString());
        startActivity(xbrew);
    }


    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
        return true;
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.DeliveryAddress:
                pickLocation(PLACE_AUTOCOMPLETE_REQUEST_CODE_FROM);
                break;

        }
    }

    private void pickLocation(int requestCode) {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields).setCountry("ke") //NIGERIA
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

                DeliveryAddress.setText(place.getAddress());
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
    private void Update() {



        String txt_name =FullName.getText().toString();
        String txt_Phonenumber =MobileNumber.getText().toString();
        String txt_email = ValidEmail.getText().toString();








        mDatabase.child("UserProfile").child(mCurrentUserId).child("FullName").setValue(txt_name);
        mDatabase.child("UserProfile").child(mCurrentUserId).child("email").setValue(txt_email);
        mDatabase.child("UserProfile").child(mCurrentUserId).child("phonenumber").setValue(txt_email);





    }

}
