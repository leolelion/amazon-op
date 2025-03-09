package ubc.cosc322;


import java.util.ArrayList;
import java.util.List;

/**
 * Manages the game state and move generation for the Game of the Amazons.
 */
public class AmazonsGame {
    private int[][] board = new int[10][10]; // 0=empty, 1=White, 2=Black, 3=Arrow
    private int currentPlayer; // 0=Black, 1=White

    /**
     * Updates the board state based on server-provided data.
     */
    public void updateBoard(ArrayList<Integer> gameS) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                board[i][j] = gameS.get(i * 10 + j);
            }
        }
        // Placeholder: Assume White starts; adjust based on game rules or server data
        currentPlayer = 1;
    }

    /**
     * Applies a move to the board and switches the current player.
     */
    public void applyMove(int player, Move move) {
        int rCurr = move.queenCurr[0] - 1; // Convert to 0-based indexing
        int cCurr = move.queenCurr[1] - 1;
        int rNext = move.queenNext[0] - 1;
        int cNext = move.queenNext[1] - 1;
        int rArrow = move.arrow[0] - 1;
        int cArrow = move.arrow[1] - 1;

        board[rCurr][cCurr] = 0; // Clear current queen position
        board[rNext][cNext] = player + 1; // 1=White, 2=Black
        board[rArrow][cArrow] = 3; // Place arrow
        currentPlayer = 1 - player; // Switch player
    }

    /**
     * Generates all legal moves for the given player.
     */
    public List<Move> generateLegalMoves(int player) {
        List<Move> moves = new ArrayList<>();
        int amazonValue = player + 1; // 1=White, 2=Black
        int[][] directions = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1}, // Orthogonal
                {-1, -1}, {-1, 1}, {1, -1}, {1, 1} // Diagonal
        };

        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                if (board[r][c] == amazonValue) {
                    for (int[] dir : directions) {
                        int dr = dir[0], dc = dir[1];
                        int rNew = r + dr, cNew = c + dc;
                        while (isValid(rNew, cNew) && board[rNew][cNew] == 0) {
                            int[] queenCurr = {r + 1, c + 1}; // Convert to 1-based indexing
                            int[] queenNext = {rNew + 1, cNew + 1};
                            for (int[] arrowDir : directions) {
                                int ar = rNew + arrowDir[0], ac = cNew + arrowDir[1];
                                while (isValid(ar, ac) && board[ar][ac] == 0) {
                                    moves.add(new Move(queenCurr, queenNext, new int[]{ar + 1, ac + 1}));
                                    ar += arrowDir[0];
                                    ac += arrowDir[1];
                                }
                            }
                            rNew += dr;
                            cNew += dc;
                        }
                    }
                }
            }
        }
        return moves;
    }

    /**
     * Checks if a position is within board bounds.
     */
    private boolean isValid(int r, int c) {
        return r >= 0 && r < 10 && c >= 0 && c < 10;
    }

    /**
     * Returns the current player.
     */
    public int getCurrentPlayer() {
        return currentPlayer;
    }
}
