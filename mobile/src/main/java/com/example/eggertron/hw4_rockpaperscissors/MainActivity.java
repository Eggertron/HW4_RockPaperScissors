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

    ScrollView bg; // Reference to the ScrollView
    int colorIndex = 0; // saves current color index.
    int NEW_GAME = 4;
    TextView rock, paper, scissors, play;
    CommHandler commHandler;
    Scores scores;
    Toast toast;
    TextView txtScores;

    /*
        Gets the string in the TextView and changes the colorIndex.
     */
    public void ChangeColor(View view) {
        String color = ((TextView) view).getText().toString();
        if (scores.opponent == -1) {
            switch (color) {
                case "Rock":
                    colorIndex = 0;
                    updateScores();
                    if (scores.opponent != -1) {
                        // opponent has already played a hand
                        compareHandShapes();
                    }
                    else {
                        // lock up the game and wait for opponent
                    }
                    break;
                case "Paper":
                    colorIndex = 1;
                    updateScores();
                    if (scores.opponent != -1) {
                        // opponent has already played a hand
                        compareHandShapes();
                    }
                    else {
                        // lock up the game and wait for opponent
                    }
                    break;
                case "Scissors":
                    colorIndex = 2;
                    updateScores();
                    if (scores.opponent != -1) {
                        // opponent has already played a hand
                        compareHandShapes();
                    }
                    else {
                        // lock up the game and wait for opponent
                    }
                    break;
                case "Play":
                    break;
            }
        }
    }

    /*
        Changes the background color based on colorIndex.
     */
    public void updateColor(int colorIndex) {
        updateScores();
        switch (colorIndex) {
            case 0:
                //bg.setBackgroundColor(Color.BLUE);
                rock.setBackgroundColor(Color.BLUE);
                break;
            case 1:
                paper.setBackgroundColor(Color.BLUE);
                break;
            case 2:
                scissors.setBackgroundColor(Color.BLUE);
                break;
            case 3:
                bg.setBackgroundColor(Color.rgb(255, 102, 0));
                break;
        }
    }

    private void updateScores() {
        txtScores.setText("Wins: " + scores.wins + ", Losses: " + scores.losses +
                            ", Ties: " + scores.ties + "Hand: " + scores.me);
    }

    /*
        Handle the messages recieved from the game.
     */
    public void updateOpponentHand(int hand) {
        scores.opponent = hand;
        // check to see if I had already chose a handshape
        if (scores.me != -1) {
            // compare and see who wins
            compareHandShapes();
        }
    }

    /*
        Compares the handShapes and updates scores
     */
    public void compareHandShapes() {
        int me = scores.me,
                you = scores.opponent;

        if (me == 0) { // I'M ROCK
            if (you == 0) {
                // TIE
                scores.ties++;
                tieToast();
            }
            else if (you == 1) {
                // LOSS
                scores.losses++;
                lossToast();
            }
            else if (you == 2) {
                // WIN
                scores.wins++;
                winToast();
            }
        }
        else if (me == 1) { // I'M PAPER
            if (you == 0) {
                // WIN
                scores.wins++;
                winToast();
            }
            else if (you == 1) {
                // TIE
                scores.ties++;
                tieToast();
            }
            else if (you == 2) {
                // LOSS
                scores.losses++;
                lossToast();
            }
        }
        else if (me == 2) { // I'M SCISSORS
            if (you == 0) {
                // LOSS
                scores.losses++;
                lossToast();
            }
            else if (you == 1) {
                // WIN
                scores.wins++;
                winToast();
            }
            else if (you == 2) {
                // TIE
                scores.ties++;
                tieToast();
            }
        }
        // send next game message.
        //commHandler.sendMessage(NEW_GAME);
        // reset
        resetHands();
    }

    private void winToast() {
        toastMe("You Won!");
    }

    private void lossToast() {
        toastMe("You Lost");
    }

    private void tieToast() {
        toastMe("You Won!");
    }

    private void resetHands() {
        scores.me = -1;
        scores.opponent = -1;
    }

    protected void toastMe(String msg) {
        toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onClick(View v) {
        String color = ((TextView) v).getText().toString();
        if (scores.me < 0) { // only allow one selection
            switch (color) {
                case "Rock":
                    colorIndex = 0;
                    scores.me = 0;
                    break;
                case "Paper":
                    colorIndex = 1;
                    scores.me = 1;
                    break;
                case "Scissors":
                    colorIndex = 2;
                    scores.me = 2;
                    break;
                case "Play":
                    colorIndex = 3;
                    scores.me = 3;
                    break;
            }

            updateScores();
            updateColor(colorIndex);

            commHandler.sendMessage(colorIndex);
        }
        /*
            The simplest way to make the watch interact with the
            mobile is using notification. You don’t even have to code
            anything for the mobile side. To do that you can use
            NotificationCompat and NotificationManagerCompat.
        */
        //Send a notification to the watch
        /*
        int notificationID = 1;
        //The intent allows user opens the activity on the phone
        Intent viewIntent = new Intent(this, MainActivity.class);
        PendingIntent viewPendingIntent = PendingIntent.getActivity(this, 0, viewIntent, 0);
        //Use the notification builder to create a notification
        NotificationCompat.Builder notificationBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.cast_ic_notification_small_icon)
                        .setContentTitle("Simple Color Picker")
                        .setContentText("User has picked color : " + color)
                        .setContentIntent(viewPendingIntent);
        //Send the notification
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(notificationID, notificationBuilder.build());
        */
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
        play = (TextView) findViewById(R.id.play);
        rock.setOnClickListener(this);
        paper.setOnClickListener(this);
        scissors.setOnClickListener(this);
        play.setOnClickListener(this);
        commHandler = new CommHandler(this);
        txtScores = (TextView)findViewById(R.id.txtScores);
        scores = new Scores();
        // Send a message to start on wear using commHandler
    }
}
