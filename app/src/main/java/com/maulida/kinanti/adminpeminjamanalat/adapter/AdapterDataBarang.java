package com.maulida.kinanti.adminpeminjamanalat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.maulida.kinanti.adminpeminjamanalat.R;
import com.maulida.kinanti.adminpeminjamanalat.ui.BarangActivity;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class AdapterDataBarang extends RecyclerView.Adapter<AdapterDataBarang.HolderDataBrg> {
    private final List<HashMap<String, String>> dataBrg;
    private final BarangActivity barangActivity;

    public AdapterDataBarang(List<HashMap<String, String>> dataBrg, BarangActivity barangActivity) {
        this.dataBrg = dataBrg;
        this.barangActivity = barangActivity;
    }

    @Override
    public int getItemCount() {
        return dataBrg.size();
    }

    @Override
    public void onBindViewHolder(HolderDataBrg holder, int position) {
        HashMap<String, String> data = dataBrg.get(position);
        holder.txKode.setText(data.get("kode_barang"));
        holder.txNama.setText(data.get("nama_barang"));
        holder.txKategori.setText(data.get("nama_kategori"));

        holder.cLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = barangActivity.daftarKategori.indexOf(data.get("nama_kategori"));
                barangActivity.spinKategori.setSelection(pos);
                barangActivity.edKode.setText(data.get("kode_barang"));
                barangActivity.edNamaBarang.setText(data.get("nama_barang"));
                Picasso.get().load(data.get("url")).into(barangActivity.imageView2);
            }
        });
        // End new

        if (!data.get("url").equals("")) {
            Picasso.get().load(data.get("url")).into(holder.photo);
        }
    }

    @Override
    public HolderDataBrg onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_barang, parent, false);
        return new HolderDataBrg(v);
    }

    static class HolderDataBrg extends RecyclerView.ViewHolder {
        final TextView txKode;
        final TextView txNama;
        final TextView txKategori;
        final ImageView photo;
        final ConstraintLayout cLayout;

        HolderDataBrg(View v) {
            super(v);
            txKode = v.findViewById(R.id.txKode);
            txNama = v.findViewById(R.id.txNama);
            txKategori = v.findViewById(R.id.txKategori);
            photo = v.findViewById(R.id.imageView);
            cLayout = v.findViewById(R.id.itemBrg);
        }
    }
}
