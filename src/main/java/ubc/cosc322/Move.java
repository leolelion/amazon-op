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

    // For simulations (no output)
    public void simulateMove(ArrayList<Integer> gameState, int pieceValue) {
        int startX = queenStart.get(0), startY = queenStart.get(1);
        int endX = queenEnd.get(0), endY = queenEnd.get(1);
        int arrowX = arrow.get(0), arrowY = arrow.get(1);
        gameState.set(startX * 11 + startY, 0);
        gameState.set(endX * 11 + endY, pieceValue);
        gameState.set(arrowX * 11 + arrowY, 3);
    }

    // Apply the real move for the AI using its assigned color.
    public void applyMoveForAI(ArrayList<Integer> gameState, int aiColor) {
        int startX = queenStart.get(0), startY = queenStart.get(1);
        int endX = queenEnd.get(0), endY = queenEnd.get(1);
        int arrowX = arrow.get(0), arrowY = arrow.get(1);
        gameState.set(startX * 11 + startY, 0);
        gameState.set(endX * 11 + endY, aiColor);
        gameState.set(arrowX * 11 + arrowY, 3);
    }

    public ArrayList<Integer> getQueenStart() {
        return queenStart;
    }

    public ArrayList<Integer> getQueenEnd() {
        return queenEnd;
    }

    public ArrayList<Integer> getArrow() {
        return arrow;
    }
}
