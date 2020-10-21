package edu.up.strategogamestate;

/**
 * tells state of individual board square, tells whether it is occupied, what piece is on it, coordinates, etc
 * NOTE: the Lake Squares and are represented as occupied being true but piece is null
 */
public class BoardSquare {
    private boolean occupied;
    private boolean highlight;
    private GamePiece piece;
    private int xPos;
    private int yPos;

    /**
     * constructor for BoardSquare, sets instance variables to given values
     * @param occupied whether the square should be occupied or not
     * @param piece game piece on the square (null if none exists)
     * @param xPos x coordinate of the square on the board
     * @param yPos y coordinate of the square on the board
     */
    public BoardSquare(boolean occupied, GamePiece piece, int xPos, int yPos){
        this.occupied = occupied;
        this.highlight = false;
        this.piece = piece;
        this.xPos = xPos;
        this.yPos = yPos;
    }

    /**
     * copy constructor for BoardSquare
     * @param orig original square being copied
     */
    public BoardSquare(BoardSquare orig) {
        this.occupied = orig.occupied;
        this.highlight = orig.highlight;
        if (orig.piece == null) {
            this.piece = null;
        } else {
            this.piece = new GamePiece(orig.piece);
        }
        this.xPos = orig.xPos;
        this.yPos = orig.yPos;

    }

    //getters and setters
    public boolean getOccupied(){
        return occupied;
    }
    public boolean getHighlight(){
        return highlight;
    }
    public GamePiece getPiece(){
        return piece;
    }
    public int getxPos(){
        return xPos;
    }
    public int getyPos(){
        return yPos;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }
    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }
    public void setPiece(GamePiece piece) {
        this.piece = piece;
    }
    public void setxPos(int xPos) {
        this.xPos = xPos;
    }
    public void setyPos(int yPos) {
        this.yPos = yPos;
    }
}
