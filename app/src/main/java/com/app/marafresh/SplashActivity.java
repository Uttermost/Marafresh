package com.app.marafresh;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import com.daimajia.androidanimations.library.YoYo;

import androidx.core.content.res.ResourcesCompat;

import com.daimajia.androidanimations.library.Techniques;

import android.os.Handler;
import android.widget.TextView;

public class SplashActivity extends Activity {
    //This is the time it will take for the splash screen to be displayed
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //This is where we change our app name font to blacklist font
        Typeface typeface = ResourcesCompat.getFont(this, R.font.blacklist);

        TextView appname= findViewById(R.id.appname);
        appname.setTypeface(typeface);

        // We use the Yoyo to make our app logo to bounce app and down.
        //There is a lot of Attension Techniques styles
        // example Flash, Pulse, RubberBand, Shake, Swing, Wobble, Bounce, Tada, StandUp, Wave.
        // Your can change the techniques to fit your liking.
        YoYo.with(Techniques.Bounce)
                .duration(7000)
                .playOn(findViewById(R.id.logo));

        //This is where we make our app name fade in as it moves up
        // There are other Fade Techniques too
        //example FadeIn, FadeInUp, FadeInDown, FadeInLeft, FadeInRight
        //FadeOut, FadeOutDown, FadeOutLeft, FadeOutRight, FadeOutUp
        YoYo.with(Techniques.FadeInUp)
                .duration(5000)
                .playOn(findViewById(R.id.appname));

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
                //  finish();
            }
        }, SPLASH_TIME_OUT);
    }
}