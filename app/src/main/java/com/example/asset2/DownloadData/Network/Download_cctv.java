package com.example.asset2.DownloadData.Network;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.media.MediaScannerConnection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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

public class Download_cctv extends AppCompatActivity {

    private static final int REQUEST_PERMISSIONS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_download);

        ImageView exportButtonCCTV = findViewById(R.id.btn_cctv);
        exportButtonCCTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAndRequestPermissions()) {
                    new DownloadDataTask().execute(
                            "https://jdksmurf.com/BUMA/Export_cctv.php",
                            "CCTV.xlsx",
                            "merk",
                            "hostname",
                            "serialnumber",
                            "ip",
                            "department",
                            "lokasi",
                            "tanggal",
                            "keterangan"
                    );
                }
            }
        });

        ImageView exportButtonKomputer = findViewById(R.id.btn_komputer);
        exportButtonKomputer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAndRequestPermissions()) {
                    new DownloadDataTask().execute(
                            "https://jdksmurf.com/BUMA/Export_komputer.php",
                            "Komputer.xlsx",
                            "merk",
                            "hostname",
                            "serialnumber",
                            "user",
                            "department",
                            "lokasi",
                            "tanggal",
                            "keterangan"
                    );
                }
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSIONS);
        }

        new DownloadDataTask().execute(
                "https://jdksmurf.com/BUMA/Export_cctv.php",
                "CCTV.xlsx",
                "merk",
                "hostname",
                "serialnumber",
                "ip",
                "department",
                "lokasi",
                "tanggal",
                "keterangan"
        );
        new DownloadDataTask().execute(
                "https://jdksmurf.com/BUMA/Export_komputer.php",
                "Komputer.xlsx",
                "merk",
                "hostname",
                "serialnumber",
                "user",
                "department",
                "lokasi",
                "tanggal",
                "keterangan"
        );

    }

    private boolean checkAndRequestPermissions() {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        List<String> permissionsNeeded = new ArrayList<>();

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(permission);
            }
        }

        if (!permissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    permissionsNeeded.toArray(new String[0]),
                    REQUEST_PERMISSIONS);
            return false;
        }

        return true;
    }

//    private class DownloadDataTask extends AsyncTask<Void, Void, List<DataItem>> {
//
//        @Override
//        protected List<DataItem> doInBackground(Void... params) {
//            try {
//                URL url = new URL("https://jdksmurf.com/BUMA/Export_cctv.php");
//                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//
//                try {
//                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
//                    return convertStreamToData(in);
//
//                } finally {
//                    urlConnection.disconnect();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//                return null;
//            }
//        }
//
//        private List<DataItem> convertStreamToData(InputStream is) {
//            List<DataItem> dataList = new ArrayList<>();
//
//            try {
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//                StringBuilder response = new StringBuilder();
//                String line;
//
//                while ((line = reader.readLine()) != null) {
//                    response.append(line);
//                }
//
//                try {
//                    JSONArray jsonArray = new JSONArray(response.toString());
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        DataItem item = new DataItem();
//                        item.setMerk(jsonObject.getString("merk"));
//                        item.setHostname(jsonObject.getString("hostname"));
//                        item.setSerialnumber(jsonObject.getString("serialnumber"));
//                        item.setIp(jsonObject.getString("ip"));
//                        item.setDepartment(jsonObject.getString("department"));
//                        item.setLokasi(jsonObject.getString("lokasi"));
//                        item.setTanggal(jsonObject.getString("tanggal"));
//                        item.setKeterangan(jsonObject.getString("keterangan"));
//                        dataList.add(item);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            return dataList;
//        }
//
//        protected void onPostExecute(List<DataItem> dataList) {
//            if (dataList != null) {
//                for (DataItem dataItem : dataList) {
//                    Log.d("DataItem", "" + dataItem.getMerk());
//                    Log.d("DataItem", "" + dataItem.getHostname());
//                    Log.d("DataItem", "" + dataItem.getSerialnumber());
//                    Log.d("DataItem", "" + dataItem.getIp());
//                    Log.d("DataItem", "" + dataItem.getDepartment());
//                    Log.d("DataItem", "" + dataItem.getLokasi());
//                    Log.d("DataItem", "" + dataItem.getTanggal());
//                    Log.d("DataItem", "" + dataItem.getKeterangan());
//
//                }
//                exportData(dataList);
//            } else {
//                Toast.makeText(Download_cctv.this, "Gagal mengunduh data", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    private class DownloadDataTask extends AsyncTask<String, Void, List<DataItem>> {
        private String fileName;  // Tambahkan ini sebagai atribut kelas

        @Override
        protected List<DataItem> doInBackground(String... params) {
            try {
                String apiUrl = params[0];
                this.fileName = params[1];
                String merkKey = params[2];
                String hostnameKey = params[3];
                String serialnumberKey = params[4];
                String ipKey = params[5];
                String userKey = params[6];
                String departmentKey = params[7];
                String lokasiKey = params[8];
                String tanggalKey = params[9];
                String keteranganKey = params[10];

                URL url = new URL(apiUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                try {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    return convertStreamToData(in, merkKey, hostnameKey, serialnumberKey, ipKey, userKey, departmentKey, lokasiKey, tanggalKey, keteranganKey);
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }


        private List<DataItem> convertStreamToData(InputStream is, String merkKey, String hostnameKey, String serialnumberKey, String ipKey, String userKey, String departmentKey, String lokasiKey, String tanggalKey, String keteranganKey) {
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
                        item.setIp(jsonObject.getString(ipKey));
                        item.setDepartment(jsonObject.getString(departmentKey));
                        item.setLokasi(jsonObject.getString(lokasiKey));
                        item.setTanggal(jsonObject.getString(tanggalKey));
                        item.setKeterangan(jsonObject.getString(keteranganKey));

                        // Cek apakah ini data untuk komputer yang memiliki variabel user
                        if (userKey != null && jsonObject.has(userKey)) {
                            item.setUser(jsonObject.getString(userKey));
                        }

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
                    Log.d("DataItem", "" + dataItem.getIp());
                    Log.d("DataItem", "" + dataItem.getDepartment());
                    Log.d("DataItem", "" + dataItem.getLokasi());
                    Log.d("DataItem", "" + dataItem.getTanggal());
                    Log.d("DataItem", "" + dataItem.getKeterangan());
                }
                exportData(dataList, this.fileName);  // Gunakan this.fileName
            } else {
                Toast.makeText(Download_cctv.this, "Gagal mengunduh data", Toast.LENGTH_SHORT).show();
            }
        }
    }

        private void exportData(List<DataItem> dataList, String fileName) {
        String folderName = "ASSET IT";
//        String fileName = "CCTV.xlsx";

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
                String[] headers = {"MERK", "HOSTNAME", "SERIAL NUMBER", "IP", "DEPARTMENT", "LOKASI", "TANGGAL", "KETERANGAN"};
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

                    Cell cellIp = dataRow.createCell(3);
                    cellIp.setCellValue(dataItem.getIp());

                    Cell cellDepartment = dataRow.createCell(4);
                    cellDepartment.setCellValue(dataItem.getDepartment());

                    Cell cellUser = dataRow.createCell(5);
                    cellUser.setCellValue(dataItem.getUser());

                    Cell cellLokasi = dataRow.createCell(6);
                    cellLokasi.setCellValue(dataItem.getLokasi());

                    Cell cellTanggal = dataRow.createCell(7);
                    cellTanggal.setCellValue(dataItem.getTanggal());

                    Cell cellKeterangan = dataRow.createCell(8);
                    cellKeterangan.setCellValue(dataItem.getKeterangan());

                    Cell cellFoto = dataRow.createCell(9);
                    cellFoto.setCellValue(dataItem.getFoto());
                }

                try (FileOutputStream fileOut = new FileOutputStream(file)) {
                    workbook.write(fileOut);
                    Toast.makeText(this, "Data exported successfully", Toast.LENGTH_SHORT).show();

                    MediaScannerConnection.scanFile(
                            this,
                            new String[]{file.getAbsolutePath()},
                            null,
                            (path, uri) -> {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> uri=" + uri);
                            }
                    );

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new DownloadDataTask().execute();
            } else {
                Log.d("ExportData", "Izin ditolak. Aplikasi tidak dapat menulis ke penyimpanan eksternal.");
                Toast.makeText(this, "Izin ditolak. Aplikasi tidak dapat menulis ke penyimpanan eksternal.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public class DataItem {
        private String merk;
        private String hostname;
        private String serialnumber;
        private String ip;
        private String user;
        private String department;
        private String lokasi;
        private String tanggal;
        private String keterangan;
        private String foto;


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

        public String getIp() {
            return ip;
        }
        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getUser() {
            return user;
        }
        public void setUser(String user) {
            this.user = user;
        }

        public String getDepartment() {
            return department;
        }
        public void setDepartment(String department) {
            this.department = department;
        }

        public String getLokasi() {
            return lokasi;
        }
        public void setLokasi(String lokasi) {
            this.lokasi = lokasi;
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

        public String getFoto() {
            return foto;
        }
        public void setFoto(String foto) {
            this.foto = foto;
        }
    }
}
