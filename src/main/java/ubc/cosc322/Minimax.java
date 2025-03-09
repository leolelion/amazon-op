package ubc.cosc322;

public class Minimax {
    private static final int MAX_DEPTH = 3;

    public Move findBestMove(GameState gameState) {
        int bestValue = Integer.MIN_VALUE;
        Move bestMove = null;

        for (Move move : gameState.getLegalMoves()) {
            // ✅ Use the copy constructor
            GameState newState = new GameState(gameState);
            newState.applyMove(move.queenStart, move.queenEnd, move.arrow);
            int moveValue = minimax(newState, MAX_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, false);

            if (moveValue > bestValue) {
                bestValue = moveValue;
                bestMove = move;
            }
        }
        return bestMove;
    }

    private int minimax(GameState state, int depth, int alpha, int beta, boolean isMax) {
        if (depth == 0) return state.evaluate();
        return isMax ? alpha : beta;
    }
}
