package net.yura.lobby.mini;

import net.yura.lobby.model.Game;
import net.yura.lobby.model.GameType;
import net.yura.mobile.gui.Icon;
import net.yura.mobile.util.Properties;

/**
 * @author Yura Mamyrin
 */
public interface MiniLobbyGame {

    void addLobbyGameMoveListener(MiniLobbyClient lgl);
    
    public Properties getProperties();

    public boolean isMyGameType(GameType gametype);
    public Icon getIconForGame(Game game);
    public String getGameDescription(Game game);
    
    /**
     * callback mlc.createNewGame(Game)
     */
    public void openGameSetup(GameType gameType);
    
    public void objectForGame(Object object);
    public void stringForGame(String message);

    public void renamePlayer(String oldname, String newname);

    void disconnected();
    
    public String getAppName();
    public String getAppVersion();

}
