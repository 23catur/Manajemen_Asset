package com.example.asset2;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

//import com.example.inventarisapp.DataBase.DBHelper;
//import com.example.inventarisapp.DataBase.DataModel;

import java.util.List;

//public class RecyclerBarangAdapter extends RecyclerView.Adapter<RecyclerBarangAdapter.ViewHolder> {
//    List<DataModel> dataModel;
//    Context context;
//    DBHelper db;

//    public RecyclerBarangAdapter(List<DataModel> dataModel, Context context) {
//        this.dataModel = dataModel;
//        this.context = context;
//    }

//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.list_view, parent, false);

//        db = new DBHelper(context);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
//        holder.Tanggal.setText(dataModel.get(position).getTanggal());
//        holder.Keterangan.setText(dataModel.get(position).getKeterangan());
//        holder.Hostname.setText(dataModel.get(position).getHostname());
//        holder.Type.setText(dataModel.get(position).getType());
//        holder.Serialnumber.setText(dataModel.get(position).getSerialnumber());
//        holder.Ip.setText(dataModel.get(position).getIp());
//        holder.cvMain.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CharSequence colors[] = new CharSequence[]{"Edit", "Delete"};
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
//                builder.setTitle("Pilih Aksi");
//                builder.setItems(colors, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (which == 0) {
//                            v.getContext().startActivity(new Intent(v.getContext(), EditActivity.class)
//                                    .putExtra("kode", dataModel.get(position).getKode())
//                                    .putExtra("hostname", dataModel.get(position).getHostname())
//                                    .putExtra("type", dataModel.get(position).getType())
//                                    .putExtra("serialnumber", dataModel.get(position).getSerialnumber())
//                                    .putExtra("ip", dataModel.get(position).getIp())
//                                    .putExtra("tanggal", dataModel.get(position).getTanggal())
//                                    .putExtra("keterangan", dataModel.get(position).getKeterangan()));

//                        } else {
//                            delete(position);
//                        }
//                    }
//                });
//                builder.show();
//            }
//        });
//    }

//    @Override
//    public int getItemCount() {
//        return dataModel != null ? dataModel.size():0;
//    }

//    public class ViewHolder extends RecyclerView.ViewHolder {
//        TextView Tanggal, Hostname, Type, Serialnumber, Ip, Keterangan;
//        CardView cvMain;
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            cvMain = itemView.findViewById(R.id.cvMain);
//            Tanggal = itemView.findViewById(R.id.tv_tanggal);
//            Hostname = itemView.findViewById(R.id.tv_hostname);
//            Type = itemView.findViewById(R.id.tv_type);
//            Serialnumber = itemView.findViewById(R.id.tv_serial);
//            Ip = itemView.findViewById(R.id.tv_ip);
//            Keterangan = itemView.findViewById(R.id.tv_ket);
//        }
//    }

//    @SuppressLint("NotifyDataSetChanged")
//    private void delete(int position) {
//        // deleting the note from db
////        db.delete(dataModel.get(position));
//
//        // removing the note from the list
////        dataModel.remove(position);
////        notifyDataSetChanged();
//
//        Toast.makeText(context, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show();
//    }
//}
