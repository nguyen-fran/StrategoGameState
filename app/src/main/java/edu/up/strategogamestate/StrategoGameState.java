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

    private final int boardSize = 10;
    public static final boolean BLUE = true;
    public static final boolean RED = false;

    public StrategoGameState() {
        gamePhase = false;
        playerTurn = true;  //should it be true to start?
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
        this.gamePhase = orig.gamePhase;
        this.playerTurn = orig.playerTurn;
        for (int i = 0; i < playerGY.length; i++) {
            this.playerGY[i] = orig.playerGY[i];
            this.oppGY[i] = orig.oppGY[i];
        }

        for (int j = 0; j < boardSize; j++) {
            for (int k = 0; k < boardSize; k++) {
                boardSquares[j][k] = new BoardSquare(orig.boardSquares[j][k]);
                gamePieces[j][k] = new GamePiece(orig.gamePieces[j][k]);
            }
        }

    }

    /**
     *method for moving the piece on a given board square to another square
     * @param square
     * @param newX
     * @param newY
     * @return
     */
    public boolean move(BoardSquare square, int newX, int newY){
        //check if it's the player's turn

        //check if coordinates you want to move to are in range and valid for piece (special exception for scout range, and immobile pieces)
        //scout may need more complicated logic, i don't think it can jump over pieces (would need to check everything in a straight line?)

        //then go through 2d array of pieces to find board square that you are trying to move to

        //check if coordinates you are looking at are occupied, if so, check if there's a piece there or it's null (like with the lakes)

        //call attack method to compare the two pieces

        //change occupied/piece variables on current board square as well as the square you are moving to

        return true;
    }

    /**
     *
     * @param piece1
     * @param piece2
     * @return
     */
    public boolean attack(GamePiece piece1, GamePiece piece2){
        //first check for special cases

        //otherwise compare numerical rank

        //depending on ranking, either one or both pieces will have their captured value change and be moved to the graveyard

        return true;
    }

}
