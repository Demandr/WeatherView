package com.oleksandr.weatherviewer.ui.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.oleksandr.weatherviewer.R;
import com.oleksandr.weatherviewer.ui.fragment.WeatherFragment;


public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    @LayoutRes
    protected int getLayoutResId(){
        return R.layout.activity_container;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null){
            fragment = WeatherFragment.newInstance();
            fm.beginTransaction().add(R.id.fragment_container,fragment).commit();
        }
    }

}
