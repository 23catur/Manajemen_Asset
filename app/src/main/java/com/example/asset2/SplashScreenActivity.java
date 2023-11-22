package com.example.asset2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    private  static  int SPLASH_TIMER = 3000;

    SessionManager sessionManager;

    SharedPreferences onBoardingScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splashscreen);

        sessionManager = new SessionManager(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                onBoardingScreen = getSharedPreferences("onBoardingScreen",MODE_PRIVATE);
                boolean isFirstTime = onBoardingScreen.getBoolean("firstTime", true);

                if (isFirstTime){

                    SharedPreferences.Editor editor = onBoardingScreen.edit();
                    editor.putBoolean("firstTime", false);
                    editor.commit();

                    Intent intent = new Intent(getApplicationContext(), NavigasiActivity.class);
                    startActivity(intent);
                    finish();

                }else {

                    if (sessionManager.getSPSudahLogin()){
                        startActivity(new Intent(SplashScreenActivity.this, NavigasiActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                        finish();
                    }else {
                        Intent pindah = new Intent(SplashScreenActivity.this, Login.class);
                        startActivity(pindah);
                        finish();
                    }

                    //Intent intent = new Intent(getApplicationContext(), Login.class);
                    //startActivity(intent);
                    //finish();
                }

                /**if (sessionManager.getSPSudahLogin()){
                 startActivity(new Intent(SplashScreen.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                 finish();
                 }else {
                 Intent intent = new Intent(SplashScreen.this, OnBoarding.class);
                 startActivity(intent);
                 finish();
                 }*/
            }
        },SPLASH_TIMER);
    }
}