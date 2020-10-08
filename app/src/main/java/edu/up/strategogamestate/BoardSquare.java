package edu.up.strategogamestate;

/**
 * tells state of individual board square, tells whether it is occupied, what piece is on it, coordinates, etc
 */
public class BoardSquare {
    private boolean occupied;
    private boolean highlight;
    private GamePiece piece;
    private int xPos;
    private int yPos;

    public BoardSquare(boolean occupied, int xPos, int yPos){
        this.occupied = occupied;
        this.highlight = false;
        piece = new GamePiece();
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public BoardSquare(BoardSquare orig) {
        this.occupied = orig.occupied;
        this.highlight = orig.highlight;
        this.piece = new GamePiece(orig.piece);
        this.xPos = orig.xPos;
        this.yPos = orig.yPos;
    }
}
