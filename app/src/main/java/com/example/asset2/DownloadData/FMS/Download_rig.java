package com.example.asset2.DownloadData.FMS;

import android.content.Intent;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.asset2.Listdata.FMS.Data_ht;
import com.example.asset2.Listdata.FMS.Data_rig;
import com.example.asset2.Listdata.Network.Data_cctv;
import com.example.asset2.R;

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

public class Download_rig extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_rig);

        // Memulai AsyncTask untuk mengunduh dan mengekspor data
        new DownloadDataTask().execute(
                "https://jdksmurf.com/BUMA/Export_rig.php",
                "HT.xlsx",
                "merk",
                "hostname",
                "serialnumber",
                "unit",
                "tanggal",
                "keterangan"
        );
    }

    private class DownloadDataTask extends AsyncTask<String, Void, List<DataItem>> {
        private String fileName;  // Tambahkan ini sebagai atribut kelas

        @Override
        protected List<DataItem> doInBackground(String... params) {
            String apiUrl = params[0];
            this.fileName = params[1];
            String merkKey = params[2];
            String hostnameKey = params[3];
            String serialnumberKey = params[4];
            String unitKey = params[5];
            String tanggalKey = params[6];
            String keteranganKey = params[7];

            try {
                URL url = new URL(apiUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                try {
                    int statusCode = urlConnection.getResponseCode();
                    if (statusCode == 200) {  // Status OK
                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                        return convertStreamToData(in, merkKey, hostnameKey, serialnumberKey, unitKey, tanggalKey, keteranganKey);
                    } else {
                        Log.e("DownloadDataTask", "HTTP error code: " + statusCode);
                        return null;
                    }
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("DownloadDataTask", "Error downloading data: " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        }

        private List<DataItem> convertStreamToData(InputStream is, String merkKey, String hostnameKey, String serialnumberKey, String unitKey, String tanggalKey, String keteranganKey) {
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
                        item.setMerk(jsonObject.getString(merkKey));
                        item.setHostname(jsonObject.getString(hostnameKey));
                        item.setSerialnumber(jsonObject.getString(serialnumberKey));
                        item.setUnit(jsonObject.getString(unitKey));
                        item.setTanggal(jsonObject.getString(tanggalKey));
                        item.setKeterangan(jsonObject.getString(keteranganKey));

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
                    Log.d("DataItem", "" + dataItem.getSerialnumber());
                    Log.d("DataItem", "" + dataItem.getUnit());
                    Log.d("DataItem", "" + dataItem.getTanggal());
                    Log.d("DataItem", "" + dataItem.getKeterangan());
                }

                // Export data
                exportData(dataList, this.fileName);

                // Pindah ke aktivitas Data_cctv setelah menyelesaikan tugas
                Intent intent = new Intent(Download_rig.this, Data_rig.class);
                startActivity(intent);
                finish(); // Menutup aktivitas saat ini agar tidak dapat dikembalikan dengan tombol "back"
            } else {
                Toast.makeText(Download_rig.this, "Gagal mengunduh data", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void exportData(List<DataItem> dataList, String fileName) {
        String folderName = "ASSET IT";

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
                String[] headers = {"MERK", "HOSTNAME", "SERIAL NUMBER", "UNIT", "TANGGAL", "KETERANGAN"};
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

                    Cell cellSerialnumber = dataRow.createCell(2);
                    cellSerialnumber.setCellValue(dataItem.getSerialnumber());

                    Cell cellUnit = dataRow.createCell(3);
                    cellUnit.setCellValue(dataItem.getUnit());

                    Cell cellTanggal = dataRow.createCell(4);
                    cellTanggal.setCellValue(dataItem.getTanggal());

                    Cell cellKeterangan = dataRow.createCell(5);
                    cellKeterangan.setCellValue(dataItem.getKeterangan());
                }

                try (FileOutputStream outputStream = new FileOutputStream(file)) {
                    workbook.write(outputStream);
                    Log.d("ExportData", "Data exported successfully");
                    Toast.makeText(this, "Data exported successfully", Toast.LENGTH_SHORT).show();
                    // Pindai file agar muncul di aplikasi pengelola file
                    MediaScannerConnection.scanFile(this, new String[]{file.getAbsolutePath()}, null, null);
                } catch (IOException e) {
                    Log.e("ExportData", "Error exporting data: " + e.getMessage());
                    Toast.makeText(this, "Error exporting data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                Log.e("ExportData", "Error creating workbook: " + e.getMessage());
                Toast.makeText(this, "Error creating workbook: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e("ExportData", "External storage not available");
            Toast.makeText(this, "External storage not available", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }


    public class DataItem {
        private String merk;
        private String hostname;
        private String serialnumber;
        private String unit;
        private String tanggal;
        private String keterangan;


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

        public String getSerialnumber() {
            return serialnumber;
        }

        public void setSerialnumber(String serialnumber) {
            this.serialnumber = serialnumber;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getTanggal() {
            return tanggal;
        }

        public void setTanggal(String tanggal) {
            this.tanggal = tanggal;
        }

        public String getKeterangan() {
            return keterangan;
        }

        public void setKeterangan(String keterangan) {
            this.keterangan = keterangan;
        }
    }
}