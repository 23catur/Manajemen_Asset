package com.example.asset2.DataInput.FMS;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import org.json.JSONObject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;


public class Mobile_input extends AppCompatActivity {

    Button btnDaftar, btnScan, btnPhoto;
    TextView Bumaasset, Serialnumber, Status, Keterangan;
    ImageView imageView;
    Bitmap bitMap = null;
    public final String APP_TAG = "MyApp";
    public String photoFileName = "photo.jpg";
    File photoFile;
    ProgressDialog progressDialog;
    String buma_asset, serialnumber, status, keterangan;
    Bitmap decoded;
    static final int REQUEST_TAKE_PHOTO = 1;
    int bitmap_size = 60; // range 1 - 100


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_mobile);

        btnDaftar = findViewById(R.id.btnSelesai);
        Bumaasset = findViewById(R.id.txtBumaasset);
        Serialnumber = findViewById(R.id.txtSerial);
        Status = findViewById(R.id.txtStatus);
        Keterangan = findViewById(R.id.txtKeterangan);
        btnScan = findViewById(R.id.btnScan);
        progressDialog = new ProgressDialog(this);

        btnPhoto = findViewById(R.id.btnPhoto);
        imageView = findViewById(R.id.imageView);

        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (bitMap != null) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Mobile_input.this);
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
                    alertDialog.show();

                } else {

                    TakePhoto();
                }
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

                buma_asset      = Bumaasset.getText().toString();
                serialnumber    = Serialnumber.getText().toString();
                status          = Status.getText().toString();
                keterangan      = Keterangan.getText().toString();

                if (bitMap == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Mobile_input.this);
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
        if (!buma_asset.isEmpty() && !serialnumber.isEmpty() && !status.isEmpty() && !keterangan.isEmpty()){
            kirimdata();
        }else{
            Bumaasset.setError("Masukkan Buma Asset!");
            Serialnumber.setError("Masukkan Serial Number!");
            Status.setError("Masukkan Status!");
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
        Uri fileProvider = FileProvider.getUriForFile(Mobile_input.this, authorities, photoFile);
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
                imageView.setImageBitmap(bitMap);
            } else {
                Toast.makeText(Mobile_input.this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }

        switch (requestCode) {
            case IntentIntegrator.REQUEST_CODE:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                if (result != null) {
                    if (result.getContents() != null) {
                        Bumaasset.setText(result.getContents());
                        Serialnumber.setText(result.getContents());
                        Status.setText(result.getContents());
                        Keterangan.setText(result.getContents());
                    } else {
                        Toast.makeText(this, "Scan dibatalkan atau hasil scan tidak valid", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Gagal mengambil hasil scan", Toast.LENGTH_SHORT).show();
                }
                break;
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

    private void setToImageView(Bitmap bmp) {
        if (bmp != null) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, bitmap_size, bytes);
            decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

            imageView.setImageBitmap(decoded);

        } else {
            Log.e("Error", "Bitmap is null");
        }
    }

    public void ListenerScanQR(View v) {
        ScanQR();
    }

    private void ScanQR() {
        IntentIntegrator integrator = new IntentIntegrator(Mobile_input.this);
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
        String foto = getStringImage(bitMap);
        AndroidNetworking.post("https://jdksmurf.com/BUMA/Api_mobile.php")
                .addBodyParameter("buma_asset",""+buma_asset)
                .addBodyParameter("serialnumber",""+serialnumber)
                .addBodyParameter("status",""+status)
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
                            Toast.makeText(Mobile_input.this, ""+pesan, Toast.LENGTH_SHORT).show();
                            Log.d("status",""+status);
                            if(status){
                                new AlertDialog.Builder(Mobile_input.this)
                                        .setMessage("DATA BERHASIL DISIMPAN")
                                        .setCancelable(false)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent i = new Intent(Mobile_input.this, NavigasiActivity.class);
                                                startActivity(i);
                                            }
                                        })
                                        .show();
                            }
                            else{
                                new AlertDialog.Builder(Mobile_input.this)
                                        .setMessage("Gagal Menambahkan Data !")
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent i = new Intent(Mobile_input.this, NavigasiActivity.class);
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

