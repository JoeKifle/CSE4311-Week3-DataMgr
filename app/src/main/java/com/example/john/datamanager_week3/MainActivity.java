package com.example.john.datamanager_week3;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    SwitchCompat mainSwitchOnOffSw;
    TextView tv_conn;

    //getting sent and received data
    private Handler mHandler = new Handler();
    private long mStartRX = 0;
    private long mStartTX = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_conn = (TextView) findViewById(R.id.connType);

        //getting sent and received data
        mStartRX = TrafficStats.getTotalRxBytes();
        mStartTX = TrafficStats.getTotalTxBytes();

        // check wether the device support the statistics
        if (mStartRX == TrafficStats.UNSUPPORTED || mStartTX == TrafficStats.UNSUPPORTED) {

            Toast.makeText(this, "Your device does not support traffic stat monitoring.", Toast.LENGTH_LONG).show();

        } else {
            mHandler.postDelayed(mRunnable, 1000);
        }
    }

    private final Runnable mRunnable = new Runnable() {
        public void run() {

            // getTotalRxBytes, returns number of packets received across mobile networks since device boot.

            if(isDataEnabled()){
                TextView RX = (TextView) findViewById(R.id.RX);
                TextView TX = (TextView) findViewById(R.id.TX);
                long rxBytes = (TrafficStats.getTotalRxBytes() - mStartRX) / 1024;
                RX.setText("RX: "+Long.toString(rxBytes)+"kb");
                long txBytes = (TrafficStats.getTotalTxBytes() - mStartTX) / 1024;
                TX.setText("TX: "+Long.toString(txBytes)+"kb");
                mHandler.postDelayed(mRunnable, 1000);

            }


        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.action_menu, menu);
        MenuItem item = menu.getItem(0);
        if (item.getItemId() == R.id.myswitch) {
            View view = MenuItemCompat.getActionView(item);
            if (view != null) {
                mainSwitchOnOffSw = (SwitchCompat) view.findViewById(R.id.switchForActionBar);
                //adding a listener for this switch

                mainSwitchOnOffSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        // added this method to implement the code
                        switchChangeListener(buttonView,isChecked);
                    }
                });

            }
        }


        return true;
    }


    public boolean isDataEnabled(){

        boolean mobileYN = false;

        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (tm.getSimState() == TelephonyManager.SIM_STATE_READY) {
            if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1)
            {
                mobileYN = Settings.Global.getInt(this.getContentResolver(), "mobile_data", 1) == 1;
            }
            else{
                mobileYN = Settings.Secure.getInt(this.getContentResolver(), "mobile_data", 1) == 1;
            }
        }

        Log.d("data_status: ", ""+mobileYN);

        return  mobileYN;
    }

    public String getNetworkClass(Context context) {
        TelephonyManager mTelephonyManager = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        int networkType = mTelephonyManager.getNetworkType();
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "2G";
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "3G";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "4G";
            case TelephonyManager.NETWORK_TYPE_NR:
                return "5G";
            default:
                return "Unknown";
        }
    }


    public void switchChangeListener(CompoundButton cpdButton, boolean isChecked){

        if(isChecked){

            if(!isDataEnabled()){

                //checking android version
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    // only for Lollipop and newer versions

                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings$DataUsageSummaryActivity"));
                    startActivity(intent);
                    mainSwitchOnOffSw.setChecked(false);

                }else{
                    // create a method to turn on data for elder versions and call it here.

                 }
             }else{


                String network_class = getNetworkClass(getBaseContext());

               // Toast.makeText(getBaseContext(),"Connection Type: "+network_class,Toast.LENGTH_SHORT).show();

                tv_conn.setText("Connected to: "+network_class);
            }
        }else{

            // Turning down connection
            




        }
    }




}
