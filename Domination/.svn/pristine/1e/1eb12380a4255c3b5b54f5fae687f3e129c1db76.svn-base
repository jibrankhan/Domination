package net.yura.domination.ui.swinggui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import net.yura.domination.engine.Risk;
import net.yura.domination.engine.RiskUIUtil;
import net.yura.domination.engine.SwingMEWrapper;
import net.yura.domination.engine.translation.TranslationBundle;
import net.yura.lobby.mini.MiniLobbyClient;
import net.yura.me4se.ME4SEPanel;
import net.yura.mobile.gui.components.Label;

/**
 * @author Yura Mamyrin
 */
public class LobbyTab extends ME4SEPanel implements SwingGUITab,ActionListener {

    ResourceBundle resb = TranslationBundle.getBundle();
    
    MiniLobbyClient mlc;
    Risk risk;

    JToolBar toolbar;
    JButton start,stop;
    
    public LobbyTab(Risk myrisk) {
        getApplicationManager().applet = RiskUIUtil.applet;
        risk = myrisk;

        toolbar = new JToolBar();
        toolbar.setRollover(true);
	toolbar.setFloatable(false);

        start = new JButton("Start Lobby");
	start.setActionCommand("start");
	start.addActionListener(this);
	toolbar.add(start);

        stop = new JButton("Stop Lobby");
	stop.setActionCommand("stop");
	stop.addActionListener(this);
	toolbar.add(stop);
        
        updateButton();
        
        add( new Label("click start in the toolbar") );
    }

    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        if ("start".equals(action)) {
            createLobby();
            updateButton();
        }
        else {
            closeLobby();
            updateButton();
        }
    }
    
    private void updateButton() {
        start.setEnabled( mlc==null );
        stop.setEnabled( mlc!=null );
    }

    public JToolBar getToolBar() {
        return toolbar;
    }
    public JMenu getMenu() {
        return null;
    }
    public String getName() {
        return resb.getString("mainmenu.online");
    }



    void createLobby() {
        mlc = SwingMEWrapper.makeMiniLobbyClient(risk, SwingUtilities.getWindowAncestor(this) );
        mlc.removeBackButton();
        add( mlc.getRoot() );
    }
    
    void closeLobby() {
        mlc.destroy();
        mlc = null;
        add( new Label("no lobby") ); // can throw when DesktopPane is null (open and close MapStore)
    }

}
