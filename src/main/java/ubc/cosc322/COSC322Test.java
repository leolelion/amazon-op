package ubc.cosc322;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import sfs2x.client.entities.Room;
import ygraph.ai.smartfox.games.BaseGameGUI;
import ygraph.ai.smartfox.games.GameClient;
import ygraph.ai.smartfox.games.GameMessage;
import ygraph.ai.smartfox.games.GamePlayer;
import ygraph.ai.smartfox.games.amazons.AmazonsGameMessage;

public class COSC322Test extends GamePlayer {

    private GameClient gameClient = null;
    private BaseGameGUI gamegui = null;
    private String userName = null;
    private String passwd = null;
    private ArrayList<Integer> currentGameState = null;
    public Brain brain = new Brain();

    // aiColor: 1 for black, 2 for white.
    private int aiColor;
    private boolean gameStarted = false;

    public static void main(String[] args) {
        // Set the third parameter to 1 for black or 2 for white.
        COSC322Test player = new COSC322Test("LeBronAI", "cosc322", 2);
        if (player.getGameGUI() == null) {
            player.Go();
        } else {
            BaseGameGUI.sys_setup();
            java.awt.EventQueue.invokeLater(() -> player.Go());
        }
    }

    public COSC322Test(String userName, String passwd, int aiColor) {
        this.userName = userName;
        this.passwd = passwd;
        this.aiColor = aiColor;
        this.gamegui = new BaseGameGUI(this);
    }

    @Override
    public void onLogin() {
        System.out.println("Login successful!");
        List<Room> roomList = gameClient.getRoomList();
        if (!roomList.isEmpty()) {
            gameClient.joinRoom(roomList.get(0).getName());
            userName = gameClient.getUserName();
            if (gamegui != null) {
                gamegui.setRoomInformation(gameClient.getRoomList());
            }
        } else {
            System.err.println("No available game rooms to join.");
        }
        gameStarted = false;
    }

    @Override
    public boolean handleGameMessage(String messageType, Map<String, Object> msgDetails) {
        System.out.println("Received message: " + messageType);
        System.out.println("Message Details: " + msgDetails);

        switch (messageType) {
            case GameMessage.GAME_ACTION_START:
                System.out.println("Game started.");
                gameStarted = true;
                // If playing as white, make the first move.
                if (aiColor == 2) {
                    playMove();
                }
                break;
            case GameMessage.GAME_STATE_BOARD:
                updateGameState(msgDetails);
                // For black, move after the opponent; for white, a move may have already been made.
                if (gameStarted && aiColor == 1) {
                    playMove();
                }
                break;
            case GameMessage.GAME_ACTION_MOVE:
                processOpponentMove(msgDetails);
                if (gameStarted) {
                    playMove();
                }
                break;
        }
        return true;
    }

    private void updateGameState(Map<String, Object> msgDetails) {
        currentGameState = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.GAME_STATE);
        if (currentGameState == null || currentGameState.size() < 121) {
            System.err.println("Error: Invalid game board state received.");
            return;
        }
        if (gamegui != null) {
            gamegui.setGameState(currentGameState);
        }
        System.out.println("Updated game state successfully.");
    }

    // Process opponent move using the opponent's color (opposite of aiColor).
    private void processOpponentMove(Map<String, Object> msgDetails) {
        ArrayList<Integer> queenPosCurr = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.QUEEN_POS_CURR);
        ArrayList<Integer> queenPosNext = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.QUEEN_POS_NEXT);
        ArrayList<Integer> arrowPos = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.ARROW_POS);

        int opponentColor = (aiColor == 1) ? 2 : 1;
        int startX = queenPosCurr.get(0);
        int startY = queenPosCurr.get(1);
        int endX = queenPosNext.get(0);
        int endY = queenPosNext.get(1);
        int arrowX = arrowPos.get(0);
        int arrowY = arrowPos.get(1);

        currentGameState.set(startX * 11 + startY, 0);
        currentGameState.set(endX * 11 + endY, opponentColor);
        currentGameState.set(arrowX * 11 + arrowY, 3);

        System.out.println("Opponent moved: Queen " + queenPosCurr + " -> " + queenPosNext + ", Arrow at " + arrowPos);
        if (gamegui != null) {
            gamegui.updateGameState(queenPosCurr, queenPosNext, arrowPos);
            gamegui.repaint();
        }
        printBoard(currentGameState);
    }

    // Delegate turn processing to Brain and print game status.
    private void playMove() {
        if (!gameStarted) {
            System.out.println("Game has not started yet. Waiting for start signal.");
            return;
        }
        Move bestMove = brain.processTurn(currentGameState, aiColor);
        if (gamegui != null && bestMove != null) {
            gamegui.updateGameState(bestMove.getQueenStart(), bestMove.getQueenEnd(), bestMove.getArrow());
            gamegui.repaint();
        }
        printBoard(currentGameState);
        if (bestMove != null) {
            sendMoveMessage(bestMove.getQueenStart(), bestMove.getQueenEnd(), bestMove.getArrow());
        }
        // After the move, print the overall game status.
        System.out.println(brain.getGameStatus(currentGameState, aiColor));
    }

    private void printBoard(ArrayList<Integer> gameState) {
        System.out.println("------ Updated Game Board ------");
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                System.out.print(gameState.get(i * 11 + j) + " ");
            }
            System.out.println();
        }
        System.out.println("--------------------------------");
    }

    public void sendMoveMessage(ArrayList<Integer> queenStart, ArrayList<Integer> queenEnd, ArrayList<Integer> arrow) {
        if (queenStart == null || queenEnd == null || arrow == null) {
            System.err.println("Cannot send move message: one or more move parameters are null.");
            return;
        }
        if (gameClient != null) {
            gameClient.sendMoveMessage(queenStart, queenEnd, arrow);
            System.out.println("Move sent: Queen from " + queenStart + " to " + queenEnd + ", Arrow at " + arrow);
        }
    }

    @Override
    public String userName() {
        return this.userName;
    }

    @Override
    public GameClient getGameClient() {
        return this.gameClient;
    }

    @Override
    public BaseGameGUI getGameGUI() {
        return this.gamegui;
    }

    @Override
    public void connect() {
        gameClient = new GameClient(userName, passwd, this);
    }
}
