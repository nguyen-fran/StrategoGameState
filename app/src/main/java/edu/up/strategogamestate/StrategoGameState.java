package edu.up.strategogamestate;

public class StrategoGameState {
    //Stratego only has two phases: setup and main gameplay
    //false if setup, true if main gameplay
    boolean gamePhase;
    boolean playerturn; //true if player's turn, false if com's turn
    int[] playerGY = new int[11];
    int[] oppGY = new int[11];

    BoardSquare[][] boardSquares;
    GamePiece[][] gamePieces;

    private final int boardSize = 10;

    public StrategoGameState() {
        gamePhase = false;
        playerturn = true;  //should it be true to start?
        for (int i = 0; i < playerGY.length; i++) {
            playerGY[i] = 0;
            oppGY[i] = 0;
        }

        for (int j = 0; j < boardSize; j++) {
            for (int k = 0; k < boardSize; k++) {
                boardSquares[j][k] = new BoardSquare();
                gamePieces[j][k] = new GamePiece();
            }
        }

    }

    /**
     * deep copy
     *
     * @param orig
     */
    public StrategoGameState(StrategoGameState orig) {

    }

}
