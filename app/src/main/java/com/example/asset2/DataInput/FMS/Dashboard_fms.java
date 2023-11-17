package com.example.asset2.DataInput.FMS;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.asset2.DataInput.Network.Dashboard_net;
import com.example.asset2.DataInput.Network.Print_input;
import com.example.asset2.R;

public class Dashboard_fms extends AppCompatActivity {

    ImageView ht, rig, jasset, mobile, network;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_fms);

        ht  = findViewById(R.id.btn_ht);
        rig = findViewById(R.id.btn_rig);
        jasset = findViewById(R.id.btn_jasset);
        mobile = findViewById(R.id.btn_mobile);
        network = findViewById(R.id.btn_network);


        ht.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tindakan yang akan dijalankan saat Tombol 2 ditekan
                Intent intent = new Intent(Dashboard_fms.this, Ht_input.class);
                startActivity(intent);
            }
        });

        rig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tindakan yang akan dijalankan saat Tombol 2 ditekan
                Intent intent = new Intent(Dashboard_fms.this, Rig_input.class);
                startActivity(intent);
            }
        });

        jasset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tindakan yang akan dijalankan saat Tombol 2 ditekan
                Intent intent = new Intent(Dashboard_fms.this, Jasset_input.class);
                startActivity(intent);
            }
        });

        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tindakan yang akan dijalankan saat Tombol 2 ditekan
                Intent intent = new Intent(Dashboard_fms.this, Mobile_input.class);
                startActivity(intent);
            }
        });

        network.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tindakan yang akan dijalankan saat Tombol 2 ditekan
                Intent intent = new Intent(Dashboard_fms.this, Network_input.class);
                startActivity(intent);
            }
        });
    }
}