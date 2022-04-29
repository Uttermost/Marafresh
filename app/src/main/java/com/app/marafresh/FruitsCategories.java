package com.app.marafresh;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.app.marafresh.R;
import com.app.marafresh.ShoppingActivity;

public class FruitsCategories extends AppCompatActivity implements View.OnClickListener{

    private Button FreshFish;
    private Button deep_fry;
    private Button SunDried;
    private Button Beef;
    private Button Goat;
    private Button Lamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vegetables_categories);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Get ax


        // support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        FreshFish = (Button) findViewById(R.id.FreshFish);
        deep_fry = (Button) findViewById(R.id.deep_fry);
        SunDried = (Button) findViewById(R.id.SunDried);
        Beef = (Button) findViewById(R.id.Beef);
        Goat = (Button) findViewById(R.id.Goat);
        Lamp = (Button) findViewById(R.id.Lamp);

        FreshFish.setOnClickListener( this );
        deep_fry.setOnClickListener( this );
        SunDried.setOnClickListener( this );
        Beef.setOnClickListener( this );
        Goat.setOnClickListener( this );
        Lamp.setOnClickListener( this );
    }

    @Override
    public void onClick(View v) {
        if ( v == FreshFish ) {
            // Handle clicks for animalFeeds
            Intent xbrew = new Intent(getApplicationContext(), ShoppingActivity.class);
            xbrew.putExtra("subcategory","FreshFish");

            startActivity(xbrew);

        } else if ( v == deep_fry ) {
            // Handle clicks for maizeSeeds
            Intent xbrew = new Intent(getApplicationContext(), ShoppingActivity.class);
            xbrew.putExtra("subcategory","deep_fry");
            startActivity(xbrew);
        } else if ( v == SunDried ) {
            // Handle clicks for vegatableSeeds
            Intent xbrew = new Intent(getApplicationContext(), ShoppingActivity.class);
            xbrew.putExtra("subcategory","SunDried");
            startActivity(xbrew);
        } else if ( v == Goat ) {
            // Handle clicks for agroChemicals
            Intent xbrew = new Intent(getApplicationContext(), ShoppingActivity.class);
            xbrew.putExtra("subcategory","Goat");
            startActivity(xbrew);
        } else if ( v == Lamp ) {
            // Handle clicks for livestockVetDrugs
            Intent xbrew = new Intent(getApplicationContext(), ShoppingActivity.class);
            xbrew.putExtra("subcategory","Lamp");
            startActivity(xbrew);
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.shopping_categories, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_cart:
                Intent i = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(i);
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
