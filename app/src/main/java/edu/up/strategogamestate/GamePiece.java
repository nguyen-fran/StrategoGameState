package edu.up.strategogamestate;

/**
 * describes an individual game piece/unit (rank, team, etc)
 */
public class GamePiece {
    int rank;
    Boolean team; //only 2 possible teams, need to set up shortcut for 0 = blue, 1 = red
    Boolean visible;
    int xPos;
    int yPos;
}
