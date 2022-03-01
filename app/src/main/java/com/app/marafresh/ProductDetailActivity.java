package com.app.marafresh;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.marafresh.model.Cart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;



public class ProductDetailActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();



    String FirebaseKey,username;


    ProgressDialog pDialog;


    Cart reportSingle;

    private DatabaseReference mDatabase;
    private FirebaseAuth auth;


    //private TextView CreateDate;

    public String toolbar_title,firekey;


    private ImageView imageView;
    private TextView CreateDate,TotalCash;

    TextView Quantity,price;
    private TextView viewsingleVlogTitle;
    private TextView viewDescription;
    TextView viewDetails;
    //private TextView viewQuantity;
    private String video_string;


    Button buyNow;
    private Toolbar toolbar;

    private String  fire_key,url,qty;
    private float y,t,item_price,product_quantity;


    private String  shop_name,shop_mail,address,location,paybill,telephone,shop_key;

    private EditText needed_quantity;


    private TextView add_cart;


    private Float CartTotal;

    String newimput,report_newimput,cash_string, x;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String mCurrentUserId;

    private Spinner SpinQuanity;

    private TextView keepShopping;
    private Button goCart;

    String subkey;

    Cart arraylist;
    LinearLayout layout_action1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       // toolbar.setDisplayHomeAsUpEnabled(true);



        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);




        Intent i = getIntent();
        subkey = i.getExtras().getString("subcat");


        arraylist= (Cart) i.getSerializableExtra("detail_data");





        System.out.println("received: " +subkey);


        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        keepShopping = (TextView) findViewById( R.id.keep_shopping );
        keepShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Intent xbrew = new Intent(getApplicationContext(), MainActivity.class);
                xbrew.putExtra("PaybillNumber", arraylist.getPaybill());
                startActivity(xbrew);

            }
        });



        if (user != null) {
            mCurrentUserId = user.getUid();


        }

        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance().getReference();
        }
        mDatabase.keepSynced(true);
        //itleTextView=(TextView)findViewById(R.id.viewsingleVlogTitle);


        // imageView = (ImageView) findViewById(R.id.image_view);
        layout_action1 = (LinearLayout) findViewById(R.id.layout_action1);
        price = (TextView) findViewById(R.id.Price);
        //Quantity = (TextView) findViewById(R.id.Quantity);

        viewsingleVlogTitle = (TextView) findViewById(R.id.viewsingleVlogTitle);
        viewDescription = (TextView) findViewById(R.id.viewDescription);
        //viewDetails = (TextView) findViewById(R.id.viewDetails);
        // viewQuantity = (TextView) findViewById(R.id.viewQuantity);



        ArrayAdapter<CharSequence> catAdapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        SpinQuanity = (Spinner) findViewById(R.id.Spin_Quanity);

        Integer[] items = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32,
                33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 47, 47, 48, 49, 50};

        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, items);
        SpinQuanity.setAdapter(adapter);





        add_cart = (TextView) findViewById(R.id.buy_now);


        // fillFiredata();



//
        layout_action1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        reportSingle.getDescription() + "\n\n" +
                                reportSingle.getTitle() + "\n"
                                + reportSingle.getDescription() +

                                "\n \n" + "Find more products from MaraFresh" +
                                " https://play.google.com/store/apps/details?id=com.app.marafresh");

                sendIntent.setType("text/plain");
                startActivity(sendIntent);


            }
        });




//        txtVideoShare = (TextView) findViewById(R.id.txt_video_share);
//        txtVideoShare.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent sendIntent = new Intent();
//                sendIntent.setAction(Intent.ACTION_SEND);
//                sendIntent.putExtra(Intent.EXTRA_TEXT,
//                        reportSingle.getDescription() + "\n\n" +
//                                reportSingle.getTitle() + "\n"
//                                + reportSingle.getDescription() +
//
//                                "\n \n" + "Find more Tips from P.Investment" +
//                                " https://play.google.com/store/apps/details?id=com.graph.origicheck.prittworld");
//
//                sendIntent.setType("text/plain");
//                startActivity(sendIntent);
//
//            }
//        });
//
//
//        videoShare = (ImageView) findViewById(R.id.video_share);
//        videoShare.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent sendIntent = new Intent();
//                sendIntent.setAction(Intent.ACTION_SEND);
//                sendIntent.putExtra(Intent.EXTRA_TEXT,
//                        reportSingle.getDescription() + "\n\n" +
//                                reportSingle.getTitle() + "\n"
//                                + reportSingle.getDescription() +
//
//                                "\n \n" + "Find more Tips from P.Investment" +
//                                " https://play.google.com/store/apps/details?id=com.graph.origicheck.prittworld");
//
//                sendIntent.setType("text/plain");
//                startActivity(sendIntent);
//
//
//            }
//        });
//
//        videoLikes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Integer vot = Integer.valueOf(reportSingle.getVotes());
//                vot++;
//                reportSingle.setVotes(String.valueOf(vot));
//
//
//                mDatabase.child("Product").child(reportSingle.getFirekey()).child("likes").setValue(String.valueOf(vot));
//                Toast.makeText(getApplicationContext(), "You Like This!", Toast.LENGTH_SHORT).show();
//                videoLikes.setText(reportSingle.getVotes() + " Like this");
//            }
//        });
//
//        likes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Integer vot = Integer.valueOf(reportSingle.getVotes());
//                vot++;
//                reportSingle.setVotes(String.valueOf(vot));
//
//
//                mDatabase.child("Product").child(reportSingle.getFirekey()).child("likes").setValue(String.valueOf(vot));
//                Toast.makeText(getApplicationContext(), "You Like This!", Toast.LENGTH_SHORT).show();
//                videoLikes.setText(reportSingle.getVotes() + " Like this");
//            }
//        });
//
//
//
//    }


//    private void fillFiredata() {
//
//        Intent i = getIntent();
//        final String key = i.getExtras().getString("firekey");
//
//        final String paybill1 = i.getExtras().getString("paybill");
//
//        System.out.println("received: " + subkey);
//
////
////        pDialog = new ProgressDialog(VideoDetailsActivity.this);
////        pDialog.setMessage("Fetching data");
////        pDialog.show();
//
//
//        //mDatabase.child("Product").child(paybill).child(subkey).orderByKey().equalTo(key);
//
//
//        Query queryReftwo = mDatabase.child("Product").child(paybill1).child(subkey).orderByKey().equalTo(key);
//
//        //Query queryReftwo = mDatabase.child("Video");
//        queryReftwo.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Map<String, Object> newPost = (Map<String, Object>) dataSnapshot.getValue();
//
//                System.out.println("dataSnapshot: " + dataSnapshot.toString());
//                if (newPost != null) {
//
//                    reportSingle = new Cart
//                            (
//                                    newPost.get("Title").toString(),
//                                    newPost.get("vDescription").toString(),
//                                    newPost.get("Price").toString(),
//                                    newPost.get("Quantity").toString(),
//                                    newPost.get("Category").toString(),
//                                    newPost.get("SubCategory").toString(),
//                                    newPost.get("SubsubCategory").toString(),
//                                    newPost.get("create_ImagedownloadURL").toString(),
//                                    newPost.get("BusinessName").toString(),
//                                    newPost.get("BusinessEmail").toString(),
//                                    newPost.get("Address").toString(),
//                                    newPost.get("Location").toString(),
//                                    newPost.get("Paybill").toString(),
//                                    newPost.get("Telephone").toString(),
//                                  newPost.get("CreateDate").toString(),
//                                  dataSnapshot.getKey().toString());
//
//                }
//


        viewsingleVlogTitle.setText(arraylist.getTitle());
        viewDescription.setText(arraylist.getDescription());
        //videoLikes.setText(reportSingle.getVotes().toString() + "Likes");
//                CreateDate.setText(reportSingle.getCreatedAt().toString());
        price.setText(arraylist.getPrice());
        // viewQuantity.setText(arraylist.getQuantity()+"Items in stock");
        /// Quantity.setText(reportSingle.getQuantity().toString()+"Items");
        qty=(arraylist.getQuantity());


        shop_name=(arraylist.getBusinessName());
        shop_mail=(arraylist.getBusinessEmail());

        location=(arraylist.getLocation());
        paybill=(arraylist.getPaybill());
        telephone=(arraylist.getTelephone());


        video_string = (arraylist.getPhoto().toString());

        firekey = (arraylist.getFirekey().toString());

        toolbar_title = arraylist.getTitle();


        //CreateDate.setText(reportSingle.getCreatedAt());

        //Picasso.with(getApplicationContext()).load(arraylist.getPhoto()).into(imageView);


        Uri uri = Uri.parse(arraylist.getPhoto().toString());

        ImageView draweeView = (ImageView) findViewById(R.id.image1);
        Picasso.get().load(arraylist.getPhoto()).into(draweeView);


        toolbar.setTitle(toolbar_title);


        add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddCart();

            }
        });



    }



    private void AddCart() {




        Map<String, Object> postBrew = new HashMap<String, Object>();
        postBrew.put("name",
                viewsingleVlogTitle.getText().toString());
        postBrew.put("price",
                price.getText().toString());
        postBrew.put("ImageUrl",
                video_string.toString());

        postBrew.put("Quantity",
                SpinQuanity.getSelectedItem().toString());
        postBrew.put("ShopName","MyBriefke");
        postBrew.put("ShopMail","info@mybrief.co.ke");
        postBrew.put("Address","Nairobi Kenya");
        postBrew.put("Location","Nairobi Kenya");
        postBrew.put("Paybill","67627");
        postBrew.put("Telephone","+254729092897");
        item_price=Float.valueOf(price.getText().toString());
        product_quantity=Float.valueOf(SpinQuanity.getSelectedItem().toString());


        t=item_price*product_quantity;
        //postBrew.put("usermail",
        //auth.getCurrentUser().getEmail());

        postBrew.put("TotalPerITEM",
                Float.valueOf(t));

        postBrew.put("key",firekey.toString());

        String keyMain = mDatabase.child("Cart").push().getKey();
        postBrew.put("keyMain",
                keyMain);

        postBrew.put("key",firekey.toString());

        mDatabase.child("Cart").child(mCurrentUserId).child(keyMain).setValue(postBrew);

        add_cart.setText("Added");





    }




    @Override
    public void onPause() {
        super.onPause();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.product_detail_menu, menu);
        //mSearchAction = menu.findItem(R.id.action_search);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_cart) {

            Intent i = new Intent(getApplicationContext(),CartActivity.class);
            startActivity(i);
            return true;
        }








        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    public boolean onSupportNavigateUp(){
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
        return true;
    }

}




