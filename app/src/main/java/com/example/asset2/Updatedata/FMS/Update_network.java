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

public class Update_network extends AppCompatActivity {

    Button btnDaftar, btnScan, btnPhoto;
    TextView Bumaasset, Serialnumber, Status, Keterangan;
    private ImageView imageView;
    Bitmap bitMap = null;

    ProgressDialog progressDialog;
    String buma_asset, serialnumber, status, keterangan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_network);


        btnDaftar = findViewById(R.id.btnSelesai);
        Bumaasset = findViewById(R.id.txtBumaasset);
        Serialnumber = findViewById(R.id.txtSerial);
        Status = findViewById(R.id.txtStatus);
        Keterangan = findViewById(R.id.txtKeterangan);
        btnScan = findViewById(R.id.btnScan);
        progressDialog = new ProgressDialog(this);


        buma_asset = getIntent().getStringExtra("buma_asset");
        serialnumber = getIntent().getStringExtra("serialnumber");
        status = getIntent().getStringExtra("status");
        keterangan = getIntent().getStringExtra("keterangan");

        Bumaasset.setText(buma_asset);
        Serialnumber.setText(serialnumber);
        Status.setText(status);
        Keterangan.setText(keterangan);

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Updating Data...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                buma_asset        = Bumaasset.getText().toString();
                serialnumber    = Serialnumber.getText().toString();
                status         = Status.getText().toString();
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
        if (buma_asset.equals("") || serialnumber.equals("") || status.equals("") || keterangan.equals("")) {
            progressDialog.dismiss();
            Toast.makeText(Update_network.this, "Check your input!", Toast.LENGTH_SHORT).show();
        } else {
            updateData();
        }
    }

    void updateData() {
        AndroidNetworking.post("hhttps://jdksmurf.com/BUMA/Update_network.php")
                .addBodyParameter("buma_asset", "" + buma_asset)
                .addBodyParameter("serialnumber", "" + serialnumber)
                .addBodyParameter("status", "" + status)
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
                            Toast.makeText(Update_network.this, "" + pesan, Toast.LENGTH_SHORT).show();
                            Log.d("status", "" + status);
                            if (status) {
                                new AlertDialog.Builder(Update_network.this)
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
                                new AlertDialog.Builder(Update_network.this)
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
}