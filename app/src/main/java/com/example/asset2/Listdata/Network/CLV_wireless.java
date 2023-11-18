package com.example.asset2.Listdata.Network;

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
import com.example.asset2.Updatedata.Network.Update_wireless;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class CLV_wireless extends ArrayAdapter<String>  {

    final Activity context;
    private ArrayList<String> vHostname;
    private  ArrayList<String> vMerk;
    private   ArrayList<String> vSerialnumber;
    private    ArrayList<String> vIp;
    private    ArrayList<String> vTanggal;
    private    ArrayList<String> vKeterangan;
    private    ArrayList<String> vFoto;
    private ValueFilter valueFilter;
    private ArrayList<String> originalData;

    public CLV_wireless(Activity context, ArrayList<String> Hostname, ArrayList<String> Merk, ArrayList<String> Serialnumber, ArrayList<String> Ip, ArrayList<String> Tanggal, ArrayList<String> Keterangan, ArrayList<String> Foto) {
        super(context, R.layout.clv_wireless, Hostname);

        this.context        = context;
        this.vHostname      = Hostname;
        this.vMerk          = Merk;
        this.vSerialnumber  = Serialnumber;
        this.vIp            = Ip;
        this.vTanggal       = Tanggal;
        this.vKeterangan    = Keterangan;
        this.vFoto          = Foto;
    }

    public void updateData(ArrayList<String> Hostname, ArrayList<String> Merk, ArrayList<String> Serialnumber, ArrayList<String> Ip, ArrayList<String> Tanggal, ArrayList<String> Keterangan, ArrayList<String> Foto) {
        vHostname.clear();
        vMerk.clear();
        vSerialnumber.clear();
        vIp.clear();
        vTanggal.clear();
        vKeterangan.clear();
        vFoto.clear();

        vHostname.addAll(Hostname);
        vMerk.addAll(Merk);
        vSerialnumber.addAll(Serialnumber);
        vIp.addAll(Ip);
        vTanggal.addAll(Tanggal);
        vKeterangan.addAll(Keterangan);
        vFoto.addAll(Foto);

        originalData = new ArrayList<>(vHostname);
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.clv_wireless, null, true);

        CardView update = rowView.findViewById(R.id.cvWireless);

        TextView hostname           = rowView.findViewById(R.id.tvHostname);
        TextView merk               = rowView.findViewById(R.id.tvType);
        TextView serialnumber       = rowView.findViewById(R.id.tvSerial);
        TextView ip                 = rowView.findViewById(R.id.tvIP);
        TextView tanggal            = rowView.findViewById(R.id.tvTanggal);
        TextView keterangan         = rowView.findViewById(R.id.tvKeterangan);
        ImageView foto              = rowView.findViewById(R.id.fotoAsset);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Update_wireless.class);
                intent.putExtra("hostname", vHostname.get(position));
                intent.putExtra("merk", vMerk.get(position));
                intent.putExtra("serialnumber", vSerialnumber.get(position));
                intent.putExtra("ip", vIp.get(position));
                intent.putExtra("tanggal", vTanggal.get(position));
                intent.putExtra("keterangan", vKeterangan.get(position));
                intent.putExtra("foto", vFoto.get(position));

                context.startActivity(intent);
            }
        });

        hostname.setText(vHostname.get(position));
        merk.setText(vMerk.get(position));
        serialnumber.setText(vSerialnumber.get(position));
        ip.setText(vIp.get(position));
        tanggal.setText(vTanggal.get(position));
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
                originalData = new ArrayList<>(vHostname);
            }

            try {
                if (vHostname != null && vMerk != null && vSerialnumber != null && vIp != null && vTanggal != null && vKeterangan != null) {
                    if (constraint != null && constraint.length() > 0) {
                        ArrayList<String> filteredListHostname      = new ArrayList<>();
                        ArrayList<String> filteredListMerk          = new ArrayList<>();
                        ArrayList<String> filteredListSerialNumber  = new ArrayList<>();
                        ArrayList<String> filteredListIp            = new ArrayList<>();
                        ArrayList<String> filteredListTanggal       = new ArrayList<>();
                        ArrayList<String> filteredListKeterangan    = new ArrayList<>();

                        for (int i = 0; i < originalData.size(); i++) {
                            if (i < vHostname.size() && i < vMerk.size() && i < vSerialnumber.size() && i < vIp.size() && i < vTanggal.size() && i < vKeterangan.size()) {
                                String hostname     = originalData.get(i);
                                String merk         = vMerk.get(i);
                                String serialNumber = vSerialnumber.get(i);
                                String ip           = vIp.get(i);
                                String tanggal      = vTanggal.get(i);
                                String keterangan   = vKeterangan.get(i);

                                if (hostname != null && merk != null && serialNumber != null && ip != null && tanggal != null && keterangan != null) {
                                    if (hostname.toLowerCase().contains(constraint.toString().toLowerCase()) ||
                                            merk.toLowerCase().contains(constraint.toString().toLowerCase()) ||
                                            serialNumber.toLowerCase().contains(constraint.toString().toLowerCase()) ||
                                            ip.toLowerCase().contains(constraint.toString().toLowerCase()) ||
                                            tanggal.toLowerCase().contains(constraint.toString().toLowerCase()) ||
                                            keterangan.toLowerCase().contains(constraint.toString().toLowerCase())) {

                                        filteredListHostname.add(hostname);
                                        filteredListMerk.add(merk);
                                        filteredListSerialNumber.add(serialNumber);
                                        filteredListIp.add(ip);
                                        filteredListTanggal.add(tanggal);
                                        filteredListKeterangan.add(keterangan);
                                    }
                                }
                            }
                        }

                        results.count = filteredListHostname.size();
                        results.values = new FilterResultModel(
                                filteredListHostname, filteredListMerk, filteredListSerialNumber,filteredListIp, filteredListTanggal, filteredListKeterangan);
                    } else {
                        results.count = originalData.size();
                        results.values = new FilterResultModel(
                                new ArrayList<>(vHostname), new ArrayList<>(vMerk), new ArrayList<>(vSerialnumber), new ArrayList<>(vIp), new ArrayList<>(vTanggal), new ArrayList<>(vKeterangan));
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
                    vHostname.clear();
                    vHostname.addAll(filterResultModel.getFilteredHostname());
                    vMerk.clear();
                    vMerk.addAll(filterResultModel.getFilteredMerk());
                    vSerialnumber.clear();
                    vSerialnumber.addAll(filterResultModel.getFilteredSerialNumber());
                    vIp.clear();
                    vIp.addAll(filterResultModel.getFilteredIp());
                    vTanggal.clear();
                    vTanggal.addAll(filterResultModel.getFilteredTanggal());
                    vKeterangan.clear();
                    vKeterangan.addAll(filterResultModel.getFilteredKeterangan());

                    originalData = new ArrayList<>(vHostname);

                    notifyDataSetChanged();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static class FilterResultModel {
        private final List<String> filteredHostname;
        private final List<String> filteredMerk;
        private final List<String> filteredSerialNumber;
        private final List<String> filteredIp;
        private final List<String> filteredTanggal;
        private final List<String> filteredKeterangan;

        public FilterResultModel(List<String> filteredHostname, List<String> filteredMerk, List<String> filteredSerialNumber, List<String> filteredIp, List<String> filteredTanggal, List<String> filteredKeterangan) {
            this.filteredHostname = filteredHostname;
            this.filteredMerk = filteredMerk;
            this.filteredSerialNumber = filteredSerialNumber;
            this.filteredIp = filteredIp;
            this.filteredTanggal = filteredTanggal;
            this.filteredKeterangan = filteredKeterangan;
        }

        public List<String> getFilteredHostname() {
            return filteredHostname;
        }
        public List<String> getFilteredMerk() {
            return filteredMerk;
        }
        public List<String> getFilteredSerialNumber() {
            return filteredSerialNumber;
        }
        public List<String> getFilteredIp() {
            return filteredIp;
        }
        public List<String> getFilteredTanggal() {
            return filteredTanggal;
        }
        public List<String> getFilteredKeterangan() {
            return filteredKeterangan;
        }
    }
}