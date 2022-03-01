package com.app.marafresh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class OtpVerification extends AppCompatActivity {
    EditText number;
    CountryCodePicker cpp;
    Button btnGenerate;
    FirebaseAuth mAuth;
    String FullNumber,pnumber;
    TextView terms;
    EditText mobile, otp;
    TextView label, click_here;
    Button submit, verify, resend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        String text = "<font color=#707070>By providing my phone number, I hereby agree and accept the </font> <font color=#21B8EB>Term of Service</font> <font color=##707070>and </font><font color=#21B8EB> Privacy Policy</font><font color=#707070>in use on this app.</font>";



        number = (EditText) findViewById(R.id.mobile);
        pnumber= number.getText().toString();
        terms= (TextView) findViewById(R.id.textView15);
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // startActivity(new Intent(getApplicationContext(), TermsandConditions.class));
            }
        });
        cpp = (CountryCodePicker) findViewById(R.id.countryCodePicker);

        terms.setText(Html.fromHtml(text));

        cpp.detectLocaleCountry(true);
        cpp.detectSIMCountry(true);
        cpp.setAutoDetectedCountry(true);
        cpp.setCountryForNameCode("KE");
        btnGenerate = (Button) findViewById(R.id.submit);
        mAuth = FirebaseAuth.getInstance();

        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = number.getText().toString();
                if (phoneNumber.length() == 0) {
                    Snackbar.make(view, "Please Input Your Phone Number", Snackbar.LENGTH_LONG).setAnchorView(cpp).show();
                } else {
                    String fullNamber = cpp.getSelectedCountryCodeWithPlus() + phoneNumber;
                    AttempAuth(fullNamber);

                }
            }
        });
    }

    private void AttempAuth(String fullNamber) {
        FullNumber = fullNamber;
        btnGenerate.setTextColor(767676);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                fullNamber,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks  mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            Log.d("TAG", "onVerificationCompleted :" + phoneAuthCredential);
            signInWithPhoneAuthCredential(phoneAuthCredential);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Log.w("TAG", "onVerificationFailed: ", e);
            if (e instanceof FirebaseAuthInvalidCredentialsException){

            }else if(e instanceof FirebaseTooManyRequestsException){
                Toast.makeText(OtpVerification.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            Log.d("TAG", "onCodeSent: " + s);
            Intent intent = new Intent(getApplicationContext(), NumberVerification.class);
            intent.putExtra("verificationId",s);
            intent.putExtra("number", FullNumber);
            intent.putExtra("pnumber", pnumber);

            startActivity(intent);
            finish();
        }
    };


    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d("TAG", "onComplete: Success"+ task);
                    FirebaseUser user = task.getResult().getUser();
                    //SendToHomeActivity();
                    if (task.getResult().getAdditionalUserInfo().isNewUser()){

                        //show user email in toast
                        Toast.makeText(OtpVerification.this, ""+user.getPhoneNumber(), Toast.LENGTH_SHORT).show();
                        //go to profile activity after logged in
                        startActivity(new Intent(OtpVerification.this,MainActivity  .class));
                        finish();
                    }else {
                        Toast.makeText(OtpVerification.this, ""+user.getPhoneNumber(), Toast.LENGTH_SHORT).show();
                        //go to profile activity after logged in
                        startActivity(new Intent(OtpVerification.this,MainActivity.class));
                        finish();

                    }
                }else{
                    Log.w("TAG", "onComplete: ",task.getException() );
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                        Toast.makeText(OtpVerification.this, (CharSequence) task.getException(), Toast.LENGTH_SHORT).show();
                    }else if(task.getException() instanceof FirebaseTooManyRequestsException){
                        Toast.makeText(OtpVerification.this, (CharSequence) task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp(){
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
        return true;
    }
}