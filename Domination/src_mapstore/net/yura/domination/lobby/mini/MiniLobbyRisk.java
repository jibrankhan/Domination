package net.yura.domination.lobby.mini;

import java.util.WeakHashMap;
import java.util.logging.Logger;
import net.yura.domination.engine.OnlineRisk;
import net.yura.domination.engine.Risk;
import net.yura.domination.engine.RiskUtil;
import net.yura.domination.engine.translation.TranslationBundle;
import net.yura.domination.mapstore.Map;
import net.yura.domination.mapstore.MapChooser;
import net.yura.lobby.mini.MiniLobbyClient;
import net.yura.lobby.mini.MiniLobbyGame;
import net.yura.lobby.model.Game;
import net.yura.lobby.model.GameType;
import net.yura.mobile.gui.Icon;
import net.yura.mobile.util.Properties;
import net.yura.swingme.core.CoreUtil;

/**
 * @author Yura Mamyrin
 */
public abstract class MiniLobbyRisk implements MiniLobbyGame,OnlineRisk {

    private static final Logger logger = Logger.getLogger( MiniLobbyRisk.class.getName() );
    
    private Risk myrisk;
    protected MiniLobbyClient lobby;

    public MiniLobbyRisk(Risk risk) {
        myrisk = risk;
    }

    public void addLobbyGameMoveListener(MiniLobbyClient lgl) {
        lobby = lgl;
    }

    public Properties getProperties() {
        return CoreUtil.wrap( TranslationBundle.getBundle() );
    }

    public boolean isMyGameType(GameType gametype) {
        return RiskUtil.GAME_NAME.equals( gametype.getName() );
    }


    public void objectForGame(Object object) {
        java.util.Map map = (java.util.Map)object;
        myrisk.lobbyMessage(map, lobby.whoAmI(), this);
    }

    public void stringForGame(String message) {
        myrisk.parserFromNetwork(message);
    }

    public void renamePlayer(String oldname, String newname) {
        myrisk.renamePlayer(oldname, newname);
    }

    public void disconnected() {
        myrisk.disconnected();
    }

    
    
    
    WeakHashMap mapping = new WeakHashMap();
                    
    public Icon getIconForGame(Game game) {

        // if local map
        Map map = MapChooser.createMap( RiskUtil.getMapNameFromLobbyStartGameOption(game.getOptions()) );
        // TODO what if this is not a local map??

        mapping.put(game, map); // keep a strong ref to the map as long as we have a strong ref to the game

        return MapChooser.getLocalIconForMap(map);
    }

    public String getGameDescription(Game game) {
        return RiskUtil.getGameDescriptionFromLobbyStartGameOption( game.getOptions() );
    }


    
    // WMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMW
    // WMWMWMWMWMWMWMWMWMWMWMWMWMW OnlineRisk MWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMW
    // WMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMW

    public void sendUserCommand(final String messagefromgui) {
        lobby.sendGameMessage(messagefromgui);
    }
    public void sendGameCommand(String mtemp) {
	// this happens for game commands on my go
        logger.info("ignore GameCommand "+mtemp );
    }
    public void closeGame() {
        lobby.closeGame();
    }
    
}
