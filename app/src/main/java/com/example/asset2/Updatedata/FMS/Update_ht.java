package com.example.asset2.Updatedata.FMS;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.asset2.R;

import org.json.JSONObject;

import java.util.Calendar;

public class Update_ht extends AppCompatActivity {

    Button btnDaftar, btnScan, btnPhoto;
    TextView Hostname, Merk, Serialnumber, Ip, Tanggal, Keterangan;
    private ImageView imageView;
    Bitmap bitMap = null;

    ProgressDialog progressDialog;
    String hostname, merk, serialnumber, ip, tanggal, keterangan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_ht);


        btnDaftar = findViewById(R.id.btnSelesai);
        Hostname = findViewById(R.id.txtHostname);
        Merk = findViewById(R.id.txtType);
        Serialnumber = findViewById(R.id.txtSerial);
        Ip = findViewById(R.id.txtIP);
        Tanggal = findViewById(R.id.txtTanggal);
        Keterangan = findViewById(R.id.txtKeterangan1);
        btnScan = findViewById(R.id.btnScan);
        progressDialog = new ProgressDialog(this);


        hostname = getIntent().getStringExtra("hostname");
        merk = getIntent().getStringExtra("merk");
        serialnumber = getIntent().getStringExtra("serialnumber");
        ip = getIntent().getStringExtra("ip");
        tanggal = getIntent().getStringExtra("tanggal");
        keterangan = getIntent().getStringExtra("keterangan");

        Hostname.setText(hostname);
        Merk.setText(merk);
        Serialnumber.setText(serialnumber);
        Ip.setText(ip);
        Tanggal.setText(tanggal);
        Keterangan.setText(keterangan);

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Updating Data...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                hostname        = Hostname.getText().toString();
                merk            = Merk.getText().toString();
                serialnumber    = Serialnumber.getText().toString();
                ip              = Ip.getText().toString();
                tanggal         = Tanggal.getText().toString();
                keterangan      = Keterangan.getText().toString();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        validatingData();
                    }
                }, 1000);
            }
        });
    }

    void validatingData() {
        if (hostname.equals("") || merk.equals("") || serialnumber.equals("") || ip.equals("") || tanggal.equals("") || keterangan.equals("")) {
            progressDialog.dismiss();
            Toast.makeText(Update_ht.this, "Check your input!", Toast.LENGTH_SHORT).show();
        } else {
            updateData();
        }
    }

    void updateData() {
        AndroidNetworking.post("https://jdksmurf.com/BUMA/Update_ht.php")
                .addBodyParameter("hostname", "" + hostname)
                .addBodyParameter("merk", "" + merk)
                .addBodyParameter("serialnumber", "" + serialnumber)
                .addBodyParameter("ip", "" + ip)
                .addBodyParameter("tanggal", "" + tanggal)
                .addBodyParameter("keterangan", "" + keterangan)
                .setPriority(Priority.MEDIUM)
                .setTag("Update Data")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.d("cekUpdate", "" + response);
                        try {
                            Boolean status = response.getBoolean("status");
                            String pesan = response.getString("result");
                            Toast.makeText(Update_ht.this, "" + pesan, Toast.LENGTH_SHORT).show();
                            Log.d("status", "" + status);
                            if (status) {
                                new AlertDialog.Builder(Update_ht.this)
                                        .setMessage("Data berhasil di update !")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //Intent i = getIntent();
                                                //setResult(RESULT_OK,i);
                                                //add_mahasiswa.this.finish();
                                            }
                                        })
                                        .show();
                            } else {
                                new AlertDialog.Builder(Update_ht.this)
                                        .setMessage("Gagal Menambahkan Data !")
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //Intent i = getIntent();
                                                //setResult(RESULT_CANCELED,i);
                                                //add_mahasiswa.this.finish();
                                            }
                                        })
                                        .setCancelable(false)
                                        .show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        //  Log.d("Tidak dapat memperbarui data Anda", "" + anError.getErrorBody());

                        Log.d("Tidak dapat memperbarui", "" + anError.getErrorBody());
                    }
                });
    }
    public void showDatePickerDialog(View v) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Tindakan yang akan diambil saat tanggal dipilih
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        Tanggal.setText(selectedDate);
                    }
                },
                year, month, day);

        datePickerDialog.show();
    }

}