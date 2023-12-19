package com.example.asset2.DownloadData;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.asset2.DownloadData.FMS.Download_ht;
import com.example.asset2.DownloadData.FMS.Download_jasset;
import com.example.asset2.DownloadData.FMS.Download_mobile;
import com.example.asset2.DownloadData.FMS.Download_network;
import com.example.asset2.DownloadData.FMS.Download_rig;
import com.example.asset2.DownloadData.Network.Download_cctv;
import com.example.asset2.DownloadData.Network.Download_komputer;
import com.example.asset2.DownloadData.Network.Download_laptop;
import com.example.asset2.DownloadData.Network.Download_print;
import com.example.asset2.DownloadData.Network.Download_server;
import com.example.asset2.DownloadData.Network.Download_switch;
import com.example.asset2.DownloadData.Network.Download_wireless;
import com.example.asset2.R;

public class Dashboard_download extends AppCompatActivity {
    ImageView btnkomputer, btnlaptop, btncctv, btnswitch, btnwireless, btnprint, btnserver;
    ImageView btnht, btnrig, btnjasset, btnmobile, btnnetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_download);

        btnkomputer = findViewById(R.id.btn_komputer);
        btnlaptop = findViewById(R.id.btn_laptop);
        btncctv = findViewById(R.id.btn_cctv);
        btnswitch = findViewById(R.id.btn_switch);
        btnserver = findViewById(R.id.btn_server);
        btnwireless = findViewById(R.id.btn_wireless);
        btnprint = findViewById(R.id.btn_print);
        btnht = findViewById(R.id.btn_ht);
        btnrig = findViewById(R.id.btn_rig);
        btnjasset = findViewById(R.id.btn_jasset);
        btnmobile = findViewById(R.id.btn_mobile);
        btnnetwork = findViewById(R.id.btn_network);


        btncctv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard_download.this, Download_cctv.class);
                startActivity(intent);
            }
        });

        btnkomputer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard_download.this, Download_komputer.class);
                startActivity(intent);
            }
        });

        btnlaptop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard_download.this, Download_laptop.class);
                startActivity(intent);
            }
        });

        btnswitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard_download.this, Download_switch.class);
                startActivity(intent);
            }
        });

        btnserver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard_download.this, Download_server.class);
                startActivity(intent);
            }
        });

        btnprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard_download.this, Download_print.class);
                startActivity(intent);
            }
        });

        btnwireless.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard_download.this, Download_wireless.class);
                startActivity(intent);
            }
        });

        btnht.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard_download.this, Download_ht.class);
                startActivity(intent);
            }
        });

        btnjasset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard_download.this, Download_jasset.class);
                startActivity(intent);
            }
        });

        btnmobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard_download.this, Download_mobile.class);
                startActivity(intent);
            }
        });

        btnnetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard_download.this, Download_network.class);
                startActivity(intent);
            }
        });

        btnrig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard_download.this, Download_rig.class);
                startActivity(intent);
            }
        });

    }
}
