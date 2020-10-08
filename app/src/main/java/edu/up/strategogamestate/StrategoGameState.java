package edu.up.strategogamestate;

public class StrategoGameState {
    //Stratego only has two phases: setup and main gameplay
    //false if setup, true if main gameplay
    private boolean gamePhase;
    private boolean playerTurn; //true if player's turn, false if com's turn
    private int[] playerGY = new int[11];
    private int[] oppGY = new int[11];

    private BoardSquare[][] boardSquares;

    private final int BOARD_SIZE = 10;
    public static final boolean BLUE = true;
    public static final boolean RED = false;

    //in order of flag, 1, 2, ..., 9, 10, bomb
    private int[] numOfPieces = {1, 1, 8, 5, 4, 4, 4, 3, 2, 1, 1, 6};

    public StrategoGameState() {
        gamePhase = false;
        playerTurn = true;  //should it be true to start?
        for (int i = 0; i < playerGY.length; i++) {
            playerGY[i] = 0;
            oppGY[i] = 0;
        }

        this.makeTeam(0, RED);
        this.makeTeam(6, BLUE);


    }

    public void makeTeam(int startRow, boolean team) {
        int numOfPiecesIndex = 0;
        for (int j = startRow; j < startRow + 4; j++) {
            for (int k = 0; k < BOARD_SIZE; k++) {
                for (int l = 0; l < numOfPieces[numOfPiecesIndex]; l++) {
                    if (k >= BOARD_SIZE) {
                        j++;
                        k = 0;
                    }
                    GamePiece piece = new GamePiece(l, team, team, false);
                    boardSquares[j][k] = new BoardSquare(true, piece, j, k);
                    k++;
                }
                numOfPiecesIndex++;
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
