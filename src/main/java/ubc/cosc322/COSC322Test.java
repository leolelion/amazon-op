package ubc.cosc322;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sfs2x.client.entities.Room;
import ygraph.ai.smartfox.games.BaseGameGUI;
import ygraph.ai.smartfox.games.GameClient;
import ygraph.ai.smartfox.games.GameMessage;
import ygraph.ai.smartfox.games.GamePlayer;
import ygraph.ai.smartfox.games.amazons.AmazonsGameMessage;

/**
 * Main class for the Game of the Amazons AI player.
 * Handles server communication, GUI updates, and triggers AI moves.
 */
public class COSC322Test extends GamePlayer {
	private GameClient gameClient = null;
	private BaseGameGUI gamegui = null;
	private String userName = null;
	private String passwd = null;

	private AmazonsGame gameState;
	private AmazonsAI ai;
	private int myColor; // 0 for Black, 1 for White

	/**
	 * Main method to launch the player.
	 */
	public static void main(String[] args) {
		COSC322Test player = new COSC322Test("cosc322", "cosc322");
		if (player.getGameGUI() == null) {
			player.Go();
		} else {
			BaseGameGUI.sys_setup();
			java.awt.EventQueue.invokeLater(() -> player.Go());
		}
	}

	/**
	 * Constructor initializing username, password, GUI, and game state.
	 */
	public COSC322Test(String userName, String passwd) {
		this.userName = userName;
		this.passwd = passwd;
		this.gamegui = new BaseGameGUI(this);
		this.gameState = new AmazonsGame();
		// AI will be initialized after myColor is determined
	}

	/**
	 * Called upon successful login to update username and room info.
	 */
	@Override
	public void onLogin() {
		userName = gameClient.getUserName();
		if (gamegui != null) {
			gamegui.setRoomInformation(gameClient.getRoomList());
		}
	}

	/**
	 * Handles incoming game messages from the server.
	 */
	@Override
	public boolean handleGameMessage(String messageType, Map<String, Object> msgDetails) {
		switch (messageType) {
			case GameMessage.GAME_STATE_JOIN:
				// Determine player color based on server-assigned roles
				String blackPlayer = (String) msgDetails.get(AmazonsGameMessage.PLAYER_BLACK);
				String whitePlayer = (String) msgDetails.get(AmazonsGameMessage.PLAYER_WHITE);
				if (userName.equals(blackPlayer)) {
					myColor = 0; // Black
				} else if (userName.equals(whitePlayer)) {
					myColor = 1; // White
				}
				ai = new AmazonsAI(gameState, myColor);
				break;

			case GameMessage.GAME_STATE_BOARD:
				// Update board state from server data
				ArrayList<Integer> gameS = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.GAME_STATE);
				if (gameS != null) {
					gameState.updateBoard(gameS);
					if (gamegui != null) {
						gamegui.setGameState(gameS);
					}
					if (gameState.getCurrentPlayer() == myColor) {
						Move move = ai.selectMove();
						sendMove(move);
					}
				} else {
					System.err.println("Error: GAME_STATE_BOARD message missing data.");
				}
				break;

			case GameMessage.GAME_ACTION_MOVE:
				// Process opponent's move
				ArrayList<Integer> queenPosCurr = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.QUEEN_POS_CURR);
				ArrayList<Integer> queenPosNext = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.QUEEN_POS_NEXT);
				ArrayList<Integer> arrowPos = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.ARROW_POS);
				if (queenPosCurr != null && queenPosNext != null && arrowPos != null) {
					int opponentColor = 1 - myColor;
					Move opponentMove = new Move(
							new int[]{queenPosCurr.get(0), queenPosCurr.get(1)},
							new int[]{queenPosNext.get(0), queenPosNext.get(1)},
							new int[]{arrowPos.get(0), arrowPos.get(1)}
					);
					gameState.applyMove(opponentColor, opponentMove);
					if (gamegui != null) {
						gamegui.updateGameState(queenPosCurr, queenPosNext, arrowPos);
					}
					if (gameState.getCurrentPlayer() == myColor) {
						Move move = ai.selectMove();
						sendMove(move);
					}
				} else {
					System.err.println("Error: GAME_ACTION_MOVE message missing data.");
				}
				break;

			default:
				System.out.println("Message type: " + messageType);
				System.out.println("Message: " + msgDetails);
				break;
		}
		return true;
	}

	/**
	 * Sends an AI-selected move to the server using AmazonsGameMessage keys.
	 */
	private void sendMove(Move move) {
		if (move != null) {
			Map<String, Object> moveDetails = new HashMap<>();
			moveDetails.put(AmazonsGameMessage.QUEEN_POS_CURR, new ArrayList<>(List.of(move.queenCurr[0], move.queenCurr[1])));
			moveDetails.put(AmazonsGameMessage.QUEEN_POS_NEXT, new ArrayList<>(List.of(move.queenNext[0], move.queenNext[1])));
			moveDetails.put(AmazonsGameMessage.ARROW_POS, new ArrayList<>(List.of(move.arrow[0], move.arrow[1])));
			gameClient.sendMoveMessage(moveDetails);
			gameState.applyMove(myColor, move);
			if (gamegui != null) {
				gamegui.updateGameState(
						new ArrayList<>(List.of(move.queenCurr[0], move.queenCurr[1])),
						new ArrayList<>(List.of(move.queenNext[0], move.queenNext[1])),
						new ArrayList<>(List.of(move.arrow[0], move.arrow[1]))
				);
			}
		}
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