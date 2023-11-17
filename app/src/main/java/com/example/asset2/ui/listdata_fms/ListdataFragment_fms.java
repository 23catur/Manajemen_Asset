package com.example.asset2.ui.listdata_fms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.asset2.R;

public class ListdataFragment_fms extends Fragment {

    private ListdataViewModel_fms listdataViewModelNet;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ListdataViewModel_fms listdataViewModelNet =
                new ViewModelProvider(this).get(ListdataViewModel_fms.class);

        View root = inflater.inflate(R.layout.fragment_listdata_fms, container, false);


        final TextView textView = root.findViewById(R.id.text_gallery);
        listdataViewModelNet.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

}