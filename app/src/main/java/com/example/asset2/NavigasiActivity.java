package com.example.asset2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.asset2.DataInput.FMS.Dashboard_fms;
import com.example.asset2.DataInput.Network.Dashboard_net;
import com.example.asset2.Listdata.FMS.Dashboard_listdata_fms;
import com.example.asset2.Listdata.Network.Dashboard_listdata_net;
import com.example.asset2.Maintenance.Dashboard_maintenance;
import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.asset2.databinding.ActivityNavigasiBinding;

public class NavigasiActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityNavigasiBinding binding;
    ImageView btnnet, btnmainte, btnfms;
    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNavigasiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sessionManager = new SessionManager(this);

        btnnet = findViewById(R.id.btn_network);
        btnmainte = findViewById(R.id.btn_maintenance);
        btnfms = findViewById(R.id.btn_fms);

        setSupportActionBar(binding.appBarNavigasi.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_listdata_net, R.id.nav_listdata_fms, R.id.nav_download)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigasi);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();

                //Jika memilih navigasi home maka user diarahkan ke home
                if (id == R.id.nav_home) {

                    Intent NextPage;
                    NextPage = new Intent(NavigasiActivity.this, NavigasiActivity.class);
                    startActivity(NextPage);

                }
                else if (id == R.id.nav_listdata_net) {

                    Intent NextPage;
                    NextPage = new Intent(NavigasiActivity.this, Dashboard_listdata_net.class);
                    startActivity(NextPage);
                }
                else if (id == R.id.nav_listdata_fms) {

                    Intent NextPage;
                    NextPage = new Intent(NavigasiActivity.this, Dashboard_listdata_fms.class);
                    startActivity(NextPage);
                }

                else if (id == R.id.nav_download) {

                    Intent intent = new Intent(NavigasiActivity.this, Download_data.class);
                    startActivity(intent);
                    finish();
                }

                else if (id == R.id.nav_logout) {

                    Intent intent = new Intent(NavigasiActivity.this, Login.class);
                    startActivity(intent);
                    finish();
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });


        btnnet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NavigasiActivity.this, Dashboard_net.class);
                startActivity(intent);
            }
        });

        btnfms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NavigasiActivity.this, Dashboard_fms.class);
                startActivity(intent);
            }
        });

        btnmainte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tindakan yang akan dijalankan saat Tombol 2 ditekan
                Intent intent = new Intent(NavigasiActivity.this, Dashboard_maintenance.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigasi);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}