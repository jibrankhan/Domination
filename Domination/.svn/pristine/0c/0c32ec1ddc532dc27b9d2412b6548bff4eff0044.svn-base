package net.yura.domination.engine;

import java.awt.Frame;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;
import net.yura.domination.engine.translation.TranslationBundle;
import net.yura.domination.lobby.mini.MiniLobbyRisk;
import net.yura.domination.mapstore.MapChooser;
import net.yura.lobby.mini.MiniLobbyClient;
import net.yura.lobby.model.Game;
import net.yura.lobby.model.GameType;
import net.yura.mobile.gui.ActionListener;
import net.yura.me4se.ME4SEPanel;

/**
 * @author Yura Mamyrin
 */
public class SwingMEWrapper {

    public static String showMapChooser(Frame parent, List files) {

       // TMP TMP TMP
       try {
           // clean up crap left by old version
            File rms = new File(".rms");
            if (rms.exists()) {
                File[] files1 = rms.listFiles();
                for (int c=0;c<files1.length;c++) {
                    files1[c].delete();
                }
                rms.delete();
            }
        }
        catch (Throwable th) { }
        // TMP TMP TMP
        
        
        final ME4SEPanel wrapper = new ME4SEPanel(); // this sets the theme to NimbusLookAndFeel
        wrapper.getApplicationManager().applet = RiskUIUtil.applet;

        MapChooser.loadThemeExtension(); // loads extra things needed for map chooser
        
        final MapChooser chooser = new MapChooser(new ActionListener() {
            public void actionPerformed(String actionCommand) {
                SwingUtilities.getWindowAncestor(wrapper).setVisible(false);
            }
        },files,true);

        wrapper.add( chooser.getRoot() );
        
        wrapper.showDialog(parent, TranslationBundle.getBundle().getString("newgame.choosemap") );
        
        // WAIT WAIT WAIT WAIT WAIT WAIT WAIT WAIT WAIT WAIT WAIT WAIT
        // WAIT WAIT WAIT WAIT WAIT WAIT WAIT WAIT WAIT WAIT WAIT WAIT
        
        String result = chooser.getSelectedMap();
        
        chooser.destroy(); // shutdown abba repo, write index to rms

        return result;
    }

    public static MiniLobbyClient makeMiniLobbyClient(Risk risk,final Window window) {
        MapChooser.loadThemeExtension();
        return new MiniLobbyClient( new MiniLobbyRisk(risk) {
            private net.yura.domination.lobby.client.GameSetupPanel gsp;
            public void openGameSetup(GameType gameType) {
                if (gsp==null) {
                    gsp = new net.yura.domination.lobby.client.GameSetupPanel();
                }
                Game result = gsp.showDialog( window , gameType.getOptions(), lobby.whoAmI() );
                if (result!=null) {
                    lobby.createNewGame(result);
                }
            }
            public String getAppName() {
                return "SwingDomination";
            }
            public String getAppVersion() {
                return Risk.RISK_VERSION;
            }
        } );
    }

}
