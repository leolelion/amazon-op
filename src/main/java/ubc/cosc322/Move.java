package ubc.cosc322;

public class Move {
    public int[] queenCurr; // Current position of the queen [row, col]
    public int[] queenNext; // New position of the queen [row, col]
    public int[] arrow;     // Arrow position [row, col]

    public Move(int[] queenCurr, int[] queenNext, int[] arrow) {
        this.queenCurr = queenCurr;
        this.queenNext = queenNext;
        this.arrow = arrow;
    }
}