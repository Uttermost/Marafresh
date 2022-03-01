package com.app.marafresh;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.chaos.view.PinView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;


public class NumberVerification extends AppCompatActivity {
    Button btnVerify;
    PinView pinview;
    FirebaseAuth mAuth,auth;
    TextView HaventReceived,textview;
    String pnumber1;
    private String mCurrentUserId,key;


    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_number_verification);
        btnVerify = (Button)findViewById(R.id.btnVerify);
        pinview = (PinView) findViewById(R.id.pinview);
        HaventReceived= (TextView) findViewById(R.id.HaventReceived);
        textview= (TextView) findViewById(R.id.textView);


        String text = "<font color=#707070></font> Didn't receive the OTP?<font color=#21B8EB>RESEND OTP</font>";

        HaventReceived.setText(Html.fromHtml(text));

        HaventReceived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NumberVerification.this,OtpVerification.class));
                finish();
            }
        });
        mAuth = FirebaseAuth.getInstance();
        auth = FirebaseAuth.getInstance();

        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance().getReference();
            key= mDatabase.child("UserProfile").push().getKey();
        }

        String verifycationId = getIntent().getStringExtra("verificationId");
        String pnumber = getIntent().getStringExtra("number");
        pnumber1 = getIntent().getStringExtra("pnumber");

        textview.setText("Enter the OPT sent to " + pnumber);


        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verifycationId, pinview.getText().toString());
                signInWithPhoneAuthCredential(credential);
            }
        });
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d("TAG", "onComplete: Success"+ task);
                    FirebaseUser user = task.getResult().getUser();
                    Log.d("TAG", "onComplete: Success"+ task);

                    //SendToHomeActivity();
                    if (task.getResult().getAdditionalUserInfo().isNewUser()){

                        //show user email in toast
                        //Toast.makeText(NumberVerification.this, ""+user.getPhoneNumber(), Toast.LENGTH_SHORT).show();
                        //go to profile activity after logged in
                       // startActivity(new Intent(NumberVerification.this,MainActivity.class));
                       // finish();
                        SendToHomeActivity();
                    }else {
                       // Toast.makeText(NumberVerification.this, ""+user.getPhoneNumber(), Toast.LENGTH_SHORT).show();
                        //go to profile activity after logged in
                        //startActivity(new Intent(NumberVerification.this,MainActivity.class));
                       // finish();
                        SendToHomeActivity();

                    }
                }else{
                    Log.w("TAG", "onComplete: ",task.getException() );
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                        Toast.makeText(NumberVerification.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }else if(task.getException() instanceof FirebaseTooManyRequestsException){
                        Toast.makeText(NumberVerification.this, task.getException().toString(), Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
    }
    private void SendToHomeActivity() {


        Map<String, Object> postBrew = new HashMap<String, Object>();


        postBrew.put("FullName",
                "Add your Names");

        postBrew.put("email",
                "Add Your Email");

        postBrew.put("phonenumber",
                mAuth.getCurrentUser().getPhoneNumber());
        postBrew.put("uid",
                mAuth.getCurrentUser().getUid());


        postBrew.put("key",
                mDatabase.child("UserProfile").push().getKey());
        postBrew.put("image", "https://firebasestorage.googleapis.com/v0/b/mara-fresh.appspot.com/o/android-profile-icon-2.jpg?alt=media&token=85257792-bdeb-4d99-b07e-22438ee101bf");

        postBrew.put("timestamp", ServerValue.TIMESTAMP);


        if (key != null) {
            mDatabase.child("UserProfile").child(auth.getCurrentUser().getUid()).setValue(postBrew);


            Intent xbrew = new Intent(getApplicationContext(), MainActivity.class);
            xbrew.putExtra("key",key);
            startActivity(xbrew);
            Toast.makeText(getApplicationContext(), "Profile Created  Successfully", Toast.LENGTH_LONG).show();
            //finish();


        }

    }
}