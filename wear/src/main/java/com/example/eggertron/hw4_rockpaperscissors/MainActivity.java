package com.example.eggertron.hw4_rockpaperscissors;

import android.graphics.Color;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.support.wearable.view.WearableListView;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/*
    Wearable Color Picker
    Look at the comments in the mainactivity of the mobile.
 */
public class MainActivity extends WearableActivity implements WearableListView.ClickListener {

    private static final SimpleDateFormat AMBIENT_DATE_FORMAT =
            new SimpleDateFormat("HH:mm", Locale.US);

    private BoxInsetLayout mContainerView;
    //private TextView mTextView;
    //private TextView mClockView;
    WearableListView wearableListView;
    // Coming back from the end of Adapter.java
    String[] elements = {"Rock", "Paper", "Scissors", "Play"};
    Adapter adapter;
    CommHandler commHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        mContainerView = (BoxInsetLayout) findViewById(R.id.container);
        //mTextView = (TextView) findViewById(R.id.text);
        //mClockView = (TextView) findViewById(R.id.clock);
        wearableListView = (WearableListView) findViewById(R.id.wearable_list);
        // Initialize the adapter
        adapter = new Adapter(this, elements);
        //Add the adapter to wearablelistView
        wearableListView.setAdapter(adapter);
        wearableListView.setClickListener(this);

        commHandler = new CommHandler(this);
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
    }

    private void updateDisplay() {
        if (isAmbient()) {
            mContainerView.setBackgroundColor(getResources().getColor(android.R.color.black));
            //mContainerView.setBackgroundColor(getResources().getColor(android.R.color.black));
            //mTextView.setTextColor(getResources().getColor(android.R.color.white));
            //mClockView.setVisibility(View.VISIBLE);

            //mClockView.setText(AMBIENT_DATE_FORMAT.format(new Date()));
        } else {
            mContainerView.setBackground(null);
            //mTextView.setTextColor(getResources().getColor(android.R.color.black));
            //mClockView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(WearableListView.ViewHolder viewHolder) {
        int posi = viewHolder.getAdapterPosition();
        updateColor(posi);
        commHandler.sendMessage(posi);
    }

    public void updateColor(int colorIndex) {
        switch (colorIndex) {
            case 0:
                wearableListView.setBackgroundColor(Color.BLUE);
                break;
            case 1:
                wearableListView.setBackgroundColor(Color.YELLOW);
                break;
            case 2:
                wearableListView.setBackgroundColor(Color.rgb(102, 0, 0));
                break;
            case 3:
                wearableListView.setBackgroundColor(Color.rgb(255, 102, 0));
                break;
            default:
                wearableListView.setBackgroundColor(Color.WHITE);
                break;
        }
    }

    @Override
    public void onTopEmptyRegionClick() {

    }
}
