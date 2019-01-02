package com.litchiny.customviews.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.litchiny.customviews.R;
import com.litchiny.customviews.model.ActivityDetailPoint;
import com.litchiny.customviews.utils.TimeUtil;
import com.litchiny.customviews.view.CurveDetailChart;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CurveDetailChartActivity extends AppCompatActivity {
    private List<ActivityDetailPoint> pointDayList;
    private int startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curve_detail_chart);
        initDayList();
        initView();
    }

    private void initDayList() {
        pointDayList = new ArrayList<>();
        long time = System.currentTimeMillis() / 1000L;
        startTime = (int) (TimeUtil.resetDayStart(Calendar.getInstance()).getTimeInMillis() / 1000);
        pointDayList.add(new ActivityDetailPoint(startTime + 60 * 60 * 1, 56));
        pointDayList.add(new ActivityDetailPoint(startTime + 60 * 60 * 2, 67));
        pointDayList.add(new ActivityDetailPoint(startTime + 60 * 60 * 3, 134));
        pointDayList.add(new ActivityDetailPoint(startTime + 60 * 60 * 4, 90));
        pointDayList.add(new ActivityDetailPoint(startTime + 60 * 60 * 5, 50));
        pointDayList.add(new ActivityDetailPoint(startTime + 60 * 60 * 6, 180));
        pointDayList.add(new ActivityDetailPoint(startTime + 60 * 60 * 7, 80));
        pointDayList.add(new ActivityDetailPoint(startTime + 60 * 60 * 8, 50));
        pointDayList.add(new ActivityDetailPoint(startTime + 60 * 60 * 9, 110));
        pointDayList.add(new ActivityDetailPoint(startTime + 60 * 60 * 10, 140));
        pointDayList.add(new ActivityDetailPoint(startTime + 60 * 60 * 11, 240));
        pointDayList.add(new ActivityDetailPoint(startTime + 60 * 60 * 17, 54));
        pointDayList.add(new ActivityDetailPoint(startTime + 60 * 60 * 21, 89));
        pointDayList.add(new ActivityDetailPoint(startTime + 60 * 60 * 23, 156));
    }

    private void initView() {
        CurveDetailChart cdc_show = findViewById(R.id.cdc_show);
        cdc_show.setData(pointDayList, startTime, startTime + 23 * 59 * 59, true);
    }
}
