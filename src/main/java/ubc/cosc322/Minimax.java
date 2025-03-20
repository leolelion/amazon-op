package ubc.cosc322;

import java.util.ArrayList;
import java.util.List;

public class Minimax {
    private static final int MAX_DEPTH = 3;

    public int evaluateBoard(ArrayList<Integer> gameState, int playerColor) {
        int score = 0;
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                int index = i * 11 + j;
                int piece = gameState.get(index);
                if (piece == playerColor) {
                    score += 50;
                    score += countValidMovesFrom(i, j, gameState) * 5;
                } else if (piece == 3) {
                    score -= 3;
                }
            }
        }
        return score;
    }

    private int countValidMovesFrom(int x, int y, ArrayList<Integer> gameState) {
        int count = 0;
        int[][] directions = { {-1,0}, {1,0}, {0,-1}, {0,1}, {-1,-1}, {1,1}, {-1,1}, {1,-1} };
        for (int[] dir : directions) {
            int newX = x + dir[0], newY = y + dir[1];
            while (isValidPosition(newX, newY, gameState)) {
                count++;
                newX += dir[0];
                newY += dir[1];
            }
        }
        return count;
    }

    private boolean isValidPosition(int row, int col, ArrayList<Integer> gameState) {
        return row > 0 && row <= 10 && col > 0 && col <= 10 &&
                gameState.get(row * 11 + col) == 0;
    }

    public Move findBestMove(ArrayList<Integer> gameState, int playerColor) {
        List<Move> possibleMoves = generateAllValidMoves(gameState, playerColor);
        if (possibleMoves.isEmpty()) {
            // Instead of printing an error message, simply return null.
            return null;
        }
        Move bestMove = null;
        int bestValue = Integer.MIN_VALUE;
        for (Move move : possibleMoves) {
            ArrayList<Integer> tempState = new ArrayList<>(gameState);
            move.simulateMove(tempState, playerColor);
            int moveValue = minimax(tempState, MAX_DEPTH - 1, false, Integer.MIN_VALUE, Integer.MAX_VALUE, playerColor);
            if (moveValue > bestValue) {
                bestValue = moveValue;
                bestMove = move;
            }
        }
        if (bestMove == null) {
            return null;
        } else {
            System.out.println("Best move: " + bestMove.getQueenStart() + " -> " +
                    bestMove.getQueenEnd() + ", Arrow at " + bestMove.getArrow());
        }
        return bestMove;
    }

    private int minimax(ArrayList<Integer> gameState, int depth, boolean isMaximizing, int alpha, int beta, int playerColor) {
        if (depth == 0 || generateAllValidMoves(gameState, playerColor).isEmpty()) {
            return evaluateBoard(gameState, playerColor);
        }
        List<Move> possibleMoves = generateAllValidMoves(gameState, playerColor);
        if (isMaximizing) {
            int bestValue = Integer.MIN_VALUE;
            for (Move move : possibleMoves) {
                ArrayList<Integer> tempState = new ArrayList<>(gameState);
                move.simulateMove(tempState, playerColor);
                int moveValue = minimax(tempState, depth - 1, false, alpha, beta, playerColor);
                bestValue = Math.max(bestValue, moveValue);
                alpha = Math.max(alpha, bestValue);
                if (beta <= alpha) break;
            }
            return bestValue;
        } else {
            int bestValue = Integer.MAX_VALUE;
            int opponent = (playerColor == 1) ? 2 : 1;
            for (Move move : possibleMoves) {
                ArrayList<Integer> tempState = new ArrayList<>(gameState);
                move.simulateMove(tempState, opponent);
                int moveValue = minimax(tempState, depth - 1, true, alpha, beta, playerColor);
                bestValue = Math.min(bestValue, moveValue);
                beta = Math.min(beta, bestValue);
                if (beta <= alpha) break;
            }
            return bestValue;
        }
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
        int[][] directions = { {-1,0}, {1,0}, {0,-1}, {0,1}, {-1,-1}, {1,1}, {-1,1}, {1,-1} };
        for (int[] dir : directions) {
            int newX = x + dir[0], newY = y + dir[1];
            while (isValidPosition(newX, newY, gameState)) {
                for (int[] arrowDir : directions) {
                    int arrowX = newX + arrowDir[0], arrowY = newY + arrowDir[1];
                    if (isValidPosition(arrowX, arrowY, gameState)) {
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
}
