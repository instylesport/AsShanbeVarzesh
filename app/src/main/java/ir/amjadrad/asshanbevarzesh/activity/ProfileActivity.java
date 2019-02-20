package ir.amjadrad.asshanbevarzesh.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ir.amjadrad.asshanbevarzesh.R;
import ir.amjadrad.asshanbevarzesh.app.App;
import ir.amjadrad.asshanbevarzesh.app.Endpoint;
import ir.amjadrad.asshanbevarzesh.helper.SessionHelper;
import ir.amjadrad.asshanbevarzesh.object.User;


public class ProfileActivity extends AppCompatActivity implements View.OnTouchListener {

    private Button btn;
    private EditText etPhone, etEmail, etBirthDayD, etBirthDayM, etBirthDayY, etHeight, etWeight, etActivity;
    private User user;
    private RadioButton radioButtonMan;
    private RadioGroup radioGroupActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        user = new User();

        radioButtonMan = findViewById(R.id.radioButtonMan);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etBirthDayD = findViewById(R.id.etBirthDay);
        etBirthDayM = findViewById(R.id.etBirthDaym);
        etBirthDayY = findViewById(R.id.etBirthDayy);
        etHeight = findViewById(R.id.etHeight);
        etWeight = findViewById(R.id.etWeight);
        etActivity = findViewById(R.id.etActivity);

        etActivity.setOnTouchListener(this);
        etEmail.setOnTouchListener(this);
        etPhone.setOnTouchListener(this);
        etBirthDayD.setOnTouchListener(this);
        etBirthDayM.setOnTouchListener(this);
        etBirthDayY.setOnTouchListener(this);
        etHeight.setOnTouchListener(this);
        etWeight.setOnTouchListener(this);

        radioGroupActivity = findViewById(R.id.radioGroupActivity);

        radioGroupActivity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                user.setActiivty_type(i);
            }
        });


        btn = findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean valid = true;

                if (!isValidHeight(etHeight.getText().toString())) {
                    Animation shake = AnimationUtils.loadAnimation(ProfileActivity.this, R.anim.shake);
                    etHeight.startAnimation(shake);
                    valid = false;
                }

                if (!isValidWeight(etWeight.getText().toString())) {
                    Animation shake = AnimationUtils.loadAnimation(ProfileActivity.this, R.anim.shake);
                    etWeight.startAnimation(shake);
                    valid = false;
                }

//                if (!isValidPhone(etPhone.getText().toString())) {
//                    Animation shake = AnimationUtils.loadAnimation(ProfileActivity.this, R.anim.shake);
//                    etPhone.startAnimation(shake);
//                    valid = false;
//                }

                if (!isValidEmail(etEmail.getText().toString())) {
                    Animation shake = AnimationUtils.loadAnimation(ProfileActivity.this, R.anim.shake);
                    etEmail.startAnimation(shake);
                    valid = false;
                }

                if (!isValidBirthD(etBirthDayD.getText().toString())) {
                    Animation shake = AnimationUtils.loadAnimation(ProfileActivity.this, R.anim.shake);
                    etBirthDayD.startAnimation(shake);
                    valid = false;
                }

                if (!isValidBirthM(etBirthDayM.getText().toString())) {
                    Animation shake = AnimationUtils.loadAnimation(ProfileActivity.this, R.anim.shake);
                    etBirthDayM.startAnimation(shake);
                    valid = false;
                }

                if (!isValidBirthY(etBirthDayY.getText().toString())) {
                    Animation shake = AnimationUtils.loadAnimation(ProfileActivity.this, R.anim.shake);
                    etBirthDayY.startAnimation(shake);
                    valid = false;
                }


                if (valid) {
                    user.setPhone(etPhone.getText().toString());
                    user.setEmail(etEmail.getText().toString());
                    user.setBirth_y(Integer.parseInt(etBirthDayY.getText().toString()));
                    user.setBirth_m(Integer.parseInt(etBirthDayM.getText().toString()));
                    user.setBirth_d(Integer.parseInt(etBirthDayD.getText().toString()));
                    user.setSex(radioButtonMan.isChecked());
                    user.setHeight(Integer.parseInt(etHeight.getText().toString()));
                    user.setWeight(Integer.parseInt(etWeight.getText().toString()));

                    request();

                } else {
                    Toast.makeText(ProfileActivity.this, "فیلدها را چک کنید", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0)
            getFragmentManager().popBackStack();
        else
            super.onBackPressed();

    }

    private void request() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoint.url_step_counter_save_user_data, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("error")) {
                        Toast.makeText(ProfileActivity.this, "خطایی رخ داد", Toast.LENGTH_SHORT).show();
                        Log.e(">>>>>>>>>>>e:", "خطای دیتابیس");
                    } else {
                        Toast.makeText(ProfileActivity.this, "با موفقیت ثبت شد", Toast.LENGTH_SHORT).show();
                        try {

//                            new SessionHelper(ProfileActivity.this).saveUser(user);


//                            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
//                            intent.putExtra("bmr", user.getBMR());
//                            intent.putExtra("sex_type", user.getSex_type());
//                            startActivity(intent);
                            onBackPressed();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(ProfileActivity.this, "لطفا فیلدها را طبق مثال پر کنید", Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (Exception e) {
                    Toast.makeText(ProfileActivity.this, "خطایی رخ داد", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(ProfileActivity.this, "خطایی رخ داد", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("id", new SessionHelper(getApplicationContext()).getUser().getId()+"");

//                map.put("phone", "" + user.getPhone());
                map.put("email", "" + user.getEmail());
                map.put("birth_d", "" + user.getBirth_d());
                map.put("birth_m", "" + user.getBirth_m());
                map.put("birth_y", "" + user.getBirth_y());
                map.put("sex", "" + user.isSex());
                map.put("height", "" + user.getHeight());
                map.put("weight", "" + user.getWeight());
                map.put("activity", "" + user.getActiivty_type());

                return map;
            }
        };

        App.addToQueue(stringRequest);

    }

    private boolean isValidHeight(String str) {

        try {
            int h = Integer.parseInt(str);
            if (h < 280 && h > 30) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

    }

    private boolean isValidWeight(String str) {

        try {
            int h = Integer.parseInt(str);
            if (h < 200 && h > 2) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

    }

    private boolean isValidEmail(String str) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();

    }

    private boolean isValidPhone(String str) {

        if (str.length() == 11) {
            if (str.startsWith("09")) {
                return true;
            }
        }

        return false;
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(20, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(20);
        }
        return false;
    }

    private boolean isValidBirthD(String str) {

        try {

            int i = Integer.parseInt(str);

            if (i > 0 && i < 32) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }
    }

    private boolean isValidBirthM(String str) {

        try {

            int i = Integer.parseInt(str);

            if (i > 0 && i < 13) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }
    }

    private boolean isValidBirthY(String str) {

        try {

            int i = Integer.parseInt(str);

            if (i > 1300 && i < (new Date().getYear() + 1900) - 620) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }
    }
}
