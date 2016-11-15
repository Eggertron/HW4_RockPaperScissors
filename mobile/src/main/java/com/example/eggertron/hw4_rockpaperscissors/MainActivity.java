package com.example.eggertron.hw4_rockpaperscissors;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

/*
    Color Picker App: First app created for use with Android Wear
    SETUP 1:
    Create new project and check boxes for Phone and Tablet and Wear.
    Select Minimum SDK API 21: 5.0 Lollipop
    Select "Always On Wear Activity"
    SETUP 2:
    Implement the UI
    SETUP 3: goto MainActivity Code
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Create Finals
    public final static String WINS = "WINS",
            LOSSES = "LOSSES", TIES = "TIES",
            MYHAND = "MYHAND", YOURHAND = "YOURHAND";

    ScrollView bg; // Reference to the ScrollView
    TextView rock, paper, scissors;
    CommHandler commHandler;
    Scores scores;
    Toast toast;
    TextView txtScores;
    private final int RESET = 99, aRock = 70, bRock = 60,
                        aPaper = 71, bPaper = 61, aScissors = 72, bScissors = 62;

    /*
        Changes the background color based on colorIndex.
     */
    public void updateColor(int colorIndex) {
        switch (colorIndex) {
            case aRock:
                rock.setBackgroundColor(Color.BLUE);
                break;
            case aPaper:
                paper.setBackgroundColor(Color.BLUE);
                break;
            case aScissors:
                scissors.setBackgroundColor(Color.BLUE);
                break;
        }
    }

    private void updateScores() {
        txtScores.setText("Wins: " + scores.wins + ", Losses: " + scores.losses +
                            ", Ties: " + scores.ties);
    }

    /*
        Handle the messages recieved from the game.
     */
    public void updateOpponentHand(int hand) {
        if (hand == 8) { // this was not a handshape update, this was query
            commHandler.sendMessage(scores.me);
        }
        else if (hand >= bRock || hand <= bScissors){
            scores.opponent = hand;
            // check to see if I had already chose a handshape
            // compare and see who wins
            compareHandShapes();
        }
    }

    /*
        Compares the handShapes and updates scores
     */
    public void compareHandShapes() {
        if (true) {
            int me = scores.me,
                    you = scores.opponent;

            if (me == aRock) { // I'M ROCK
                if (you == bRock) {
                    // TIE
                    scores.ties++;
                    tieToast();
                    // reset
                    updateScores();
                    resetHands();
                }
                else if (you == bPaper) {
                    // LOSS
                    scores.losses++;
                    lossToast();
                    // reset
                    updateScores();
                    resetHands();
                }
                else if (you == bScissors) {
                    // WIN
                    scores.wins++;
                    winToast();
                    // reset
                    updateScores();
                    resetHands();
                }
            }
            else if (me == aPaper) { // I'M PAPER
                if (you == bRock) {
                    // WIN
                    scores.wins++;
                    winToast();
                    // reset
                    updateScores();
                    resetHands();
                }
                else if (you == bPaper) {
                    // TIE
                    scores.ties++;
                    tieToast();
                    // reset
                    updateScores();
                    resetHands();
                }
                else if (you == bScissors) {
                    // LOSS
                    scores.losses++;
                    lossToast();
                    // reset
                    updateScores();
                    resetHands();
                }
            }
            else if (me == aScissors){ // I'M SCISSORS
                if (you == bRock) {
                    // LOSS
                    scores.losses++;
                    lossToast();
                    // reset
                    updateScores();
                    resetHands();
                }
                else if (you == bPaper) {
                    // WIN
                    scores.wins++;
                    winToast();
                    // reset
                    updateScores();
                    resetHands();
                }
                else if (you == bScissors) {
                    // TIE
                    scores.ties++;
                    tieToast();
                    // reset
                    updateScores();
                    resetHands();
                }
            }
        }
    }

    private void winToast() {
        String me, you;
        if (scores.me == aRock) me = "Rock ✊";
        else if (scores.me == aPaper) me = "Paper ✋";
        else me = "Scissors ✌";
        if (scores.opponent == bRock) you = "Rock ✊";
        else if (scores.opponent == bPaper) you = "Paper ✋";
        else you = "Scissors ✌";
        toastMe("You Won!\n" + me + " beats " + you);
    }

    private void lossToast() {
        String me, you;
        if (scores.me == aRock) me = "Rock ✊";
        else if (scores.me == aPaper) me = "Paper ✋";
        else me = "Scissors ✌";
        if (scores.opponent == bRock) you = "Rock ✊";
        else if (scores.opponent == bPaper) you = "Paper ✋";
        else you = "Scissors ✌";
        toastMe("You Lost\n" + me + " lose to " + you);
    }

    private void tieToast() {
        String me, you;
        if (scores.me == aRock) me = "Rock ✊";
        else if (scores.me == aPaper) me = "Paper ✋";
        else me = "Scissors ✌";
        if (scores.opponent == bRock) you = "Rock ✊";
        else if (scores.opponent == bPaper) you = "Paper ✋";
        else you = "Scissors ✌";
        toastMe("Tie Game!\n" + me + " ties with " + you);
    }

    private void resetHands() {
        scores.me = RESET;
        scores.opponent = RESET;
        rock.setBackgroundColor(Color.WHITE);
        paper.setBackgroundColor(Color.WHITE);
        scissors.setBackgroundColor(Color.WHITE);
    }

    protected void toastMe(String msg) {
        toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onClick(View v) {
        String color = ((TextView) v).getText().toString();
        if (scores.me == RESET) { // only allow one selection
            if (color.equals("Rock ✊")) {
                    scores.me = aRock;
                    updateColor(scores.me);
                    commHandler.sendMessage(scores.me);
                    //commHandler.sendMessage(7);
                    updateScores();
                    compareHandShapes();
            } else if (color.equals("Paper ✋")) {
                scores.me = aPaper;
                updateColor(scores.me);
                commHandler.sendMessage(scores.me);
                //commHandler.sendMessage(7);
                updateScores();
                compareHandShapes();
            } else if (color.equals("Scissors ✌")) {
                scores.me = aScissors;
                updateColor(scores.me);
                commHandler.sendMessage(scores.me);
                //commHandler.sendMessage(7);
                updateScores();
                compareHandShapes();
            }
        }
        /*
        if (scores.me == RESET) { // only allow one selection
            switch (color) {
                case "Rock ✊":
                    scores.me = 4;
                    updateColor(scores.me);
                    commHandler.sendMessage(scores.me);
                    commHandler.sendMessage(7);
                    updateScores();
                    compareHandShapes();
                    break;
                case "Paper ✋":
                    scores.me = 5;
                    updateColor(scores.me);
                    commHandler.sendMessage(scores.me);
                    commHandler.sendMessage(7);
                    updateScores();
                    compareHandShapes();
                    break;
                case "Scissors ✌":
                    scores.me = 6;
                    updateColor(scores.me);
                    commHandler.sendMessage(scores.me);
                    commHandler.sendMessage(7);
                    updateScores();
                    compareHandShapes();
                    break;
            }
        }
        */


    }

    private void sendNotification() {
        /*
            The simplest way to make the watch interact with the
            mobile is using notification. You don’t even have to code
            anything for the mobile side. To do that you can use
            NotificationCompat and NotificationManagerCompat.
        */
        //Send a notification to the watch

        int notificationID = 1;
        //The intent allows user opens the activity on the phone
        Intent viewIntent = new Intent(this, MainActivity.class);
        PendingIntent viewPendingIntent = PendingIntent.getActivity(this, 0, viewIntent, 0);
        //Use the notification builder to create a notification
        NotificationCompat.Builder notificationBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.cast_ic_notification_small_icon)
                        .setContentTitle("RockPaperScissors!")
                        .setContentText("Let's Play!")
                        .setContentIntent(viewPendingIntent);
        //Send the notification
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(notificationID, notificationBuilder.build());
    }
    // Go and update the UI of the watch face.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bg = (ScrollView) findViewById(R.id.bg);
        rock = (TextView) findViewById(R.id.rock);
        paper = (TextView) findViewById(R.id.paper);
        scissors = (TextView) findViewById(R.id.scissors);
        rock.setOnClickListener(this);
        paper.setOnClickListener(this);
        scissors.setOnClickListener(this);
        commHandler = new CommHandler(this);
        txtScores = (TextView)findViewById(R.id.txtScores);
        scores = new Scores();
        resetHands();
        commHandler.sendMessage(7); // maybe the game was already played?
        updateScores();
        // Send a message to start on wear using commHandler
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

        //sendNotification();
        outState.putInt(WINS, scores.wins);
        outState.putInt(LOSSES, scores.losses);
        outState.putInt(TIES, scores.ties);
        outState.putInt(MYHAND, scores.me);
        outState.putInt(YOURHAND, scores.opponent);
        super.onSaveInstanceState(outState);
    }

    public void sendMessage(final String path, final String text, final GoogleApiClient mApiClient ) {
        new Thread( new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes( mApiClient ).await();
                for(Node node : nodes.getNodes()) {
                    MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                            mApiClient, node.getId(), path, text.getBytes() ).await();
                }

                runOnUiThread( new Runnable() {
                    @Override
                    public void run() {
                        //mEditText.setText( "" );
                    }
                });
            }
        }).start();
    }
}
