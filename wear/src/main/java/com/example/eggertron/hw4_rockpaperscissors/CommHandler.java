package com.example.eggertron.hw4_rockpaperscissors;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

/**
 * Created by eggertron on 11/3/16.
 */
public class CommHandler implements DataApi.DataListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    MainActivity mainActivity;
    GoogleApiClient googleApiClient;
    static Handler UIHandler = null;

    public static final String CURRENT_COLOR_INDEX="current color";


    public CommHandler(final MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        googleApiClient = new GoogleApiClient.Builder(mainActivity).addApi(Wearable.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        if(!googleApiClient.isConnected()) {
            googleApiClient.connect();
        }UIHandler = new Handler(Looper.getMainLooper()) {

            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0) { // message key expecting
                    mainActivity.updateColor((int)msg.obj);
                }
                super.handleMessage(msg);
            }
        };
    }

    public void sendMessage(int currentIndex) {
        new NumberSenderAsync().execute(currentIndex);
    }

    private class NumberSenderAsync extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            // the forward slash indicates that the key is for the root of the datamap
            PutDataMapRequest putDataMapRequest = PutDataMapRequest.create("/" + CURRENT_COLOR_INDEX);
            putDataMapRequest.getDataMap().putInt(CURRENT_COLOR_INDEX, params[0]);
            //putDataMapRequest.getDataMap().putString("a different key", "somestring here");
            PutDataRequest putDataRequest = putDataMapRequest.asPutDataRequest();
            Wearable.DataApi.putDataItem(googleApiClient, putDataRequest);
            return null;
        }
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Wearable.DataApi.addListener(googleApiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        for (DataEvent event:dataEventBuffer) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().compareTo("/" + CURRENT_COLOR_INDEX) == 0) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    int colorIndex = dataMap.getInt(CURRENT_COLOR_INDEX);
                    Message msg = UIHandler.obtainMessage(0, colorIndex); // with message key 0
                    msg.sendToTarget(); // sends from one thread to the other thread.
                }
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
