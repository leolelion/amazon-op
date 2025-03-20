package ubc.cosc322;

import java.util.ArrayList;

public class Brain {
    private final int BOARD_SIZE = 10;

    // Processes the AI's turn:
    // 1. Checks if any AI queen can move (loss condition).
    // 2. Generates and applies the best move.
    // 3. Returns the best move so that COSC322Test can update the GUI.
    //    (Also prints a win message if the opponent has no moves.)
    public Move processTurn(ArrayList<Integer> gameState, int aiColor) {
        if (!hasValidMoves(gameState, aiColor)) {
            System.out.println("All your queens are blocked. LeBronAI has lost.");
            return null;
        }
        Move bestMove = getBestMove(gameState, aiColor);
        if (bestMove == null) {
            System.out.println("No valid moves available for LeBronAI. LeBronAI has lost.");
            return null;
        }
        bestMove.applyMoveForAI(gameState, aiColor);
        String colorName = (aiColor == 1) ? "Black" : "White";
        System.out.println("LeBronAI (" + colorName + ") moved: Queen "
                + bestMove.getQueenStart() + " -> "
                + bestMove.getQueenEnd() + ", Arrow at "
                + bestMove.getArrow());
        if (!opponentHasValidMoves(gameState, aiColor)) {
            System.out.println("No valid moves left for the opponent. LeBronAI wins!");
        }
        return bestMove;
    }

    // Returns the best move using either MCTS or Minimax.
    public Move getBestMove(ArrayList<Integer> gameState, int aiColor) {
        System.out.println("Brain analyzing board for AI color " + aiColor + "...");
        if (shouldUseMCTS(gameState)) {
            System.out.println("Algorithm used: MCTS");
            return new MCTS().findBestMove(gameState, aiColor);
        } else {
            System.out.println("Algorithm used: Minimax");
            return new Minimax().findBestMove(gameState, aiColor);
        }
    }

    // Decide which algorithm to use based on the number of empty cells in the 10x10 board.
    private boolean shouldUseMCTS(ArrayList<Integer> gameState) {
        int emptySpaces = 0;
        for (int i = 1; i <= BOARD_SIZE; i++) {
            for (int j = 1; j <= BOARD_SIZE; j++) {
                int index = i * 11 + j;
                if (gameState.get(index) == 0) {
                    emptySpaces++;
                }
            }
        }
        return emptySpaces > 50;
    }

    // Returns a status message only if the game is over.
    // Returns an empty string if the game is still ongoing.
    public String getGameStatus(ArrayList<Integer> gameState, int aiColor) {
        if (!hasValidMoves(gameState, aiColor)) {
            return "Game Over: All your queens are blocked. LeBronAI has lost.";
        } else if (!opponentHasValidMoves(gameState, aiColor)) {
            return "Game Over: No valid moves left for the opponent. LeBronAI wins!";
        }
        return "";
    }

    // Checks if any queen of the specified color has at least one valid move.
    public boolean hasValidMoves(ArrayList<Integer> gameState, int playerColor) {
        for (int i = 1; i <= BOARD_SIZE; i++) {
            for (int j = 1; j <= BOARD_SIZE; j++) {
                int index = i * 11 + j;
                if (gameState.get(index) == playerColor && canMovePiece(gameState, i, j)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Checks if a queen at (x,y) can move in any direction.
    private boolean canMovePiece(ArrayList<Integer> gameState, int x, int y) {
        int[] directions = {-1, 0, 1};
        for (int dx : directions) {
            for (int dy : directions) {
                if (dx == 0 && dy == 0)
                    continue;
                int newX = x + dx;
                int newY = y + dy;
                if (isInsideBoard(newX, newY) && gameState.get(newX * 11 + newY) == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isInsideBoard(int x, int y) {
        return x >= 1 && x <= BOARD_SIZE && y >= 1 && y <= BOARD_SIZE;
    }

    // Checks if the opponent (the opposite color) has any valid moves.
    public boolean opponentHasValidMoves(ArrayList<Integer> gameState, int aiColor) {
        int opponentColor = (aiColor == 1) ? 2 : 1;
        return hasValidMoves(gameState, opponentColor);
    }
}