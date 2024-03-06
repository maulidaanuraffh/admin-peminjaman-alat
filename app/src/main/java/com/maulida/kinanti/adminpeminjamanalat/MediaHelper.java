package com.maulida.kinanti.adminpeminjamanalat;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class MediaHelper {
    private final Context context;
    public static final int REQ_CODE_GALLERY = 100;

    public MediaHelper(Context context) {
        this.context = context;
    }

    public int getRcGallery() {
        return REQ_CODE_GALLERY;
    }

    public String bitmapToString(Bitmap bmp) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 60, outputStream);
        byte[] byteArray = outputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public String getBitmapToString(Uri uri, ImageView imv) throws Exception {
        Bitmap bmp = MediaStore.Images.Media.getBitmap(
                context.getContentResolver(), uri);
        int dim = 720;
        if (bmp.getHeight() > bmp.getWidth()) {
            bmp = Bitmap.createScaledBitmap(bmp,
                    (bmp.getWidth() * dim) / bmp.getHeight(), dim, true);
        } else {
            bmp = Bitmap.createScaledBitmap(bmp,
                    dim, (bmp.getHeight() * dim) / bmp.getWidth(), true);
        }
        imv.setImageBitmap(bmp);
        return bitmapToString(bmp);
    }
}
