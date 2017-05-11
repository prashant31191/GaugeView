package io.sule.gaugeview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;


import java.util.Formatter;
import java.util.Locale;
import java.util.Random;

import io.fly.MainActivity;
import io.sule.gaugelibrary.GaugeView;


public class GaugeActivity extends Activity implements IBaseGpsListener{

    private GaugeView mGaugeView;
    private TextView txtCurrentSpeed;
    private TextView tvLog;
    String strLog = "..............";

    private final Random RAND = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gauge);

        mGaugeView = (GaugeView) findViewById(R.id.gauge_view);
         txtCurrentSpeed = (TextView) findViewById(R.id.txtCurrentSpeed);
        tvLog = (TextView) this.findViewById(R.id.tvLog);
        tvLog.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startActivity(new Intent(GaugeActivity.this, MainActivity.class));
                return false;
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

    }



    public void finish()
    {
        super.finish();
        System.exit(0);
    }


    private void updateSpeed(CLocation location) {
        // TODO Auto-generated method stub
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