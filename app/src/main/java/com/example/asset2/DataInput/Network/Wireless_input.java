package com.example.asset2.DataInput.Network;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.asset2.NavigasiActivity;
import com.example.asset2.R;
import com.example.asset2.VerticalCaptureActivity;
import com.github.chrisbanes.photoview.OnViewTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import org.json.JSONObject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;


public class Wireless_input extends AppCompatActivity {

    Button btnDaftar, btnScan, btnPhoto;
    TextView Hostname, Merk, Serialnumber, Ip, Tanggal, Keterangan, Department, Lokasi;
    PhotoView photoView;
    Bitmap bitMap = null;
    public final String APP_TAG = "MyApp";
    public String photoFileName = "photo.jpg";
    File photoFile;
    ProgressDialog progressDialog;
    String hostname, merk, serialnumber, ip, tanggal, keterangan, department, lokasi;
    Bitmap decoded;
    static final int REQUEST_TAKE_PHOTO = 1;
    int bitmap_size = 60; // range 1 - 100


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_wireless);

        btnDaftar = findViewById(R.id.btnSelesai);
        Hostname = findViewById(R.id.txtHostname);
        Merk = findViewById(R.id.txtType);
        Serialnumber = findViewById(R.id.txtSerial);
        Department = findViewById(R.id.txtDepartment);
        Lokasi = findViewById(R.id.txtLokasi);
        Ip = findViewById(R.id.txtIP);
        Tanggal = findViewById(R.id.txtTanggal);
        Keterangan = findViewById(R.id.txtKeterangan1);
        btnScan = findViewById(R.id.btnScan);
        progressDialog = new ProgressDialog(this);

        btnPhoto = findViewById(R.id.btnPhoto);
        photoView = findViewById(R.id.photoView);
        PhotoViewAttacher photoAttacher = new PhotoViewAttacher(photoView);
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photoAttacher.getScale() > 1.0f) {
                    // Jika gambar sudah diperbesar, kembalikan ke ukuran normal
                    photoAttacher.setScale(1.0f, true);
                } else {
                    // Jika belum diperbesar, perbesar gambar
                    photoAttacher.setScale(1.5f, true);
                }
            }
        });
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (bitMap != null) {
                    photoView.setImageBitmap(bitMap);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Wireless_input.this);
                    alertDialogBuilder.setMessage("Do yo want to take photo again?");

                    alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            TakePhoto();
                        }
                    });

                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                    alertDialog.show();


                } else {

                    TakePhoto();
                }

                photoAttacher.setOnViewTapListener(new OnViewTapListener() {
                    @Override
                    public void onViewTap(View view, float x, float y) {
                        // Respon ketika gambar diklik
                        showFullScreenImage(bitMap);
                    }
                });
            }
        });

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScanQR();
            }
        });

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hostname        = Hostname.getText().toString();
                merk            = Merk.getText().toString();
                serialnumber    = Serialnumber.getText().toString();
                ip              = Ip.getText().toString();
                department      = Department.getText().toString();
                lokasi          = Lokasi.getText().toString();
                tanggal         = Tanggal.getText().toString();
                keterangan      = Keterangan.getText().toString();

                if (bitMap == null && photoFile == null) {
                    bitMap = BitmapFactory.decodeResource(getResources(), R.drawable.buma);
                    photoView.setImageBitmap(bitMap);

                }
                validasiData();


            }
        });
    }

    private void showFullScreenImage(Bitmap bitmap) {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_fullscreen_image);

        PhotoView fullscreenPhotoView = dialog.findViewById(R.id.fullscreenPhotoView);
        fullscreenPhotoView.setImageBitmap(bitmap);

        fullscreenPhotoView.setOnViewTapListener(new OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    void validasiData() {
        hostname = Hostname.getText().toString();
        merk = Merk.getText().toString();
        serialnumber = Serialnumber.getText().toString();
        ip = Ip.getText().toString();
        department = Department.getText().toString();
        lokasi = Lokasi.getText().toString();
        tanggal = Tanggal.getText().toString();
        keterangan = Keterangan.getText().toString();


        if (!hostname.isEmpty() && !merk.isEmpty() && !ip.isEmpty() && !serialnumber.isEmpty() && !department.isEmpty() && !lokasi.isEmpty() && !tanggal.isEmpty() && !keterangan.isEmpty()) {
            kirimdata();

        } else {
            Hostname.setError("Masukkan Hostname!");
            Merk.setError("Masukkan Type / Merk!");
            Ip.setError("Masukkan IP!");
            Serialnumber.setError("Masukkan Serial Number!");
            Department.setError("Masukkan Department!");
            Lokasi.setError("Masukkan Lokasi!");
            Tanggal.setError("Masukkan Tanggal!");
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

        }
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

        }
    }

    public  void TakePhoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName);

        String authorities = getPackageName() + ".fileprovider";
        Uri fileProvider = FileProvider.getUriForFile(Wireless_input.this, authorities, photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_TAKE_PHOTO);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == Activity.RESULT_OK) {
                bitMap = decodeSampledBitmapFromFile(String.valueOf(photoFile), 1000, 700);
                photoView.setImageBitmap(bitMap);
            } else {
                Toast.makeText(Wireless_input.this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }

        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    if (photo != null) {
                        setToImageView(getResizedBitmap(photo, 512));
                    } else {
                        Toast.makeText(this, "Gambar dari kamera null", Toast.LENGTH_SHORT).show();
                    }
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Wireless_input.this);
                    alertDialogBuilder.setMessage("Do yo want to take photo again?");

                    alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            TakePhoto();
                        }
                    });

                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Handle the "No" case
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                }
                break;

            case IntentIntegrator.REQUEST_CODE:
                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                if (result != null) {
                    if (result.getContents() != null) {
                        Log.d("QR Code Result", result.getContents());
                        String[] qrCodeValues = result.getContents().split(",");

                        if (qrCodeValues.length >= 8) {
                            Hostname.setText(qrCodeValues[0]);
                            Merk.setText(qrCodeValues[1]);
                            Serialnumber.setText(qrCodeValues[2]);
                            Ip.setText(qrCodeValues[3]);
                            Department.setText(qrCodeValues[4]);
                            Lokasi.setText(qrCodeValues[5]);
                            Tanggal.setText(qrCodeValues[6]);
                            Keterangan.setText(qrCodeValues[7]);
                        } else {
                            Toast.makeText(this, "Format QR code tidak sesuai", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Scan dibatalkan atau hasil scan tidak valid", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Gagal mengambil hasil scan", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void setToImageView(Bitmap bmp) {
        if (bmp != null) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, bitmap_size, bytes);
            decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
            photoView.setImageBitmap(decoded);
            kirimdata();

        } else {
            Log.e("Error", "Bitmap is null");
        }
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

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight) {
            inSampleSize = Math.round((float) height / (float) reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
            inSampleSize = Math.round((float) width / (float) reqWidth);
        }

        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    public String getStringImage(Bitmap bmp){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
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

    public void ListenerScanQR(View v) {
        ScanQR();
    }

    private void ScanQR() {
        IntentIntegrator integrator = new IntentIntegrator(Wireless_input.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning . . .");
        integrator.addExtra("SCAN_WIDTH", 768);
        integrator.addExtra("SCAN_HEIGHT", 1024);

        integrator.setOrientationLocked(false);
        integrator.setCaptureActivity(VerticalCaptureActivity.class);
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }


    void kirimdata(){
        progressDialog.setMessage("Mengirim Data...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String foto = "";
        if (bitMap != null) {
            foto = getStringImage(bitMap);
        }
        AndroidNetworking.post("https://jdksmurf.com/BUMA/Api_wireless.php")
                .addBodyParameter("hostname",""+hostname)
                .addBodyParameter("merk",""+merk)
                .addBodyParameter("serialnumber",""+serialnumber)
                .addBodyParameter("ip",""+ip)
                .addBodyParameter("department",""+department)
                .addBodyParameter("lokasi",""+lokasi)
                .addBodyParameter("tanggal",""+tanggal)
                .addBodyParameter("keterangan",""+keterangan)
                .addBodyParameter("foto",""+foto)
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
                            Toast.makeText(Wireless_input.this, ""+pesan, Toast.LENGTH_SHORT).show();
                            Log.d("status",""+status);
                            if(status){
                                new AlertDialog.Builder(Wireless_input.this)
                                        .setMessage("DATA BERHASIL DISIMPAN")
                                        .setCancelable(false)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent i = new Intent(Wireless_input.this, NavigasiActivity.class);
                                                startActivity(i);
                                            }
                                        })
                                        .show();
                            }
                            else{
                                new AlertDialog.Builder(Wireless_input.this)
                                        .setMessage("Gagal Menambahkan Data !")
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent i = new Intent(Wireless_input.this, NavigasiActivity.class);
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

