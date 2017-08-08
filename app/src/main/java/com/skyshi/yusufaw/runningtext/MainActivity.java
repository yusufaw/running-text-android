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
    private List<AdsRunningText> runningTextList = new ArrayList<>();
    private int intIdCurrentRunningAds;

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

                    private static final float SPEED = 4000f;

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

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d(TAG, "new state: " + newState);
                if (newState > 0) {
                    Log.d(TAG, "move items");
                    adapter.moveItems();
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);
                }
            }
        });

        runningTextList = adsRunningTextList;

        autoScroll();
    }

    public void autoScroll() {
        handlerRunningAds.postDelayed(runnableRunningAds, 1000);
    }

    private Runnable runnableRunningAds = new Runnable() {
        @Override
        public void run() {
            Integer intPositionRun = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
            if (0 <= intPositionRun && intPositionRun < adapter.getData().size()) {
                AdsRunningText adsRunningTextAdapter = adapter.getData().get(intPositionRun);

                AdsRunningText adsRunningTextCurrent = new AdsRunningText();
                for (AdsRunningText adsRunningText :
                        runningTextList) {
                    if (adsRunningTextAdapter.intId == adsRunningText.intId) {
                        adsRunningTextCurrent = adsRunningText;
                        break;
                    }
                }
                Log.d(TAG, "Run auto scroll " + adsRunningTextCurrent.intId+ " - " + adsRunningTextCurrent.strBody + " = "+ adsRunningTextCurrent.intTotalPlayed);
                if (intIdCurrentRunningAds != adsRunningTextCurrent.intId) {
                    intIdCurrentRunningAds = adsRunningTextCurrent.intId;
                }
                recyclerView.smoothScrollToPosition(adapter.getItemCount());

                handlerRunningAds.postDelayed(this, 1000);
            }
        }
    };

}
