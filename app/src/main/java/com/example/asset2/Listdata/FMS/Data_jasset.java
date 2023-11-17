package com.example.asset2.Listdata.FMS;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.asset2.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Data_jasset extends AppCompatActivity {

    ArrayList<String> array_serialnumber, array_status, array_keterangan;
    SwipeRefreshLayout srl_main;
    ProgressDialog progressDialog;
    SearchView searchView;
    CLV_jasset adapter;
//    EditText inputsearch;
    ListView listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_jasset);

        listData = findViewById(R.id.LV_jasset);
        srl_main = findViewById(R.id.swipe_container);
        progressDialog = new ProgressDialog(this);
//        searchView = findViewById(R.id.searchView);
//        inputsearch = findViewById(R.id.searchView);
        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Update adapter with the filtered data
                if (adapter != null) {
                    adapter.getFilter().filter(newText);
                }
                return true;
            }
        });

        srl_main.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                scrollRefresh();
                srl_main.setRefreshing(false);
            }
        });
        // Scheme colors for animation
        srl_main.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)

        );

        initializeArray();
        adapter = new CLV_jasset(Data_jasset.this, array_serialnumber, array_status, array_keterangan);
        listData.setAdapter(adapter);

//        setupSearchView();

        scrollRefresh();

    }

//    private void setupSearchView() {
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                // Update adapter with the filtered data
//                if (adapter != null) {
//                    adapter.getFilter().filter(newText);
//                }
//                return true;
//            }
//        });
//    }

    public  void  scrollRefresh(){
        getData();
    }

    void initializeArray(){
        array_serialnumber = new ArrayList<String>();
        array_status = new ArrayList<String>();
        array_keterangan = new ArrayList<String>();

        //clear
        array_serialnumber.clear();
        array_status.clear();
        array_keterangan.clear();
    }

    public void getData(){
        initializeArray();
        //URL
        AndroidNetworking.post("https://jdksmurf.com/BUMA/getdata_jasset.php")
                .setTag("Get Data")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Boolean status = response.getBoolean("status");
                            if(status){
                                JSONArray ja = response.getJSONArray("result");
                                Log.d("respon",""+ja);
                                for(int i = 0 ; i < ja.length() ; i++){
                                    JSONObject jo = ja.getJSONObject(i);

                                    array_serialnumber.add(jo.getString("serialnumber"));
                                    array_status.add(jo.getString("status"));
                                    array_keterangan.add(jo.getString("keterangan"));
                                }

                                // Jika adapter belum ada, inisialisasi
                                if (adapter == null) {
                                    adapter = new CLV_jasset(Data_jasset.this, array_serialnumber, array_status, array_keterangan);
                                    listData.setAdapter(adapter);
                                } else {
                                    // Jika adapter sudah ada, update datanya
                                    adapter.updateData(array_serialnumber, array_status, array_keterangan);
                                    // Memberi tahu adapter bahwa dataset telah berubah
                                    adapter.notifyDataSetChanged();
                                }

                            }else{
                                Toast.makeText(Data_jasset.this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        // Handle error, for example:
                        if (anError.getErrorCode() != 0) {
                            Log.e("Error", "onError: " + anError.getErrorDetail());
                            Toast.makeText(Data_jasset.this, "Error: " + anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("Error", "onError: " + anError.getErrorDetail());
                            Toast.makeText(Data_jasset.this, "Error: " + anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                        }
                    }

                });
    }
}