package ubc.cosc322;

import java.util.ArrayList;

public class Brain {
    public Move getBestMove(ArrayList<Integer> gameState, int playerColor) {
        System.out.println("Brain received board state. Computing best move...");
        Minimax minimax = new Minimax();
        return minimax.findBestMove(gameState, playerColor);
    }
}
