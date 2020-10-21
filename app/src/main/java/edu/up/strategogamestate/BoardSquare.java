package edu.up.strategogamestate;

/**
 * tells state of individual board square, tells whether it is occupied, what piece is on it, coordinates, etc
 * NOTE: the Lake Squares and are represented as occupied being true but piece is null
 */
public class BoardSquare {
    private boolean occupied;
    private boolean highlight;
    private GamePiece piece;
    private int row;
    private int col;

    /**
     * constructor for BoardSquare, sets instance variables to given values
     * @param occupied whether the square should be occupied or not
     * @param piece game piece on the square (null if none exists)
     * @param row x coordinate of the square on the board
     * @param col y coordinate of the square on the board
     */
    public BoardSquare(boolean occupied, GamePiece piece, int row, int col){
        this.occupied = occupied;
        this.highlight = false;
        this.piece = piece;
        this.row = row;
        this.col = col;
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
        this.row = orig.row;
        this.col = orig.col;

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
    public int getRow(){
        return row;
    }
    public int getCol(){
        return col;
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
    public void setRow(int row) {
        this.row = row;
    }
    public void setCol(int col) {
        this.col = col;
    }
}
