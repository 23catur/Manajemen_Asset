package com.example.asset2.Updatedata.FMS;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
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
import com.example.asset2.Updatedata.Network.Update_wireless;
import com.github.chrisbanes.photoview.OnViewTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.Calendar;

public class Update_network extends AppCompatActivity {

    Button btnUpdate, btnDelete;
    TextView Bumaasset, Serialnumber, Status, Keterangan;
    PhotoView photoView;
    ProgressDialog progressDialog;
    String buma_asset, serialnumber, status, keterangan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_network);


        btnUpdate = findViewById(R.id.btnUpdate);
        Bumaasset = findViewById(R.id.txtBumaasset);
        Serialnumber = findViewById(R.id.txtSerial);
        Status = findViewById(R.id.txtStatus);
        Keterangan = findViewById(R.id.txtKeterangan);
        btnDelete = findViewById(R.id.btnDelete);
        progressDialog = new ProgressDialog(this);


        buma_asset = getIntent().getStringExtra("buma_asset");
        serialnumber = getIntent().getStringExtra("serialnumber");
        status = getIntent().getStringExtra("status");
        keterangan = getIntent().getStringExtra("keterangan");

        Bumaasset.setText(buma_asset);
        Serialnumber.setText(serialnumber);
        Status.setText(status);
        Keterangan.setText(keterangan);

        photoView = findViewById(R.id.photoView);

        getDataIntent();

        PhotoViewAttacher photoAttacher = new PhotoViewAttacher(photoView);
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photoAttacher.getScale() > 1.0f) {
                    photoAttacher.setScale(1.0f, true);
                } else {
                    photoAttacher.setScale(1.5f, true);
                }
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
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

        photoAttacher.setOnViewTapListener(new OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                showFullScreenImage();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KonfirmasiHapus();
            }
        });
    }
    private void showFullScreenImage() {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_fullscreen_image);

        PhotoView fullscreenPhotoView = dialog.findViewById(R.id.fullscreenPhotoView);
        Picasso.get().load("https://jdksmurf.com/BUMA/foto_asset/" + getIntent().getStringExtra("foto")).into(fullscreenPhotoView);

        fullscreenPhotoView.setOnViewTapListener(new OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void KonfirmasiHapus() {
        new AlertDialog.Builder(Update_network.this)
                .setMessage("Udah yakin boss?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hapusData();
                    }
                })
                .setNegativeButton("Tidak", null)
                .show();
    }

    private void hapusData() {
        progressDialog.setMessage("Menghapus Data...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        AndroidNetworking.post("https://jdksmurf.com/BUMA/delete_network.php")
                .addBodyParameter("buma_asset", buma_asset)
                .setPriority(Priority.MEDIUM)
                .setTag("Hapus Data")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.d("cekHapus", "" + response);
                        try {
                            Boolean status = response.getBoolean("status");
                            String pesan = response.getString("result");
                            Toast.makeText(Update_network.this, "" + pesan, Toast.LENGTH_SHORT).show();

                            if (status) {
                                new AlertDialog.Builder(Update_network.this)
                                        .setMessage("Data berhasil dihapus!")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent i = getIntent();
                                                setResult(RESULT_OK, i);
                                                Update_network.this.finish();
                                            }
                                        })
                                        .show();
                            } else {
                                new AlertDialog.Builder(Update_network.this)
                                        .setMessage("Gagal Menghapus Data!")
                                        .setPositiveButton("OK", null)
                                        .setCancelable(false)
                                        .show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("Tidak dapat menghapus", "" + anError.getErrorBody());
                        progressDialog.dismiss();
                        Toast.makeText(Update_network.this, "Error menghapus data", Toast.LENGTH_SHORT).show();
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

    void getDataIntent(){
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            Bumaasset.setText(bundle.getString("buma_asset"));
            Serialnumber.setText(bundle.getString("serialnumber"));
            Status.setText(bundle.getString("tanggal"));
            Keterangan.setText(bundle.getString("keterangan"));
            Picasso.get().load("https://jdksmurf.com/BUMA/foto_asset/" + bundle.getString("foto")).into(photoView);
        }else{
            Bumaasset.setText("");
            Serialnumber.setText("");
            Status.setText("");
            Keterangan.setText("");
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
                                                Intent i = getIntent();
                                                setResult(RESULT_OK,i);
                                                Update_network.this.finish();
                                            }
                                        })
                                        .show();
                            } else {
                                new AlertDialog.Builder(Update_network.this)
                                        .setMessage("Gagal Menambahkan Data !")
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent i = getIntent();
                                                setResult(RESULT_CANCELED,i);
                                                Update_network.this.finish();
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