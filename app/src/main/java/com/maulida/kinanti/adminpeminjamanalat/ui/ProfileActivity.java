package com.maulida.kinanti.adminpeminjamanalat.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.maulida.kinanti.adminpeminjamanalat.R;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        back = findViewById(R.id.back);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.back) {
            onBackPressed();
        }
    }
}