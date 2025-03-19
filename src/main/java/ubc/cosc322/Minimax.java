package ubc.cosc322;

import java.util.ArrayList;
import java.util.Random;
import java.util.List;

public class Minimax {
    private final Random random = new Random();

    public Move findBestMove(ArrayList<Integer> gameState, int playerColor) {
        System.out.println("Minimax analyzing board...");

        ArrayList<int[]> blackQueens = new ArrayList<>();
        for (int i = 0; i < gameState.size(); i++) {
            if (gameState.get(i) == playerColor) { // 1 = Black queen
                int row = i / 10;
                int col = i % 10;
                blackQueens.add(new int[]{row, col});
            }
        }

        if (blackQueens.isEmpty()) {
            System.err.println("Error: No Black queens found on board.");
            return null;
        }

        ArrayList<Move> possibleMoves = new ArrayList<>();

        for (int[] queen : blackQueens) {
            int row = queen[0];
            int col = queen[1];

            for (int newRow = row - 1; newRow <= row + 1; newRow++) {
                for (int newCol = col - 1; newCol <= col + 1; newCol++) {
                    if (isValidMove(row, col, newRow, newCol, gameState)) {
                        ArrayList<Integer> queenStart = new ArrayList<>(List.of(row, col));
                        ArrayList<Integer> queenEnd = new ArrayList<>(List.of(newRow, newCol));
                        ArrayList<Integer> arrowPos = new ArrayList<>(List.of(newRow, newCol));

                        possibleMoves.add(new Move(queenStart, queenEnd, arrowPos, gameState));
                    }
                }
            }
        }

        if (possibleMoves.isEmpty()) {
            System.err.println("Minimax failed to find a valid move.");
            return null;
        }

        Move chosenMove = possibleMoves.get(random.nextInt(possibleMoves.size()));
        System.out.println("LeBronAI is moving from " + chosenMove.getQueenStart() + " to " + chosenMove.getQueenEnd());
        return chosenMove;
    }

    private boolean isValidMove(int startRow, int startCol, int endRow, int endCol, ArrayList<Integer> gameState) {
        if (endRow < 0 || endRow >= 10 || endCol < 0 || endCol >= 10) return false;
        int endIndex = endRow * 10 + endCol;
        return gameState.get(endIndex) == 0;
    }
}
