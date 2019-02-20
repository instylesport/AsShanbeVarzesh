package ir.amjadrad.asshanbevarzesh.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import ir.amjadrad.asshanbevarzesh.R;
import ir.amjadrad.asshanbevarzesh.activity.LoginActivity;
import ir.amjadrad.asshanbevarzesh.activity.MainActivity;
import ir.amjadrad.asshanbevarzesh.activity.MyQrCodeActivity;
import ir.amjadrad.asshanbevarzesh.app.App;
import ir.amjadrad.asshanbevarzesh.app.Endpoint;
import ir.amjadrad.asshanbevarzesh.helper.SessionHelper;
import ir.amjadrad.asshanbevarzesh.object.User;

public class FragmentWallet extends Fragment implements View.OnClickListener {

    private ImageButton imageButton_my_qr_code;
    private SessionHelper sessionHelper;
    private User user;
    private TextView tvWallet_all, tvWallet_user, tvWallet_app;

    private Button btnCharge, btnDisCharge;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_wallet, container, false);

        sessionHelper = new SessionHelper(getContext());
        user = sessionHelper.getUser();
        imageButton_my_qr_code = rootView.findViewById(R.id.imageButton_my_qr_code);
        imageButton_my_qr_code.setOnClickListener(this);

        tvWallet_all = rootView.findViewById(R.id.tvWallet_all);
        tvWallet_app = rootView.findViewById(R.id.tvWallet_app);
        tvWallet_user = rootView.findViewById(R.id.tvWallet_user);

        btnCharge = rootView.findViewById(R.id.btnCharge);
        btnDisCharge = rootView.findViewById(R.id.btnDisCharge);
        btnCharge.setOnClickListener(this);
        btnDisCharge.setOnClickListener(this);

        tvWallet_app.setText(user.getShambe_money() + "");
        tvWallet_user.setText(user.getMy_money() + "");
        tvWallet_all.setText((user.getMy_money() + user.getShambe_money()) + "");

        return rootView;
    }

    AlertDialog alertDialog2;

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.imageButton_my_qr_code: {
                startActivity(new Intent(getActivity(), MyQrCodeActivity.class));
                break;
            }
            case R.id.btnCharge: {
                final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                View v = LayoutInflater.from(getContext()).inflate(R.layout.popup_pay_to_account, null);
                final View v2 = LayoutInflater.from(getContext()).inflate(R.layout.popup_pay_to_loading, null);
                TextView tvName = v.findViewById(R.id.tvName);
                tvName.setText("کیف پول");
                final EditText editText = v.findViewById(R.id.etPrice);
                alertDialog.setView(v);
                alertDialog.show();
                Button btn = v.findViewById(R.id.btnPay);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestAddPrice(editText.getText().toString());
                        alertDialog.dismiss();
                        alertDialog2 = new AlertDialog.Builder(getContext()).create();
                        alertDialog2.setView(v2);
                        alertDialog2.show();
                    }
                });

                break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        requestUser();
    }

    private void requestUser() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoint.url_get_user, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("error")) {
                        Toast.makeText(getContext(), "مشکلی رخ داد", Toast.LENGTH_SHORT).show();
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
                        user.setBirth_m(Integer.parseInt(bd.substring(5,7)));
                        user.setBirth_y(Integer.parseInt(bd.substring(0,4)));
                        user.setQr_code(user.getId()+"");
                        user.setMy_money(userOb.getInt("my_money"));
                        user.setShambe_money(userOb.getInt("shambe_money"));
                        sessionHelper.saveUser(user);
                        user = sessionHelper.getUser();
                        tvWallet_app.setText(user.getShambe_money() + "");
                        tvWallet_user.setText(user.getMy_money() + "");
                        tvWallet_all.setText((user.getShambe_money() + user.getMy_money()) + "");


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "نمی تواند آپدیت شود", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "مشکلی رخ داد", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("id", sessionHelper.getUser().getId()+"");
                map.put("api_key", Endpoint.api_key);
                return map;
            }
        };

        App.addToQueue(stringRequest);
    }

    private void requestAddPrice(final String price) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoint.url_add_price, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                alertDialog2.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("error")) {
                        Toast.makeText(getContext(), "مشکلی رخ داد", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "با موفقیت انجام شد", Toast.LENGTH_SHORT).show();
                    }

                    requestUser();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "مشکلی رخ داد", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                alertDialog2.dismiss();
                Toast.makeText(getContext(), "مشکلی رخ داد", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("id", sessionHelper.getUser().getId()+"");
                map.put("price", price);
                return map;
            }
        };

        App.addToQueue(stringRequest);
    }

}
