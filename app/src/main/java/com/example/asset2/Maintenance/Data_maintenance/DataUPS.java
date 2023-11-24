package com.example.asset2.Maintenance.Data_maintenance;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.example.asset2.R;

public class DataUPS extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_ups);


    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
//        switch(view.getId()) {
//            case R.id.checkbox_1:
//                if (checked) {
//                    // Put some meat on the sandwich
//                    // Misalnya, tampilkan pesan "Tambahkan daging ke sandwich" atau jalankan kode yang sesuai.
//                } else {
//                    // Remove the meat
//                    // Misalnya, tampilkan pesan "Hilangkan daging dari sandwich" atau jalankan kode yang sesuai.
//                }
//                break;
//            case R.id.checkbox_11:
//                if (checked) {
//                    // Cheese me
//                    // Misalnya, tampilkan pesan "Tambahkan keju" atau jalankan kode yang sesuai.
//                } else {
//                    // I'm lactose intolerant
//                    // Misalnya, tampilkan pesan "Saya intoleran terhadap laktosa" atau jalankan kode yang sesuai.
//                }
//                break;
//        }
    }

}