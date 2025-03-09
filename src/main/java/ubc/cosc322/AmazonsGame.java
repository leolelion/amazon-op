package ubc.cosc322;

import java.util.ArrayList;

public class AmazonsGame {
    private int[] board; // 10x10 board flattened to 100 elements
    private int currentPlayer; // 0 for Black, 1 for White

    public AmazonsGame() {
        board = new int[100];
        currentPlayer = 0; // Black starts
        initializeBoard();
    }

    // Initialize the standard starting position for Amazons
    private void initializeBoard() {
        // Empty board
        for (int i = 0; i < 100; i++) {
            board[i] = 0;
        }
        // Black queens (2)
        board[3] = 2;  // (0, 3)
        board[6] = 2;  // (0, 6)
        board[30] = 2; // (3, 0)
        board[39] = 2; // (3, 9)
        // White queens (1)
        board[60] = 1; // (6, 0)
        board[69] = 1; // (6, 9)
        board[93] = 1; // (9, 3)
        board[96] = 1; // (9, 6)
    }

    // Update board from server data
    public void updateBoard(ArrayList<Integer> gameState) {
        if (gameState != null && gameState.size() == 100) {
            for (int i = 0; i < 100; i++) {
                board[i] = gameState.get(i);
            }
        }
    }

    // Apply a move to the board
    public void applyMove(int player, Move move) {
        int queenValue = (player == 0) ? 2 : 1; // Black = 2, White = 1
        int currPos = move.queenCurr[0] * 10 + move.queenCurr[1];
        int nextPos = move.queenNext[0] * 10 + move.queenNext[1];
        int arrowPos = move.arrow[0] * 10 + move.arrow[1];

        board[currPos] = 0;           // Clear old queen position
        board[nextPos] = queenValue;  // Move queen to new position
        board[arrowPos] = 3;          // Place arrow
        currentPlayer = 1 - player;   // Switch turns
    }

    // Get the current board state
    public int[] getBoard() {
        return board;
    }

    // Get the current player
    public int getCurrentPlayer() {
        return currentPlayer;
    }
}