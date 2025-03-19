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

    private int aiColor = 1;
    public Brain brain = new Brain();
    private boolean opponentMoved = false; //

    public static void main(String[] args) {
        COSC322Test player = new COSC322Test("LeBronAI", "cosc322");

        if (player.getGameGUI() == null) {
            player.Go();
        } else {
            BaseGameGUI.sys_setup();
            java.awt.EventQueue.invokeLater(() -> player.Go());
        }
    }

    public COSC322Test(String userName, String passwd) {
        this.userName = userName;
        this.passwd = passwd;
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
    }

    @Override
    public boolean handleGameMessage(String messageType, Map<String, Object> msgDetails) {
        switch (messageType) {
            case GameMessage.GAME_STATE_BOARD:
                updateGameState(msgDetails);
                break; // AI waits for opponent to move first

            case GameMessage.GAME_ACTION_MOVE:
                processOpponentMove(msgDetails);
                playMove(); //
                break;
        }
        return true;
    }

    private void updateGameState(Map<String, Object> msgDetails) {
        currentGameState = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.GAME_STATE);
        if (currentGameState == null || currentGameState.size() < 100) {
            System.err.println("Error: Invalid game board state received.");
            return;
        }

        if (gamegui != null) {
            gamegui.setGameState(currentGameState);
        }

        System.out.println("Updated game state successfully.");
    }

    private void processOpponentMove(Map<String, Object> msgDetails) {
        ArrayList<Integer> queenPosCurr = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.QUEEN_POS_CURR);
        ArrayList<Integer> queenPosNext = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.QUEEN_POS_NEXT);
        ArrayList<Integer> arrowPos = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.ARROW_POS);

        if (queenPosCurr == null || queenPosNext == null || arrowPos == null) {
            System.err.println("Error: Missing opponent move data.");
            return;
        }

        System.out.println("Opponent moved: Queen " + queenPosCurr + " -> " + queenPosNext + ", Arrow at " + arrowPos);

        Move move = new Move(queenPosCurr, queenPosNext, arrowPos, currentGameState);
        move.processOpponentMove(currentGameState);

        if (gamegui != null) {
            gamegui.updateGameState(queenPosCurr, queenPosNext, arrowPos);
        }

        opponentMoved = true; //
    }

    private void playMove() {
        if (!opponentMoved) {
            System.out.println("Waiting for opponent to move...");
            return;
        }

        System.out.println("LeBronAI is selecting a move...");
        Move bestMove = brain.getBestMove(currentGameState, aiColor);

        if (bestMove != null && bestMove.isValidMove()) {
            sendMoveMessage(bestMove.getQueenStart(), bestMove.getQueenEnd(), bestMove.getArrow());
        } else {
            System.err.println("AI failed to generate a move.");
        }
    }

    public void sendMoveMessage(ArrayList<Integer> queenStart, ArrayList<Integer> queenEnd, ArrayList<Integer> arrow) {
        if (gameClient != null) {
            gameClient.sendMoveMessage(queenStart, queenEnd, arrow);
            System.out.println("Move sent by LeBronAI: Queen from " + queenStart + " to " + queenEnd + ", Arrow at " + arrow);
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
