package ubc.cosc322;
import java.util.List;
import java.util.Random;

public class MCTS {
    private static final int SIMULATIONS = 1000;
    private Random random = new Random();

    public Move findBestMove(GameState gameState) {
        List<Move> moves = gameState.getLegalMoves();
        if (moves.isEmpty()) return null;

        Move bestMove = moves.get(0);
        double bestWinRate = -1;

        for (Move move : moves) {
            int wins = 0;
            for (int i = 0; i < SIMULATIONS; i++) {
                // ✅ Use the copy constructor
                GameState tempState = new GameState(gameState);
                tempState.applyMove(move.queenStart, move.queenEnd, move.arrow);
                if (simulate(tempState)) {
                    wins++;
                }
            }
            double winRate = (double) wins / SIMULATIONS;
            if (winRate > bestWinRate) {
                bestWinRate = winRate;
                bestMove = move;
            }
        }
        return bestMove;
    }

    private boolean simulate(GameState state) {
        // Run a random simulation to determine win/loss
        return random.nextBoolean();
    }
}
