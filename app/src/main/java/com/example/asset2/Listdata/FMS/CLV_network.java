package com.example.asset2.Listdata.FMS;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import com.example.asset2.R;
import com.example.asset2.Updatedata.FMS.Update_network;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class CLV_network extends ArrayAdapter<String> {

    final Activity context;

    private  ArrayList<String> vBumaasset;
    private  ArrayList<String> vSerialnumber;
    private  ArrayList<String> vStatus;
    private  ArrayList<String> vKeterangan;
    private  ArrayList<String> vFoto;
    private ValueFilter valueFilter;
    private ArrayList<String> originalData;


    public CLV_network(Activity context, ArrayList<String> Bumaasset, ArrayList<String> Serialnumber, ArrayList<String> Status, ArrayList<String> Keterangan, ArrayList<String> Foto) {
        super(context, R.layout.clv_network, Bumaasset);
        this.context        = context;
        this.vBumaasset     = Bumaasset;
        this.vSerialnumber  = Serialnumber;
        this.vStatus        = Status;
        this.vKeterangan    = Keterangan;
        this.vFoto          = Foto;
    }

    public void updateData(ArrayList<String> Bumaasset, ArrayList<String> Serialnumber, ArrayList<String> Status, ArrayList<String> Keterangan, ArrayList<String> Foto) {
        vBumaasset.clear();
        vSerialnumber.clear();
        vStatus.clear();
        vKeterangan.clear();
        vFoto.clear();

        vBumaasset.addAll(Bumaasset);
        vSerialnumber.addAll(Serialnumber);
        vStatus.addAll(Status);
        vKeterangan.addAll(Keterangan);
        vFoto.addAll(Foto);

        originalData = new ArrayList<>(vBumaasset);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.clv_network, null, true);

        TextView buma_asset         = rowView.findViewById(R.id.tvBumaasset);
        TextView serialnumber       = rowView.findViewById(R.id.tvSerial);
        TextView status             = rowView.findViewById(R.id.tvStatus);
        TextView keterangan         = rowView.findViewById(R.id.tvKeterangan);
        ImageView foto              = rowView.findViewById(R.id.fotoAsset);

        CardView update = rowView.findViewById(R.id.cvNetwork);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Update_network.class);
                intent.putExtra("buma_asset", vBumaasset.get(position));
                intent.putExtra("serialnumber", vSerialnumber.get(position));
                intent.putExtra("status", vStatus.get(position));
                intent.putExtra("keterangan", vKeterangan.get(position));
                intent.putExtra("foto", vFoto.get(position));

                context.startActivity(intent);
            }
        });

        buma_asset.setText(vBumaasset.get(position));
        serialnumber.setText(vSerialnumber.get(position));
        status.setText(vStatus.get(position));
        keterangan.setText(vKeterangan.get(position));

        if (!vFoto.get(position).equals("")) {

            Picasso.get().load("https://jdksmurf.com/BUMA/foto_asset/"+vFoto.get(position)).into(foto);

        } else {
            Picasso.get().load("https://jdksmurf.com/BUMA/foto_asset/BUMA.png").into(foto);
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
                originalData = new ArrayList<>(vBumaasset);
            }

            try {
                if (vBumaasset != null && vSerialnumber != null && vStatus != null && vKeterangan != null) {
                    if (constraint != null && constraint.length() > 0) {
                        ArrayList<String> filteredListBumaasset = new ArrayList<>();
                        ArrayList<String> filteredListSerialNumber = new ArrayList<>();
                        ArrayList<String> filteredListStatus = new ArrayList<>();
                        ArrayList<String> filteredListKeterangan = new ArrayList<>();

                        for (int i = 0; i < originalData.size(); i++) {
                            if (i < vBumaasset.size() && i < vSerialnumber.size() && i < vStatus.size() && i < vKeterangan.size()) {
                                String bumaasset = originalData.get(i);
                                String serialNumber = vSerialnumber.get(i);
                                String status = vStatus.get(i);
                                String keterangan = vKeterangan.get(i);

                                if (bumaasset != null && serialNumber != null && status != null && keterangan != null) {
                                    if (bumaasset.toLowerCase().contains(constraint.toString().toLowerCase()) ||
                                            serialNumber.toLowerCase().contains(constraint.toString().toLowerCase()) ||
                                            status.toLowerCase().contains(constraint.toString().toLowerCase()) ||
                                            keterangan.toLowerCase().contains(constraint.toString().toLowerCase())) {
                                        filteredListBumaasset.add(bumaasset);
                                        filteredListSerialNumber.add(serialNumber);
                                        filteredListStatus.add(status);
                                        filteredListKeterangan.add(keterangan);
                                    }
                                }
                            }
                        }

                        results.count = filteredListBumaasset.size();
                        results.values = new FilterResultModel(
                                filteredListBumaasset, filteredListSerialNumber, filteredListStatus, filteredListKeterangan);
                    } else {
                        results.count = originalData.size();
                        results.values = new FilterResultModel(
                                new  ArrayList<>(vBumaasset), new ArrayList<>(vSerialnumber), new ArrayList<>(vStatus), new ArrayList<>(vKeterangan));
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
                    vBumaasset.clear();
                    vBumaasset.addAll(filterResultModel.getFilteredBumaasset());
                    vSerialnumber.clear();
                    vSerialnumber.addAll(filterResultModel.getFilteredSerialNumbers());
                    vStatus.clear();
                    vStatus.addAll(filterResultModel.getFilteredStatus());
                    vKeterangan.clear();
                    vKeterangan.addAll(filterResultModel.getFilteredKeterangan());

                    originalData = new ArrayList<>(vBumaasset);

                    notifyDataSetChanged();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private static class FilterResultModel {
        private final List<String> filteredBumaasset;
        private final List<String> filteredSerialNumbers;
        private final List<String> filteredStatus;
        private final List<String> filteredKeterangan;

        public FilterResultModel(List<String> filteredBumaasset, List<String> filteredSerialNumbers, List<String> filteredStatus, List<String> filteredKeterangan) {
            this.filteredBumaasset = filteredBumaasset;
            this.filteredSerialNumbers = filteredSerialNumbers;
            this.filteredStatus = filteredStatus;
            this.filteredKeterangan = filteredKeterangan;
        }
        public List<String> getFilteredBumaasset() {
            return filteredBumaasset;
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