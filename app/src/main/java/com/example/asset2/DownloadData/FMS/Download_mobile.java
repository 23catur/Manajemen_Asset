package com.example.asset2.DownloadData.FMS;

import android.content.Intent;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.asset2.Listdata.FMS.Data_mobile;
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

public class Download_mobile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_mobile);

        new DownloadDataTask().execute(
                "https://jdksmurf.com/BUMA/Export_mobile.php",
                "MOBILE.xlsx",
                "buma_asset",
                "serialnumber",
                "status",
                "keterangan"
        );
    }

    private class DownloadDataTask extends AsyncTask<String, Void, List<DataItem>> {
        private String fileName;

        @Override
        protected List<DataItem> doInBackground(String... params) {
            String apiUrl = params[0];
            this.fileName = params[1];
            String buma_assetKey = params[2];
            String serialnumberKey = params[3];
            String statusKey = params[4];
            String keteranganKey = params[5];

            try {
                URL url = new URL(apiUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                try {
                    int statusCode = urlConnection.getResponseCode();
                    if (statusCode == 200) {
                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                        return convertStreamToData(in, buma_assetKey, serialnumberKey, statusKey, keteranganKey);
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

        private List<DataItem> convertStreamToData(InputStream is, String buma_assetKey, String serialnumberKey, String statusKey, String keteranganKey) {
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
                        item.setBuma_asset(jsonObject.getString(buma_assetKey));
                        item.setSerialnumber(jsonObject.getString(serialnumberKey));
                        item.setStatus(jsonObject.getString(statusKey));
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
                    Log.d("DataItem", "" + dataItem.getBuma_asset());
                    Log.d("DataItem", "" + dataItem.getSerialnumber());
                    Log.d("DataItem", "" + dataItem.getStatus());
                    Log.d("DataItem", "" + dataItem.getKeterangan());
                }

                exportData(dataList, this.fileName);

                Intent intent = new Intent(Download_mobile.this, Data_mobile.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(Download_mobile.this, "Gagal mengunduh data", Toast.LENGTH_SHORT).show();
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
                String[] headers = {"BUMA ASSET", "SERIAL NUMBER", "STATUS", "KETERANGAN"};
                for (int i = 0; i < headers.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(headers[i]);
                }

                for (int rowIndex = 0; rowIndex < dataList.size(); rowIndex++) {
                    Row dataRow = sheet.createRow(rowIndex + 1);
                    DataItem dataItem = dataList.get(rowIndex);

                    Cell cellBuma_asset = dataRow.createCell(0);
                    cellBuma_asset.setCellValue(dataItem.getBuma_asset());

                    Cell cellSerialnumber = dataRow.createCell(1);
                    cellSerialnumber.setCellValue(dataItem.getSerialnumber());

                    Cell cellStatus = dataRow.createCell(2);
                    cellStatus.setCellValue(dataItem.getStatus());

                    Cell cellKeterangan = dataRow.createCell(3);
                    cellKeterangan.setCellValue(dataItem.getKeterangan());
                }

                try (FileOutputStream outputStream = new FileOutputStream(file)) {
                    workbook.write(outputStream);
                    Log.d("ExportData", "Data exported successfully");
                    Toast.makeText(this, "Data exported successfully", Toast.LENGTH_SHORT).show();
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
        private String buma_asset;
        private String serialnumber;
        private String status;
        private String keterangan;

        public String getBuma_asset() {
            return buma_asset;
        }

        public void setBuma_asset(String buma_asset) {
            this.buma_asset = buma_asset;
        }

        public String getSerialnumber() {
            return serialnumber;
        }

        public void setSerialnumber(String serialnumber) {
            this.serialnumber = serialnumber;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getKeterangan() {
            return keterangan;
        }

        public void setKeterangan(String keterangan) {
            this.keterangan = keterangan;
        }
    }
}