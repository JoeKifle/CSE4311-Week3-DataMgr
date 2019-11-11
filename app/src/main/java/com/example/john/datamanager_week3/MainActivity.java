package com.example.john.datamanager_week3;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.action_menu, menu);
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.getItemId() == R.id.myswitch) {
                View view = MenuItemCompat.getActionView(item);
                if (view != null) {
                    SwitchCompat mainSwitchOnOffSw = (SwitchCompat) view.findViewById(R.id.switchForActionBar);
                    //to do add a listener for this switch

                    mainSwitchOnOffSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){


                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                            // toggle code goes here
                        }
                    });

                }
            }
        }

        return true;
    }


}
