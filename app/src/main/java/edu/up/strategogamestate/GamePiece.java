package edu.up.strategogamestate;

/**
 * data holding class that describes an individual game piece/unit (rank, team, etc)
 */
public class GamePiece {
    private int rank; //standard nums for mobile pieces, bomb is 11, flag is 0
    private boolean team;
    private boolean visible;
    private boolean captured;

    /**
     * default constructor for GamePiece
     * default values that need to be changed to proper ones later
     */
    public GamePiece(){
        rank = -1;
        team  = true;
        visible = false;
        captured = false;
    }

    /**
     * constructor for GamePiece
     * creates new game piece and sets values to the ones given
     * @param rank piece's numerical rank (0-11)
     * @param team piece's team (RED or BLUE)
     * @param visible whether the piece is visible or not
     * @param captured whether the piece has been captured or not
     */
   public GamePiece(int rank, boolean team, boolean visible, boolean captured){
        this.rank = rank;
        this.team = team;
        this.visible = visible;
        this.captured = captured;
    }

    public GamePiece(GamePiece orig) {
        this.rank = orig.getRank();
        this.team = orig.getTeam();
        this.visible = orig.getVisible();
        this.captured = orig.getCaptured();
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
