package com.example.asset2.Maintenance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.asset2.Maintenance.Data_maintenance.DataCPU;
import com.example.asset2.Maintenance.Data_maintenance.DataIPPhone;
import com.example.asset2.Maintenance.Data_maintenance.DataKeymou;
import com.example.asset2.Maintenance.Data_maintenance.DataMonitor;
import com.example.asset2.Maintenance.Data_maintenance.DataPrinter;
import com.example.asset2.Maintenance.Data_maintenance.DataUPS;
import com.example.asset2.R;

public class Maintenance_cctv extends AppCompatActivity {

    Button btnDaftar, btnCPU, btnMONITOR, btnUPS, btnKeyboard, btnPrint, btnPhone;
    TextView Nama, Jumlah, Satuan, Tanggal, Keterangan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_maintenance_cctv);

        btnDaftar = findViewById(R.id.btnSelesai);
        Nama = findViewById(R.id.txtHostname);
        Jumlah = findViewById(R.id.txtSerial);
        Satuan = findViewById(R.id.txtLokasi);
        Tanggal = findViewById(R.id.txtHostname);
        Keterangan = findViewById(R.id.txtNoregis);

        btnCPU = findViewById(R.id.btnCPU);
        btnMONITOR = findViewById(R.id.btnMonitor);
        btnUPS = findViewById(R.id.btnUPS);
        btnKeyboard = findViewById(R.id.btnKeyboard);
        btnPrint = findViewById(R.id.btnPrint);
        btnPhone = findViewById(R.id.btnPhone);

        btnCPU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Maintenance_cctv.this, DataCPU.class);
                startActivity(intent);
            }
        });

        btnUPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Maintenance_cctv.this, DataUPS.class);
                startActivity(intent);
            }
        });

        btnMONITOR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Maintenance_cctv.this, DataMonitor.class);
                startActivity(intent);
            }
        });

        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Maintenance_cctv.this, DataPrinter.class);
                startActivity(intent);
            }
        });

        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tindakan yang akan dijalankan saat Tombol 2 ditekan
                Intent intent = new Intent(Maintenance_cctv.this, DataIPPhone.class);
                startActivity(intent);
            }
        });

        btnKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tindakan yang akan dijalankan saat Tombol 2 ditekan
                Intent intent = new Intent(Maintenance_cctv.this, DataKeymou.class);
                startActivity(intent);
            }
        });

    }


    void clearText() {
        Nama.setText("");
        Jumlah.setText("");
        Satuan.setText("");
        Tanggal.setText("");
        Keterangan.setText("");
    }
}