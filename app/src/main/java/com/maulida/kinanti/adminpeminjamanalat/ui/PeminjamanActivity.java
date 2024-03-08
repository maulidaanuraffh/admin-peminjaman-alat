package com.maulida.kinanti.adminpeminjamanalat.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.maulida.kinanti.adminpeminjamanalat.MediaHelper;
import com.maulida.kinanti.adminpeminjamanalat.R;
import com.maulida.kinanti.adminpeminjamanalat.adapter.AdapterDataBarang;
import com.maulida.kinanti.adminpeminjamanalat.adapter.AdapterDataPeminjaman;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PeminjamanActivity extends AppCompatActivity implements View.OnClickListener{

    private MediaHelper mediaHelper;
    private AdapterDataPeminjaman pjmAdapter;
    private AdapterDataBarang barangAdapter;
    private List<HashMap<String, String>> daftarPeminjaman = new ArrayList<>();
    private List<HashMap<String, String>> daftarBarang = new ArrayList<>();
    private Bitmap bitmap;

    private final String url = "http://172.16.202.209/pinjambarang/show_data_peminjaman.php";
    private final String url2 = "http://172.16.202.209/pinjambarang/get_nama_barang.php";
    private String imStr = "";
    private String pilihKategori = "";
    private ImageView imagekp;

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imagekp) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, mediaHelper.getRcGallery());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peminjaman);

        mediaHelper = new MediaHelper(this);
        RecyclerView listPeminjaman = findViewById(R.id.listPeminjaman);
        listPeminjaman.setLayoutManager(new LinearLayoutManager(this));

        pjmAdapter = new AdapterDataPeminjaman(daftarPeminjaman, this, mediaHelper);
        listPeminjaman.setAdapter(pjmAdapter);

        showDataPeminjam("");
        getNamaBarang();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bthome);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bthome) {
                return true;
            } else if (item.getItemId() == R.id.btkat) {
                startActivity(new Intent(getApplicationContext(), KategoriActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
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
        showDataPeminjam("");
        getNamaBarang();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == mediaHelper.getRcGallery()) {
                try {
                    imStr = mediaHelper.getBitmapToString(data.getData(), imagekp);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void getNamaBarang() {
        StringRequest request = new StringRequest(
                Request.Method.POST, url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        daftarBarang.clear();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int x = 0; x < jsonArray.length(); x++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(x);
                                HashMap<String, String> brg = new HashMap<>();
                                brg.put("nama_barang", jsonObject.getString("nama_barang"));
                                daftarBarang.add(brg);
                            }
                            //barangAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void showDataPeminjam(String namaPjm) {
        StringRequest request = new StringRequest(
                Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        daftarPeminjaman.clear();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int x = 0; x < jsonArray.length(); x++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(x);
                                HashMap<String, String> brg = new HashMap<>();
                                brg.put("id_pinjam", jsonObject.getString("id_pinjam"));
                                brg.put("nama_pinjam", jsonObject.getString("nama_pinjam"));
                                brg.put("no_hp", jsonObject.getString("no_hp"));
                                brg.put("tgl_pinjam", jsonObject.getString("tgl_pinjam"));
                                brg.put("nama_barang", jsonObject.getString("nama_barang"));
                                brg.put("status", jsonObject.getString("status"));
                                brg.put("url", jsonObject.getString("url"));
                                daftarPeminjaman.add(brg);
                            }
                            pjmAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PeminjamanActivity.this, "Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("nama_pinjam", namaPjm);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}