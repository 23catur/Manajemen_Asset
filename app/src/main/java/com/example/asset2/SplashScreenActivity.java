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

    SharedPreferences login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splashscreen);

        sessionManager = new SessionManager(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                login = getSharedPreferences("Login", MODE_PRIVATE);
                boolean isFirstTime = login.getBoolean("firstTime", true);

                if (isFirstTime) {
                    SharedPreferences.Editor editor = login.edit();
                    editor.putBoolean("firstTime", false);
                    editor.apply(); // Menggunakan apply() untuk menyimpan perubahan async

                    // Redirect ke halaman NavigasiActivity jika ini bukan kali pertama
                    Intent intent = new Intent(getApplicationContext(), NavigasiActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Selalu arahkan pengguna ke halaman login setelah logout
                    Intent pindah = new Intent(SplashScreenActivity.this, Login.class);
                    startActivity(pindah);
                    finish();
                }
            }
        }, SPLASH_TIMER);

    }
}
