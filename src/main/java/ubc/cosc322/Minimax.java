package ubc.cosc322;

import java.util.ArrayList;
import java.util.List;

public class Minimax {
    private static final int MAX_DEPTH = 3; // Adjust for performance vs. accuracy

    public Move findBestMove(ArrayList<Integer> gameState, int playerColor) {
        System.out.println("🔍 Minimax analyzing board...");

        List<Move> possibleMoves = generateAllValidMoves(gameState, playerColor);
        System.out.println("🛠️ Possible moves found: " + possibleMoves.size());

        if (possibleMoves.isEmpty()) {
            System.err.println("No valid moves found for Minimax.");
            return null;
        }

        Move bestMove = null;
        int bestValue = Integer.MIN_VALUE;

        for (Move move : possibleMoves) {
            ArrayList<Integer> tempState = new ArrayList<>(gameState);
            move.applyMove(tempState, "Simulated Move");

            int moveValue = minimax(tempState, MAX_DEPTH, false, Integer.MIN_VALUE, Integer.MAX_VALUE, playerColor);
            if (moveValue > bestValue) {
                bestValue = moveValue;
                bestMove = move;
            }
        }

        if (bestMove == null) {
            System.err.println("Minimax failed to generate a move.");
        } else {
            System.out.println("Best move chosen: " + bestMove.getQueenStart() + " -> " + bestMove.getQueenEnd() +
                    ", Arrow at " + bestMove.getArrow());
        }

        return bestMove;
    }

    private int minimax(ArrayList<Integer> gameState, int depth, boolean isMaximizing, int alpha, int beta, int playerColor) {
        if (depth == 0) {
            return evaluateBoard(gameState, playerColor);
        }

        List<Move> possibleMoves = generateAllValidMoves(gameState, playerColor);
        if (possibleMoves.isEmpty()) {
            return isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE; // No moves = losing position
        }

        if (isMaximizing) {
            int bestValue = Integer.MIN_VALUE;
            for (Move move : possibleMoves) {
                ArrayList<Integer> tempState = new ArrayList<>(gameState);
                move.applyMove(tempState, "Simulated Move");

                int moveValue = minimax(tempState, depth - 1, false, alpha, beta, playerColor);
                bestValue = Math.max(bestValue, moveValue);
                alpha = Math.max(alpha, bestValue);

                if (beta <= alpha) break; // Beta cut-off
            }
            return bestValue;
        } else {
            int bestValue = Integer.MAX_VALUE;
            for (Move move : possibleMoves) {
                ArrayList<Integer> tempState = new ArrayList<>(gameState);
                move.applyMove(tempState, "Simulated Move");

                int moveValue = minimax(tempState, depth - 1, true, alpha, beta, playerColor);
                bestValue = Math.min(bestValue, moveValue);
                beta = Math.min(beta, bestValue);

                if (beta <= alpha) break; // Alpha cut-off
            }
            return bestValue;
        }
    }

    private int evaluateBoard(ArrayList<Integer> gameState, int playerColor) {
        int score = 0;
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                int index = i * 11 + j;
                int piece = gameState.get(index);

                if (piece == playerColor) {
                    score += 5; // More queens = better position
                } else if (piece == 3) {
                    score -= 1; // Arrows restrict movement
                }
            }
        }
        return score;
    }

    private List<Move> generateAllValidMoves(ArrayList<Integer> gameState, int playerColor) {
        List<Move> validMoves = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                int index = i * 11 + j;
                if (gameState.get(index) == playerColor) {
                    validMoves.addAll(getPossibleQueenMoves(gameState, i, j));
                }
            }
        }
        return validMoves;
    }

    private List<Move> getPossibleQueenMoves(ArrayList<Integer> gameState, int x, int y) {
        List<Move> moves = new ArrayList<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {1, 1}, {-1, 1}, {1, -1}};

        for (int[] dir : directions) {
            int newX = x + dir[0], newY = y + dir[1];
            while (isValidPosition(newX, newY) && gameState.get(newX * 11 + newY) == 0) {
                for (int[] arrowDir : directions) {
                    int arrowX = newX + arrowDir[0], arrowY = newY + arrowDir[1];
                    if (isValidPosition(arrowX, arrowY) && gameState.get(arrowX * 11 + arrowY) == 0) {
                        moves.add(new Move(
                                new ArrayList<>(List.of(x, y)),
                                new ArrayList<>(List.of(newX, newY)),
                                new ArrayList<>(List.of(arrowX, arrowY)),
                                gameState
                        ));
                    }
                }
                newX += dir[0];
                newY += dir[1];
            }
        }
        return moves;
    }

    private boolean isValidPosition(int row, int col) {
        return row > 0 && row <= 10 && col > 0 && col <= 10;
    }
}
