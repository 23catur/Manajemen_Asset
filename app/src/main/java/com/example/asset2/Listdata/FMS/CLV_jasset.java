package com.example.asset2.Listdata.FMS;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.asset2.R;
import com.example.asset2.Updatedata.FMS.Update_jasset;
import com.example.asset2.Updatedata.Network.Update_wireless;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CLV_jasset extends ArrayAdapter<String> implements Filterable {

    final Activity context;

    private ArrayList<String> vSerialnumber;
    private ArrayList<String> vStatus;
    private ArrayList<String> vKeterangan;
    private ArrayList<String> vFoto;
    private ValueFilter valueFilter;
    private ArrayList<String> originalData;

    public CLV_jasset(Activity context, ArrayList<String> Serialnumber, ArrayList<String> Status, ArrayList<String> Keterangan, ArrayList<String> Foto) {
        super(context, R.layout.clv_jasset, Serialnumber);
        this.context = context;
        this.vSerialnumber = Serialnumber;
        this.vStatus = Status;
        this.vKeterangan = Keterangan;
        this.vFoto          = Foto;

    }

    public void updateData(ArrayList<String> Serialnumber, ArrayList<String> Status, ArrayList<String> Keterangan, ArrayList<String> Foto) {
        vSerialnumber.clear();
        vStatus.clear();
        vKeterangan.clear();
        vFoto.clear();

        vSerialnumber.addAll(Serialnumber);
        vStatus.addAll(Status);
        vKeterangan.addAll(Keterangan);
        vFoto.addAll(Foto);

        originalData = new ArrayList<>(vSerialnumber);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.clv_jasset, null, true);

        TextView serialnumber   = rowView.findViewById(R.id.tvSerial);
        TextView status         = rowView.findViewById(R.id.tvStatus);
        TextView keterangan     = rowView.findViewById(R.id.tvKeterangan);
        ImageView foto          = rowView.findViewById(R.id.fotoAsset);

        serialnumber.setText(vSerialnumber.get(position));
        status.setText(vStatus.get(position));
        keterangan.setText(vKeterangan.get(position));

        CardView update = rowView.findViewById(R.id.cvJasset);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, Update_jasset.class);
                intent.putExtra("serialnumber", vSerialnumber.get(position));
                intent.putExtra("status", vStatus.get(position));
                intent.putExtra("keterangan", vKeterangan.get(position));
                intent.putExtra("foto", vFoto.get(position));

                context.startActivity(intent);
            }
        });

        if (!vFoto.get(position).equals("")) {
            Picasso.get().load("https://jdksmurf.com/BUMA/foto_asset/"+vFoto.get(position)).into(foto);
        } else {
            Picasso.get().load(R.drawable.buma1).into(foto);
        }
        return rowView;
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (originalData == null) {
                originalData = new ArrayList<>(vSerialnumber);
            }

            try {
                if (vSerialnumber != null && vStatus != null && vKeterangan != null) {
                    if (constraint != null && constraint.length() > 0) {
                        ArrayList<String> filteredListSerialNumber = new ArrayList<>();
                        ArrayList<String> filteredListStatus = new ArrayList<>();
                        ArrayList<String> filteredListKeterangan = new ArrayList<>();

                        for (int i = 0; i < originalData.size(); i++) {
                            if (i < vSerialnumber.size() && i < vStatus.size() && i < vKeterangan.size()) {
                                String serialNumber = originalData.get(i);
                                String status = vStatus.get(i);
                                String keterangan = vKeterangan.get(i);

                                if (serialNumber != null && status != null && keterangan != null) {
                                    if (serialNumber.toLowerCase().contains(constraint.toString().toLowerCase()) ||
                                            status.toLowerCase().contains(constraint.toString().toLowerCase()) ||
                                            keterangan.toLowerCase().contains(constraint.toString().toLowerCase())) {
                                        filteredListSerialNumber.add(serialNumber);
                                        filteredListStatus.add(status);
                                        filteredListKeterangan.add(keterangan);
                                    }
                                }
                            }
                        }

                        results.count = filteredListSerialNumber.size();
                        results.values = new FilterResultModel(
                                filteredListSerialNumber, filteredListStatus, filteredListKeterangan);
                    } else {
                        results.count = originalData.size();
                        results.values = new FilterResultModel(
                                new ArrayList<>(vSerialnumber), new ArrayList<>(vStatus), new ArrayList<>(vKeterangan));
                    }
                } else {
                    results.count = 0;
                    results.values = new ArrayList<>();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            try {
                if (results.values != null) {
                    FilterResultModel filterResultModel = (FilterResultModel) results.values;
                    vSerialnumber.clear();
                    vSerialnumber.addAll(filterResultModel.getFilteredSerialNumbers());
                    vStatus.clear();
                    vStatus.addAll(filterResultModel.getFilteredStatus());
                    vKeterangan.clear();
                    vKeterangan.addAll(filterResultModel.getFilteredKeterangan());

                    originalData = new ArrayList<>(vSerialnumber);

                    notifyDataSetChanged();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private static class FilterResultModel {
        private final List<String> filteredSerialNumbers;
        private final List<String> filteredStatus;
        private final List<String> filteredKeterangan;

        public FilterResultModel(List<String> filteredSerialNumbers, List<String> filteredStatus, List<String> filteredKeterangan) {
            this.filteredSerialNumbers = filteredSerialNumbers;
            this.filteredStatus = filteredStatus;
            this.filteredKeterangan = filteredKeterangan;
        }

        public List<String> getFilteredSerialNumbers() {
            return filteredSerialNumbers;
        }
        public List<String> getFilteredStatus() {
            return filteredStatus;
        }
        public List<String> getFilteredKeterangan() {
            return filteredKeterangan;
        }
    }

}
