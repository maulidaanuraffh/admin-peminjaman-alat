package com.maulida.kinanti.adminpeminjamanalat.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.maulida.kinanti.adminpeminjamanalat.R;
import com.maulida.kinanti.adminpeminjamanalat.ui.KategoriActivity;

import java.util.HashMap;
import java.util.List;

public class AdapterDataKategori extends RecyclerView.Adapter<AdapterDataKategori.HolderDataKtg> {

    private final List<HashMap<String, String>> dataKtg;
    private final KategoriActivity kategoriActivity;

    public AdapterDataKategori(List<HashMap<String, String>> dataKtg, KategoriActivity kategoriActivity) {
        this.dataKtg = dataKtg;
        this.kategoriActivity = kategoriActivity;
    }

    @Override
    public int getItemCount() {
        return dataKtg.size();
    }

    @Override
    public void onBindViewHolder(HolderDataKtg holder, int position) {
        HashMap<String, String> data = dataKtg.get(position);
        holder.txKode.setText(data.get("kode_kategori"));
        holder.txNama.setText(data.get("nama_kategori"));

        //begin new
//        if (position % 2 == 0) {
//            holder.cLayout1.setBackgroundColor(Color.rgb(230, 245, 240));
//        } else {
//            holder.cLayout1.setBackgroundColor(Color.rgb(255, 255, 245));
//        }

        holder.cLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kategoriActivity.edKodeKat.setText(data.get("kode_kategori"));
                kategoriActivity.edNamaKategori.setText(data.get("nama_kategori"));
            }
        });
    }

    @Override
    public HolderDataKtg onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kategori, parent, false);
        return new HolderDataKtg(v);
    }

    public void updateData(List<HashMap<String, String>> newData) {
        dataKtg.clear();
        dataKtg.addAll(newData);
        notifyDataSetChanged();
    }

    public static class HolderDataKtg extends RecyclerView.ViewHolder {
        TextView txKode, txNama;
        ConstraintLayout cLayout1;

        public HolderDataKtg(View itemView) {
            super(itemView);
            txKode = itemView.findViewById(R.id.txKode);
            txNama = itemView.findViewById(R.id.txNama);
            cLayout1 = itemView.findViewById(R.id.itemKat);
        }
    }
}