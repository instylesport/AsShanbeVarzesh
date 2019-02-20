package ir.amjadrad.asshanbevarzesh.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ir.amjadrad.asshanbevarzesh.R;
import ir.amjadrad.asshanbevarzesh.app.App;
import ir.amjadrad.asshanbevarzesh.app.Endpoint;
import ir.amjadrad.asshanbevarzesh.helper.SessionHelper;
import ir.amjadrad.asshanbevarzesh.helper.panoramahelper.GyroscopeObserver;
import ir.amjadrad.asshanbevarzesh.helper.panoramahelper.PanoramaImageView;
import ir.amjadrad.asshanbevarzesh.object.User;
import ir.amjadrad.asshanbevarzesh.receiver.ServiceCommunicator;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private SessionHelper sessionHelper;

    private Handler handler;
    private TextView tvMessage;
    private Button btnNameNext, btnGetPhone, btnConfirmCode, btnGetPhoneBack, btnConfirmCodeBack;
    public EditText etName, etPhone, etConfirmCode;
    private GyroscopeObserver gyroscopeObserver;

    private SpinKitView spinKit;

    private ScrollView scrollView;

    private void write(final String msg, final int i, final TextView tv, final int level) {

        if (i == msg.length() + 1) {
            if (level == 0) {
                Animation RightSwipe = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.anim_right_to_left);
                etName.startAnimation(RightSwipe);
                btnNameNext.startAnimation(RightSwipe);
                etName.setVisibility(View.VISIBLE);
                btnNameNext.setVisibility(View.VISIBLE);
            } else if (level == 1) {
                Animation RightSwipe = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.anim_right_to_left);
                etPhone.startAnimation(RightSwipe);
                btnGetPhone.startAnimation(RightSwipe);
                btnGetPhoneBack.startAnimation(RightSwipe);
                etPhone.setVisibility(View.VISIBLE);
                btnGetPhone.setVisibility(View.VISIBLE);
                btnGetPhoneBack.setVisibility(View.VISIBLE);
            } else {
                Animation RightSwipe = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.anim_right_to_left);
                etConfirmCode.startAnimation(RightSwipe);
                btnConfirmCode.startAnimation(RightSwipe);
                btnConfirmCodeBack.startAnimation(RightSwipe);
                etConfirmCode.setVisibility(View.VISIBLE);
                btnConfirmCode.setVisibility(View.VISIBLE);
                btnConfirmCodeBack.setVisibility(View.VISIBLE);
            }
            return;
        }
        tv.setText(msg.substring(0, i));


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int j = i + 1;
                write(msg, j, tv, level);
            }
        }, 45);

    }

    private void clearWriteAndWrite(final String msg, final int i, final TextView tv, final int level, final int k) {

        if (k == 0) {
            write(msg, i, tv, level);
        } else {
            final String txt = tv.getText().toString();
            tv.setText(txt.substring(0, k - 1));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    clearWriteAndWrite(msg, i, tv, level, k - 1);
                }
            }, 10);
        }
    }

    private int touch = 0;

    private void waittt(){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(touch==30){
                    final VideoView videoView = findViewById(R.id.videoView3);
                    final CoordinatorLayout coordinatorLayout = findViewById(R.id.cll);
                    coordinatorLayout.setVisibility(View.VISIBLE);
                    coordinatorLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            videoView.pause();
                            coordinatorLayout.setVisibility(View.GONE);
                        }
                    });
                    videoView.setVisibility(View.VISIBLE);
                    String path = "android.resource://" + getPackageName() + "/" + R.raw.shambaba_wait;
                    videoView.setVideoURI(Uri.parse(path));
                    videoView.start();
                    touch = 0;
                    waittt();
                }else{
//                    Toast.makeText(LoginActivity.this, ""+touch, Toast.LENGTH_SHORT).show();
                    touch++;
                    waittt();
                }
            }
        } , 1000);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        CoordinatorLayout coordinatorLayout = findViewById(R.id.cl);
        coordinatorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                touch =0;
            }
        });

        waittt();


        sessionHelper = new SessionHelper(this);
        startService(new Intent(LoginActivity.this, ServiceCommunicator.class));


//        VideoView videoView = findViewById(R.id.videoView2);
//        String path = "android.resource://" + getPackageName() + "/" + R.raw.shambaba_f;
//        videoView.setVideoURI(Uri.parse(path));
//        videoView.setZOrderOnTop(true);
//        videoView.start();


        initPano();

        spinKit = findViewById(R.id.spin_kit);
        scrollView = findViewById(R.id.scrollview);
        tvMessage = findViewById(R.id.tvMessage);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etConfirmCode = findViewById(R.id.etConfirmCode);
        btnNameNext = findViewById(R.id.btnNameNext);
        btnGetPhone = findViewById(R.id.btnGetPhone);
        btnConfirmCode = findViewById(R.id.btnConfirmCode);
        btnConfirmCodeBack = findViewById(R.id.btnConfirmCodeBack);
        btnGetPhoneBack = findViewById(R.id.btnGetPhoneBack);
        btnNameNext.setOnClickListener(this);
        btnGetPhone.setOnClickListener(this);
        btnConfirmCode.setOnClickListener(this);
        btnConfirmCodeBack.setOnClickListener(this);
        btnGetPhoneBack.setOnClickListener(this);
        handler = new Handler();
        String msg = "سلام خوبی؟";
        write(msg, 0, tvMessage, 0);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS}, 103);
        }


    }


    private void initPano() {
        PanoramaImageView panoramaImageView = findViewById(R.id.panorama_image_view2);
        panoramaImageView.setEnablePanoramaMode(true);
        panoramaImageView.setEnableScrollbar(false);
        panoramaImageView.setInvertScrollDirection(false);
        gyroscopeObserver = new GyroscopeObserver();
        panoramaImageView.setGyroscopeObserver(gyroscopeObserver);
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Register GyroscopeObserver.
        gyroscopeObserver.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister GyroscopeObserver.
        gyroscopeObserver.unregister();
    }

    private void request() {

        spinKit.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoint.url_signin, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                spinKit.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("error")) {
                        Toast.makeText(LoginActivity.this, "مشکلی رخ داد", Toast.LENGTH_SHORT).show();
                    } else {
                        updateCode();
                        Animation RightSwipe = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.anim_left_to_right);
                        etPhone.startAnimation(RightSwipe);
                        btnGetPhone.startAnimation(RightSwipe);
                        btnGetPhoneBack.startAnimation(RightSwipe);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                etPhone.setVisibility(View.GONE);
                                btnGetPhone.setVisibility(View.GONE);
                                btnGetPhoneBack.setVisibility(View.GONE);
                                String msg = "کدی که واسه شماره ات فرستادم رو اینجا وارد کن";
                                write(msg, 0, tvMessage, 2);

                            }
                        }, 300);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, "خطایی رخ داد", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                spinKit.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, "خطایی رخ داد", Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("phone", etPhone.getText().toString());
                map.put("name", etName.getText().toString());
                map.put("api_key", "san75amji76");
                return map;
            }
        };

        App.addToQueue(stringRequest);
    }


    private void updateCode() {
        Log.i(">>>>>>>", "updateeeeeeeeeeeee" + App.confirm_code);
        if (!App.confirm_code.equals("")) {
            etConfirmCode.setText(App.confirm_code);
            App.confirm_code = "";
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                updateCode();
            }
        }, 400);
    }

    private void requestUser() {

        spinKit.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoint.url_confirm_code, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                spinKit.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), "مشکلی رخ داد", Toast.LENGTH_SHORT).show();
                    } else {
                        //
                        Log.i(">>>>>>>>>", response);

                        JSONObject userOb = jsonObject.getJSONObject("msg");
                        User user = new User();
                        user.setId(userOb.getInt("id"));
                        user.setName(userOb.getString("name"));
                        user.setPhone(userOb.getString("phone"));
//                        user.setEmail(userOb.getString("email"));
                        user.setSex((userOb.getInt("sex")) == 0 ? false : true);
                        user.setHeight(userOb.getInt("height"));
                        user.setWeight(userOb.getInt("weight"));
                        String bd = userOb.getString("birth_day");
                        user.setBirth_d(Integer.parseInt(bd.substring(8)));
                        user.setBirth_m(Integer.parseInt(bd.substring(5, 7)));
                        user.setBirth_y(Integer.parseInt(bd.substring(0, 4)));
                        user.setQr_code(user.getId() + "");
                        user.setMy_money(userOb.getInt("my_money"));
                        user.setShambe_money(userOb.getInt("shambe_money"));

                        sessionHelper.saveUser(user);
                        sessionHelper.setLogin(true, user.getPhone());
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "مشکلی رخ داد", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                spinKit.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "مشکلی رخ داد", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("phone", etPhone.getText().toString());
                map.put("api_key", "san75amji76");
                map.put("confirm_code", etConfirmCode.getText().toString());
                return map;
            }
        };

        App.addToQueue(stringRequest);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNameNext: {
                spinKit.setVisibility(View.VISIBLE);
                Animation RightSwipe = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.anim_left_to_right);
                etName.startAnimation(RightSwipe);
                btnNameNext.startAnimation(RightSwipe);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        etName.setVisibility(View.GONE);
                        btnNameNext.setVisibility(View.GONE);
                        spinKit.setVisibility(View.GONE);
                        String msg = "خب " + etName.getText().toString() + " جونم، حالا آفرین شماره تو بده بم ببینم";
                        write(msg, 0, tvMessage, 1);
                    }
                }, 300);

                break;
            }

            case R.id.btnGetPhone: {
                request();
                break;
            }

            case R.id.btnConfirmCode: {
                requestUser();
                break;
            }
            case R.id.btnGetPhoneBack: {

//                Animation RightSwipe = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.anim_right_to_left);
//                etName.startAnimation(RightSwipe);
//                btnNameNext.startAnimation(RightSwipe);
//                etName.setVisibility(View.VISIBLE);
//                btnNameNext.setVisibility(View.VISIBLE);
                etPhone.setVisibility(View.GONE);
                btnGetPhone.setVisibility(View.GONE);
                btnGetPhoneBack.setVisibility(View.GONE);

                String msg = "چی شد " + etName.getText().toString() + " " + "جون، اسمت رو درست وارد نکرده بودی؟ اشکال نداره دوباره بزن";
                clearWriteAndWrite(msg, 0, tvMessage, 0, tvMessage.getText().toString().length());
//                write(msg, 0, tvMessage, 0);


                break;
            }

            case R.id.btnConfirmCodeBack: {
                Animation RightSwipe = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.anim_right_to_left);
//                etName.startAnimation(RightSwipe);
//                btnNameNext.startAnimation(RightSwipe);
                etPhone.setVisibility(View.VISIBLE);
                btnGetPhone.setVisibility(View.VISIBLE);
                etConfirmCode.setVisibility(View.GONE);
                btnConfirmCode.setVisibility(View.GONE);
                btnConfirmCodeBack.setVisibility(View.GONE);
                tvMessage.setText("");
                break;
            }

        }
    }
}
