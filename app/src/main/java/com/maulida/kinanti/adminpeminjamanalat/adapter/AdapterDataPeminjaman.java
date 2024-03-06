package com.maulida.kinanti.adminpeminjamanalat.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.maulida.kinanti.adminpeminjamanalat.MediaHelper;
import com.maulida.kinanti.adminpeminjamanalat.R;
import com.maulida.kinanti.adminpeminjamanalat.ui.PeminjamanActivity;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class AdapterDataPeminjaman extends RecyclerView.Adapter<AdapterDataPeminjaman.HolderDataPjm> {
    private final List<HashMap<String, String>> dataPjm;
    private final PeminjamanActivity peminjamanActivity;

    public AdapterDataPeminjaman(List<HashMap<String, String>> dataPjm, PeminjamanActivity peminjamanActivity, MediaHelper mediaHelper) {
        this.dataPjm = dataPjm;
        this.peminjamanActivity = peminjamanActivity;
    }

    @Override
    public int getItemCount() {
        return dataPjm.size();
    }

    @Override
    public void onBindViewHolder(AdapterDataPeminjaman.HolderDataPjm holder, int position) {
        HashMap<String, String> data = dataPjm.get(position);
        holder.txId.setText(data.get("id_pinjam"));
        holder.txNama.setText(data.get("nama_pinjam"));
        holder.txNo.setText(data.get("no_hp"));
        holder.txTgl.setText(data.get("tgl_pinjam"));
        holder.txBarang.setText(data.get("nama_barang"));
        holder.txStatus.setText(data.get("status"));

        if (!data.get("url").equals(""))
            Picasso.get().load(data.get("url")).into(holder.photo);
    }

    @Override
    public AdapterDataPeminjaman.HolderDataPjm onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_peminjaman, parent, false);
        return new HolderDataPjm(v);
    }

    static class HolderDataPjm extends RecyclerView.ViewHolder {
        final TextView txId,txNama,txNo,txTgl,txBarang,txStatus;
        final ImageView photo;
        final ConstraintLayout clayout2;

        HolderDataPjm(View v) {
            super(v);
            txId = v.findViewById(R.id.textid);
            txNama = v.findViewById(R.id.textnama);
            txNo = v.findViewById(R.id.textno);
            txTgl = v.findViewById(R.id.texttgl);
            txBarang = v.findViewById(R.id.textbarang);
            txStatus = v.findViewById(R.id.textstatus);
            photo = v.findViewById(R.id.imagekp);
            clayout2 = v.findViewById(R.id.itemPmj);
        }
    }
}
