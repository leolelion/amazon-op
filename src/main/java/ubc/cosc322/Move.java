package ubc.cosc322;


/**
 * Represents a move in the Game of the Amazons, consisting of queen movement and arrow placement.
 */
public class Move {
    public int[] queenCurr; // Current queen position [row, col]
    public int[] queenNext; // New queen position [row, col]
    public int[] arrow;     // Arrow position [row, col]

    /**
     * Constructor for a move.
     */
    public Move(int[] queenCurr, int[] queenNext, int[] arrow) {
        this.queenCurr = queenCurr;
        this.queenNext = queenNext;
        this.arrow = arrow;
    }
}
