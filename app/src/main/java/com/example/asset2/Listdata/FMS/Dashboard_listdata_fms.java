package com.example.asset2.Listdata.FMS;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.asset2.R;

public class Dashboard_listdata_fms extends AppCompatActivity {

    ImageView ht, rig, jasset, mobile, network;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_listdata_fms);

        ht  = findViewById(R.id.btn_ht);
        rig = findViewById(R.id.btn_rig);
        jasset = findViewById(R.id.btn_jasset);
        mobile = findViewById(R.id.btn_mobile);
        network = findViewById(R.id.btn_network);


        ht.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tindakan yang akan dijalankan saat Tombol 2 ditekan
                Intent intent = new Intent(Dashboard_listdata_fms.this, Data_ht.class);
                startActivity(intent);
            }
        });

        rig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tindakan yang akan dijalankan saat Tombol 2 ditekan
                Intent intent = new Intent(Dashboard_listdata_fms.this, Data_rig.class);
                startActivity(intent);
            }
        });

        jasset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tindakan yang akan dijalankan saat Tombol 2 ditekan
                Intent intent = new Intent(Dashboard_listdata_fms.this, Data_jasset.class);
                startActivity(intent);
            }
        });

        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tindakan yang akan dijalankan saat Tombol 2 ditekan
                Intent intent = new Intent(Dashboard_listdata_fms.this, Data_mobile.class);
                startActivity(intent);
            }
        });

        network.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tindakan yang akan dijalankan saat Tombol 2 ditekan
                Intent intent = new Intent(Dashboard_listdata_fms.this, Data_network.class);
                startActivity(intent);
            }
        });
    }
}