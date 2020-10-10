package edu.up.strategogamestate;

import java.util.Random;

public class StrategoGameState {
    //Stratego only has two phases: setup and main gameplay
    private boolean gamePhase;  //false if on setup, true if on main gameplay
    private boolean playerTurn; //true if human's turn, false if com's turn
    //these arrays holds the number of deaths of each type of piece
    private int[] playerGY = new int[11];
    private int[] oppGY = new int[11];

    private BoardSquare[][] boardSquares;

    private static final int BOARD_SIZE = 10;
    public static final boolean BLUE = true;
    public static final boolean RED = false;
    public static final boolean HUMANTURN = true;
    public static final boolean COMPTURN = false;

    //the amount of each type of piece each player has in order of: flag, 1, 2, ..., 9, 10, bomb
    private static final int[] numOfPieces = {1, 1, 8, 5, 4, 4, 4, 3, 2, 1, 1, 6};
    //coordinates for the Lake Squares which can't be occupied
    private static final int[][] lakeSquares = {{4, 2}, {4, 3}, {5, 2}, {5, 3}, {4, 6}, {4, 7}, {5, 6}, {5, 7}};

    /**
     * constructor
     */
    public StrategoGameState() {
        gamePhase = false;
        playerTurn = HUMANTURN;  //should it be like this to start?
        //there are zero deaths at the start of a game
        for (int i = 0; i < playerGY.length; i++) {
            playerGY[i] = 0;
            oppGY[i] = 0;
        }

        //making the squares for RED and BLUE team
        this.makeTeam(0, RED);
        this.makeTeam(6, BLUE);
        //making the BoardSquares that start empty
        for (int j = 4; j < 6; j++) {
            for (int k = 0; k < BOARD_SIZE; k++) {
                boardSquares[j][k] = new BoardSquare(false, null, j, k);
            }
        }
        //updating the Lake Squares which can't be occupied by any pieces
        for (int[] lakeSquare : lakeSquares) {
            boardSquares[lakeSquare[0]][lakeSquare[1]].setOccupied(true);
        }

        //the game starts with a randomized board
        randomize(0, 4, 0, 10);
        randomize(6, 10, 0, 10);
    }

    /**
     * Helper method for constructor.
     * Initializes one team's side of the board with the right number of each type of GamePiece
     *
     * @param startRow  should be 0 if initializing RED team, 6 if initializing BLUE team
     * @param team      either BLUE or RED.
     */
    private void makeTeam(int startRow, boolean team) {
        int numOfPiecesIndex = 0;   //this will also signify the rank of the GamePiece being made
        for (int i = startRow; i < startRow + 4; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                for (int k = 0; k < numOfPieces[numOfPiecesIndex]; k++) {
                    //check if at end of row, if so move to next row and start on col 0
                    if (j >= BOARD_SIZE) {
                        i++;
                        j = 0;
                    }
                    GamePiece piece = new GamePiece(numOfPiecesIndex, team, team, false);
                    boardSquares[i][j] = new BoardSquare(true, piece, i, j);
                    j++;
                }
                numOfPiecesIndex++;
            }
        }
    }

    /**
     * Helper method for constructor.
     * Randomizes the region of boardSquares within ranges given by param
     * Intended use is the randomize one team's side of the board before the start of a game
     *
     * @param startRow
     * @param endRow
     * @param startCol
     * @param endCol
     */
    private void randomize(int startRow, int endRow, int startCol, int endCol) {
        if (startRow < 0 || endRow > BOARD_SIZE || startCol < 0 || endCol > BOARD_SIZE) {
            return;
        }

        Random rand = new Random();
        int randXPos, randYPos;
        BoardSquare temp;
        for (int i = startRow; i < endRow; i++) {
            for (int j = startCol; j < endCol; j++) {
                randXPos = rand.nextInt(endRow - startRow) + startRow;
                randYPos = rand.nextInt(endCol - startCol) + startCol;

                temp = boardSquares[randXPos][randYPos];
                boardSquares[randXPos][randYPos] = boardSquares[i][j];
                boardSquares[i][j] = temp;
            }
        }
    }


    /**
     * deep copy constructor
     *
     * @param orig  original GameState to copy
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
     *method for moving the piece on a given board square to another square
     * TODO: see if it's possible to make this nicer looking, reduce amount of if/else statements
     * @param square
     * @param newX
     * @param newY
     * @return
     */
    public boolean move(BoardSquare square, int newX, int newY){
        GamePiece piece = square.getPiece();

        //check if it's the player's turn (if not, then move is illegal)
        if(canMove( HUMANTURN /*current player, don't have player classes in yet so defaulting to human*/) == false){
            return false;
        }

        //check if coordinates you want to move to are in range and valid for piece (special exception for scout range, and immobile pieces)
        if(piece.getRank() == 2){ //special movement range for scout
            //TODO: special movement logic here (may need to make helper method)
        } else if(piece.getRank() == 11 || piece.getRank() == 0){ //immobile pieces (cannot move)
            return false;
        }else{ //all other pieces have normal movement range (check if square is in range)
            if(newX > square.getxPos() + 1 || newX < square.getxPos() - 1 ||
               newY > square.getyPos() + 1 || newY < square.getyPos() - 1){
                return false;
            }
        }


        //if new coords are occupied by another piece (not null), then attack
        if(boardSquares[newX][newY].getOccupied() == true && boardSquares[newX][newY].getPiece() == null){
            return false;
        }else if(boardSquares[newX][newY].getOccupied() == true){
            attack(square.getPiece(), boardSquares[newX][newY].getPiece());
        }

        //if current board piece is now captured, then remove piece from square (potential graveyard update happens in attack method)
        //if not, then update new board square
        if(square.getPiece().getCaptured() == true){
            square.setPiece(null);
        }else{
            boardSquares[newX][newY].setPiece(square.getPiece());
            square.setPiece(null);
        }

        return true;
    }

    /** TODO: fill this method out
     * method for logic of attacks between pieces
     * determines which piece (or both) get captured and sent to the graveyard
     * @param piece1
     * @param piece2
     * @return
     */
    public boolean attack(GamePiece piece1, GamePiece piece2){
        //first check for special cases (bomb, spy, miner)

        //otherwise compare numerical rank

        //depending on ranking, either one or both pieces will have their captured value change

        //update graveyard(s)

        return true;
    }

    /**
     * helper method to check if a given player can make a move or not
     * @param playerId Id of the current player
     * @return true if they can move, false if they can't
     */
    public boolean canMove(boolean playerId){
        if(playerId == getPlayerTurn()){
            return true;
        }else{
            return false;
        }
    }

    //getters and setters
    public boolean getGamePhase(){
        return gamePhase;
    }
    public boolean getPlayerTurn(){
        return playerTurn;
    }
    public int[] getPlayerGY(){
        return playerGY;
    }
    public int[] getOppGY() {
        return oppGY;
    }
    public BoardSquare[][] getBoardSquares() {
        return boardSquares;
    }

    public void setGamePhase(boolean gamePhase){
        this.gamePhase = gamePhase;
    }
    public void setPlayerTurn(boolean playerTurn) {
        this.playerTurn = playerTurn;
    }
    public void setPlayerGY(int[] playerGY) {
        this.playerGY = playerGY;
    }
    public void setOppGY(int[] oppGY) {
        this.oppGY = oppGY;
    }
    public void setBoardSquares(BoardSquare[][] boardSquares) {
        this.boardSquares = boardSquares;
    }
}
