package com.litchiny.customviews.activity;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.litchiny.customviews.PublicConstant;
import com.litchiny.customviews.R;
import com.litchiny.customviews.model.CircleRingMode;
import com.litchiny.customviews.view.CircleRingView;

import java.util.ArrayList;
import java.util.List;

public class CircleRingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_ring);
        initView();
    }

    private void initView() {
        CircleRingView crv_show = findViewById(R.id.crv_show);
        List<CircleRingMode> sleepModes = new ArrayList<>();
        sleepModes.add(new CircleRingMode(10, ContextCompat.getColor(this, R.color.colorCircleRingType1)));
        sleepModes.add(new CircleRingMode(5, ContextCompat.getColor(this, R.color.colorCircleRingType2)));
        sleepModes.add(new CircleRingMode(25, ContextCompat.getColor(this, R.color.colorCircleRingType3)));
        sleepModes.add(new CircleRingMode(60, ContextCompat.getColor(this, R.color.colorCircleRingType4)));
        crv_show.setData(sleepModes);
    }
}
