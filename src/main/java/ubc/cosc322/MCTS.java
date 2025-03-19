package ubc.cosc322;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;

public class MCTS {
    private Random random = new Random();

    public Move findBestMove(ArrayList<Integer> gameState, int playerColor) {
        System.out.println("🔍 MCTS selecting a move...");
        ArrayList<Move> possibleMoves = generateAllValidMoves(gameState, playerColor);
        if (possibleMoves.isEmpty()) return null;
        return possibleMoves.get(random.nextInt(possibleMoves.size())); // Pick a random move from valid moves
    }

    private ArrayList<Move> generateAllValidMoves(ArrayList<Integer> gameState, int playerColor) {
        ArrayList<Move> validMoves = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                if (gameState.get(i * 11 + j) == playerColor) { // Find all queen positions
                    validMoves.addAll(findValidMovesFrom(i, j, gameState));
                }
            }
        }
        return validMoves;
    }

    private ArrayList<Move> findValidMovesFrom(int x, int y, ArrayList<Integer> gameState) {
        ArrayList<Move> validMoves = new ArrayList<>();
        int[] directions = {-1, 0, 1};

        for (int dx : directions) {
            for (int dy : directions) {
                if (dx == 0 && dy == 0) continue;
                int newX = x + dx, newY = y + dy;
                while (isInsideBoard(newX, newY) && gameState.get(newX * 11 + newY) == 0) {
                    int arrowX = newX, arrowY = newY;
                    while (isInsideBoard(arrowX, arrowY) && gameState.get(arrowX * 11 + arrowY) == 0) {
                        ArrayList<Integer> start = new ArrayList<>(List.of(x, y));
                        ArrayList<Integer> end = new ArrayList<>(List.of(newX, newY));
                        ArrayList<Integer> arrow = new ArrayList<>(List.of(arrowX, arrowY));

                        validMoves.add(new Move(start, end, arrow, gameState));
                        arrowX += dx;
                        arrowY += dy;
                    }
                    newX += dx;
                    newY += dy;
                }
            }
        }
        return validMoves;
    }

    private boolean isInsideBoard(int x, int y) {
        return x >= 1 && x <= 10 && y >= 1 && y <= 10;
    }
}
