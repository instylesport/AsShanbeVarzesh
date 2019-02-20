package ir.amjadrad.asshanbevarzesh.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ir.amjadrad.asshanbevarzesh.app.App;
import ir.amjadrad.asshanbevarzesh.app.Endpoint;
import ir.amjadrad.asshanbevarzesh.fragment.FragmentScanner;
import ir.amjadrad.asshanbevarzesh.fragment.FragmentStep;
import ir.amjadrad.asshanbevarzesh.fragment.FragmentWallet;
import ir.amjadrad.asshanbevarzesh.R;
import ir.amjadrad.asshanbevarzesh.helper.SessionHelper;
import ir.amjadrad.asshanbevarzesh.object.User;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static FragmentScanner fragmentScanner;
    private static FragmentStep fragmentStep;
    private static FragmentWallet fragmentWallet;

    private FragmentManager fragmentManager;
    private SessionHelper sessionHelper;
    private User user;
    private static int what = 2;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_step:
                    if (sessionHelper.getUser().getBmr() <200) {
                        what = 0;
//                        updateFragment(fragmentStep);
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                    } else {
                        what = 0;
                        updateFragment(fragmentStep);
                    }
                    return true;
                case R.id.navigation_scanner:
                    what = 1;
                    updateFragment(fragmentScanner);
                    return true;
                case R.id.navigation_wallet:
                    what = 2;
//                    fragmentScanner.mCodeScanner.releaseResources();
                    updateFragment(fragmentWallet);

                    ImageView imageView = findViewById(R.id.imageViewToolbar);
                    TextView textView = findViewById(R.id.tvToolbar);
                    if (!user.getName().equals("null null")) {
                        textView.setText(user.getName());//user name
                    }
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_exit_to_app_black_24dp));
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            sessionHelper.setLogin(false, "");
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            finish();
                        }
                    });

                    return true;
            }
            return false;
        }
    };

    boolean b = false;

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "برای خروج دوباره کلیک کنید", Toast.LENGTH_SHORT).show();
        if (b) {
            finish();
        } else {
            b = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    b = false;
                }
            }, 500);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 100) {
            fragmentScanner = new FragmentScanner();
            updateFragment(fragmentScanner);

        }else if(requestCode==101){
//            fragmentStep = new FragmentStep();
//            updateFragment(fragmentStep);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentScanner = new FragmentScanner();
        fragmentStep = new FragmentStep();
        fragmentWallet = new FragmentWallet();

        sessionHelper = new SessionHelper(this);
        user = sessionHelper.getUser();
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().findItem(R.id.navigation_wallet).setChecked(true);

        fragmentManager = getSupportFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.container, fragmentWallet);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack(null);
        transaction.commit();

        ImageView imageView = findViewById(R.id.imageViewToolbar);
        TextView textView = findViewById(R.id.tvToolbar);
        if (!user.getName().equals("null null")) {
            textView.setText(user.getName());//user name
        }
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_exit_to_app_black_24dp));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionHelper.setLogin(false, "");
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });


    }


    private void updateFragment(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (what == 0) {
//            updateFragment(new FragmentStep());
//        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }
}
