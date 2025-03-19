package ubc.cosc322;
import java.util.ArrayList;

public class Move {
    private ArrayList<Integer> queenStart;
    private ArrayList<Integer> queenEnd;
    private ArrayList<Integer> arrow;

    public Move(ArrayList<Integer> queenStart, ArrayList<Integer> queenEnd, ArrayList<Integer> arrow, ArrayList<Integer> gameState) {
        this.queenStart = queenStart;
        this.queenEnd = queenEnd;
        this.arrow = arrow;
    }

    public void applyMove(ArrayList<Integer> gameState, String player) {
        int startX = queenStart.get(0);
        int startY = queenStart.get(1);
        int endX = queenEnd.get(0);
        int endY = queenEnd.get(1);
        int arrowX = arrow.get(0);
        int arrowY = arrow.get(1);

        gameState.set(startX * 11 + startY, 0); // Clear old queen position
        gameState.set(endX * 11 + endY, 1); // Move queen
        gameState.set(arrowX * 11 + arrowY, 3); // Place arrow

        System.out.println(player + " moved: Queen " + queenStart + " -> " + queenEnd + ", Arrow at " + arrow);
    }

    public ArrayList<Integer> getQueenStart() { return queenStart; }
    public ArrayList<Integer> getQueenEnd() { return queenEnd; }
    public ArrayList<Integer> getArrow() { return arrow; }
}
