package ir.amjadrad.asshanbevarzesh.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.VideoView;

import ir.amjadrad.asshanbevarzesh.R;

public class SplashScreenActivity extends AppCompatActivity {

    private VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        videoView = findViewById(R.id.videoView);
        String path = "android.resource://" + getPackageName() + "/" + R.raw.splash;
        videoView.setVideoURI(Uri.parse(path));

        videoView.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(SplashScreenActivity.this , LoginActivity.class));
                finish();
            }
        } , 3200);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.view).setVisibility(View.GONE);

            }
        } , 500);


    }
}
