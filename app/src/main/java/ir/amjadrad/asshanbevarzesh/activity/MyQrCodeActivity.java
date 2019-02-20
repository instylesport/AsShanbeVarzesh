package ir.amjadrad.asshanbevarzesh.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import ir.amjadrad.asshanbevarzesh.R;
import ir.amjadrad.asshanbevarzesh.helper.QRCodeGenerator;
import ir.amjadrad.asshanbevarzesh.helper.SessionHelper;

public class MyQrCodeActivity extends AppCompatActivity {

    private ImageView imageView;
    private SessionHelper sessionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_qr_code);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView tvToolbar = findViewById(R.id.tvToolbar);
        tvToolbar.setText("عنوان");

//        String name="";
        imageView = findViewById(R.id.imageView);
        sessionHelper = new SessionHelper(this);
//        if(sessionHelper.getUser().getName().equals("")){
//            name = "تعیین نشده";
//        }else {
//            name = sessionHelper.getUser().getName();
//        }
        String q = sessionHelper.getUser().getQr_code();
        QRCodeGenerator.generate(q, this, imageView);
        Log.i(">>>>>>>>>" , q);

    }
}
