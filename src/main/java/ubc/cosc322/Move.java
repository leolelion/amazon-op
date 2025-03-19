package ubc.cosc322;

import java.util.ArrayList;

public class Move {
    private final ArrayList<Integer> queenStart;
    private final ArrayList<Integer> queenEnd;
    private final ArrayList<Integer> arrow;
    private final ArrayList<Integer> gameState;

    public Move(ArrayList<Integer> queenStart, ArrayList<Integer> queenEnd, ArrayList<Integer> arrow, ArrayList<Integer> gameState) {
        this.queenStart = queenStart;
        this.queenEnd = queenEnd;
        this.arrow = arrow;
        this.gameState = gameState;
    }

    public boolean isValidMove() {
        if (queenStart == null || queenEnd == null || arrow == null || gameState == null) {
            System.err.println("Error: Null values detected in move validation.");
            return false;
        }

        int startIndex = toIndex(queenStart);
        int endIndex = toIndex(queenEnd);
        int arrowIndex = toIndex(arrow);

        if (!isWithinBounds(startIndex) || !isWithinBounds(endIndex) || !isWithinBounds(arrowIndex)) {
            System.err.println("Error: Move contains out-of-bounds coordinates.");
            return false;
        }

        if (!isBlackQueenMove()) {
            System.err.println("Error: AI is attempting to move a non-black queen.");
            return false;
        }

        if (gameState.get(endIndex) != 0 || gameState.get(arrowIndex) != 0) {
            System.err.println("Error: Target position or arrow position is occupied.");
            return false;
        }

        return true;
    }

    private boolean isBlackQueenMove() {
        int startIndex = toIndex(queenStart);
        if (!isWithinBounds(startIndex)) return false;
        return gameState.get(startIndex) == 1; // 1 represents a Black Queen
    }

    private int toIndex(ArrayList<Integer> position) {
        if (position == null || position.size() != 2) return -1;
        int row = position.get(0);
        int col = position.get(1);
        return row * 10 + col;
    }

    private boolean isWithinBounds(int index) {
        return index >= 0 && index < 100;
    }

    public void processOpponentMove(ArrayList<Integer> gameState) {
        System.out.println("Processing opponent move...");
    }

    public ArrayList<Integer> getQueenStart() { return queenStart; }
    public ArrayList<Integer> getQueenEnd() { return queenEnd; }
    public ArrayList<Integer> getArrow() { return arrow; }
}
