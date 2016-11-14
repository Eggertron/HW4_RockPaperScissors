package com.example.eggertron.hw4_rockpaperscissors;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
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

    //Create Finals
    public final static String WINS = "WINS",
            LOSSES = "LOSSES", TIES = "TIES",
            MYHAND = "MYHAND", YOURHAND = "YOURHAND";

    private BoxInsetLayout mContainerView;
    WearableListView wearableListView;
    // Coming back from the end of Adapter.java
    String[] elements = {"Rock", "Paper", "Scissors"};
    Adapter adapter;
    CommHandler commHandler;
    Scores scores;
    TextView txtScores;
    private final int RESET = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        mContainerView = (BoxInsetLayout) findViewById(R.id.container);
        wearableListView = (WearableListView) findViewById(R.id.wearable_list);
        // Initialize the adapter
        adapter = new Adapter(this, elements);
        //Add the adapter to wearablelistView
        wearableListView.setAdapter(adapter);
        wearableListView.setClickListener(this);

        commHandler = new CommHandler(this);
        scores = new Scores();
        resetHands();
        txtScores = (TextView)findViewById(R.id.txtScores);
        updateScores();
        commHandler.sendMessage(7); // maybe the game was already played?

        /*
        int notificationID = 1;
        //The intent allows user opens the activity on the phone
        Intent viewIntent = new Intent(this, MainActivity.class);
        PendingIntent viewPendingIntent = PendingIntent.getActivity(this, 0, viewIntent, 0);
        //Use the notification builder to create a notification
        NotificationCompat.Builder notificationBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setContentTitle("Rock Paper Scissors!")
                        .setContentText("User has picked a hand shape.")
                        .setContentIntent(viewPendingIntent);
        //Send the notification
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        //notificationManagerCompat.notify(notificationID, notificationBuilder.build());
        */

        int notificationId = 001;
        // Build intent for notification content
        Intent viewIntent = new Intent(this, MainActivity.class);
        //viewIntent.putExtra(EXTRA_EVENT_ID, eventId);
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(this, 0, viewIntent, 0);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.open_on_phone)
                        .setContentTitle("RockPaperScissors!")
                        .setContentText("Let's Play!")
                        .setContentIntent(viewPendingIntent);

        // Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);

        // Build the notification and issues it with notification manager.
        notificationManager.notify(notificationId, notificationBuilder.build());
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
        } else {
            mContainerView.setBackground(null);
        }
    }

    @Override
    public void onClick(WearableListView.ViewHolder viewHolder) {
        int posi = viewHolder.getAdapterPosition();
        if (scores.me == RESET) {
            scores.me = posi;
            wearableListView.setBackgroundColor(Color.BLUE);
            commHandler.sendMessage(scores.me);
            updateScores();
        }
        compareHandShapes();
        int notificationId = 001;
        // Build intent for notification content
        Intent viewIntent = new Intent(this, MainActivity.class);
        //viewIntent.putExtra(EXTRA_EVENT_ID, eventId);
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(this, 0, viewIntent, 0);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.open_on_phone)
                        .setContentTitle("RockPaperScissors!")
                        .setContentText("Let's Play!")
                        .setContentIntent(viewPendingIntent);

        // Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);

        // Build the notification and issues it with notification manager.
        notificationManager.notify(notificationId, notificationBuilder.build());
    }

    private void updateScores() {
        txtScores.setText("Wins: " + scores.wins + ", Losses: " + scores.losses + ", Ties: " +
                            scores.ties + ", you: " +  scores.me +
                ", them: " + scores.opponent);
    }

    /*
       Compares the handShapes and updates scores
    */
    public void compareHandShapes() {
        if (scores.me != RESET && scores.opponent != RESET) {
            int me = scores.me,
                you = scores.opponent;

            if (me == 0) { // I'M ROCK
                if (you == 4) {
                    // TIE
                    scores.ties++;
                    updateScores();
                    resetHands();
                    tieToast();
                }
                else if (you == 5) {
                    // LOSS
                    scores.losses++;
                    updateScores();
                    resetHands();
                    lossToast();
                }
                else if (you == 6) {
                    // WIN
                    scores.wins++;
                    updateScores();
                    resetHands();
                    winToast();
                }
            }
            else if (me == 1) { // I'M PAPER
                if (you == 4) {
                    // WIN
                    scores.wins++;
                    updateScores();
                    resetHands();
                    winToast();
                }
                else if (you == 5) {
                    // TIE
                    scores.ties++;
                    updateScores();
                    resetHands();
                    tieToast();
                }
                else if (you == 6) {
                    // LOSS
                    scores.losses++;
                    updateScores();
                    resetHands();
                    lossToast();
                }
            }
            else if (me == 2) { // I'M SCISSORS
                if (you == 4) {
                    // LOSS
                    scores.losses++;
                    updateScores();
                    resetHands();
                    lossToast();
                }
                else if (you == 5) {
                    // WIN
                    scores.wins++;
                    updateScores();
                    resetHands();
                    winToast();
                }
                else if (you == 6){
                    // TIE
                    scores.ties++;
                    updateScores();
                    resetHands();
                    tieToast();
                }
            }
        }

    }

    private void resetHands() {
        scores.me = RESET;
        scores.opponent = RESET;
        wearableListView.setBackgroundColor(Color.WHITE);
    }

    public void setOpponent(int hand) {
        if (hand == 7) {
            // this is not a hand shape update. its'a query
            commHandler.sendMessage(scores.me);
        }
        else if (hand > 3){
            scores.opponent = hand;
            compareHandShapes();
        }
    }

    public void updateColor(int colorIndex) {
        switch (colorIndex) {
            case 0:
                wearableListView.setBackgroundColor(Color.BLUE);
                break;
            case 1:
                wearableListView.setBackgroundColor(Color.BLUE);
                break;
            case 2:
                wearableListView.setBackgroundColor(Color.BLUE);
                break;
            case 3:
                wearableListView.setBackgroundColor(Color.BLUE);
                break;
            default:
                wearableListView.setBackgroundColor(Color.BLUE);
                break;
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        scores = new Scores(savedInstanceState.getInt(WINS),
                savedInstanceState.getInt(LOSSES),
                savedInstanceState.getInt(TIES),
                savedInstanceState.getInt(MYHAND),
                savedInstanceState.getInt(YOURHAND));
        // check if the opponent had updated their hand.
        commHandler.sendMessage(7);
        updateScores();
        if (scores.me != RESET) {
            updateColor(0); // doesn't matter what color.
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putInt(WINS, scores.wins);
        outState.putInt(LOSSES, scores.losses);
        outState.putInt(TIES, scores.ties);
        outState.putInt(MYHAND, scores.me);
        outState.putInt(YOURHAND, scores.opponent);
        super.onSaveInstanceState(outState);
    }

    public void winToast() {
        String me, you;
        if (scores.me == 0) me = "Rock";
        else if (scores.me == 1) me = "Paper";
        else me = "Scissors";
        if (scores.opponent == 4) you = "Rock";
        else if (scores.opponent == 5) you = "Paper";
        else you = "Scissors";
        showDialog("You Win!\n" + me + " beats " + you);
    }

    public void lossToast() {
        String me, you;
        if (scores.me == 0) me = "Rock";
        else if (scores.me == 1) me = "Paper";
        else me = "Scissors";
        if (scores.opponent == 4) you = "Rock";
        else if (scores.opponent == 5) you = "Paper";
        else you = "Scissors";
        showDialog("You Lose!\n" + me + " loss to " + you);
    }

    public void tieToast() {
        String me, you;
        if (scores.me == 0) me = "Rock";
        else if (scores.me == 1) me = "Paper";
        else me = "Scissors";
        if (scores.opponent == 4) you = "Rock";
        else if (scores.opponent == 5) you = "Paper";
        else you = "Scissors";
        showDialog("Tie game!\n" + me + " equals " + you);
    }

    public void showDialog(String msg) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(msg);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @Override
    public void onTopEmptyRegionClick() {

    }
}
