package com.example.asset2.DataInput.FMS;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

//import com.example.inventarisapp.DataBase.DBHelper;
//import com.example.inventarisapp.DataBase.DataModel;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.asset2.DataInput.FMS.Dashboard_fms;
import com.example.asset2.Halaman_utama;
import com.example.asset2.NavigasiActivity;
import com.example.asset2.R;
import com.google.zxing.integration.android.IntentIntegrator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;


public class Rig_input extends AppCompatActivity {

    //deklarasi variabel
    Button btnDaftar, btnScan, btnPhoto;
    TextView Hostname, Merk, Serialnumber, Ip, Tanggal, Keterangan;
    private ImageView imageView;
    Bitmap bitMap = null;

    ProgressDialog progressDialog;
    String hostname, merk, serialnumber, ip, tanggal, keterangan;

    Bitmap bitmap, decoded;
    int PICK_IMAGE_REQUEST = 1;
    int bitmap_size = 60; // range 1 - 100
    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    private static final int CAMERA_REQUEST_CODE = 7777;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_rig);

        //memberikan nilai/object ke variabel
        btnDaftar = findViewById(R.id.btnSelesai);
        Hostname = findViewById(R.id.txtHostname);
        Merk = findViewById(R.id.txtType);
        Serialnumber = findViewById(R.id.txtSerial);
        Ip = findViewById(R.id.txtIP);
        Tanggal = findViewById(R.id.txtTanggal);
        Keterangan = findViewById(R.id.txtKeterangan1);
        btnScan = findViewById(R.id.btnScan);
        progressDialog = new ProgressDialog(this);

        btnPhoto = findViewById(R.id.btnPhoto);
//        imageView = findViewById(R.id.imageView);


        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showFileChooser();
                //intent khusus untuk menangkap foto lewat kamera
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
            }
        });

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hostname        = Hostname.getText().toString();
                merk            = Merk.getText().toString();
                serialnumber    = Serialnumber.getText().toString();
                ip              = Ip.getText().toString();
                tanggal         = Tanggal.getText().toString();
                keterangan      = Keterangan.getText().toString();

                if (Hostname == null){

                    AlertDialog.Builder builder = new AlertDialog.Builder(Rig_input.this);
                    builder.setMessage("Mohon masukkan foto");
                    AlertDialog alert1 = builder.create();
                    alert1.show();
                    progressDialog.dismiss();


                }
                else {
                    validasiData();



                }
            }
        });
    }

    void validasiData(){
        if (!hostname.isEmpty() && !merk.isEmpty() && !ip.isEmpty() && !serialnumber.isEmpty() && !tanggal.isEmpty() && !keterangan.isEmpty()){
            kirimdata();
        }else{
            Hostname.setError("Masukkan Hostname!");
            Merk.setError("Masukkan Type / Merk!");
            Ip.setError("Masukkan IP!");
            Serialnumber.setError("Masukkan Serial Number!");
            Tanggal.setError("Masukkan Tanggal!");
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //mengambil fambar dari Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                // 512 adalah resolusi tertinggi setelah image di resize, bisa di ganti.
                setToImageView(getResizedBitmap(bitmap, 512));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        switch (requestCode) {
            case(CAMERA_REQUEST_CODE) :
                if(resultCode == Ht_input.RESULT_OK)
                {
                    // result code sama, save gambar ke bitmap
                    Bitmap bitmap;
                    bitmap = (Bitmap) data.getExtras().get("data");
                    //imageView.setImageBitmap(bitmap);
                    setToImageView(getResizedBitmap(bitmap, 512));
                }
                break;
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void setToImageView(Bitmap bmp) {
        //compress image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

        //menampilkan gambar yang dipilih dari camera/gallery ke ImageView
        imageView.setImageBitmap(decoded);
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

    public void ListenerScanQR(View v) {
        ScanQR();
    }

    private void ScanQR(){
        IntentIntegrator integrator = new IntentIntegrator(Rig_input.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning . . .");
        integrator.addExtra("SCAN_WIDTH",768);
        integrator.addExtra("SCAN_HEIGHT",1024);
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    void kirimdata(){
        progressDialog.setMessage("Mengirim Laporan...");
        progressDialog.setCancelable(false);
        progressDialog.show();
//        String foto = getStringImage(bitMap);
        AndroidNetworking.post("https://jdksmurf.com/BUMA/Api_rig.php")
                .addBodyParameter("hostname",""+hostname)
                .addBodyParameter("merk",""+merk)
                .addBodyParameter("serialnumber",""+serialnumber)
                .addBodyParameter("ip",""+ip)
                .addBodyParameter("tanggal",""+tanggal)
                .addBodyParameter("keterangan",""+keterangan)
//                .addBodyParameter("foto",""+foto)
                .setPriority(Priority.MEDIUM)
                .setTag("Tambah Data")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("cekTambah",""+response);
                        try {
                            Boolean status = response.getBoolean("status");
                            String pesan   = response.getString("result");
                            Toast.makeText(Rig_input.this, ""+pesan, Toast.LENGTH_SHORT).show();
                            Log.d("status",""+status);
                            if(status){
                                new AlertDialog.Builder(Rig_input.this)
                                        .setMessage("DATA BERHASIL DISIMPAN")
                                        .setCancelable(false)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent i = new Intent(Rig_input.this, NavigasiActivity.class);
                                                startActivity(i);
                                            }
                                        })
                                        .show();
                            }
                            else{
                                new AlertDialog.Builder(Rig_input.this)
                                        .setMessage("Gagal Menambahkan Data !")
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent i = new Intent(Rig_input.this, NavigasiActivity.class);
                                                startActivity(i);
                                            }
                                        })
                                        .setCancelable(false)
                                        .show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("ErrorTambahData",""+anError.getErrorBody());
                    }
                });

    }

}

