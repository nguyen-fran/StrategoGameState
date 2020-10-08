package edu.up.strategogamestate;

public class StrategoGameState {
    //Stratego only has two phases: setup and main gameplay
    //false if setup, true if main gameplay
    private boolean gamePhase;
    private boolean playerTurn; //true if player's turn, false if com's turn
    private int[] playerGY = new int[11];
    private int[] oppGY = new int[11];

    private BoardSquare[][] boardSquares;
    private GamePiece[][] gamePieces;

    private final int BOARD_SIZE = 10;
    public static final boolean BLUE = true;
    public static final boolean RED = false;

    //in order of bombs, 10, 9, ..., 2, 1, flag
    private int[] numOfPieces = {6, 1, 1, 2, 3, 4, 4, 4, 5, 8, 1, 1};

    public StrategoGameState() {
        gamePhase = false;
        playerTurn = true;  //should it be true to start?
        for (int i = 0; i < playerGY.length; i++) {
            playerGY[i] = 0;
            oppGY[i] = 0;
        }

        for (int j = 0; j < BOARD_SIZE; j++) {
            for (int k = 0; k < BOARD_SIZE; k++) {
                for (int l = 0; l < numOfPieces[k]; l++) {
                    boardSquares[j][k] = new BoardSquare();
                    gamePieces[j][k] = new GamePiece();
                    k++;
                }
            }
        }

    }

    /**
     * deep copy
     *
     * @param orig
     */
    public StrategoGameState(StrategoGameState orig) {
        this.gamePhase = orig.gamePhase;
        this.playerTurn = orig.playerTurn;
        for (int i = 0; i < playerGY.length; i++) {
            this.playerGY[i] = orig.playerGY[i];
            this.oppGY[i] = orig.oppGY[i];
        }

        for (int j = 0; j < BOARD_SIZE; j++) {
            for (int k = 0; k < BOARD_SIZE; k++) {
                boardSquares[j][k] = new BoardSquare(orig.boardSquares[j][k]);
                gamePieces[j][k] = new GamePiece(orig.gamePieces[j][k]);
            }
        }

    }

    /**
     *
     * @param square
     * @param newX
     * @param newY
     * @return
     */
    public boolean move(BoardSquare square, int newX, int newY){
        //check if it's the player's turn

        //check if coordinates you want to move to are in range and valid for piece (special exception for scout range)

        //then go through 2d array of pieces to find board square that you are trying to move to

        //check if coordinates you are looking at are occupied, if so, check if there's a piece there or it's null (like with the lakes)

        //call attack method to compare the two pieces

        //change occupied/piece variables on current board square as well as the square you are moving to

        return true;
    }

    //attack (helper method used in move method)
    public boolean attack(GamePiece piece1, GamePiece piece2){
        //first check for special cases

        //otherwise compare numerical rank

        return true;
    }

}
