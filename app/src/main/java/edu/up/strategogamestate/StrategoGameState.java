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
    public static final boolean HUMANTURN = true;
    public static final boolean COMPTURN = false;

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
                    GamePiece piece = new GamePiece(numOfPiecesIndex, team, team, false);
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
     * @param orig original game state being copied
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
     * TODO: make sure turn indexing/player vs computer turns make sense
     * @param square
     * @param newX
     * @param newY
     * @param playerIndex who is trying to make the move
     * @return
     */
    public boolean move(BoardSquare square, int newX, int newY, boolean playerIndex){
        GamePiece piece = square.getPiece();

        //check if it's the player's turn (if not, then move is illegal), or if new square is out of range
        //also check if piece's team is the same as your team
        if(!canMove(playerIndex) || !squareOnBoard(newX, newY) || piece.getTeam() != playerIndex){
            return false;
        }

        //check if coordinates you want to move to are valid for piece (special exception for scout range, and immobile pieces)
        if(piece.getRank() == 2){ //checking validity for scout (special range)
            if(scoutMove(square, newX, newY) == false){
                return false;
            }
        } else if(piece.getRank() == 11 || piece.getRank() == 0){ //immobile pieces (cannot move)
            return false;
        }else{ //all other pieces have normal movement range (check if square is in range, and is moving at all)
            if(newX > square.getxPos() + 1 || newX < square.getxPos() - 1 ||
               newY > square.getyPos() + 1 || newY < square.getyPos() - 1 ||
               (square.getxPos() == newX && square.getyPos() == newY)){
                return false;
            }
        }


        //if new coords are occupied by another piece (not null or player's), then attack
        if(boardSquares[newX][newY].getOccupied() == true &&
           (boardSquares[newX][newY].getPiece() == null ||
           boardSquares[newX][newY].getPiece().getTeam() != playerIndex)){
            //moving into occupied square that you cannot attack is an illegal move
            return false;
        }else if(boardSquares[newX][newY].getOccupied() == true){
            attack(square.getPiece(), boardSquares[newX][newY].getPiece(), playerIndex);
        }

        //check initial square/new square pieces for capture
        //update init/new square pieces appropriately
        if(square.getPiece().getCaptured()){
            square.setPiece(null);
        }else{
            boardSquares[newX][newY].setPiece(square.getPiece());
            square.setPiece(null);
        }

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
        if(playerIndex == HUMANTURN){ //human attacking computer
            if(piece1.getCaptured() == true){ //updating player graveyard
                setPlayerGYIdx(piece1.getRank() - 1, getPlayerGY()[piece1.getRank() - 1] + 1);
            }
            if(piece2.getCaptured() == true){ //updating opponent graveyard
                setOppGYIdx(piece2.getRank() - 1, getOppGY()[piece2.getRank() - 1] + 1);
            }
        }else if(playerIndex == COMPTURN){ //computer attacking human
            if(piece1.getCaptured() == true){ //updating opponent graveyard
                setOppGYIdx(piece1.getRank() - 1, getOppGY()[piece1.getRank() - 1] + 1);
            }
            if(piece2.getCaptured() == true){ //updating player graveyard
                setPlayerGYIdx(piece2.getRank() - 1, getPlayerGY()[piece2.getRank() - 1] + 1);
            }

        }
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

    /** TODO: for loops to finish checking for valid movement (checking if spaces between tiles are occupied)
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

        }else if(square.getyPos() == newY){ //left/right movement

        }
    }

    /**
     * helper method to determine if given square is on the board or not
     * @param x x coord of square being checked
     * @param y y coord of square being checked
     * @return true if square is within range of the board, false if not
     */
    public boolean squareOnBoard(int x, int y){
        if(x > BOARD_SIZE || y > BOARD_SIZE || x < 0 || y < 0){
            return false;
        }else{
            return true;
        }
    }

    //getters and setters

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
}
