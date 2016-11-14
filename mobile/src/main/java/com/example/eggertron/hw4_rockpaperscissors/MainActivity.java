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
    private final int RESET = 99;

    /*
        Changes the background color based on colorIndex.
     */
    public void updateColor(int colorIndex) {
        switch (colorIndex) {
            case 4:
                rock.setBackgroundColor(Color.BLUE);
                break;
            case 5:
                paper.setBackgroundColor(Color.BLUE);
                break;
            case 6:
                scissors.setBackgroundColor(Color.BLUE);
                break;
        }
    }

    private void updateScores() {
        txtScores.setText("Wins: " + scores.wins + ", Losses: " + scores.losses +
                            ", Ties: " + scores.ties + ", you: " +  scores.me +
                            ", them: " + scores.opponent);
    }

    /*
        Handle the messages recieved from the game.
     */
    public void updateOpponentHand(int hand) {
        if (hand == 7) { // this was not a handshape update, this was query
            commHandler.sendMessage(scores.me);
        }
        else if (hand < 3){
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
        if (scores.me != RESET && scores.opponent != RESET) {
            int me = scores.me,
                    you = scores.opponent;

            if (me == 4) { // I'M ROCK
                if (you == 0) {
                    // TIE
                    scores.ties++;
                    tieToast();
                    // reset
                    updateScores();
                    resetHands();
                }
                else if (you == 1) {
                    // LOSS
                    scores.losses++;
                    lossToast();
                    // reset
                    updateScores();
                    resetHands();
                }
                else if (you == 2) {
                    // WIN
                    scores.wins++;
                    winToast();
                    // reset
                    updateScores();
                    resetHands();
                }
            }
            else if (me == 5) { // I'M PAPER
                if (you == 0) {
                    // WIN
                    scores.wins++;
                    winToast();
                    // reset
                    updateScores();
                    resetHands();
                }
                else if (you == 1) {
                    // TIE
                    scores.ties++;
                    tieToast();
                    // reset
                    updateScores();
                    resetHands();
                }
                else if (you == 2) {
                    // LOSS
                    scores.losses++;
                    lossToast();
                    // reset
                    updateScores();
                    resetHands();
                }
            }
            else if (me == 6){ // I'M SCISSORS
                if (you == 0) {
                    // LOSS
                    scores.losses++;
                    lossToast();
                    // reset
                    updateScores();
                    resetHands();
                }
                else if (you == 1) {
                    // WIN
                    scores.wins++;
                    winToast();
                    // reset
                    updateScores();
                    resetHands();
                }
                else if (you == 2) {
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
        if (scores.me == 4) me = "Rock";
        else if (scores.me == 5) me = "Paper";
        else me = "Scissors";
        if (scores.opponent == 0) you = "Rock";
        else if (scores.opponent == 1) you = "Paper";
        else you = "Scissors";
        toastMe("You Won!\n" + me + " beats " + you);
    }

    private void lossToast() {
        String me, you;
        if (scores.me == 4) me = "Rock";
        else if (scores.me == 5) me = "Paper";
        else me = "Scissors";
        if (scores.opponent == 0) you = "Rock";
        else if (scores.opponent == 1) you = "Paper";
        else you = "Scissors";
        toastMe("You Lost\n" + me + " lose to " + you);
    }

    private void tieToast() {
        String me, you;
        if (scores.me == 4) me = "Rock";
        else if (scores.me == 5) me = "Paper";
        else me = "Scissors";
        if (scores.opponent == 0) you = "Rock";
        else if (scores.opponent == 1) you = "Paper";
        else you = "Scissors";
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
            switch (color) {
                case "Rock":
                    scores.me = 4;
                    break;
                case "Paper":
                    scores.me = 5;
                    break;
                case "Scissors":
                    scores.me = 6;
                    break;
            }
            updateColor(scores.me);
            updateScores();
            compareHandShapes();
            commHandler.sendMessage(scores.me);
        }
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
                        .setContentText("Let's Play!" + color)
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
        if (scores.me != -1) {
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
}
