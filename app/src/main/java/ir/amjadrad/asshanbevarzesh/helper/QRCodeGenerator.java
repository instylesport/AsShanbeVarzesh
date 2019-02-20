package ir.amjadrad.asshanbevarzesh.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import com.google.zxing.WriterException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QRCodeGenerator {

    public static void generate(String value , Context context, ImageView imageView){


        QRGEncoder qrgEncoder = new QRGEncoder(value, null, QRGContents.Type.TEXT, 600);
        try {
            Bitmap bitmap = qrgEncoder.encodeAsBitmap();
            imageView.setImageBitmap(bitmap);
            saveImage(bitmap , context , value);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }


    public static String saveImage(Bitmap myBitmap , Context context , String value) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + "/in_style");
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }
        try {
            File f = new File(wallpaperDirectory, "QR_" + value + ".png");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(context, new String[]{f.getPath()}, new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());
            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

}
