package ubc.cosc322;
import java.util.ArrayList;
import java.util.List;

public class GameState {
    private char[][] board;
    private boolean isWhiteTurn;

    // Constructor from raw game state data
    public GameState(ArrayList<Integer> boardState) {
        this.board = convertTo2D(boardState);
        this.isWhiteTurn = true; // Set this correctly based on game state
    }

    // ✅ FIX: Copy constructor
    public GameState(GameState other) {
        this.board = new char[10][10];
        for (int i = 0; i < 10; i++) {
            System.arraycopy(other.board[i], 0, this.board[i], 0, 10);
        }
        this.isWhiteTurn = other.isWhiteTurn;
    }

    public List<Move> getLegalMoves() {
        // Implement logic to generate legal moves
        return new ArrayList<>();
    }

    public void applyMove(ArrayList<Integer> queenStart, ArrayList<Integer> queenEnd, ArrayList<Integer> arrow) {
        // Implement move execution logic
    }

    private char[][] convertTo2D(ArrayList<Integer> boardState) {
        // Convert 1D list to 2D board representation
        return new char[10][10];
    }

    public int evaluate() {
        // Implement heuristic function
        return 0;
    }
}
