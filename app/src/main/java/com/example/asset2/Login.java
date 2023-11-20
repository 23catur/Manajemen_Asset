package com.example.asset2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

public class Login extends AppCompatActivity {

    EditText ETnik;
    String nik;
    ProgressDialog progressDialog;
    ImageView btnlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //preferences

        ETnik     = findViewById(R.id.ETnik);
        btnlogin = findViewById(R.id.IVlogin);
        progressDialog = new ProgressDialog(this);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                progressDialog.setMessage("Loading ...");
                progressDialog.setCancelable(false);
                progressDialog.show( );

                nik = ETnik.getText().toString();


                validasiData();
            }
        });
    }
    void validasiData(){
        if (!nik.isEmpty() ){
            SignIn();
        }else{
            ETnik.setError("Masukkan Email Anda!");
        }
    }
    void SignIn() {
        AndroidNetworking.post("https://jdksmurf.com/BUMA/login.php")
                .addBodyParameter("nik", "" + nik)
                .setPriority(Priority.MEDIUM)
                .setTag("Tambah Data")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.d("cekTambah", "" + response);
                        try {
                            // Periksa apakah kunci "status" ada dalam objek JSON
                            if (response.has("status")) {
                                // Ambil nilai yang terkait dengan kunci "status"
                                Boolean status = response.getBoolean("status");
                                String pesan = response.getString("result");
                                String nama = response.getString("nama");
                                String nik = response.getString("nik");

                                Toast.makeText(Login.this, "" + pesan, Toast.LENGTH_SHORT).show();
                                Log.d("status", "" + status);

                                if (status) {
                                    new AlertDialog.Builder(Login.this)
                                            .setMessage("Selamat Datang di LAPOR-PAK!!")
                                            .setCancelable(false)
                                            .setPositiveButton("Go", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent(Login.this, NavigasiActivity.class);
                                                    startActivity(intent);
                                                }
                                            })
                                            .show();
                                } else {
                                    new AlertDialog.Builder(Login.this)
                                            .setMessage("Periksa Kembali Email dan Password !")
                                            .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                }
                                            })
                                            .setCancelable(false)
                                            .show();
                                }
                            } else {
                                // Kode untuk menangani ketika kunci "status" tidak ada dalam objek JSON
                                Toast.makeText(Login.this, "Respons tidak sesuai format", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(com.androidnetworking.error.ANError anError) {
                        Log.d("Error Login", "" + anError.getErrorBody());
                    }
                });
    }

}
