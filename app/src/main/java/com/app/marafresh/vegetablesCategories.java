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

public class vegetablesCategories extends AppCompatActivity implements View.OnClickListener{

    private Button Normal;
    private Button Indegenous;
    private Button Organic;

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

        Normal = (Button) findViewById(R.id.Normal);
        Indegenous = (Button) findViewById(R.id.Indegenous);
        Organic = (Button) findViewById(R.id.Organic);

        Normal.setOnClickListener( this );
        Indegenous.setOnClickListener( this );
        Organic.setOnClickListener( this );
    }

    @Override
    public void onClick(View v) {
        if ( v == Normal) {
            // Handle clicks for animalFeeds
            Intent xbrew = new Intent(getApplicationContext(), ShoppingActivity.class);
            xbrew.putExtra("subcategory","Normal");

            startActivity(xbrew);

        } else if ( v == Indegenous) {
            // Handle clicks for maizeSeeds
            Intent xbrew = new Intent(getApplicationContext(), ShoppingActivity.class);
            xbrew.putExtra("subcategory","Indegenous");
            startActivity(xbrew);
        } else if ( v == Organic ) {
            // Handle clicks for vegatableSeeds
            Intent xbrew = new Intent(getApplicationContext(), ShoppingActivity.class);
            xbrew.putExtra("subcategory","SunDried");
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
