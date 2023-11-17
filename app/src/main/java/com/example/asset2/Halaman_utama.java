package com.example.asset2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.asset2.DataInput.FMS.Dashboard_fms;
import com.example.asset2.DataInput.Network.Dashboard_net;
import com.example.asset2.Maintenance.Dashboard_maintenance;

public class Halaman_utama extends AppCompatActivity {

    private ActionBar toolbar;

    ImageView btnnet, btnmainte, btnfms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.halaman_utama);

//        toolbar = getSupportActionBar();
//        BottomNavigationView navigation = findViewById(R.id.navigation);

        btnnet = findViewById(R.id.btn_network);
        btnmainte = findViewById(R.id.btn_maintenance);
        btnfms = findViewById(R.id.btn_fms);

//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        btnnet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tindakan yang akan dijalankan saat Tombol 2 ditekan
                Intent intent = new Intent(Halaman_utama.this, Dashboard_net.class);
                startActivity(intent);
            }
        });

        btnfms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tindakan yang akan dijalankan saat Tombol 2 ditekan
                Intent intent = new Intent(Halaman_utama.this, Dashboard_fms.class);
                startActivity(intent);
            }
        });

        btnmainte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tindakan yang akan dijalankan saat Tombol 2 ditekan
                Intent intent = new Intent(Halaman_utama.this, Dashboard_maintenance.class);
                startActivity(intent);
            }
        });



    }
//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.navigation_home:
//                    return true;
//                case R.id.navigation_input:
//                    Intent i = new Intent(getApplicationContext(), InputActivity.class);
//                    startActivity(i);
//                    return true;
//                case R.id.navigation_daftar:
//                    Intent j = new Intent(getApplicationContext(), ListActivity.class);
//                    startActivity(j);
//                    return true;
//            }
//            return false;
//        }
//    };
    }