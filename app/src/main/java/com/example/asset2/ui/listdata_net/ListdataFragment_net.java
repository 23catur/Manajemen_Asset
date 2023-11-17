package com.example.asset2.ui.listdata_net;

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

public class ListdataFragment_net extends Fragment {

    private ListdataViewModel_net listdataViewModelNet;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ListdataViewModel_net listdataViewModelNet =
                new ViewModelProvider(this).get(ListdataViewModel_net.class);

        View root = inflater.inflate(R.layout.fragment_listdata_net, container, false);


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