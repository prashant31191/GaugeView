package io.sule.gaugeview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;


import java.util.Formatter;
import java.util.Locale;
import java.util.Random;

import io.demo.HomeActivity;
import io.fly.GPSTracker;
import io.fly.MainActivity;
import io.sule.gaugelibrary.GaugeView;


public class GaugeActivity extends Activity implements IBaseGpsListener{

    private GaugeView mGaugeView;
    private TextView txtCurrentSpeed;
    private Button bntHome;
    private TextView tvLog;
    String strLog = "..............";

    private final Random RAND = new Random();



    private static final int HANDLER_DELAY = 1000*5;
    private static final int START_HANDLER_DELAY = 500;
    Handler handler;
    GPSTracker gpsTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gauge);

        mGaugeView = (GaugeView) findViewById(R.id.gauge_view);
        bntHome = (Button) findViewById(R.id.bntHome);
         txtCurrentSpeed = (TextView) findViewById(R.id.txtCurrentSpeed);
        tvLog = (TextView) this.findViewById(R.id.tvLog);
        tvLog.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                strLog = "";
                tvLog.setText("");
                startActivity(new Intent(GaugeActivity.this, MainActivity.class));
                return false;
            }
        });

        bntHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GaugeActivity.this, HomeActivity.class));
                finish();
            }
        });


       // mTimer.start();

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        this.updateSpeed(null);

        CheckBox chkUseMetricUntis = (CheckBox) this.findViewById(R.id.chkMetricUnits);
        chkUseMetricUntis.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                GaugeActivity.this.updateSpeed(null);
            }
        });


        gpsTracker = new GPSTracker(GaugeActivity.this);



        handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {



                Location location = gpsTracker.getLocation();
                if(location != null)
                {
                    strLog = strLog +
                            "\n******************\n "+
                            "Lat # Lon --> \n " + location.getLatitude() + " # " + location.getLongitude()+
                            "\n******************\n ";

                    CLocation myLocation = new CLocation(location, useMetricUnits());
                    updateSpeed(myLocation);
                }


                handler.postDelayed(this, HANDLER_DELAY);
            }
        }, START_HANDLER_DELAY);


    }



    public void finish()
    {
        super.finish();
        System.exit(0);
    }

int i=0;
    private void updateSpeed(CLocation location) {
        // TODO Auto-generated method stub
        try{

        i = i+1;
        Log.i("111","====updateSpeed=====i==="+i);
       // Log.i("111","====location=====getLongitude==="+location.getLongitude());
       // Log.i("111","====location=====getLatitude==="+location.getLatitude());


        float nCurrentSpeed = 0;

        if(location != null)
        {
            location.setUseMetricunits(this.useMetricUnits());
            nCurrentSpeed = location.getSpeed();
        }

        Formatter fmt = new Formatter(new StringBuilder());
        fmt.format(Locale.US, "%5.1f", nCurrentSpeed);
        String strCurrentSpeed = fmt.toString();
        strCurrentSpeed = strCurrentSpeed.replace(' ', '0');

        String strUnits = "miles/hour";
        if(this.useMetricUnits())
        {
            strUnits = "meters/second";
        }

        strLog = strLog +
                "\n--------------\n "+
                strCurrentSpeed + " " + strUnits+
                "\n--------------\n ";

        txtCurrentSpeed.setText(strCurrentSpeed + " " + strUnits);
        tvLog.setText(strLog);

        float speed = Float.parseFloat(strCurrentSpeed);
        mGaugeView.setTargetValue(speed);}
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private boolean useMetricUnits() {
        // TODO Auto-generated method stub
        CheckBox chkUseMetricUnits = (CheckBox) this.findViewById(R.id.chkMetricUnits);
        return chkUseMetricUnits.isChecked();
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        if(location != null)
        {
            strLog = strLog +
                    "\n******************\n "+
                  "Lat # Lon --> \n " + location.getLatitude() + " # " + location.getLongitude()+
                    "\n******************\n ";


            CLocation myLocation = new CLocation(location, this.useMetricUnits());
            this.updateSpeed(myLocation);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onGpsStatusChanged(int event) {
        // TODO Auto-generated method stub

    }












































    private final CountDownTimer mTimer = new CountDownTimer(30000, 1000) {

        @Override
        public void onTick(final long millisUntilFinished) {
            mGaugeView.setTargetValue(RAND.nextInt(101));
        }

        @Override
        public void onFinish() {}
    };


}