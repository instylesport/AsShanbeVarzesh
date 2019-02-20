package ir.amjadrad.asshanbevarzesh.app;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class App extends Application {

    private static RequestQueue requestQueue;
    public static String confirm_code = "";

    @Override
    public void onCreate() {
        super.onCreate();
        requestQueue = Volley.newRequestQueue(getApplicationContext());
    }

    public static void addToQueue(StringRequest stringRequest) {
        requestQueue.add(stringRequest);
    }
}
