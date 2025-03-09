package ubc.cosc322;

public class AmazonsAI {
    private AmazonsGame gameState;
    private int myColor; // 0 for Black, 1 for White

    public AmazonsAI(AmazonsGame gameState, int myColor) {
        this.gameState = gameState;
        this.myColor = myColor;
    }

    public Move selectMove() {
        int queenValue = (myColor == 0) ? 2 : 1; // Black queens = 2, White queens = 1
        int[] board = gameState.getBoard();

        // Find the first queen of my color
        int queenPos = -1;
        for (int i = 0; i < board.length; i++) {
            if (board[i] == queenValue) {
                queenPos = i;
                break;
            }
        }

        if (queenPos == -1) return null; // No queen found

        int row = queenPos / 10;
        int col = queenPos % 10;
        int[] queenCurr = {row, col};

        // Possible directions: right, left, up, down (simplified for now)
        int[][] directions = {{0, 1}, {0, -1}, {-1, 0}, {1, 0}};
        int[] queenNext = null;
        int[] arrow = null;

        // Try each direction for queen move
        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            if (isValidPosition(newRow, newCol) && board[newRow * 10 + newCol] == 0) {
                queenNext = new int[]{newRow, newCol};
                // Try an arrow shot in the same direction from the new position
                int arrowRow = newRow + dir[0];
                int arrowCol = newCol + dir[1];
                if (isValidPosition(arrowRow, arrowCol) && board[arrowRow * 10 + arrowCol] == 0) {
                    arrow = new int[]{arrowRow, arrowCol};
                    break; // Found a valid move, stop searching
                }
            }
        }

        if (queenNext == null || arrow == null) return null; // No valid move found

        return new Move(queenCurr, queenNext, arrow);
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < 10 && col >= 0 && col < 10;
    }
}