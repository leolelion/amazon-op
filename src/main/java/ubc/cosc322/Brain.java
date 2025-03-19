package ubc.cosc322;
import java.util.ArrayList;

public class Brain {
    private final int BOARD_SIZE = 10;

    public Move getBestMove(ArrayList<Integer> gameState, int playerColor) {
        System.out.println("Brain analyzing board...");

        // Select MCTS for early game, Minimax for late game
        if (shouldUseMCTS(gameState)) {
            return new MCTS().findBestMove(gameState, playerColor);
        } else {
            return new Minimax().findBestMove(gameState, playerColor);
        }
    }

    private boolean shouldUseMCTS(ArrayList<Integer> gameState) {
        int emptySpaces = (int) gameState.stream().filter(i -> i == 0).count();
        return emptySpaces > (BOARD_SIZE * BOARD_SIZE) / 2; // Use MCTS early on
    }
}
