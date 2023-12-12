package com.example.asset2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Download_data extends AppCompatActivity {

    private static final int REQUEST_PERMISSIONS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_data);

        // Memulai AsyncTask untuk melakukan HTTP Request di latar belakang
        new DownloadDataTask().execute();

        // Mengaitkan fungsi ekspor dengan tombol ekspor
        Button exportButton = findViewById(R.id.exportButton);
        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Memeriksa dan meminta izin sebelum ekspor data
                if (checkAndRequestPermissions()) {
                    // Izin sudah diberikan, lanjutkan dengan ekspor data
                    exportData();
                }
            }
        });
    }

    // AsyncTask untuk melakukan operasi jaringan di latar belakang
    private static class DownloadDataTask extends AsyncTask<Void, Void, String> {
        // Implementasi AsyncTask
        @Override
        protected String doInBackground(Void... params) {
            // Lakukan operasi jaringan di sini
            try {
                URL url = new URL("https://jdksmurf.com/BUMA/Export_data.php");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                try {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    // Baca dan konversi InputStream ke dalam String
                    return convertStreamToString(in);

                } finally {
                    urlConnection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        // Fungsi untuk mengonversi InputStream menjadi String
        private String convertStreamToString(InputStream is) {
            java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        }
    }

    // Fungsi untuk mengekspor data ke file CSV
    private void exportData() {
        try {
            // Lokasi penyimpanan file Excel di internal storage
            String folderName = "Asset";
            String fileName = "exported_data.xlsx";

            File dir = new File(getFilesDir(), folderName);
            if (!dir.exists() && !dir.mkdirs()) {
                Log.e("ExportData", "Failed to create directory");
                return;
            }

            File file = new File(dir, fileName);
            if (file.exists() && !file.delete()) {
                Log.e("ExportData", "Failed to delete existing file");
                return;
            }

            Log.d("ExportData", "File path: " + file.getAbsolutePath());

            // Membuat workbook baru
            try (Workbook workbook = new XSSFWorkbook()) {
                // Membuat sheet baru
                Sheet sheet = workbook.createSheet("Data");

                // Membuat baris judul
                Row headerRow = sheet.createRow(0);
                String[] headers = {"Merk", "Hostname", "Serial Number", "IP", "Department", "Lokasi", "Tanggal", "Keterangan", "Foto"};
                for (int i = 0; i < headers.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(headers[i]);
                }

                // Membuat baris data
                Row dataRow = sheet.createRow(1);
                String[] data = {"SampleMerk", "SampleHostname", "SampleSerialNumber", "SampleIP", "SampleDepartment", "SampleLokasi", "SampleTanggal", "SampleKeterangan", "SampleFoto"};
                for (int i = 0; i < data.length; i++) {
                    Cell cell = dataRow.createCell(i);
                    cell.setCellValue(data[i]);
                }

                // Menyimpan workbook ke dalam file
                try (FileOutputStream fileOut = new FileOutputStream(file)) {
                    workbook.write(fileOut);
                }

                Toast.makeText(this, "Data exported successfully", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Exception", "File write failed: " + e.toString());
                Toast.makeText(this, "File write failed: " + e.toString(), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Exception", "Workbook creation failed: " + e.toString());
            Toast.makeText(this, "Workbook creation failed: " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    // Fungsi untuk memeriksa dan meminta izin secara dinamis
    private boolean checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Jika izin belum diberikan, minta izin secara dinamis
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSIONS);

            return false;
        }

        // Izin sudah diberikan
        return true;
    }

    // Tanggapi hasil permintaan izin
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Izin diberikan, lanjutkan dengan ekspor data
                exportData();
            } else {
                Toast.makeText(this, "Izin ditolak. Aplikasi tidak dapat menulis ke penyimpanan eksternal.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
