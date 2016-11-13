package com.example.eggertron.hw4_rockpaperscissors;

/**
 * Created by eggertron on 11/9/16.
 */
public class Scores {

    int wins, losses, ties;
    int me, opponent;

    public Scores () {
        wins = 0;
        losses = 0;
        ties = 0;
        me = -1;
        opponent = -1;
    }

    public Scores(int w, int l, int t, int m, int o) {
        wins = w;
        losses = l;
        ties = t;
        me = m;
        opponent = o;
    }
}
