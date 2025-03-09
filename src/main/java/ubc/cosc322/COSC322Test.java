package ubc.cosc322;

import sfs2x.client.entities.Room;
import ygraph.ai.smartfox.games.BaseGameGUI;
import ygraph.ai.smartfox.games.GameClient;
import ygraph.ai.smartfox.games.GameMessage;
import ygraph.ai.smartfox.games.GamePlayer;
import ygraph.ai.smartfox.games.amazons.AmazonsGameMessage;

import java.util.*;

public class COSC322Test extends GamePlayer {
    private GameClient gameClient = null;
    private BaseGameGUI gamegui = null;
    private String userName;
    private String passwd;
    private GameState gameState;
    private MCTS mcts;
    private Minimax minimax;
    private TransitionFunction transitionFunction;

    public static void main(String[] args) {
        COSC322Test player = new COSC322Test("cosc322", "cosc322");

        if (player.getGameGUI() == null) {
            player.Go();
        } else {
            BaseGameGUI.sys_setup();
            java.awt.EventQueue.invokeLater(player::Go);
        }
    }

    public COSC322Test(String userName, String passwd) {
        this.userName = userName;
        this.passwd = passwd;
        this.gamegui = new BaseGameGUI(this);
        this.mcts = new MCTS();
        this.minimax = new Minimax();
        this.transitionFunction = new TransitionFunction();
    }

    @Override
    public void onLogin() {
        this.userName = gameClient.getUserName();

        if (gamegui != null) {
            gamegui.setRoomInformation(gameClient.getRoomList());
        }
    }

    @Override
    public boolean handleGameMessage(String messageType, Map<String, Object> msgDetails) {
        switch (messageType) {
            case GameMessage.GAME_STATE_BOARD:
                ArrayList<Integer> boardState = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.GAME_STATE);
                if (boardState != null) {
                    gameState = new GameState(boardState);
                    if (gamegui != null) gamegui.setGameState(boardState);
                }
                break;

            case GameMessage.GAME_ACTION_MOVE:
                ArrayList<Integer> queenPosCurr = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.QUEEN_POS_CURR);
                ArrayList<Integer> queenPosNext = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.QUEEN_POS_NEXT);
                ArrayList<Integer> arrowPos = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.ARROW_POS);

                if (queenPosCurr != null && queenPosNext != null && arrowPos != null) {
                    gameState.applyMove(queenPosCurr, queenPosNext, arrowPos);
                    if (gamegui != null) gamegui.updateGameState(queenPosCurr, queenPosNext, arrowPos);
                    makeMove();
                }
                break;

            default:
                System.out.println("Unhandled message type: " + messageType);
                break;
        }
        return true;
    }

    private void makeMove() {
        Move bestMove;
        if (transitionFunction.useMinimax(gameState)) {
            bestMove = minimax.findBestMove(gameState);
        } else {
            bestMove = mcts.findBestMove(gameState);
        }

        if (bestMove != null) {
            sendMove(bestMove);
        }
    }

    private void sendMove(Move move) {
        Map<String, Object> moveData = new HashMap<>();
        moveData.put(AmazonsGameMessage.QUEEN_POS_CURR, move.queenStart);
        moveData.put(AmazonsGameMessage.QUEEN_POS_NEXT, move.queenEnd);
        moveData.put(AmazonsGameMessage.ARROW_POS, move.arrow);
        gameClient.sendMoveMessage(moveData);
    }

    @Override
    public String userName() {
        return userName;
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
