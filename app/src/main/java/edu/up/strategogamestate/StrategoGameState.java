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
