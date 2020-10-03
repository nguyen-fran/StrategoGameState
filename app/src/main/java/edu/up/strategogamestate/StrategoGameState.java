package edu.up.strategogamestate;

public class StrategoGameState {
    int[] playerGY = new int[11];
    int[] oppGY = new int[11];
    boolean playerturn; //true if player's turn, false if com's turn
    //TODO: this
    // BoardSquares[][] boardSquares;

    public StrategoGameState() {
        for (int i = 0; i < playerGY.length; i++) {
            playerGY[i] = 0;
            oppGY[i] = 0;
        }


    }

}
