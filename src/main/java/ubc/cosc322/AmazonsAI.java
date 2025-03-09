package ubc.cosc322;

import java.util.List;
import java.util.Random;

/**
 * AI player for the Game of the Amazons.
 * Currently selects a random move; can be extended with MCTS or Minimax.
 */
public class AmazonsAI {
    private AmazonsGame game;
    private int myColor;
    private Random rand = new Random();

    /**
     * Constructor initializing the AI with game state and player color.
     */
    public AmazonsAI(AmazonsGame game, int myColor) {
        this.game = game;
        this.myColor = myColor;
    }

    /**
     * Selects a move for the AI player.
     */
    public Move selectMove() {
        List<Move> moves = game.generateLegalMoves(myColor);
        if (moves.isEmpty()) {
            return null; // No legal moves available
        }
        return moves.get(rand.nextInt(moves.size())); // Random move selection
    }

    // Placeholder for future advanced AI implementations (e.g., MCTS, Minimax)
}