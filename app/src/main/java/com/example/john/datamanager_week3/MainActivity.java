package com.example.john.datamanager_week3;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.view.MenuItemCompat;
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
import android.widget.Toast;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    SwitchCompat mainSwitchOnOffSw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

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

                Toast.makeText(getBaseContext(),"Data is on",Toast.LENGTH_SHORT).show();

                String network_class = getNetworkClass(getBaseContext());

                Toast.makeText(getBaseContext(),"Connection Type: "+network_class,Toast.LENGTH_SHORT).show();

            }
        }else{

            // Turning down connection
            




        }
    }

}
