package edu.up.strategogamestate;

/**
 * describes an individual game piece/unit (rank, team, etc)
 */
public class GamePiece {
    private int rank;
    private Boolean team; //only 2 possible teams, need to set up shortcut for true = blue, false = red
    private Boolean visible;
    private Boolean captured;
    private int xPos;
    private int yPos;

    //basic constructor when given values to work off of
    GamePiece(int rank, Boolean team, Boolean visible, Boolean captured, int xPos, int yPos){
        this.rank = rank;
        this.team = team;
        this.visible = visible;
        this.captured = captured;
        this.xPos = xPos;
        this.yPos = yPos;
    }

    //move (may need to be moved to BoardSquare class)

    //attack (helper method used in move method, may need to be moved to BoardSquare class)

    //getters/setters
}
