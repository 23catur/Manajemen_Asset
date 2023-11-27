package com.example.asset2.Listdata.Network;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.asset2.R;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class Data_komputer extends AppCompatActivity {

    ArrayList<String>  array_hostname, array_merk, array_serialnumber, array_user, array_tanggal, array_keterangan,array_foto, array_department, array_lokasi;
    SwipeRefreshLayout srl_main;
    ProgressDialog progressDialog;
    SearchView searchView;
    CLV_komputer adapter;
    ListView listData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_komputer);

        listData = findViewById(R.id.LV_komputer);
        srl_main = findViewById(R.id.swipe_container);
        progressDialog = new ProgressDialog(this);

        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
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
        srl_main.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)

        );

        initializeArray();
        adapter = new CLV_komputer(Data_komputer.this, array_hostname, array_merk, array_serialnumber, array_user, array_tanggal, array_keterangan, array_foto, array_department, array_lokasi);
        listData.setAdapter(adapter);

        scrollRefresh();
    }

    public  void  scrollRefresh(){
        getData();
    }

    void initializeArray(){
        array_hostname      = new ArrayList<String>();
        array_merk          = new ArrayList<String>();
        array_serialnumber  = new ArrayList<String>();
        array_user            = new ArrayList<String>();
        array_tanggal       = new ArrayList<String>();
        array_keterangan    = new ArrayList<String>();
        array_foto          = new ArrayList<String>();
        array_department    = new ArrayList<String>();
        array_lokasi        = new ArrayList<String>();

        array_hostname.clear();
        array_merk.clear();
        array_serialnumber.clear();
        array_user.clear();
        array_tanggal.clear();
        array_keterangan.clear();
        array_foto.clear();
        array_department.clear();
        array_lokasi.clear();

    }

    public void getData(){
        initializeArray();
        AndroidNetworking.post("https://jdksmurf.com/BUMA/getdata_komputer.php")
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

                                    array_hostname.add(jo.getString("hostname"));
                                    array_merk.add(jo.getString("merk"));
                                    array_serialnumber.add(jo.getString("serialnumber"));
                                    array_user.add(jo.getString("user"));
                                    array_department.add(jo.getString("department"));
                                    array_lokasi.add(jo.getString("lokasi"));
                                    array_tanggal.add(jo.getString("tanggal"));
                                    array_keterangan.add(jo.getString("keterangan"));
                                    array_foto.add(jo.getString("foto"));

                                }

                                if (adapter == null) {
                                    adapter = new CLV_komputer(Data_komputer.this,array_hostname,array_merk,array_serialnumber,array_user,array_department,array_lokasi,array_tanggal,array_keterangan,array_foto);
                                    listData.setAdapter(adapter);
                                } else {
                                    adapter.updateData(array_hostname, array_merk, array_serialnumber,array_user,array_department,array_lokasi,array_tanggal,array_keterangan,array_foto);
                                    adapter.notifyDataSetChanged();
                                }

                            }else{
                                Toast.makeText(Data_komputer.this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorCode() != 0) {
                            Log.e("Error", "onError: " + anError.getErrorDetail());
                            Toast.makeText(Data_komputer.this, "Error: " + anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("Error", "onError: " + anError.getErrorDetail());
                            Toast.makeText(Data_komputer.this, "Error: " + anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                        }
                    }

                });
    }
}