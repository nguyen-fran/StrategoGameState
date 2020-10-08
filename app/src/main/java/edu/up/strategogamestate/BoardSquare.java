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

    BoardSquare(){

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

