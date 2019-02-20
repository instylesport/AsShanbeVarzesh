package ir.amjadrad.asshanbevarzesh.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ir.amjadrad.asshanbevarzesh.R;
import ir.amjadrad.asshanbevarzesh.app.App;
import ir.amjadrad.asshanbevarzesh.app.Endpoint;
import ir.amjadrad.asshanbevarzesh.helper.SessionHelper;
import ir.amjadrad.asshanbevarzesh.object.User;

public class FragmentScanner extends Fragment {
//    private ZBarScannerView mScannerView;

    private User user;

    public CodeScanner mCodeScanner;
    private CodeScannerView scannerView;
    private boolean garanted = false;
    private LayoutInflater inflater;
    ViewGroup viewGroup;
    Bundle bundle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.inflater = inflater;
        this.viewGroup = container;
        this.bundle = savedInstanceState;
        View rootView = inflater.inflate(R.layout.fragment_scanner, container, false);
        scannerView = rootView.findViewById(R.id.scanner_view);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 100);
        } else {
            garanted = true;
            prepare();
            onResume();
        }

        user = new SessionHelper(getActivity()).getUser();
        return rootView;
    }






    private void prepare() {

        final Activity activity = getActivity();
        try {
//        View root = inflater.inflate(R.layout.fragment_scanner, container, false);

            mCodeScanner = new CodeScanner(activity, scannerView);
            mCodeScanner.setDecodeCallback(new DecodeCallback() {
                @Override
                public void onDecoded(@NonNull final Result result) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                final String qr = result.getText();

                                final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();

                                final View v = LayoutInflater.from(getContext()).inflate(R.layout.popup_pay_to_someone, null);
                                final View v2 = LayoutInflater.from(getContext()).inflate(R.layout.popup_pay_to_loading, null);
                                final TextView tvName = v.findViewById(R.id.tvName);

                                StringRequest requestName = new StringRequest(Request.Method.POST, Endpoint.url_get_user_name, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            Log.i(">>>>>>>>>", response);
                                            JSONObject userOb = jsonObject.getJSONObject("msg");
                                            tvName.setText(userOb.getString("firstname") + " " + userOb.getString("lastname"));
                                            alertDialog.setView(v);
                                            yourid = userOb.getInt("id");
//                        Toast.makeText(getContext(), ""+yourid, Toast.LENGTH_SHORT).show();
                                            yourname = userOb.getString("firstname") + " " + userOb.getString("lastname");
                                            alertDialog.show();

                                        } catch (Exception e) {
                                            Toast.makeText(getContext(), "این کیوآر کد پشتیبانی نمی شود.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> map = new HashMap<>();
                                        map.put("qr_code", qr);
                                        return map;
                                    }
                                };

                                App.addToQueue(requestName);


                                final EditText editText = v.findViewById(R.id.etPrice);
                                Button button = v.findViewById(R.id.btnPay);
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String str = editText.getText().toString();
                                        try {
                                            int price = Integer.parseInt(str);
                                            if (price >= 1000 && price <= 100000000) {
                                                alertDialog.dismiss();
                                                alertDialog1 = new AlertDialog.Builder(getContext()).create();
                                                alertDialog1.setView(v2);
                                                alertDialog1.show();
//                            Toast.makeText(getContext(), ""+user.getId(), Toast.LENGTH_SHORT).show();
                                                request(user.getId(), yourid, price);
                                            } else {
                                                Toast.makeText(getContext(), "مبلغ باید بین 10000 تا 10 میلیون ریال باشد", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (Exception e) {
                                            Toast.makeText(getContext(), "لطفا در وارد کردن ورودی دقت کنید", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });

                            } catch (Exception e) {
//                            mScannerView.resumeCameraPreview(this);
                                Toast.makeText(getContext(), "این کیوآر کد پیشتیبانی نمی شود", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });
                }
            });
            scannerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCodeScanner.startPreview();
                }
            });


        } catch (Exception e) {

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (garanted) {
                    mCodeScanner.startPreview();
                }
            }
        }, 30);

    }

    @Override
    public void onPause() {

        if (garanted) {
            mCodeScanner.releaseResources();
        }

        super.onPause();
    }


    AlertDialog alertDialog1;
    int yourid = -1;
    String yourname = "";


    private void request(final int myid, final int yourid, final int price) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoint.url_transfer_user_to_user, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i(">>>>>>>>>", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("error")) {
                        alertDialog1.dismiss();
                        alertDialog1 = new AlertDialog.Builder(getContext()).create();
                        View v3 = LayoutInflater.from(getContext()).inflate(R.layout.popup_pay_to_message, null);

                        TextView tvFromName, tvToName, tvPrice, tvState;
                        Button close = v3.findViewById(R.id.btnClose);
                        tvFromName = v3.findViewById(R.id.tvFromName);
                        tvState = v3.findViewById(R.id.tvState);
                        tvToName = v3.findViewById(R.id.tvToName);
                        tvPrice = v3.findViewById(R.id.tvPrice);

                        tvFromName.setText("از: " + user.getName());
                        tvToName.setText("به: " + yourname);
                        tvPrice.setText("مبلغ: " + price);
                        tvState.setText("موجودی ناکافی");
                        tvState.setTextColor(Color.parseColor("#ee2200"));
                        alertDialog1.setView(v3);

                        alertDialog1.show();
                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog1.dismiss();
//                                mScannerView.resumeCameraPreview(FragmentScanner.this);
                            }
                        });

//                        mScannerView.resumeCameraPreview(FragmentScanner.this);
                    } else {
                        alertDialog1.dismiss();
                        alertDialog1 = new AlertDialog.Builder(getContext()).create();
                        View v3 = LayoutInflater.from(getContext()).inflate(R.layout.popup_pay_to_message, null);
                        alertDialog1.setCancelable(false);
                        alertDialog1.setCanceledOnTouchOutside(false);
                        TextView tvFromName, tvToName, tvPrice;
                        Button close = v3.findViewById(R.id.btnClose);
                        tvFromName = v3.findViewById(R.id.tvFromName);
                        tvToName = v3.findViewById(R.id.tvToName);
                        tvPrice = v3.findViewById(R.id.tvPrice);

                        tvFromName.setText("از: " + user.getName());
                        tvToName.setText("به: " + yourname);
                        tvPrice.setText("مبلغ: " + price);
                        alertDialog1.setView(v3);

                        alertDialog1.show();
                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog1.dismiss();
//                                mScannerView.resumeCameraPreview(FragmentScanner.this);
                            }
                        });
//                        Toast.makeText(getContext(), "انتقال وجه با موفقیت انجام شد", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "خطایی رخ داد", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "خطایی رخ داد", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("myid", "" + myid);
                map.put("yourid", "" + yourid);
                map.put("price", "" + price);
                return map;
            }
        };

        App.addToQueue(stringRequest);
    }
}
