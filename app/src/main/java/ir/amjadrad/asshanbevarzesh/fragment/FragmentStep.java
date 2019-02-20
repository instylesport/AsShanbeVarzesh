package ir.amjadrad.asshanbevarzesh.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import ir.amjadrad.asshanbevarzesh.R;
import ir.amjadrad.asshanbevarzesh.help_step_counter.StepDetector;
import ir.amjadrad.asshanbevarzesh.help_step_counter.StepListener;
import ir.amjadrad.asshanbevarzesh.helper.SessionHelper;

import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.SENSOR_SERVICE;

public class FragmentStep extends Fragment implements SensorEventListener, StepListener, View.OnClickListener {


    private TextView TvSteps, tvTimer, tvCaloriesMasrafShode, tvNeedCalories, tvGoalCalories, tvMoney;
    private Button BtnStart, BtnStop;
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private int numSteps = 0, numStepJust = 0;
    private ProgressBar progressBarStep, progressBarCalories, progressBarTimer;

    private ImageView imageViewCharacter;
    private CountDownTimer countDownTimer;

    private boolean started = false, finish = false;
    private SessionHelper sessionHelper;

    int time = 0;
    double bmr = 0;
    double usedCaloreis = 0;

    static double latA = 0;
    static double lonA = 0;
    static double latB = 0;
    static double lonB = 0;

    private TextView tvStartStop;
    private boolean start = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.LOCATION_HARDWARE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        Log.i("><>>>>>>", "1");

        View rootView = inflater.inflate(R.layout.fragment_step, container, false);
        progressBarStep = rootView.findViewById(R.id.progressBar);
        progressBarCalories = rootView.findViewById(R.id.progressBar2);
        progressBarTimer = rootView.findViewById(R.id.progressBar3);
        tvCaloriesMasrafShode = rootView.findViewById(R.id.tvCaloriesMasrafShode);
        tvNeedCalories = rootView.findViewById(R.id.tvNeedCalories);
        tvGoalCalories = rootView.findViewById(R.id.tvGoalCalories);
        tvMoney = rootView.findViewById(R.id.tvMoney);
        sessionHelper = new SessionHelper(getContext());
        tvStartStop = rootView.findViewById(R.id.tvStartStop);
        tvStartStop.setOnClickListener(this);
        bmr = sessionHelper.getUser().getBmr();
        tvNeedCalories.setText("کالری مورد نیاز روزانه: " + (int) bmr);

        tvGoalCalories.setText("+" + (int) (bmr + usedCaloreis));

        progressBarStep.setMax(50000);
        progressBarCalories.setMax(((int)bmr)+2500);//2500 + mycal
        progressBarCalories.setProgress((int) bmr);
        progressBarTimer.setMax(86400);//3600000


        tvTimer = rootView.findViewById(R.id.tvTimer);

        imageViewCharacter = rootView.findViewById(R.id.imageViewCharacter);


        // Get an instance of the SensorManager
        sensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);

        TvSteps = rootView.findViewById(R.id.tv_steps);


        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_FASTEST);


        try {
            if (sessionHelper.getUser().getSex_type() == 0) {
                characterAnimateBoy(0, 100);
            } else if (sessionHelper.getUser().getSex_type() == 1) {
                characterAnimateDad(0, 100);
            } else {
                characterAnimateMom(0, 100);
            }
        } catch (Exception e) {

        }


        LocationManager locationMaager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

//                latA = location.getLatitude();
//                lonA = location.getLongitude();
//                tvCaloriesMasrafShode.append("location : " + latA + " - " + lonA + "\n");
//                Toast.makeText(getContext(), latA+"", Toast.LENGTH_SHORT).show();

//                TvSteps.setText(numSteps);

//                Toast.makeText(getContext(), "num:" + numStepJust, Toast.LENGTH_SHORT).show();
//                if (start) {
//                    if (numStepJust <= 18) {
//                        numSteps += numStepJust;
//                        progressBarStep.setProgress(numSteps);
//                        TvSteps.setText("قدم شمار: " + numSteps + "");
//                        numStepJust = 0;
//                    } else {
//                        numSteps += 14;
//                        progressBarStep.setProgress(numSteps);
//                        numStepJust = 0;
//                        TvSteps.setText("قدم شمار: " + numSteps + "");
//                    }
//                }


            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        } else {
            locationMaager.requestLocationUpdates("gps", 7500, 10, locationListener);
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    //-----------------------------

    private void characterAnimateDad(final int what, final int time) {
        switch (what) {
            case 0: {
                try {
                    imageViewCharacter.setImageDrawable(getResources().getDrawable(R.drawable.dad4));
                } catch (Exception e) {

                }
                break;
            }
            case 1: {
                try {
                    imageViewCharacter.setImageDrawable(getResources().getDrawable(R.drawable.dad5));
                } catch (Exception e) {

                }
                break;
            }

        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int[] arr = {1000, 1100, 150, 1000, 1300, 120, 1100, 1000, 1100};
                int t = new Random().nextInt(arr.length);
                int r = new Random().nextInt(2);
                int w = 0;
//                if(what==1){
//                    w = r+1;
//                }
                w = what + 1;
                characterAnimateDad((w) % 2, arr[t]);
            }
        }, time);
    }

    private void characterAnimateMom(final int what, final int time) {
        switch (what) {
            case 0: {
                try {
                    imageViewCharacter.setImageDrawable(getResources().getDrawable(R.drawable.mom));
                } catch (Exception e) {

                }
                break;
            }
            case 1: {
                try {
                    imageViewCharacter.setImageDrawable(getResources().getDrawable(R.drawable.mom2));
                } catch (Exception e) {

                }
                break;
            }

        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int[] arr = {1000, 1100, 150, 1000, 1300, 120, 1100, 1000, 1100};
                int t = new Random().nextInt(arr.length);
                int r = new Random().nextInt(2);
                int w = 0;
//                if(what==1){
//                    w = r+1;
//                }
                w = what + 1;
                characterAnimateMom((w) % 2, arr[t]);
            }
        }, time);
    }


    private void characterAnimateBoy(final int what, final int time) {
        switch (what) {
            case 0: {
                try {
                    imageViewCharacter.setImageDrawable(getResources().getDrawable(R.drawable.boy_head));

                } catch (Exception e) {

                }
                break;

            }
            case 1: {
                try {
                    imageViewCharacter.setImageDrawable(getResources().getDrawable(R.drawable.boy2));
                } catch (Exception e) {

                }
                break;
            }

        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int[] arr = {1000, 1100, 150, 1000, 1300, 120, 1100, 1000, 1100};
                int t = new Random().nextInt(arr.length);
                int r = new Random().nextInt(2);
                int w = 0;
//                if(what==1){
//                    w = r+1;
//                }
                w = what + 1;
                characterAnimateBoy((w) % 2, arr[t]);
            }
        }, time);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void step(long timeNs) {

        if (!finish) {
            if (numSteps == progressBarStep.getMax() - 1) {
                Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    v.vibrate(1000);
                }
//            Toast.makeText(this, "تموم شد...", Toast.LENGTH_SHORT).show();
                finish = true;
                stopTimer();
                sensorManager.unregisterListener(FragmentStep.this);
                numSteps++;
//                progressBarStep.setProgress(numSteps);
//                numStepJust++;
                progressBarCalories.setProgress((int) (bmr + usedCaloreis));
                TvSteps.setText("" + numSteps);
            } else {
//                numStepJust++;
                numSteps++;
//                progressBarStep.setProgress(numSteps);
                TvSteps.setText("" + numSteps);
            }
            if (!started && !finish) {
                started = !started;
                timer();
            }

            String str = "کالری مصرف شده: ";
            usedCaloreis = numSteps / 20;
            str += "" + ((int) usedCaloreis);
            tvCaloriesMasrafShode.setText(str);
            tvMoney.setText("" + (int) (usedCaloreis * 10));
            tvGoalCalories.setText((int) (bmr + usedCaloreis) + "+");
        }

    }


    private void timer() {


        countDownTimer = new CountDownTimer(progressBarTimer.getMax() * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                if (!finish && start) {
                    tvTimer.setText(toHourTime(time));
                    time++;
                    progressBarTimer.setProgress(time);
                    progressBarStep.setProgress(numSteps);
//                    TvSteps.setText(numSteps+"");

                }
            }

            public void onFinish() {
                finish = true;
//                tvTimer.setText("try again");
            }

        }.start();

    }

    private void stopTimer() {
        countDownTimer.cancel();
    }

    private String toHourTime(int time) {

        int minutes, seconds;

        minutes = time / 60;
        seconds = time - 60 * (minutes);
        if (seconds < 10) {
            return "" + minutes + ":0" + seconds;
        } else {
            return "" + minutes + ":" + seconds;
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvStartStop: {
                if (start) {
                    tvStartStop.setText("شروع");
                    start = false;

                } else {
                    tvStartStop.setText("پایان");
                    start = true;
                }
                break;
            }
        }
    }

    //----------------------------------
}
