package com.app.marafresh;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;


public class PaymentActivity extends AppCompatActivity {

    SweetAlertDialog sDialog;



    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;


    private String userEmail, userUID;
    private TextView myDashboard;
    private TextView myName;
    private TextView TodaysPrompt;
    private TextView mpesa_text;
    private TextView mpesa_steps0;
    private TextView mpesa_steps1;
    private TextView mpesa_steps2;
    private TextView business_number;
    private TextView account_number;
    private TextView mpesa_steps4;
    private TextView mpesa_steps9;
    private Button submitBtn;
    private  String name,amount,phonenumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        // mService = Common.getFCMService();
        ab.setTitle("Mpesa Payments");

        Intent intent = getIntent();
        amount = intent.getStringExtra("amount");
        name = intent.getStringExtra("fullname");

        phonenumber = intent.getStringExtra("phonenumber");


        myDashboard = (TextView) findViewById(R.id.myDashboard);
        myName = (TextView) findViewById(R.id.myName);
        myName.setText(name);
        TodaysPrompt = (TextView) findViewById(R.id.TodaysPrompt);
        mpesa_text = (TextView) findViewById(R.id.mpesa_text);
        mpesa_steps0 = (TextView) findViewById(R.id.mpesa_steps0);
        mpesa_steps1 = (TextView) findViewById(R.id.mpesa_steps1);
        mpesa_steps2 = (TextView) findViewById(R.id.mpesa_steps2);
        business_number = (TextView) findViewById(R.id.business_number);
        account_number = (TextView) findViewById(R.id.account_number);
        account_number.setText(phonenumber);

        mpesa_steps4 = (TextView) findViewById(R.id.mpesa_steps4);
        mpesa_steps4.setText("Amount: "+amount);
        mpesa_steps9 = (TextView) findViewById(R.id.mpesa_steps9);
        submitBtn = (Button) findViewById(R.id.submitBtn);

        auth = FirebaseAuth.getInstance();



        final FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            userEmail = user.getEmail();
            userUID = user.getUid();

        }

        Button backBtn = findViewById(R.id.submitBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });





    }

}