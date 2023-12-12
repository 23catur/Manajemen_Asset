package com.example.asset2;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Download_data extends AppCompatActivity {

    private static final int REQUEST_PERMISSIONS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_data);

        // Mengaitkan fungsi ekspor dengan tombol ekspor
        Button exportButton = findViewById(R.id.exportButton);
        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Memeriksa dan meminta izin sebelum ekspor data
                if (checkAndRequestPermissions()) {
                    // Izin sudah diberikan, lanjutkan dengan ekspor data
                    new DownloadDataTask().execute();
                }
            }
        });
    }

    // AsyncTask untuk melakukan operasi jaringan di latar belakang
    private class DownloadDataTask extends AsyncTask<Void, Void, List<DataItem>> {

        @Override
        protected List<DataItem> doInBackground(Void... params) {
            try {
                URL url = new URL("https://jdksmurf.com/BUMA/Export_data.php");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                try {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    return convertStreamToData(in);

                } finally {
                    urlConnection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        private List<DataItem> convertStreamToData(InputStream is) {
            List<DataItem> dataList = new ArrayList<>();

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                try {
                    JSONArray jsonArray = new JSONArray(response.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        DataItem item = new DataItem();
                        item.setMerk(jsonObject.getString("merk"));
                        item.setHostname(jsonObject.getString("hostname"));
                        // Set properti lainnya sesuai kebutuhan
                        dataList.add(item);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return dataList;
        }

        protected void onPostExecute(List<DataItem> dataList) {
            if (dataList != null) {
                for (DataItem dataItem : dataList) {
                    Log.d("DataItem", "" + dataItem.getMerk());
                    Log.d("DataItem", "" + dataItem.getHostname());
                    // Log properti lainnya sesuai kebutuhan
                }
                exportData(dataList);
            } else {
                Toast.makeText(Download_data.this, "Gagal mengunduh data", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void exportData(List<DataItem> dataList) {
        String folderName = "ASSET IT";
        String fileName = "output.xlsx";

        if (isExternalStorageWritable()) {
            File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), folderName);

            if (!dir.exists() && !dir.mkdirs()) {
                Log.e("ExportData", "Failed to create directory");
                Toast.makeText(this, "Failed to create directory", Toast.LENGTH_SHORT).show();
                return;
            }

            File file = new File(dir, fileName);

            if (file.exists() && !file.delete()) {
                Log.e("ExportData", "Failed to delete existing file");
                Toast.makeText(this, "Failed to delete existing file", Toast.LENGTH_SHORT).show();
                return;
            }

            Log.d("ExportData", "File path: " + file.getAbsolutePath());

            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Data");

                Row headerRow = sheet.createRow(0);
                String[] headers = {"Merk", "Hostname" /*, Tambahkan properti lainnya */};
                for (int i = 0; i < headers.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(headers[i]);
                }

                for (int rowIndex = 0; rowIndex < dataList.size(); rowIndex++) {
                    Row dataRow = sheet.createRow(rowIndex + 1);
                    DataItem dataItem = dataList.get(rowIndex);

                    Cell cellMerk = dataRow.createCell(0);
                    cellMerk.setCellValue(dataItem.getMerk());

                    Cell cellHostname = dataRow.createCell(1);
                    cellHostname.setCellValue(dataItem.getHostname());
                    // Set properti lainnya sesuai kebutuhan
                }

                try (FileOutputStream fileOut = new FileOutputStream(file)) {
                    workbook.write(fileOut);
                    Toast.makeText(this, "Data exported successfully", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("ExportData", "File write failed: " + e.toString());
                    Toast.makeText(this, "File write failed: " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("ExportData", "Workbook creation failed: " + e.toString());
                Toast.makeText(this, "Workbook creation failed: " + e.toString(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "External storage not available", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private boolean checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != android.content.pm.PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSIONS);

            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                new DownloadDataTask().execute();
            } else {
                Log.d("ExportData", "Izin ditolak. Aplikasi tidak dapat menulis ke penyimpanan eksternal.");
                Toast.makeText(this, "Izin ditolak. Aplikasi tidak dapat menulis ke penyimpanan eksternal.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // src/main/java/com/example/asset2/DataItem.java
    public class DataItem {
        private String merk;
        private String hostname;

        public String getMerk() {
            return merk;
        }

        public void setMerk(String merk) {
            this.merk = merk;
        }

        public String getHostname() {
            return hostname;
        }

        public void setHostname(String hostname) {
            this.hostname = hostname;
        }
    }

}
