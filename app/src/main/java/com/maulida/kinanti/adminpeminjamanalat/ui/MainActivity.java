package com.maulida.kinanti.adminpeminjamanalat.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.maulida.kinanti.adminpeminjamanalat.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int BRG = 100;
    private static final int KTG = 101;
    private static final int PNJM = 102;
    private static final int PROFILE = 103;

    ImageView profile;
    ConstraintLayout kategori, barang, peminjaman;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        kategori = findViewById(R.id.kategori);
        barang = findViewById(R.id.barang);
        peminjaman = findViewById(R.id.peminjaman);
        profile = findViewById(R.id.profile);

        kategori.setOnClickListener(this);
        barang.setOnClickListener(this);
        peminjaman.setOnClickListener(this);
        profile.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.kategori) {
            Intent intentBrg = new Intent(this, KategoriActivity.class);
            startActivityForResult(intentBrg, BRG);
        } else if (v.getId() == R.id.barang) {
            Intent intentKtg = new Intent(this, BarangActivity.class);
            startActivityForResult(intentKtg, KTG);
        } else if (v.getId() == R.id.peminjaman) {
            Intent intentPnjm = new Intent(this, PeminjamanActivity.class);
            startActivityForResult(intentPnjm, PNJM);
        } else if(v.getId() == R.id.profile) {
            Intent profileIntent = new Intent(this, ProfileActivity.class);
            startActivityForResult(profileIntent, PROFILE);
        }
    }
}