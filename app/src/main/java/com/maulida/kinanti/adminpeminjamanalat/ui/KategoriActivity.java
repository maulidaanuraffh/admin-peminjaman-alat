package com.maulida.kinanti.adminpeminjamanalat.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.maulida.kinanti.adminpeminjamanalat.R;
import com.maulida.kinanti.adminpeminjamanalat.adapter.AdapterDataKategori;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KategoriActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String URL = "http://172.16.202.209/pinjambarang/show_data_kategori.php";
    private static final String URL3 = "http://172.16.202.209/pinjambarang/query_IUD_kategori.php";

    private static final int INSERT_MODE = 1;
    private static final int UPDATE_MODE = 2;
    private static final int DELETE_MODE = 3;

    private RecyclerView listKategori;
    private AdapterDataKategori ktgAdapter;
    private ImageView btnInsert1, btnUpdate1, btnDelete1;
    public TextInputEditText edKodeKat;
    public TextInputEditText edNamaKategori;

    private final Map<String, String> params = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategori);

        listKategori = findViewById(R.id.listKategori);
        ktgAdapter = new AdapterDataKategori(new ArrayList<>(), this);
        listKategori.setLayoutManager(new LinearLayoutManager(this));
        listKategori.setAdapter(ktgAdapter);

        btnInsert1 = findViewById(R.id.btnInsert1);
        btnUpdate1 = findViewById(R.id.btnUpdate1);
        btnDelete1 = findViewById(R.id.btnDelete1);
        edKodeKat = findViewById(R.id.edKodeKat);
        edNamaKategori = findViewById(R.id.edNamaKategori);

        btnInsert1.setOnClickListener(this);
        btnUpdate1.setOnClickListener(this);
        btnDelete1.setOnClickListener(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bthome);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bthome) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (item.getItemId() == R.id.btkat) {
                return true;
            } else if (item.getItemId() == R.id.btbrg) {
                startActivity(new Intent(getApplicationContext(), BarangActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (item.getItemId() == R.id.btdata) {
                startActivity(new Intent(getApplicationContext(), PeminjamanActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        showDataKat("");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnInsert1) {
            queryInsertUpdateDelete(INSERT_MODE);
        } else if (v.getId() == R.id.btnUpdate1) {
            queryInsertUpdateDelete(UPDATE_MODE);
        } else if (v.getId() == R.id.btnDelete1) {
            queryInsertUpdateDelete(DELETE_MODE);
        }
    }

    private void showDataKat(String namaBrg) {
        StringRequest request = new StringRequest(
                Request.Method.POST, URL,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        List<HashMap<String, String>> dataKategoriList = new ArrayList<>();

                        for (int x = 0; x < jsonArray.length(); x++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(x);
                            HashMap<String, String> kategori = new HashMap<>();
                            kategori.put("kode_kategori", jsonObject.getString("kode_kategori"));
                            kategori.put("nama_kategori", jsonObject.getString("nama_kategori"));
                            dataKategoriList.add(kategori);
                        }

                        ktgAdapter.updateData(dataKategoriList); // Memperbarui data adapter
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                params.clear();
                params.put("nama_kategori", namaBrg);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void queryInsertUpdateDelete(int mode) {
        StringRequest request = new StringRequest(
                Request.Method.POST, URL3,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String errorCode = jsonObject.getString("kode");

                        if ("000".equals(errorCode)) {
                            // Success
                            showDataKat("");

                        } else {
                            Toast.makeText(this, "Gagal melakukan Operasi", Toast.LENGTH_LONG).show();
                        }

                        switch (mode) {
                            case INSERT_MODE:
                                Toast.makeText(this, "Berhasil insert data", Toast.LENGTH_LONG).show();
                                break;
                            case UPDATE_MODE:
                                Toast.makeText(this, "Berhasil update data", Toast.LENGTH_LONG).show();
                                break;
                            case DELETE_MODE:
                                Toast.makeText(this, "Berhasil delete data", Toast.LENGTH_LONG).show();
                                break;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                params.clear();
                switch (mode) {
                    case INSERT_MODE:
                        params.put("mode", "insert");
                        break;
                    case UPDATE_MODE:
                        params.put("mode", "update");
                        break;
                    case DELETE_MODE:
                        params.put("mode", "delete");
                        break;
                }

                params.put("kode_kategori", edKodeKat.getText().toString());
                params.put("nama_kategori", edNamaKategori.getText().toString());

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}