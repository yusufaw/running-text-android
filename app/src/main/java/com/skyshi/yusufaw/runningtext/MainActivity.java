package com.skyshi.yusufaw.runningtext;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private static final String TAG = "zzzz";
    private RecyclerView recyclerView;
    private RunningTextAdapter adapter;

    private Handler handlerRunningAds = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = (RecyclerView) findViewById(R.id.running_text);

        List<AdsRunningText> adsRunningTextList = new ArrayList<>();
        adsRunningTextList.add(new AdsRunningText(1, "Dan jika kami bersama", 0));
        adsRunningTextList.add(new AdsRunningText(2, "Kami adalah kamu", 0));
        adsRunningTextList.add(new AdsRunningText(3, "Jadilah legenda", 0));
        adsRunningTextList.add(new AdsRunningText(4, "Pertarungan abad setan malaikat", 0));
        adsRunningTextList.add(new AdsRunningText(5, "Aku bukan pahlawan berparas tampan", 0));

        final LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false) {
            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                LinearSmoothScroller smoothScroller = new LinearSmoothScroller(MainActivity.this) {

                    private static final float SPEED = 5000f;

                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return SPEED / displayMetrics.densityDpi;
                    }
                };
                smoothScroller.setTargetPosition(position);
                startSmoothScroll(smoothScroller);
            }
        };
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RunningTextAdapter(adsRunningTextList);
        recyclerView.setAdapter(adapter);

        autoScroll();
    }

    public void autoScroll() {
        handlerRunningAds.postDelayed(runnableRunningAds, 1000);
    }

    private Runnable runnableRunningAds = new Runnable() {
        @Override
        public void run() {
            Integer intPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
            if (intPosition == 1) {
                adapter.getData().get(0).intTotalPlayed++;
                Log.d(TAG, "index: " + adapter.getData().get(0).intId + "total: " + adapter.getData().get(0).intTotalPlayed);
                adapter.moveItems();
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
            }
            recyclerView.smoothScrollToPosition(adapter.getItemCount());

            handlerRunningAds.postDelayed(this, 10);
        }
    };

}
