package net.yura.lobby.mini;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;
import java.util.logging.Logger;
import net.yura.domination.engine.RiskUtil;
import net.yura.domination.mapstore.MapChooser;
import net.yura.lobby.client.Connection;
import net.yura.lobby.client.LobbyClient;
import net.yura.lobby.client.LobbyCom;
import net.yura.lobby.model.Game;
import net.yura.lobby.model.GameType;
import net.yura.lobby.model.Player;
import net.yura.mobile.gui.ActionListener;
import net.yura.mobile.gui.Midlet;
import net.yura.mobile.gui.components.Button;
import net.yura.mobile.gui.components.ComboBox;
import net.yura.mobile.gui.components.Frame;
import net.yura.mobile.gui.components.List;
import net.yura.mobile.gui.components.OptionPane;
import net.yura.mobile.gui.components.Panel;
import net.yura.mobile.gui.components.TextField;
import net.yura.mobile.gui.layout.XULLoader;
import net.yura.mobile.util.Option;
import net.yura.mobile.util.Properties;
import net.yura.mobile.util.Url;
import net.yura.swingme.core.ViewChooser;

public class MiniLobbyClient implements LobbyClient,ActionListener {

    private static final Logger logger = Logger.getLogger( MiniLobbyClient.class.getName() );

    private static final String LOBBY_SERVER = "lobby.yura.net";
    //private static final String LOBBY_SERVER = "192.168.0.2";
    
    XULLoader loader;
    List list;

    public Connection mycom;
    MiniLobbyGame game;

    String myusername;
    GameType theGameType;
    int openGameId = -1;
    
    private Properties resBundle;
    
    public MiniLobbyClient(MiniLobbyGame lobbyGame) {
        game = lobbyGame;
        game.addLobbyGameMoveListener(this);

        resBundle = game.getProperties();
        
        try {
            loader = XULLoader.load( Midlet.getResourceAsStream("/ms_lobby.xml") , this, resBundle);
        }
        catch(Exception ex) {
            throw new RuntimeException(ex);
        }
        
        list = (List)loader.find("ResultList");
        list.setCellRenderer( new GameRenderer(this) );
        list.setFixedCellHeight( MapChooser.adjustSizeToDensityFromMdpi(50) );
        list.setFixedCellWidth(10); // will streach
        
        ComboBox box = (ComboBox)loader.find("listView");
        ViewChooser viewChooser = new ViewChooser( (Option[])box.getItems().toArray(new Option[box.getItemCount()]) );
        loader.swapComponent("listView", viewChooser);
        viewChooser.addActionListener(this);
        viewChooser.setActionCommand("filter");
        viewChooser.setStretchCombo(true);
        viewChooser.setName(null);

        String uuid = getMyUUID();





        mycom = new LobbyCom(uuid,lobbyGame.getAppName(),lobbyGame.getAppVersion());
        mycom.addEventListener(this);
        mycom.connect(LOBBY_SERVER, 1964);
    }

    public void removeBackButton() {
        Button button = (Button)loader.find("BackButton");
        button.setMnemonic(0);
        button.setVisible(false);
    }
    
    ActionListener closeListener;
    public void addCloseListener(ActionListener al) {
        closeListener = al;
    }
    
    public void destroy() {
        mycom.disconnect();
    }
    
    public static String getMyUUID() {
        
        java.util.Properties prop = new java.util.Properties();
        
        File lobbySettingsFile = new File( System.getProperty("user.home"),".lobby" );
        
        try {
            prop.load( new FileInputStream(lobbySettingsFile) );
        }
        catch (Exception ex) { }
        
        String uuid = prop.getProperty("uuid");
        if (uuid!=null) {
            return uuid;
        }
        uuid = UUID.randomUUID().toString();
        prop.setProperty("uuid", uuid);
        
        try {
            prop.store(new FileOutputStream(lobbySettingsFile), "yura.net Lobby");
        }
        catch (Exception ex) { }
        
        return uuid;
    }
    
    public Panel getRoot() {
        return ((Panel)loader.getRoot());
    }
    public String getTitle() {
        return resBundle.getProperty("lobby.windowtitle");
    }

    public void actionPerformed(String actionCommand) {
        
        if ("listSelect".equals(actionCommand)) {
            final Game game = (Game)list.getSelectedValue();
            if (game!=null) {
                int state = game.getState( whoAmI() );
                switch (state) {
                    case Game.STATE_CAN_JOIN:
                        if (game.getMaxPlayers() == game.getNumOfPlayers()+1) {
                            OptionPane.showConfirmDialog(new ActionListener() {
                                public void actionPerformed(String actionCommand) {
                                    if ("ok".equals(actionCommand)) {
                                        mycom.joinGame(game.getId());
                                    }
                                }
                            }, "This game will start if you join!", "Are you sure?", OptionPane.OK_CANCEL_OPTION);
                        }
                        else {
                            mycom.joinGame(game.getId());
                        }
                        break;
                    case Game.STATE_CAN_LEAVE:
                        mycom.leaveGame( game.getId() );
                        break;
                    case Game.STATE_CAN_PLAY:
                    case Game.STATE_CAN_WATCH:
                        if (openGameId==game.getId()) return; // we have already tried to open this game, do nothing
                        if (openGameId!=-1) {
                            // TODO should we close the game in the UI?
                            closeGame();
                        }
                        openGameId = game.getId();
                        mycom.playGame(game);
                        break;
                }
            }
        }
        else if ("create".equals(actionCommand)) {
            if (theGameType!=null) {
        	game.openGameSetup(theGameType);
            }
            else {
        	logger.info("GameType is null, can not openGameSetup");
            }
        }
        else if ("setnick".equals(actionCommand)) {
            if (myusername!=null) {
                final TextField saveText = new TextField();
                saveText.setText( myusername );
                OptionPane.showOptionDialog(new ActionListener() {
                    public void actionPerformed(String actionCommand) {
                        if ("ok".equals(actionCommand)) {
                            mycom.setNick( saveText.getText() );
                        }
                    }
                }, saveText, resBundle.getProperty("lobby.set-nick") , OptionPane.OK_CANCEL_OPTION, OptionPane.QUESTION_MESSAGE, null, null, null);
            }
            else {
                logger.info("current username is null, can not set nick dialog");
            }
        }
        else if ("close".equals(actionCommand)) {
            destroy();
            if (closeListener!=null) {
                closeListener.actionPerformed( Frame.CMD_CLOSE );
            }
            else {
                getRoot().getWindow().setVisible(false);
            }
        }
        else if ("filter".equals(actionCommand)) {
            filter();
        }
        else if ("login".equals(actionCommand)) {
            // TODO
        }
        else if ("register".equals(actionCommand)) {
            // TODO
        }
        else {
            OptionPane.showMessageDialog(null,"unknown command: "+actionCommand, null, OptionPane.INFORMATION_MESSAGE);
        }
    }

    public void sendGameMessage(String messagefromgui) {
        mycom.sendGameMessage(openGameId,messagefromgui);
    }
    
    
    public void closeGame() {
        mycom.closeGame(openGameId);
        openGameId = -1;
    }
    
    public void createNewGame(Game game) {
        game.setType(theGameType); // we can only make a game of this type
        mycom.createNewGame(game);
    }
    
    // WMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMW
    // WMWMWMWMWMWMWMWMWMWMWMWMWMWM LobbyClient MWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMW
    // WMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMW
    

    public ClassLoader getClassLoader(GameType gameType) {
        return getClass().getClassLoader();
    }

    public void connected() {
	
	Midlet.openURL("nativeNoResult://net.yura.domination.android.GCMActivity");
	
        mycom.getGameTypes();
    }

    public void disconnected() {
        if (openGameId!=-1) {
            game.disconnected();
            openGameId = -1;
        }
    }

    public void connecting(String message) {
        logger.info(message);
    }

    public void error(String error) {
        logger.info(error);
        OptionPane.showMessageDialog(null, error, "Error", OptionPane.ERROR_MESSAGE);
    }

    
    
    

    public void setUsername(String name, boolean guest) {
        myusername = name;
        toast("You are logged in as: "+name);
    }
    public String whoAmI() {
            return myusername;
    }

    public void addGameType(java.util.List gametypes) {
        
        for (int c=0;c<gametypes.size();c++) {
            GameType gametype = (GameType)gametypes.get(c);
        
            if (game.isMyGameType(gametype) ) {
                theGameType = gametype;
                
                mycom.getGames( gametype );
            }
            else {
                logger.info("ignore GameType: "+gametype);
            }
        }
    }

    private java.util.List games = Collections.synchronizedList( new ArrayList() );
    public void addOrUpdateGame(Game game) {
        int index = games.indexOf(game);
        if (index>=0) {
            games.set(index,game);
        }
        else {
            games.add(game);
        }
        filter();
    }
    
    public boolean amAPlayer() {
        if (openGameId ==-1) {
            return false;
        }
        for (int c=0;c<games.size();c++) {
            Game g = (Game)games.get(c);
            if (g.getId() == openGameId) {
                return g.getState( whoAmI() ) == Game.STATE_CAN_PLAY;
            }
        }
        return false;
    }
    
    public void resign() {
        mycom.leaveGame( openGameId );
    }

    public void removeGame(int gameid) {
        Game found=null;
        for (int c=0;c<games.size();c++) {
            Game game = (Game)games.get(c);
            if ( gameid == game.getId() ) {
                games.remove(c);
                found = game;
                break;
            }
        }
        if (found!=null) {
            list.removeElement(found);
            getRoot().revalidate();
            getRoot().repaint();
        }
    }

    void filter() {
        ViewChooser box = (ViewChooser)loader.find("listView");
        list.setListData( RiskUtil.asVector( filter(games, ((Option)box.getSelectedItem()).getKey() ) ) );
        getRoot().revalidate();
        getRoot().repaint();
    }
    java.util.List filter(java.util.List list,String filter) {
        // all
        // my
        // open
        // running
        synchronized(list) {
            if ("all".equals(filter)) {
                return new java.util.Vector(list);
            }
            java.util.List result = new java.util.Vector();
            for (int c=0;c<list.size();c++) {
                Game game = (Game)list.get(c);
                if ("my".equals(filter)) {
                    if (game.hasPlayer(myusername)) {
                        result.add( game );
                    }
                }
                else if ("open".equals(filter)) {
                    if (game.getNumOfPlayers() < game.getMaxPlayers()) {
                        // STATE_CAN_LEAVE or STATE_CAN_JOIN
                        result.add( game );
                    }
                }
                else if ("running".equals(filter)) {
                    if (game.getNumOfPlayers() == game.getMaxPlayers()) {
                        // STATE_CAN_PLAY or STATE_CAN_WATCH
                        result.add( game );
                    }
                }
            }
            return result;
        }
    }


    public void messageForGame(int gameid, Object message) {
        if (gameid==openGameId) {
            if (message instanceof String) {
                String string = (String)message;
                if (string.equals("LOBBY_GAMEOVER")) {
                    // TODO
                    //paused=true;
                }
                else {
                    game.stringForGame(string);
                }
            }
            else if (message instanceof byte[]) {
                try {
                    ByteArrayInputStream in = new ByteArrayInputStream( (byte[])message );
                    ObjectInputStream oin = new ObjectInputStream(in);
                    Object object = oin.readObject();
                    game.objectForGame(object);
                }
                catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
            else {
                throw new RuntimeException("unknown object "+message);
            }
        }
        else {
            // this can happen if we close a game as we are getting a message for it
            logger.info("we got a message for game "+gameid+" but our game is "+openGameId);
        }
    }

    public void renamePlayer(String oldname, String newname,int newtype) {
        game.renamePlayer(oldname,newname);
    }



    // chat
    public void incomingChat(String fromwho,String message) {
        showMessage(fromwho, message);
    }
    public void incomingChat(int roomid, String fromwho, String message) {
        if (openGameId == roomid) {
            showMessage(fromwho, message);
        }
    }

    public void addPlayer(Player player) { }
    public void removePlayer(String player) { }
    public void addPlayer(int roomid, Player player) { }
    public void removePlayer(int roomid, String player) { }

    public void privateMessage(String fromwho, String message) { }
    public void setUserInfo(String user,java.util.List info) { }

    void showMessage(String fromwho,String message) {
        if (fromwho!=null) {
            toast(fromwho+": "+message);
        }
        else {
            toast(message);
        }
    }
    static void toast(String message) {
        Midlet.openURL("nativeNoResult://net.yura.android.ToastActivity?message="+Url.encode(message));
    }
    
}
