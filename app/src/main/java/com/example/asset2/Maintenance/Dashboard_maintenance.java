package com.example.asset2.Maintenance;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.asset2.DataInput.FMS.Ht_input;
import com.example.asset2.DataInput.FMS.Rig_input;
import com.example.asset2.DataInput.Network.Cctv_input;
import com.example.asset2.DataInput.Network.Laptop_input;
import com.example.asset2.DataInput.Network.Print_input;
import com.example.asset2.DataInput.Network.Switch_input;
import com.example.asset2.DataInput.Network.Wireless_input;
import com.example.asset2.Maintenance.Komputer.Input_maintenance;
import com.example.asset2.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Dashboard_maintenance extends AppCompatActivity {

    private ActionBar toolbar;

    ImageView cctv, print, wireless, komputer, laptop, sw, maintenance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_maintenance);

//        toolbar = getSupportActionBar();
//        BottomNavigationView navigation = findViewById(R.id.navigation);

        cctv  = findViewById(R.id.btn_cctv);
        print = findViewById(R.id.btn_print);
        wireless = findViewById(R.id.btn_wireless);
        komputer = findViewById(R.id.btn_komputer);
        laptop = findViewById(R.id.btn_laptop);
        sw = findViewById(R.id.btn_switch);

//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        cctv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tindakan yang akan dijalankan saat Tombol 2 ditekan
                Intent intent = new Intent(Dashboard_maintenance.this, Cctv_input.class);
                startActivity(intent);
            }
        });

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tindakan yang akan dijalankan saat Tombol 2 ditekan
                Intent intent = new Intent(Dashboard_maintenance.this, Print_input.class);
                startActivity(intent);
            }
        });

        laptop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tindakan yang akan dijalankan saat Tombol 2 ditekan
                Intent intent = new Intent(Dashboard_maintenance.this, Laptop_input.class);
                startActivity(intent);
            }
        });

        komputer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tindakan yang akan dijalankan saat Tombol 2 ditekan
                Intent intent = new Intent(Dashboard_maintenance.this, Input_maintenance.class);
                startActivity(intent);
            }
        });

        wireless.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tindakan yang akan dijalankan saat Tombol 2 ditekan
                Intent intent = new Intent(Dashboard_maintenance.this, Wireless_input.class);
                startActivity(intent);
            }
        });

        sw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tindakan yang akan dijalankan saat Tombol 2 ditekan
                Intent intent = new Intent(Dashboard_maintenance.this, Switch_input.class);
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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_option, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.exitbtn:
//                finish();
//            default:
//                return super.onContextItemSelected(item);
//        }
//    }
}