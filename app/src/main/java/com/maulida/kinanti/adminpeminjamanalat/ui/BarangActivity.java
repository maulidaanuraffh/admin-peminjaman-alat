package com.maulida.kinanti.adminpeminjamanalat.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.maulida.kinanti.adminpeminjamanalat.MediaHelper;
import com.maulida.kinanti.adminpeminjamanalat.R;
import com.maulida.kinanti.adminpeminjamanalat.adapter.AdapterDataBarang;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

public class BarangActivity extends AppCompatActivity implements View.OnClickListener{
    private MediaHelper mediaHelper;
    private AdapterDataBarang brgAdapter;
    private ArrayAdapter<String> kategoriAdapter;
    private RecyclerView listBarang;
    private ImageView btnInsert, btnUpdate, btnDelete, btngenerate, btnsave;
    public TextInputEditText edKode;
    public TextInputEditText edNamaBarang;
    public Spinner spinKategori;
    public ImageView imageView2;
    private Bitmap bitmap;
    private String imStr = "";
    private String pilihKategori = "";

    private final String url = "http://172.16.202.209/pinjambarang/show_data_barang.php";
    private final String url2 = "http://172.16.202.209/pinjambarang/get_nama_kategori.php";
    private final String url3 = "http://172.16.202.209/pinjambarang/query_IUD_barang.php";
    private final Map<String, String> params = new HashMap<>();
    private final ArrayList<HashMap<String, String>> daftarBarang = new ArrayList<>();
    public final ArrayList<String> daftarKategori = new ArrayList<>();

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        if (viewId == R.id.imageView2) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, mediaHelper.getRcGallery());
        } else if (viewId == R.id.btnInsert) {
            queryInsertUpdateDelete("insert");
        } else if (viewId == R.id.btnUpdate) {
            queryInsertUpdateDelete("update");
        } else if (viewId == R.id.btnDelete) {
            queryInsertUpdateDelete("delete");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barang);

        brgAdapter = new AdapterDataBarang(daftarBarang, this);
        kategoriAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, daftarKategori);
        mediaHelper = new MediaHelper(this);

        listBarang = findViewById(R.id.listBarang);
        listBarang.setLayoutManager(new LinearLayoutManager(this));
        listBarang.setAdapter(brgAdapter);

        spinKategori = findViewById(R.id.spinKategori);
        spinKategori.setAdapter(kategoriAdapter);

        imageView2 = findViewById(R.id.imageView2);
        btnInsert = findViewById(R.id.btnInsert);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        btngenerate = findViewById(R.id.btngenerate);
        btnsave = findViewById(R.id.btnsave);

        edKode = findViewById(R.id.edKode);
        edNamaBarang = findViewById(R.id.edNamaBarang);

        spinKategori.setOnItemSelectedListener(itemSelected);
        imageView2.setOnClickListener(this);
        btnInsert.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btngenerate.setOnClickListener(this);

        initView();
        cekpermission();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bthome);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bthome) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (item.getItemId() == R.id.btkat) {
                startActivity(new Intent(getApplicationContext(), KategoriActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (item.getItemId() == R.id.btbrg) {
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
        showDataMhs("");
        getNamaProdi();
    }

    private final AdapterView.OnItemSelectedListener itemSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            spinKategori.setSelection(0);
            pilihKategori = daftarKategori.get(0);
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            pilihKategori = daftarKategori.get(position);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == mediaHelper.getRcGallery()) {
                try {
                    imStr = mediaHelper.getBitmapToString(data.getData(), imageView2);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void cekpermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 30);
            }
        }
    }

    private void initView() {
        btngenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kode = edKode.getText().toString();
                String nama = edNamaBarang.getText().toString();
                String XML = "{'kode_barang':'" + kode + "','nama_barang':'" + nama + "'}";

                QRGEncoder qrgEncoder = new QRGEncoder(XML, null, QRGContents.Type.TEXT, 300);
                qrgEncoder.setColorBlack(Color.BLACK);
                qrgEncoder.setColorWhite(Color.WHITE);

                // Getting QR-Code as Bitmap without margin
                bitmap = qrgEncoder.getBitmap(0);
                imageView2.setImageBitmap(bitmap);
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String savePath = Environment.getExternalStorageDirectory().getPath() + "/Qrcode/";

                // Save QR Code as Image
                QRGSaver qrgSaver = new QRGSaver();
                qrgSaver.save(savePath, "QR" + " " + edNamaBarang.getText(), bitmap, QRGContents.ImageType.IMAGE_JPEG);

                Toast.makeText(BarangActivity.this, "QR CODE Telah Disimpan", Toast.LENGTH_SHORT).show();
                imageView2.setImageBitmap(null);
            }
        });
    }

    private void getNamaProdi() {
        StringRequest request = new StringRequest(
                Request.Method.POST, url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        daftarKategori.clear();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int x = 0; x < jsonArray.length(); x++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(x);
                                daftarKategori.add(jsonObject.getString("nama_kategori"));
                            }
                            kategoriAdapter.notifyDataSetChanged();
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

    private void showDataMhs(String namaBrg) {
        StringRequest request = new StringRequest(
                Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        daftarBarang.clear();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int x = 0; x < jsonArray.length(); x++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(x);
                                HashMap<String, String> brg = new HashMap<>();
                                brg.put("kode_barang", jsonObject.getString("kode_barang"));
                                brg.put("nama_barang", jsonObject.getString("nama_barang"));
                                brg.put("nama_kategori", jsonObject.getString("nama_kategori"));
                                brg.put("url", jsonObject.getString("url"));
                                daftarBarang.add(brg);
                            }
                            brgAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(BarangActivity.this, "Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                params.clear();
                params.put("nama_barang", namaBrg);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void queryInsertUpdateDelete(final String mode) {
        StringRequest request = new StringRequest(
                Request.Method.POST, url3,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String errorCode = jsonObject.getString("kode");

                            if ("000".equals(errorCode)) {
                                showDataMhs("");

                            } else {
                                Toast.makeText(BarangActivity.this, "Gagal melakukan Operasi", Toast.LENGTH_LONG).show();
                            }

                            brgAdapter.notifyDataSetChanged();
                            switch (mode) {
                                case "insert":
                                    Toast.makeText(BarangActivity.this, "Berhasil insert data", Toast.LENGTH_LONG).show();
                                    break;
                                case "update":
                                    Toast.makeText(BarangActivity.this, "Berhasil update data", Toast.LENGTH_LONG).show();
                                    break;
                                case "delete":
                                    Toast.makeText(BarangActivity.this, "Berhasil delete data", Toast.LENGTH_LONG).show();
                                    break;
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(BarangActivity.this, "Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                params.clear();
                String nmFile = "QR" + edNamaBarang.getText() + new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
                        .format(new Date()) + ".jpg";
                switch (mode) {
                    case "insert":
                        params.put("mode", "insert");
                        params.put("kode_barang", edKode.getText().toString());
                        params.put("nama_barang", edNamaBarang.getText().toString());
                        params.put("image", imStr);
                        params.put("file", nmFile);
                        params.put("nama_kategori", pilihKategori);
                        break;
                    case "update":
                        params.put("mode", "update");
                        params.put("kode_barang", edKode.getText().toString());
                        params.put("nama_barang", edNamaBarang.getText().toString());
                        params.put("image", imStr);
                        params.put("file", nmFile);
                        params.put("nama_kategori", pilihKategori);
                        break;
                    case "delete":
                        params.put("mode", "delete");
                        params.put("kode_barang", edKode.getText().toString());
                        break;
                }
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}