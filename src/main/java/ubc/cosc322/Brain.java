package ubc.cosc322;

import java.util.ArrayList;

public class Brain {
    private final int BOARD_SIZE = 10;

    // Generate best move for the given board and AI color.
    public Move getBestMove(ArrayList<Integer> gameState, int aiColor) {
        System.out.println("Brain analyzing board for AI color " + aiColor + "...");
        if (shouldUseMCTS(gameState)) {
            return new MCTS().findBestMove(gameState, aiColor);
        } else {
            return new Minimax().findBestMove(gameState, aiColor);
        }
    }

    private boolean shouldUseMCTS(ArrayList<Integer> gameState) {
        int emptySpaces = (int) gameState.stream().filter(i -> i == 0).count();
        return emptySpaces > (BOARD_SIZE * BOARD_SIZE) / 2;
    }
}
