package edu.up.strategogamestate;

/**
 * describes an individual game piece/unit (rank, team, etc)
 */
public class GamePiece {
    private int rank; //standard nums for mobile pieces, bomb is 11, flag is 0
    private boolean team;
    private boolean visible;
    private boolean captured;


    GamePiece(){ //nonsense default values, all need to be changed later
        rank = -1;
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
    public int getRank(){
        return rank;
    }
    public boolean getTeam(){
        return team;
    }
    public boolean getVisible(){
        return visible;
    }
    public boolean getCaptured(){
        return captured;
    }

    public void setVisible(boolean visible){
        this.visible = visible;
    }
    public void setCaptured(boolean captured){
        this.captured = captured;
    }
}
