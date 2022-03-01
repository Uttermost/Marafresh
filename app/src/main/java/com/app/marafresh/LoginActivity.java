package com.app.marafresh;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private static final int GOOGLE_SIGN_IN_REQUEST =112 ;
    FirebaseAuth auth;
    CallbackManager callbackManager;
    String verificationOtp;
    FirebaseAuth mAuth;
    TextView HaventReceived,textview;
    String pnumber1;
    private String mCurrentUserId,key;


    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth=FirebaseAuth.getInstance();

        mAuth = FirebaseAuth.getInstance();
        auth = FirebaseAuth.getInstance();

        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance().getReference();
            key= mDatabase.child("UserProfile").push().getKey();
        }

        InitiallizeOTPLogin();
        if(auth.getCurrentUser()!=null){
            startActivity(new Intent(LoginActivity.this,LoginActivity.class));
            finish();
        }
    }



    private void InitiallizeOTPLogin(){
        Button sendOtp=findViewById(R.id.send_otp);
        Button verifyOtp=findViewById(R.id.verify_otp);
        final EditText phoneInput=findViewById(R.id.phone_input);
        final EditText otpInput=findViewById(R.id.otp_input);

        sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOtpCode(phoneInput.getText().toString());
            }
        });

        verifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VerifyOtp(otpInput.getText().toString());
            }
        });
    }

    private void  sendOtpCode(String phone){
        PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Log.d("Success","Verified");
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.d("Success","Failed");
                e.printStackTrace();
            }

            @Override
            public void onCodeSent(@NonNull String verifyToke, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                verificationOtp=verifyToke;
                PhoneAuthProvider.ForceResendingToken  token=forceResendingToken;
            }
        };

        PhoneAuthProvider
                .getInstance().
                verifyPhoneNumber(phone,60, TimeUnit.SECONDS,LoginActivity.this,callbacks);
    }

    private void VerifyOtp(String otp){
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationOtp,otp);

        auth.signInWithCredential(credential)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
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
                                Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }else if(task.getException() instanceof FirebaseTooManyRequestsException){
                                Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();

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


    private void  SendUserData(FirebaseUser user){
        Log.d("Login Success","SUccess");
        Log.d("User ",user.getUid());
    }
}