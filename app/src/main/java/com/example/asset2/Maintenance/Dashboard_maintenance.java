package com.example.asset2.Maintenance;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.asset2.DataInput.Network.Cctv_input;
import com.example.asset2.DataInput.Network.Laptop_input;
import com.example.asset2.DataInput.Network.Print_input;
import com.example.asset2.DataInput.Network.Switch_input;
import com.example.asset2.DataInput.Network.Wireless_input;
import com.example.asset2.R;

public class Dashboard_maintenance extends AppCompatActivity {


    ImageView cctv, print, wireless, komputer, laptop, sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_maintenance);

        cctv  = findViewById(R.id.btn_cctv);
        print = findViewById(R.id.btn_print);
        wireless = findViewById(R.id.btn_wireless);
        komputer = findViewById(R.id.btn_komputer);
        laptop = findViewById(R.id.btn_laptop);
        sw = findViewById(R.id.btn_switch);

        cctv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard_maintenance.this, Maintenance_cctv.class);
                startActivity(intent);
            }
        });

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard_maintenance.this, Maintenance_print.class);
                startActivity(intent);
            }
        });

        laptop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard_maintenance.this, Maintenance_laptop.class);
                startActivity(intent);
            }
        });

        komputer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard_maintenance.this, Maintenance_komputer.class);
                startActivity(intent);
            }
        });

        wireless.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard_maintenance.this, Maintenance_wireless.class);
                startActivity(intent);
            }
        });

        sw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard_maintenance.this, Maintenance_switch.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_option, menu);
        return true;
    }
}