package com.litchiny.customviews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.litchiny.customviews.view.FlowLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FlowActivity extends AppCompatActivity {
    private static final String TAG = "FlowActivity";
    private FlowLayout fl_layout;
    private List<String> datas = new ArrayList<>();
    private String[] testArray = {"add1", "fgdjk", "yriwyfsfd", "1","hi","lemon","hahah"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow);
        initData();
        initView();
    }

    private void initData() {
        String[] arrays = {"orange", "phone", "computer", "vegetable", "litchi", "apple","+"};
        datas.addAll(Arrays.asList(arrays));
    }

    private void initView() {
        fl_layout = findViewById(R.id.fl_layout);
        fl_layout.setData(datas, true, 0);
        fl_layout.setFlowClickListener(new FlowLayout.FlowClickListener() {
            @Override
            public void setOnClickItemListener(int index) {
                Log.d(TAG, "setOnClickItemListener: index: " + index);
                if (index == datas.size() - 1) {
                    int lastSize = datas.size() - 1;
                    int indexC = (int) (Math.random() * (testArray.length) - 1);
                    datas.set(lastSize, testArray[indexC]);
                    datas.add("+");
                    fl_layout.setData(datas, true, lastSize);
                }
            }

            @Override
            public void setOnClickLongItemListener(int index) {
                if (index == datas.size() - 1) {
                    return;
                }
        datas.remove(index);
        fl_layout.removeItem(index);
        if (datas.size() == 1) {
            fl_layout.clearDeleteType();
        }
    }
});
    }
}
