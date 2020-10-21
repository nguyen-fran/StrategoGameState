package edu.up.strategogamestate;

import java.util.Random;

import android.util.Log;

import androidx.annotation.NonNull;

/**
 * A class that contains the data for a Stratego game and methods to do actions in Stratego
 */
public class StrategoGameState {
    //Stratego only has two phases: setup and main gameplay
    private boolean gamePhase;  //false if on setup, true if on main gameplay
    private boolean playerTurn; //true if human's turn, false if com's turn
    //these arrays holds the number of deaths of each type of piece (no flag) in order of: 1, 2, ..., 9, 10, bomb
    private int[] playerGY = new int[11];
    private int[] oppGY = new int[11];

    private StrategoGameState prevGameState;
    private BoardSquare[][] boardSquares = new BoardSquare[10][10];

    private static final int BOARD_SIZE = 10;
    public static final boolean BLUE = true;
    public static final boolean RED = false;
    public static final boolean HUMAN_TURN = true;
    public static final boolean COMP_TURN = false;

    //the amount of each type of piece each player has in order of: flag, 1, 2, ..., 9, 10, bomb
    private static final int[] NUM_OF_PIECES = {1, 1, 8, 5, 4, 4, 4, 3, 2, 1, 1, 6};
    //coordinates for the Lake Squares which can't be occupied
    private static final int[][] LAKE_SQUARES = {{4, 2}, {4, 3}, {5, 2}, {5, 3}, {4, 6}, {4, 7}, {5, 6}, {5, 7}};

    /**
     * constructor
     */
    public StrategoGameState() {
        gamePhase = false;
        playerTurn = HUMAN_TURN;  //should it be like this to start?
        //there are zero deaths at the start of a game
        for (int i = 0; i < playerGY.length; i++) {
            playerGY[i] = 0;
            oppGY[i] = 0;
        }


        //making the BoardSquares that start empty
        for (int j = 4; j < 6; j++) {
            for (int k = 0; k < BOARD_SIZE; k++) {
                boardSquares[j][k] = new BoardSquare(false, null, j, k);
            }
        }
        //updating the Lake Squares which can't be occupied by any pieces
        for (int[] lakeSquare : LAKE_SQUARES) {
            boardSquares[lakeSquare[0]][lakeSquare[1]].setOccupied(true);
        }

        //making the squares for RED and BLUE team
        this.makeTeam(0, RED);
        this.makeTeam(6, BLUE);

        //the game starts with a randomized board
        //randomize(0, 4, 0, 10);
        //randomize(6, 10, 0, 10);

        //this will be used for the undo action later in StrategoLocalGame, but for this checkpoint it is just null to avoid infinite recursion
        prevGameState = null;
    }

    /**
     * Helper method for constructor.
     * Initializes one team's side of the board with the right number of each type of GamePiece
     *
     * @param startRow  should be 0 if initializing RED team, 6 if initializing BLUE team
     * @param team      either BLUE or RED.
     */
    private void makeTeam(int startRow, boolean team) {
        int numOfPiecesIndex = 0;
        GamePiece piece;
        //this will also signify the rank of the GamePiece being made
        //outer 2 for loops used to provide coordinates of the board square being initialized
        for (int i = startRow; i < startRow + 4; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                //innermost for loop used to initialize correct number of pieces of certain rank
                for (int k = 0; k < NUM_OF_PIECES[numOfPiecesIndex]; k++) {
                    //check if at end of row, if so move to next row and start on col 0
                    if (j >= BOARD_SIZE) {
                        i++;
                        j = 0;
                    }
                    //piece = new GamePiece(numOfPiecesIndex, team, team, false);
                    boardSquares[i][j] = new BoardSquare(true, new GamePiece(numOfPiecesIndex, team, team, false), i, j);

                    //only increment j if there is another piece to make
                    //this avoids j being incremented twice: once on the last piece and again when re-entering the middle for loop
                    if (k < NUM_OF_PIECES[numOfPiecesIndex] - 1) {
                        j++;
                    }
                }
                numOfPiecesIndex++;
            }
        }
    }

    /**
     * Helper method for constructor.
     * Randomizes the pieces in the region of boardSquares within ranges given by params
     * Intended use is the randomize one team's side of the board before the start of a game
     *
     * @param startRow starting row boundary for randomization
     * @param endRow ending row boundary
     * @param startCol starting column boundary
     * @param endCol ending column boundary
     */
    private void randomize(int startRow, int endRow, int startCol, int endCol) {
        if (startRow < 0 || endRow > BOARD_SIZE || startCol < 0 || endCol > BOARD_SIZE) {
            return;
        }

        Random rand = new Random();
        int randXPos, randYPos;
        GamePiece temp;
        for (int i = startRow; i < endRow; i++) {
            for (int j = startCol; j < endCol; j++) {
                randXPos = rand.nextInt(endRow - startRow) + startRow;
                randYPos = rand.nextInt(endCol - startCol) + startCol;

                temp = boardSquares[randXPos][randYPos].getPiece();
                boardSquares[randXPos][randYPos].setPiece(boardSquares[i][j].getPiece());
                boardSquares[i][j].setPiece(temp);
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
                this.boardSquares[j][k] = new BoardSquare(orig.getBoardSquares()[j][k]);
            }
        }

    }

    /**
     *method for moving the piece on a given board square to another square
     * @param square square being selected by the player in order to move the piece on it
     * @param newX x coordinate that player is trying to move to
     * @param newY y coordinate that player is trying to move to
     * @param playerIndex who is trying to make the move
     * @return true if move is legal, false if not
     */
    public boolean move(BoardSquare square, int newX, int newY, boolean playerIndex){
        prevGameState = new StrategoGameState(this);

        Log.i("move", "entered move");
        if(square.getPiece() == null){
            return false;
        }

        Log.i("move", "not null piece");

        GamePiece piece = square.getPiece();

        //check if it's the player's turn (if not, then move is illegal), or if new square is out of range
        //also check if piece's team is the same as your team
        if(!canMove(playerIndex) || !squareOnBoard(newX, newY) || piece.getTeam() != playerIndex){
            return false;
        }

        Log.i("move", "basic error checking");

        //check if coordinates you want to move to are valid for piece (special exception for scout range, and immobile pieces)
        if(piece.getRank() == 2){ //checking validity for scout (special range)
            Log.i("move", "is a scout");
            return (scoutMove(square, newX, newY));
        } else if(piece.getRank() == 11 || piece.getRank() == 0){ //immobile pieces (cannot move)
            Log.i("move", "is flag or bomb");
            return false;
        }else{ //all other pieces have normal movement range (check if square is in range, and is moving at all)
            if(newX > square.getxPos() + 1 || newX < square.getxPos() - 1 ||
               newY > square.getyPos() + 1 || newY < square.getyPos() - 1 ||
               (square.getxPos() == newX && square.getyPos() == newY)){
                Log.i("move", "square out of range");
                return false;
            }
        }

        Log.i("move", "checked special pieces");


        //if new coords are occupied by another piece (not null or player's), then attack
        if(boardSquares[newX][newY].getOccupied() == true &&
           (boardSquares[newX][newY].getPiece() == null ||
           boardSquares[newX][newY].getPiece().getTeam() == playerIndex)){
            //moving into occupied square that you cannot attack is an illegal move
            return false;
        }else if(boardSquares[newX][newY].getOccupied() == true){
            attack(square.getPiece(), boardSquares[newX][newY].getPiece(), playerIndex);    //note: if you do the return statement now then you can't move the piece later
            //just in case both pieces are captured
            if (boardSquares[newX][newY].getPiece().getCaptured()) {
                boardSquares[newX][newY].setPiece(null);
                boardSquares[newX][newY].setOccupied(false);
            }
        }

        Log.i("move", "checked occupation");

        //check initial square/new square pieces for capture
        //update init/new square pieces appropriately
        if (!square.getPiece().getCaptured()) {
            boardSquares[newX][newY].setPiece(square.getPiece());
        }
        square.setPiece(null);
        square.setOccupied(false);
        Log.i("move", "leaving move");

        return true;
    }

    /**
     * method for logic of attacks between pieces
     * determines which piece (or both) get captured and sent to the graveyard
     * assuming attack is valid move, and that there are both an attacking and defending piece
     * @param piece1 piece initiating the attack
     * @param piece2 piece being attacked
     * @param playerIndex who is making the attack (assuming it is their turn to move)
     * @return true if attack was successful, false if something in the comparison went wrong
     */
    public boolean attack(GamePiece piece1, GamePiece piece2, boolean playerIndex){
        //first check for special cases (spy, miner)
        if(piece1.getRank() == 1 && piece2.getRank() == 10){ //spy attacking marshal
            piece2.setCaptured(true);
        }else if(piece1.getRank() == 5 && piece2.getRank() == 11){ //miner attacking bomb
            piece2.setCaptured(true);
        }else if(piece1.getRank() < piece2.getRank()){ //attacker gets captured
            piece1.setCaptured(true);
        }else if(piece1.getRank() > piece2.getRank()){ //defender gets captured
            piece2.setCaptured(true);
        }else if(piece1.getRank() == piece2.getRank()){ //both get captured
            piece1.setCaptured(true);
            piece2.setCaptured(true);
        }else{ //some other combination, indicates something went wrong
            return false;
        }

        //updating graveyard(s)
        if(playerIndex == HUMAN_TURN){ //human attacking computer
            if(piece1.getCaptured() == true){ //updating player graveyard
                setPlayerGYIdx(piece1.getRank() - 1, getPlayerGY()[piece1.getRank() - 1] + 1);
            }
            if(piece2.getCaptured() == true){ //updating opponent graveyard
                setOppGYIdx(piece2.getRank() - 1, getOppGY()[piece2.getRank() - 1] + 1);
            }
        }else if(playerIndex == COMP_TURN){ //computer attacking human
            if(piece1.getCaptured() == true){ //updating opponent graveyard
                setOppGYIdx(piece1.getRank() - 1, getOppGY()[piece1.getRank() - 1] + 1);
            }
            if(piece2.getCaptured() == true){ //updating player graveyard
                setPlayerGYIdx(piece2.getRank() - 1, getPlayerGY()[piece2.getRank() - 1] + 1);
            }

        }

        //TODO piece does not need to be revealed if defender is higher
        //on attacking or being attacked, pieces will become visible to opponent
        piece1.setVisible(true);
        piece2.setVisible(true);
        return true;
    }

    /**
     * helper method to check if a given player can make a move or not
     * @param playerId Id of the current player
     * @return true if they can move, false if they can't
     */
    public boolean canMove(boolean playerId){
        return (playerId == getPlayerTurn());
    }

    /**
     * helper method to determine if selected square is a valid move for a scout piece
     * assumes that it is able to move at all (turn is correct)
     * assumes that new square is in range of board
     * @param square initial board square that the scout piece is on
     * @param newX x coord that you are trying to move the scout to
     * @param newY y coord that you are trying to move the scout to
     * @return true if movement is valid, false if not
     */
    public boolean scoutMove(BoardSquare square, int newX, int newY){
        if(square.getxPos() != newX && square.getyPos() != newY){ //checking if square is not in straight line from init
            return false;
        }else if(square.getxPos() == newX && square.getyPos() == newY){ //checking if square is exactly the same as init
            return false;
        }else if(square.getxPos() == newX){ //up/down movement
            //first determine if y value of init square or new square is bigger (take difference)
            //use this to determine if step should be +1 or -1 in y direction (might have this flipped around)
            int diff = square.getyPos() - newY;
            int step;
            if(diff > 0){ //means init square is lower on board than new square (moving up)
                step = -1;
            }else if(diff < 0){ //means init square is higher on board than new square (moving down)
                step = 1;
            }else{ //new square cannot be the same as old square
                return false;
            }

            //use diff for for loop, for each square in the range (NOT inclusive of new square), check if occupied
            //if a square is occupied, then return false, else keep going
            for(int i = square.getyPos(); i < newY; i += step){
                if(getBoardSquares()[newX][i].getOccupied() == true){
                    return false;
                }
            }
        }else if(square.getyPos() == newY){ //left/right movement (same as up/down with x and y swapped)
            int diff = square.getxPos() - newX;
            int step;
            if(diff > 0){
                step = -1;
            }else if(diff < 0){
                step = 1;
            }else{ //new square cannot be the same as old square
                return false;
            }

            //use diff for for loop, for each square in the range (NOT inclusive of new square), check if occupied
            //if a square is occupied, then return false, else keep going
            for(int i = square.getxPos(); i < newX; i += step){
                if(getBoardSquares()[i][newY].getOccupied() == true){
                    return false;
                }
            }
        }

        //should only hit here if new square is a valid movement
        return true;
    }

    /**
     * helper method to determine if given square is on the board or not
     * @param x x coord of square being checked
     * @param y y coord of square being checked
     * @return true if square is within range of the board, false if not
     */
    public boolean squareOnBoard(int x, int y){
        return !(x > BOARD_SIZE || y > BOARD_SIZE || x < 0 || y < 0);
    }

    /**
     * Sets gamestate to what it was before the previous action
     *
     * @param currGameState the current state of the game
     * @return true if move was successfully undone, false otherwise
     */
    public boolean undo (StrategoGameState currGameState) {
        /*
        if (currGameState == null || prevGameState == null) {
            return false;
        }

        currGameState = prevGameState;
        */
        return true;
    }

    /**
     * Swaps pieces that are on the same team, this is for the setup phase of the game
     *
     * @param square1 first square being selected for swap
     * @param square2 second square being selected for swap
     * @param player    to know if human player or comp player
     * @return true if swap was successful, false otherwise
     */
    public boolean setup(BoardSquare square1, BoardSquare square2, boolean player) {
        //check if there are pieces of the squares to swap
        if (square1.getPiece() == null || square2.getPiece() == null) {
            return false;
        }
        //check if the pieces belong to the player doing the swap
        if (square1.getPiece().getTeam() != player || square2.getPiece().getTeam() != player) {
            return false;
        }

        GamePiece temp = square1.getPiece();
        square1.setPiece(square2.getPiece());
        square2.setPiece(temp);
        return true;
    }

    /**
     * Resets the game
     *
     * @param currGameState reference to current state of the game
     * @return true if game state was successfully reset, false otherwise
     */
    public boolean reset(StrategoGameState currGameState) {
        /*
        if (currGameState == null) {
            return false;
        }
        currGameState = new StrategoGameState();
        */

        return true;


    }

    @Override
    public String toString() {
        String str = "";

        if (gamePhase) {
            str += "Main gameplay phase\n";
        } else {
            str += "Setup phase\n";
        }
        if (playerTurn) {
            str += "Player's turn\n\n";
        } else {
            str += "Opponent's turn\n\n";
        }

        //adding graveyards to the string
        str += "Player's Graveyard: ";
        for (int i = 0; i < playerGY.length; i++) {
            str += "[" + playerGY[i] + "] ";
        }
        str += "\nOpponent's Graveyard: ";
        for (int i = 0; i < oppGY.length; i++) {
            str += "[" + oppGY[i] + "] ";
        }
        str += "\n\n";

        //adding whole board to the string
        for(int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                //check if there is a GamePiece on the square
                if (boardSquares[i][j].getPiece() != null) {
                    //check which team the piece is on
                    if (boardSquares[i][j].getPiece().getTeam() == RED) {
                        str += "[Red " + boardSquares[i][j].getPiece().getRank() + "]\t";
                    } else {
                        str += "[Blue " + boardSquares[i][j].getPiece().getRank() + "]\t";
                    }
                } else {
                    //check if it's a lake square
                    if (boardSquares[i][j].getOccupied()) {
                        str += "[LAKE]\t\t";
                    } else {
                        str += "[     ] \t\t\t";
                    }
                }
            }
            str += "\n";
        }

        str += "\n----------------\n\n";
        return str;
    }

    /**
     * helper method for toString, to check if a given coordinate is a lake square
     * @param x x coord being checked
     * @param y y coord being checked
     * @return true if given coordinate is a lake, false if not
     */
    public boolean lakeChecker(int x, int y){
        int[] arr = {x,y};
        for(int[] lakeSquare : LAKE_SQUARES){
            if (arr == lakeSquare) {
                return true;
            }
        }

        return false;
    }

    /*
    getters and setters
    */

    //getters
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
    public StrategoGameState getPrevGameState() {
        return prevGameState;
    }

    //setters
    public void setGamePhase(boolean gamePhase){
        this.gamePhase = gamePhase;
    }
    public void setPlayerTurn(boolean playerTurn) {
        this.playerTurn = playerTurn;
    }
    public void setPlayerGY(int[] playerGY) {
        this.playerGY = playerGY;
    }
    public void setPlayerGYIdx(int index, int val){
        if(index >= 0 && index < playerGY.length ){
            this.playerGY[index] = val;
        }
    }
    public void setOppGY(int[] oppGY) {
        this.oppGY = oppGY;
    }
    public void setOppGYIdx(int index, int val){
        if(index >= 0 && index < oppGY.length ){
            this.oppGY[index] = val;
        }
    }
    public void setBoardSquares(BoardSquare[][] boardSquares) {
        this.boardSquares = boardSquares;
    }
    public void setPrevGameState(StrategoGameState prevGameState) {
        this.prevGameState = new StrategoGameState(prevGameState);
    }
}
