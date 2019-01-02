package com.litchiny.customviews.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.litchiny.customviews.R;
import com.litchiny.customviews.view.BatteryView;

public class IconsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icons);
        initView();
    }

    private void initView() {
        BatteryView bv_title_battery = findViewById(R.id.bv_title_battery);
        bv_title_battery.setData(30,10);
    }
}
