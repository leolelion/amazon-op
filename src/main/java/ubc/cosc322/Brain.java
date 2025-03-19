package ubc.cosc322;

import java.util.ArrayList;

public class Brain {
    private final int BOARD_SIZE = 10;

    // Ensure that playerColor is 1 (black) for the AI.
    public Move getBestMove(ArrayList<Integer> gameState, int playerColor) {
        if (playerColor != 1) {
            System.err.println("Error: AI must move a black queen (piece value 1).");
            return null;
        }
        System.out.println("Finding best move for Black Queen...");
        if (shouldUseMCTS(gameState)) {
            return new MCTS().findBestMove(gameState, playerColor);
        } else {
            return new Minimax().findBestMove(gameState, playerColor);
        }
    }

    private boolean shouldUseMCTS(ArrayList<Integer> gameState) {
        int emptySpaces = (int) gameState.stream().filter(i -> i == 0).count();
        return emptySpaces > (BOARD_SIZE * BOARD_SIZE) / 2;
    }
}
