package com.example.asset2.Updatedata.Network;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.asset2.R;
import com.github.chrisbanes.photoview.OnViewTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;

public class Update_switch extends AppCompatActivity {

    Button btnUpdate, btnDelete, btnPhoto;
    TextView Hostname, Merk, Serialnumber, Ip, Department, Lokasi, Tanggal, Keterangan;
    PhotoView photoView;
    ProgressDialog progressDialog;
    String hostname, merk, serialnumber, ip, department, lokasi, tanggal, keterangan, foto;
    Bitmap bitMap = null;
    File photoFile;
    public String photoFileName = "photo.jpg";
    static final int REQUEST_TAKE_PHOTO = 2;
    public final String APP_TAG = "MyApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_switch);


        btnUpdate = findViewById(R.id.btnUpdate);
        Hostname = findViewById(R.id.txtHostname);
        Merk = findViewById(R.id.txtType);
        Serialnumber = findViewById(R.id.txtSerial);
        Ip = findViewById(R.id.txtIP);
        Department = findViewById(R.id.txtDepartment);
        Lokasi = findViewById(R.id.txtLokasi);
        Tanggal = findViewById(R.id.txtTanggal);
        Keterangan = findViewById(R.id.txtKeterangan1);
        btnDelete = findViewById(R.id.btnDelete);
        progressDialog = new ProgressDialog(this);

        hostname = getIntent().getStringExtra("hostname");
        merk = getIntent().getStringExtra("merk");
        serialnumber = getIntent().getStringExtra("serialnumber");
        ip = getIntent().getStringExtra("ip");
        department = getIntent().getStringExtra("department");
        lokasi = getIntent().getStringExtra("lokasi");
        tanggal = getIntent().getStringExtra("tanggal");
        keterangan = getIntent().getStringExtra("keterangan");

        Hostname.setText(hostname);
        Merk.setText(merk);
        Serialnumber.setText(serialnumber);
        Ip.setText(ip);
        Department.setText(department);
        Lokasi.setText(lokasi);
        Tanggal.setText(tanggal);
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

        btnPhoto = findViewById(R.id.btnPhoto);
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePhotoButtonClick();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Memperbarui Data...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                hostname = Hostname.getText().toString();
                merk = Merk.getText().toString();
                serialnumber = Serialnumber.getText().toString();
                ip = Ip.getText().toString();
                department = Department.getText().toString();
                lokasi = Lokasi.getText().toString();
                tanggal = Tanggal.getText().toString();
                keterangan = Keterangan.getText().toString();

                Log.d("Update_switch", "bitMap: " + bitMap);

                if (bitMap == null) {
                    progressDialog.dismiss();
                    new AlertDialog.Builder(Update_switch.this)
                            .setMessage("Mohon masukkan foto")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Anda dapat menambahkan tindakan apa pun yang diperlukan ketika pengguna mengakui pesan.
                                }
                            })
                            .show();
                } else {
                    validatingData();
                }
            }
        });

        photoAttacher.setOnViewTapListener(new OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                showFullScreenImage(bitMap);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KonfirmasiHapus();
            }
        });
    }


    private void handlePhotoButtonClick() {
        if (bitMap != null) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Update_switch.this);
            alertDialogBuilder.setMessage("Apakah Anda ingin mengambil foto baru?");
            alertDialogBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    ambilFoto(); // Menggunakan metode ambilFoto() untuk mengambil foto baru
                }
            });

            alertDialogBuilder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Tidak melakukan apa-apa atau tambahkan tindakan khusus
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else {
            ambilFoto(); // Menggunakan metode ambilFoto() untuk mengambil foto baru
        }
    }


    private void ambilFoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName);

        String authorities = getPackageName() + ".fileprovider";
        Uri fileProvider = FileProvider.getUriForFile(Update_switch.this, authorities, photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_TAKE_PHOTO);
        } else {
            Log.e("Update_switch", "Tidak ada aplikasi kamera yang tersedia");
        }
    }


    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    public File getPhotoFileUri(String fileName)  {
        if (isExternalStorageAvailable()) {
            File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
                Log.d(APP_TAG, "failed to create directory");
            }
            File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

            return file;

        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Log.d("Update_switch", "Foto berhasil diambil, path: " + photoFile.getPath());
            muatGambarKeImageView(photoFile.getPath());

            // Inisialisasi bitMap dengan gambar yang baru diambil
            bitMap = BitmapFactory.decodeFile(photoFile.getPath());
        } else {
            Log.e("Update_switch", "Gagal mengambil foto, resultCode: " + resultCode);
        }
    }

    private void muatGambarKeImageView(String imagePath) {
        Log.d("Update_switch", "Memuat gambar ke dalam ImageView, imagePath: " + imagePath);
        Picasso.get().load("file://" + imagePath).into(photoView);
    }


    public void TakePhoto() {
        Log.d("Update_switch", "Memulai pengambilan foto");

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName);

        String authorities = getPackageName() + ".fileprovider";
        Uri fileProvider = FileProvider.getUriForFile(Update_switch.this, authorities, photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_TAKE_PHOTO);
        } else {
            Log.e("Update_switch", "Tidak ada aplikasi kamera yang tersedia");
        }
    }
    private void showFullScreenImage(Bitmap bitmap) {
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

    void validatingData() {
        if (hostname.equals("") || merk.equals("") || serialnumber.equals("") || ip.equals("") || department.equals("") || lokasi.equals("") || tanggal.equals("") || keterangan.equals("")) {
            progressDialog.dismiss();
            Toast.makeText(Update_switch.this, "Check your input!", Toast.LENGTH_SHORT).show();
        } else {
            updateData();
        }
    }

    void getDataIntent(){
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            Hostname.setText(bundle.getString("hostname"));
            Merk.setText(bundle.getString("merk"));
            Serialnumber.setText(bundle.getString("serialnumber"));
            Ip.setText(bundle.getString("ip"));
            Department.setText(bundle.getString("department"));
            Lokasi.setText(bundle.getString("lokasi"));
            Tanggal.setText(bundle.getString("tanggal"));
            Keterangan.setText(bundle.getString("keterangan"));
            Picasso.get().load("https://jdksmurf.com/BUMA/foto_asset/" + bundle.getString("foto")).into(photoView);
        }else{
            Hostname.setText("");
            Merk.setText("");
            Serialnumber.setText("");
            Ip.setText("");
            Department.setText("");
            Lokasi.setText("");
            Tanggal.setText("");
            Keterangan.setText("");
        }
    }

    private void KonfirmasiHapus() {
        new AlertDialog.Builder(Update_switch.this)
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

        AndroidNetworking.post("https://jdksmurf.com/BUMA/delete_switch.php")
                .addBodyParameter("hostname", hostname)
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
                            Toast.makeText(Update_switch.this, "" + pesan, Toast.LENGTH_SHORT).show();

                            if (status) {
                                new AlertDialog.Builder(Update_switch.this)
                                        .setMessage("Data berhasil dihapus!")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent i = getIntent();
                                                setResult(RESULT_OK, i);
                                                Update_switch.this.finish();
                                            }
                                        })
                                        .show();
                            } else {
                                new AlertDialog.Builder(Update_switch.this)
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
                        Toast.makeText(Update_switch.this, "Error menghapus data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String convertFileToString(File photoFile) {
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(photoFile.getPath());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] imageBytes = baos.toByteArray();
            return Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    void updateData() {
        String foto = "";  // Default foto kosong
        if (bitMap != null) {
            foto = getStringImage(bitMap);
        } else if (photoFile != null) {
            // Ubah file menjadi String atau lakukan proses lain yang sesuai
            foto = convertFileToString(photoFile);
        }

//        Log.d("Update_komputer", "Mengirim permintaan pembaruan dengan foto: " + foto);

        AndroidNetworking.post("https://jdksmurf.com/BUMA/Update_switch.php")
                .addBodyParameter("hostname", "" + hostname)
                .addBodyParameter("merk", "" + merk)
                .addBodyParameter("serialnumber", "" + serialnumber)
                .addBodyParameter("ip", "" + ip)
                .addBodyParameter("department", "" + department)
                .addBodyParameter("lokasi", "" + lokasi)
                .addBodyParameter("tanggal", "" + tanggal)
                .addBodyParameter("keterangan", "" + keterangan)
                .addBodyParameter("foto", "" + foto) // Tambahkan foto ke dalam permintaan
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
                            Toast.makeText(Update_switch.this, "" + pesan, Toast.LENGTH_SHORT).show();
                            Log.d("status", "" + status);
                            if (status) {
                                new AlertDialog.Builder(Update_switch.this)
                                        .setMessage("Data berhasil diupdate!")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent i = getIntent();
                                                setResult(RESULT_OK, i);
                                                Update_switch.this.finish();
                                            }
                                        })
                                        .show();
                            } else {
                                new AlertDialog.Builder(Update_switch.this)
                                        .setMessage("Gagal mengupdate data!")
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent i = getIntent();
                                                setResult(RESULT_CANCELED, i);
                                                Update_switch.this.finish();
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
                        Log.d("Tidak dapat memperbarui", "" + anError.getErrorBody());
                        progressDialog.dismiss();
                        Toast.makeText(Update_switch.this, "Error mengupdate data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public String getStringImage(Bitmap bmp) {
        if (bmp == null) {
            return "";  // Jika bitmap null, kembalikan string kosong
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
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
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        Tanggal.setText(selectedDate);
                    }
                },
                year, month, day);

        datePickerDialog.show();
    }

}