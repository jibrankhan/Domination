package net.yura.domination.lobby.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import net.yura.domination.engine.Risk;
import net.yura.domination.engine.RiskIO;
import net.yura.domination.engine.RiskUtil;
import net.yura.domination.engine.ai.AIPlayer;
import net.yura.domination.engine.core.Player;
import net.yura.domination.engine.core.RiskGame;
import net.yura.lobby.server.LobbySession;
import net.yura.lobby.server.TurnBasedGame;

public class ServerGameRisk extends TurnBasedGame {

	private ServerRisk myrisk;

        static {
            final URL mapsdir;
            try {
                mapsdir = new java.io.File( RiskUtil.GAME_NAME + "/maps").toURI().toURL();
            }
            catch(Exception ex) {
                throw new RuntimeException(ex);
            }

            RiskUtil.streamOpener = new RiskIO() {
                public InputStream openStream(String name) throws IOException {
                    return new File(name).toURI().toURL().openStream();
                }
                public InputStream openMapStream(String name) throws IOException {
                    return new URL(mapsdir,name).openStream();
                }
                public ResourceBundle getResourceBundle(Class c, String n, Locale l) {
                    // TODO this should be different for different clients connected to this server
                    return ResourceBundle.getBundle(c.getPackage().getName()+"."+n, l );
                }
                public void openURL(URL url) throws Exception {

                }
                public void openDocs(String doc) throws Exception {

                }
                public void saveGameFile(String name,Object obj) throws Exception {

                }
                public InputStream loadGameFile(String file) throws Exception {
                    return null;
                }
                public void getMap(String filename, Risk risk,Exception ex) {
                    RiskUtil.printStackTrace(ex);
                    risk.getMapError(ex.toString());
                }
                public java.io.OutputStream saveMapFile(String fileName) throws Exception {
                    throw new UnsupportedOperationException("can not add maps to server");
                }
                public void renameMapFile(String oldName, String newName) {
                    throw new UnsupportedOperationException("can not add maps to server");
                }
            };
            
            try {
                GameSettings settings = new GameSettings();
                MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
                mbs.registerMBean(settings, new ObjectName("net.yura.domination:type=GameSettings") );
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }

	public ServerGameRisk() {
		myrisk = new ServerRisk(this);
		// POP UP DEBUG WINDOW
		//Increment1Frame gui = new Increment1Frame( myrisk );
		//RiskGUI gui = new RiskGUI( myrisk );
        	//gui.setVisible(true);
	}

	public void startGame(String startGameOptions, String[] players) {

		// sort them so if player bob was green last time, they r again
		Arrays.sort(players);

		//System.out.println("\tNEW GAME STARTING FOR RISK: "+gameid);

		//myguid = gameid;

		myrisk.makeNewGame();


		String[] options = startGameOptions.split("\\n");

		int aicrap = Integer.parseInt(options[0]);
		int aieasy = Integer.parseInt(options[1]);
		int aihard = Integer.parseInt(options[2]);

		if ((players.length+aicrap+aieasy+aihard)>RiskGame.MAX_PLAYERS ) { throw new RuntimeException("player number missmatch for startgame"); }

		myrisk.addSetupCommandToInbox(options[3]); // set the map file to use

		List<String> colorString = new ArrayList<String>();
		colorString.add( myrisk.getRiskconfig("default.player1.color") );
		colorString.add( myrisk.getRiskconfig("default.player2.color") );
		colorString.add( myrisk.getRiskconfig("default.player3.color") );
		colorString.add( myrisk.getRiskconfig("default.player4.color") );
		colorString.add( myrisk.getRiskconfig("default.player5.color") );
		colorString.add( myrisk.getRiskconfig("default.player6.color") );
		Iterator<String> it = colorString.iterator();

		for (int c=0;c<players.length;c++) {
			it.hasNext();
			String color = it.next();
			String playerid = "player"+(c+1);
			myrisk.addSetupCommandToInbox(playerid,"newplayer human "+color + " " + players[c]);
		}

		for (int c=0;c<aicrap;c++) {
			it.hasNext();
			String color = it.next();
			myrisk.addSetupCommandToInbox("newplayer ai crap "+color + " CrapBot" + (c+1));
		}

		for (int c=0;c<aieasy;c++) {
			it.hasNext();
			String color = it.next();
			myrisk.addSetupCommandToInbox("newplayer ai easy "+color + " EasyBot" + (c+1));
		}

		for (int c=0;c<aihard;c++) {
			it.hasNext();
			String color = it.next();
			myrisk.addSetupCommandToInbox("newplayer ai hard "+color + " HardBot" + (c+1));
		}

		myrisk.addSetupCommandToInbox(options[4]); // start the game

		// HACK: only return when the game is setup
		while(!myrisk.getWaiting()) {
			try { Thread.sleep(100); }
			catch(InterruptedException e){}
		}

                myrisk.setPaued(false);
	}

        public byte[] saveGameState() {
            try {
                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(bout);
                out.writeObject(myrisk.getGame()); // TODO this is prob not very thread safe!!
                out.flush();
                out.close();
                return bout.toByteArray();
            }
            catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

	public void loadGame(byte[] gameData) {
            try {
                ByteArrayInputStream in = new ByteArrayInputStream(gameData);
                ObjectInputStream oin = new ObjectInputStream(in);
                RiskGame riskGame = (RiskGame)oin.readObject();
                
                String address=myrisk.getAddress();
                List<Player> players = riskGame.getPlayers();
                for (Player player:players) {
                    if (player.getType()!=Player.PLAYER_HUMAN) {
                        player.setAddress(address);
                    }
                }
                myrisk.setGame(riskGame);
                myrisk.setPaued(false);
            }
            catch (Exception ex) {
                throw new RuntimeException(ex);
            }
	}

	public void destroyGame() {
		myrisk.setKillFlag();
	}

	public void clientHasJoined(String username) {

		String playerid = getPlayerId(username);

                // this person is NOT a player in the game, they must just be watching
                if (playerid==null) {
                    playerid="_watch_";
                }
                
		System.out.println(username+" -> "+playerid);

                HashMap map = new HashMap();
                map.put("command", "game");
                map.put("playerId", playerid );
                map.put("game", myrisk.getGame() );
                
		sendObjectToClient(map, username );

	}
        
        private String getPlayerId(String username) {
            RiskGame game = myrisk.getGame();
            List<Player> players = game.getPlayers();
            for (Player player:players) {
                if (player.getType()==Player.PLAYER_HUMAN && username.equals(player.getName())) {
                    return player.getAddress();
                }
            }
            return null;
        }

	// get message from the user
	public void stringFromPlayer(String username, String message) {
		//System.out.print("\tGOTFROMCLIENT "+username+":"+message+"\n");
		String address = getPlayerId(username);
                Player player = myrisk.getGame().getCurrentPlayer();
		//if (game.getCurrentPlayer()!=null) { System.out.print( "\t"+game.getCurrentPlayer().getAddress()+" "+address ); }
		// game not started OR game IS started and it is there go
		if (message.trim().equals("closegame")) {
			throw new RuntimeException("closegame not allowed to be sent to core: "+username);
		}
                if (message.trim().equals("undo")) {
			throw new RuntimeException("undo not allowed to be sent to core: "+username);
		}
		if (player==null) {
                        throw new RuntimeException("currentPlayer is null");
		}
                if (!player.getAddress().equals( address )) {
                        throw new RuntimeException("got command but it is not our go: "+username+" "+address+" "+message+" current player: "+player.getName() );
                }

		myrisk.addPlayerCommandToInbox(address , message);
	}


	public void doBasicGo(String username) {
		String playerid = getPlayerId(username);
		// this check is already done
		//if (myrisk.getGame().getCurrentPlayer().getAddress().equals(playerid)) {
			myrisk.addPlayerCommandToInbox(playerid+"-doBasicGo", AIPlayer.getOutput(myrisk.getGame(),AIPlayer.aicrap) );
		//}
		// else something is going very wrong!!!!
		// such as cheating
	}

        private List<String> oldIds = new java.util.Vector();
        @Override
	public void playerResigns(String username) {

		String playerid = getPlayerId(username);

		//String currentAddress = myrisk.getGame().getCurrentPlayer().getAddress();

		if (playerid != null) {
                        String newName = username+"-Resigned";
			myrisk.renamePlayer(username,newName,myrisk.getAddress(),Player.PLAYER_AI_CRAP);

                        sendRename(username,newName,myrisk.getAddress(),Player.PLAYER_AI_CRAP);

                        oldIds.add(playerid);

                        int aliveHumans=0;
                        for (Player player:(List<Player>)myrisk.getGame().getPlayers()) {
                            if (player.getType()==Player.PLAYER_HUMAN && (player.getExtraArmies()> 0 || player.getNoTerritoriesOwned() > 0) ) {
                                aliveHumans++;
                            }
                        }
                        if (aliveHumans==0) {
                            gameFinished( whoHasMostPoints() );
                        }
		}
                else {
                    throw new RuntimeException("can not resign player "+username+" they are not in this game");
                }

		// already handled by the turn based game
		// ----
		// if they have resigned on there own go then do a go for them
		//if (currentAddress.equals(playerid)) {
		//	myrisk.parser( AIPlayer.getOutput(myrisk.getGame(),AIPlayer.aicrap) );
		//}

	}

        private void sendRename(String oldName,String newName,String newAddress,int newType) {
            HashMap map = new HashMap();
            map.put("command", "rename");
            map.put("oldName", oldName);
            map.put("newName", newName);
            map.put("newAddress", newAddress);
            map.put("newType", newType);
            for (LobbySession session: getAllClients()) {
                String username = session.getUsername();
                String playerid = getPlayerId(username);
                if (playerid==null) {
                    playerid="_watch_";
                }
                map.put("playerId", playerid);
                sendObjectToClient(map,username);
            }
        }
        
        @Override
        public void playerJoins(String newuser) {
            if (oldIds.isEmpty()) {
                throw new RuntimeException("no slots left");
            }
            else {
                String playerid = oldIds.remove(0);
                Player player = myrisk.findEmptySpot();
                if (player==null) {
                    throw new RuntimeException("no AI CRAP found in game");
                }
                String oldName = player.getName();
                myrisk.renamePlayer(oldName, newuser, playerid, Player.PLAYER_HUMAN);
                sendRename(oldName,newuser,playerid,Player.PLAYER_HUMAN);
            }
            // TODO
            //if paused and oldIds.isEmpty() now, we can kick off another game
        }

        @Override
	public void renamePlayer(String oldser,String newuser) {
	    myrisk.renamePlayer(oldser,newuser);
	}


	public void getInputFromSomeone() {
            Player player = myrisk.getGame().getCurrentPlayer();
            String username = player.getType()==Player.PLAYER_HUMAN?player.getName():null;
            getInputFromClient(username);
	}
        
        private String whoHasMostPoints() {
		RiskGame game = myrisk.getGame();
		if ( game.checkPlayerWon() ) {
			return game.getCurrentPlayer().getName();
		}
                String name="???";
                int best=-1;
                List<Player> players = game.getPlayers();
                for (int c=0;c<players.size();c++) {
                        Player player = players.get(c);
                        // player.getType() == Player.PLAYER_HUMAN &&
                        // if all resign then no humans left
                        if ( player.getNoTerritoriesOwned()>best) {
                                name = player.getName();
                                best = player.getNoTerritoriesOwned();
                        }
                }
                return name;
	}

}
