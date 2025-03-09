
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

/**
 * An example illustrating how to implement a GamePlayer
 * @author Yong Gao (yong.gao@ubc.ca).
 * Jan 5, 2021
 *
 */
public class COSC322Test extends GamePlayer{

    private GameClient gameClient = null; 
    private BaseGameGUI gamegui = null;
	
    private String userName = null;
    private String passwd = null;
 
	
    /**
     * The main method
     * @param args for name and passwd (current, any string would work)
     */
    public static void main(String[] args) {				 
    	COSC322Test player = new COSC322Test("cosc322", "cosc322");
    	
    	if(player.getGameGUI() == null) {
    		player.Go();
    	}
    	else {
    		BaseGameGUI.sys_setup();
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                	player.Go();
                }
            });
    	}
    }
	
    /**
     * Any name and passwd 
     * @param userName
      * @param passwd
     */
    public COSC322Test(String userName, String passwd) {
    	this.userName = userName;
    	this.passwd = passwd;


    	//To make a GUI-based player, create an instance of BaseGameGUI
    	//and implement the method getGameGUI() accordingly
    	this.gamegui = new BaseGameGUI(this);
    }
 


    @Override
    public void onLogin() {

    	// System.out.println("Congratualations!!! "
    	// 		+ "I am called because the server indicated that the login is successfully");
    	// System.out.println("The next step is to find a room and join it: "
    	// 		+ "the gameClient instance created in my constructor knows how!");

		// List<Room> roomList = gameClient.getRoomList();

		// System.out.println("The available room/rooms is/are:");
		// for(Room roomName : roomList){
		// 	System.out.println(" - " + roomName.getName());
		// };

		// gameClient.joinRoom(roomList.get(0).getName());

		userName = gameClient.getUserName();

		if(gamegui != null) {
			gamegui.setRoomInformation(gameClient.getRoomList());
		}
    }

	@Override
	public boolean handleGameMessage(String messageType, Map<String, Object> msgDetails) {
		switch (messageType) {
			case GameMessage.GAME_STATE_BOARD:
				ArrayList<Integer> gameS = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.GAME_STATE);
				if (gameS != null && gamegui != null) {
					gamegui.setGameState(gameS);
				} else {
					System.err.println("Error: GAME_STATE_BOARD message missing required data or gamegui is null.");
				}
				break;

			case GameMessage.GAME_ACTION_MOVE:
				ArrayList<Integer> queenPosCurr = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.QUEEN_POS_CURR);
				ArrayList<Integer> queenPosNext = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.QUEEN_POS_NEXT);
				ArrayList<Integer> arrowPos = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.ARROW_POS);

				if (queenPosCurr != null && queenPosNext != null && arrowPos != null) {
					if (gamegui != null) {
						gamegui.updateGameState(queenPosCurr, queenPosNext, arrowPos);
					}
					// calculate move
				} else {
					System.err.println("Error: GAME_ACTION_MOVE message missing required data.");
				}
            	break;

			default:
				System.out.println("Message type: " + messageType);
				System.out.println("Message: " + msgDetails);
				break;
		}
				return true;
	}



	@Override
    public String userName() {
    	return userName;
    }

	@Override
	public GameClient getGameClient() {
		// TODO Auto-generated method stub
		return this.gameClient;
	}

	@Override
	public BaseGameGUI getGameGUI() {
		// TODO Auto-generated method stub
		return this.gamegui;
	}

	@Override
	public void connect() {
		// TODO Auto-generated method stub
    	gameClient = new GameClient(userName, passwd, this);			
	}

 
}//end of class
