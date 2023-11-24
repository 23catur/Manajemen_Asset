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

public class Maintenance_switch extends AppCompatActivity {

    //deklarasi variabel
    Button btnDaftar, btnCPU, btnMONITOR, btnUPS, btnKeyboard, btnPrint, btnPhone;
    TextView Nama, Jumlah, Satuan, Tanggal, Keterangan;
//    DBHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_maintenance_switch);

        //memberikan nilai/object ke variabel
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


//        btnDaftar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (Nama.getText().toString().isEmpty()||Jumlah.getText().toString().isEmpty()
//                        ||Satuan.getText().toString().isEmpty()||Tanggal.getText().toString().isEmpty()||Keterangan.getText().toString().isEmpty()) {
//                    Toast.makeText(Input_maintenance.this, "Data Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
//                } else{
//                    DataModel notes = new DataModel();
//                    notes.setHostname(Nama.getText().toString());
//                    notes.setType(Jumlah.getText().toString());
//                    notes.setSerialnumber(Satuan.getText().toString());
//                    notes.setTanggal(Tanggal.getText().toString());
//                    notes.setKeterangan(Keterangan.getText().toString());
//                    db = new DBHelper(Input_maintenance.this);
//                    db.insert(notes);
//                    Toast.makeText(getApplicationContext(), "Data Barang " + Nama.getText() + " Berhasil Disimpan !", Toast.LENGTH_SHORT).show();
//                    clearText();
//                    Input_maintenance.this.finish();
//                }
//            }
//        });

        btnCPU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tindakan yang akan dijalankan saat Tombol 2 ditekan
                Intent intent = new Intent(Maintenance_switch.this, DataCPU.class);
                startActivity(intent);
            }
        });

        btnUPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tindakan yang akan dijalankan saat Tombol 2 ditekan
                Intent intent = new Intent(Maintenance_switch.this, DataUPS.class);
                startActivity(intent);
            }
        });

        btnMONITOR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tindakan yang akan dijalankan saat Tombol 2 ditekan
                Intent intent = new Intent(Maintenance_switch.this, DataMonitor.class);
                startActivity(intent);
            }
        });

        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tindakan yang akan dijalankan saat Tombol 2 ditekan
                Intent intent = new Intent(Maintenance_switch.this, DataPrinter.class);
                startActivity(intent);
            }
        });

        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tindakan yang akan dijalankan saat Tombol 2 ditekan
                Intent intent = new Intent(Maintenance_switch.this, DataIPPhone.class);
                startActivity(intent);
            }
        });

        btnKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tindakan yang akan dijalankan saat Tombol 2 ditekan
                Intent intent = new Intent(Maintenance_switch.this, DataKeymou.class);
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