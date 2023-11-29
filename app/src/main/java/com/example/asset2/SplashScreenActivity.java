package com.example.asset2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    private static final int SPLASH_TIMER = 3000;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splashscreen);

        sessionManager = new SessionManager(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean isLoggedIn = sessionManager.isLoggedIn();

                if (isLoggedIn) {
                    Intent intent = new Intent(getApplicationContext(), NavigasiActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent pindah = new Intent(SplashScreenActivity.this, Login.class);
                    startActivity(pindah);
                    finish();
                }
            }
        }, SPLASH_TIMER);
    }
}


