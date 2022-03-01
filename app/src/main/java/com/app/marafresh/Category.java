package com.app.marafresh;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.app.marafresh.R;
import com.app.marafresh.SelectedCategory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

public class Category extends AppCompatActivity implements View.OnClickListener{

    private Uri mImageUri = null;

    private Spinner spinCity;


    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUsers;

    private FirebaseAuth auth;

    private ImageView meat;
    private ImageView fruits;
    private ImageView vegetables;


    private String grade;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Get ax
        // support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);


        meat = (ImageView) findViewById(R.id.meat);
        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/mara-fresh.appspot.com/o/flesh-geba22575f_640.jpg?alt=media&token=b7b0e451-7da5-4cd7-96a1-f1f849733407f").into(meat);

        fruits = (ImageView) findViewById(R.id.fruits);
        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/mara-fresh.appspot.com/o/fruits-g59090e25f_640.jpg?alt=media&token=277861ad-ce24-48f4-a74a-9cc7faf817ac").into(fruits);

        vegetables = (ImageView) findViewById(R.id.vegetables);
        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/mara-fresh.appspot.com/o/vegetables-g849a92e16_640.jpg?alt=media&token=135b57df-543a-402b-8d79-53f8e8d59471").into(vegetables);

        meat.setOnClickListener( this );
        fruits.setOnClickListener( this );
        vegetables.setOnClickListener( this );

    }









    @Override
    public boolean onSupportNavigateUp(){
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
        return true;
    }


    @Override
    public void onClick(View v) {

        if ( v == meat ) {
            // Handle clicks for HajjUmrah

            Intent xbrew = new Intent(getApplicationContext(), SelectedCategory.class);

            xbrew.putExtra("category","meat");
            startActivity(xbrew);


        } else if ( v == fruits ) {
            // Handle clicks for Quran
            Intent xbrew = new Intent(getApplicationContext(), SelectedCategory.class);

            xbrew.putExtra("category","fruits");
            startActivity(xbrew);
        } else if ( v == vegetables ) {
            // Handle clicks for Islamscholars
            Intent xbrew = new Intent(getApplicationContext(), SelectedCategory.class);

            xbrew.putExtra("category","vegs");
            startActivity(xbrew);
        }


    }

}
