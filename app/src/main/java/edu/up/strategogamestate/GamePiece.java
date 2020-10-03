package edu.up.strategogamestate;

/**
 * describes an individual game piece/unit (rank, team, etc)
 */
public class GamePiece {
    private int rank;
    private boolean team;
    private boolean visible;
    private boolean captured;


    GamePiece(){ //nonsense default values, all need to be changed later
        rank = 0;
        team  = true;
        visible = false;
        captured = false;
    }

    GamePiece(int rank, boolean team, boolean visible, boolean captured){
        this.rank = rank;
        this.team = team;
        this.visible = visible;
        this.captured = captured;
    }

    //getters/setters
}
