package net.yura.domination.mobile.flashgui;

import java.util.Arrays;
import java.util.List;
import net.yura.domination.engine.ColorUtil;
import net.yura.domination.engine.Risk;
import net.yura.domination.engine.RiskUtil;
import net.yura.domination.engine.core.Player;
import net.yura.domination.engine.core.RiskGame;
import net.yura.domination.mapstore.BadgeButton;
import net.yura.domination.mapstore.MapChooser;
import net.yura.domination.mapstore.MapUpdateService;
import net.yura.domination.mobile.MiniUtil;
import net.yura.mobile.gui.ActionListener;
import net.yura.mobile.gui.ButtonGroup;
import net.yura.mobile.gui.ChangeListener;
import net.yura.mobile.gui.DesktopPane;
import net.yura.mobile.gui.Midlet;
import net.yura.mobile.gui.components.Button;
import net.yura.mobile.gui.components.ComboBox;
import net.yura.mobile.gui.components.Component;
import net.yura.mobile.gui.components.FileChooser;
import net.yura.mobile.gui.components.Frame;
import net.yura.mobile.gui.components.Label;
import net.yura.mobile.gui.components.OptionPane;
import net.yura.mobile.gui.components.Panel;
import net.yura.mobile.gui.components.ScrollPane;
import net.yura.mobile.gui.components.Spinner;
import net.yura.mobile.gui.components.TextComponent;
import net.yura.mobile.gui.components.Window;
import net.yura.mobile.gui.layout.GridBagConstraints;
import net.yura.mobile.gui.layout.XULLoader;
import net.yura.mobile.util.Option;
import net.yura.mobile.util.Properties;

public class MiniFlashGUI extends Frame implements ChangeListener,ActionListener {

    private static final String[] compsNames = new String[]{"crapAI","easyAI","hardAI","human"};
    private static final int[] compTypes = new int[] {Player.PLAYER_AI_CRAP,Player.PLAYER_AI_EASY,Player.PLAYER_AI_HARD,Player.PLAYER_HUMAN};

    // shares res
    Properties resb = GameActivity.resb;
    public Risk myrisk;

    // main menu res
    FileChooser chooser;

    // new game res
    XULLoader newgame;
    Button autoplaceall;
    private boolean localgame;
    
    MiniFlashRiskAdapter controller;
    
    // online play
    private String[] allowedMaps;
    String lobbyMapName;
    // end online play
    
    public MiniFlashGUI(Risk risk,MiniFlashRiskAdapter controller) {
        myrisk = risk;
        this.controller = controller;
        setMaximum(true);
        
        setBorder(GameActivity.marble);
        setBackground( 0x00FFFFFF );
    }

    @Override
    public void actionPerformed(String actionCommand) {
            if ("new game".equals(actionCommand)) {
                myrisk.parser("newgame");
            }
            else if ("load game".equals(actionCommand)) {

                chooser = new FileChooser();
                chooser.setCurrentDirectory( MiniUtil.getSaveGameDirURL() );
                chooser.showDialog(this, "doLoad", resb.getProperty("mainmenu.loadgame.loadbutton") , resb.getProperty("mainmenu.loadgame.loadbutton") );

            }
            else if ("doLoad".equals(actionCommand)) {

                String file = chooser.getSelectedFile();
                chooser = null;
                
                if (file.endsWith( GameActivity.SAVE_EXTENSION )) {
                    myrisk.parser("loadgame " + file );
                }
                // else ignore file

            }
            else if ("manual".equals(actionCommand)) {

                //WebView webView = new WebView( AndroidMeActivity.DEFAULT_ACTIVITY );
                //webView.loadUrl("file:///android_asset/help/index.htm");
                //AndroidMeActivity.DEFAULT_ACTIVITY.setContentView(webView);

                MiniUtil.openHelp();
            }
            else if ("about".equals(actionCommand)) {
                MiniUtil.showAbout();
            }
            else if ("quit".equals(actionCommand)) {
                System.exit(0);
            }
            else if ("join game".equals(actionCommand)) {
                OptionPane.showMessageDialog(null,"not done yet","Error", OptionPane.ERROR_MESSAGE);
            }
            else if ("start server".equals(actionCommand)) {
                OptionPane.showMessageDialog(null,"not done yet","Error", OptionPane.ERROR_MESSAGE);
            }
            else if ("closegame".equals(actionCommand)) { // user clicks back in game setup screen
        	
        	if (localgame) {
        	    myrisk.parser("closegame");
        	}
        	else {
        	    openMainMenu();
        	}
            }
            else if ("startgame".equals(actionCommand)) {

                ButtonGroup GameType = (ButtonGroup)newgame.getGroups().get("GameType");
                ButtonGroup CardType = (ButtonGroup)newgame.getGroups().get("CardType");

                //Button autoplaceall = (Button)newgame.find("autoplaceall");
                Button recycle = (Button)newgame.find("recycle");

                int numOfPlayers = getNoOfPlayers();
                
                if (numOfPlayers >= 2 && numOfPlayers <= RiskGame.MAX_PLAYERS ) {
                
                    if (localgame) {
                        RiskUtil.savePlayers(myrisk, getClass());
                        
                        myrisk.parser("startgame "+
                                GameType.getSelection().getActionCommand()+" "+
                                CardType.getSelection().getActionCommand()+
                                ((autoplaceall!=null&&autoplaceall.isSelected()?" autoplaceall":""))+
                                ((recycle!=null&&recycle.isSelected()?" recycle":""))
                                );
                    }
                    else {
                        
                        controller.createLobbyGame(
                        	    ((TextComponent)newgame.find("GameName")).getText(),
                        	    RiskUtil.createGameString(
                                    	    getNoPlayers(Player.PLAYER_AI_CRAP),
                                    	    getNoPlayers(Player.PLAYER_AI_EASY),
                                    	    getNoPlayers(Player.PLAYER_AI_HARD),
                                    	    getStartGameOption( GameType.getSelection().getActionCommand() ),
                                    	    getStartGameOption( CardType.getSelection().getActionCommand() ),
                                    	    autoplaceall.isSelected(),
                                    	    recycle.isSelected(),
                                    	    lobbyMapName),
                        	    getNoPlayers(Player.PLAYER_HUMAN),
                                    Integer.parseInt( ((Option)((ComboBox)newgame.find("TimeoutValue")).getSelectedItem()).getKey() )
                            );
                        
                        openMainMenu(); // close the game setup screen
                    }   
                }
                else {
                        OptionPane.showMessageDialog(null, resb.getProperty("newgame.error.numberofplayers") , resb.getProperty("newgame.error.title"), OptionPane.ERROR_MESSAGE );
                }

            }
            else if ("choosemap".equals(actionCommand)) {

                MapListener al = new MapListener();

                MapChooser mapc = new MapChooser(al, allowedMaps==null?MiniUtil.getFileList("map"):Arrays.asList(allowedMaps), localgame );
                al.mapc = mapc;

                Frame mapFrame = new Frame( resb.getProperty("newgame.choosemap") );
                mapFrame.setContentPane( mapc.getRoot() );
                mapFrame.setMaximum(true);
                mapFrame.setVisible(true);

            }
            else if ("mission".equals(actionCommand)) {
                    autoplaceall.setFocusable(false);
            }
            else if ("domination".equals(actionCommand)) {
                    autoplaceall.setFocusable(true);
            }
            else if ("capital".equals(actionCommand)) {
                    autoplaceall.setFocusable(true);
            }
            else if ("customPlayers".equals(actionCommand)) {
                // TODO
            }
            else if ("donate".equals(actionCommand)) {
                try {
                    RiskUtil.donate();
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            else if ("online".equals(actionCommand)) {

        	controller.openLobby();
                
            }
            else {
                System.out.println("Unknown command: "+actionCommand);
            }
        }

    	int getNoOfPlayers() {
            if (localgame) {
                return myrisk.getGame().getPlayers().size();
            }
            else {
        	int count=0;
        	for (int c=0;c<compTypes.length;c++) {
        	    count = count + getNoPlayers(compTypes[c]);
        	}
        	return count;
            }
    	}
    	int getNoPlayers(int type) {
    	    for (int c=0;c<compTypes.length;c++) {
    		if (compTypes[c]==type) {
    		    Component comp = newgame.find(compsNames[c]);
    		    if (comp!=null) {
    			return ((Integer)((Spinner)comp).getValue()).intValue();
    		    }
    		    return 0;
    		}
    	    }
    	    throw new RuntimeException("invalid type "+type);
    	}
    	int getStartGameOption(String newOption) {
		if ( newOption.equals("domination") ) {
		    return RiskGame.MODE_DOMINATION;
		}
		if ( newOption.equals("capital") ) {
		    return RiskGame.MODE_CAPITAL;
		}
		if ( newOption.equals("mission") ) {
		    return RiskGame.MODE_SECRET_MISSION;
		}
		if ( newOption.equals("increasing") ) {
		    return RiskGame.CARD_INCREASING_SET;
		}
		if ( newOption.equals("fixed") ) {
		    return RiskGame.CARD_FIXED_SET;
		}
		if ( newOption.equals("italianlike") ) {
		    return RiskGame.CARD_ITALIANLIKE_SET;
		}
		throw new RuntimeException("unknown option "+newOption);
    	}
    
    class MapListener implements ActionListener {
        MapChooser mapc;
        public void actionPerformed(String arg0) {
            
            String name = mapc.getSelectedMap();
            if (name != null) {
        	
        	if (localgame) {
                    myrisk.parser("choosemap " + name );
        	}
        	else {
        	    setLobbyMap(name);
        	}
            }
            
            mapc.getRoot().getWindow().setVisible(false);
            
            mapc.destroy();
        }
    };

    void setLobbyMap(String name) {
	lobbyMapName = name;
	showMapPic(name);
        java.util.Map mapinfo = RiskUtil.loadInfo(name,false);
        String cardsFile = (String)mapinfo.get("crd");
        java.util.Map cardsinfo = RiskUtil.loadInfo(cardsFile,true);
        String[] missions = (String[])cardsinfo.get("missions");
        showCardsFile(cardsFile, missions.length > 0);
    }
    
    private XULLoader getPanel(String xmlfile) {

        XULLoader loader;
        try {
            loader = XULLoader.load( Midlet.getResourceAsStream(xmlfile) , this, resb);
        }
        catch(Exception ex) {
            throw new RuntimeException(ex);
        }
        return loader;

    }

    public void openMainMenu() {

        if (newgame!=null) {
            // clean up
            MapUpdateService.getInstance().deleteObserver( (BadgeButton)newgame.find("MapImg") );
            newgame = null;
            autoplaceall=null;
        }
        
        setTitle( resb.getProperty("mainmenu.title") );

        XULLoader loader = getPanel("/mainmenu.xml");

        //Component onlineButton = loader.find("OnlineButton");
        //if (onlineButton!=null) {
        //    onlineButton.setVisible( Locale.getDefault().equals(new Locale("en","GB")) );
        //}
        
        final Panel newContentPane = new ScrollPane( loader.getRoot() );
        
        


        
        
        if (isVisible()) {
            DesktopPane.invokeLater(new Runnable() {
                public void run() {
                    setContentPane( newContentPane );
                    revalidate();
                    repaint();
                    moveToBack();
                }
            });
            
        }
        else {
            setContentPane( newContentPane );
            revalidate();
            setVisible(true);
            moveToBack();
        }
        
    }
    
    private void moveToBack() {
        // we want to always be at the bottom of the stack
        // so move anything bellow us to be above us
        List windows = getDesktopPane().getAllFrames();
        for (int c=1;c<windows.size();c++) {
            getDesktopPane().setSelectedFrame((Window)windows.get(0));
        }
    }

    // ================================================ GAME SETUP
    public void openNewGame(boolean islocalgame,String[] allowedMaps,String gameName) {

        // clean up
        chooser = null;
        
        localgame = islocalgame;
        this.allowedMaps = allowedMaps;

        newgame = getPanel("/newgame.xml");

        if (gameName!=null) {
            TextComponent tc = (TextComponent)newgame.find("GameName");
            tc.setText( gameName );
            tc.setVisible(true);
            newgame.find("Timeout").setVisible(true);
        }
        
        MapUpdateService.getInstance().addObserver( (BadgeButton)newgame.find("MapImg") );
        
        autoplaceall = (Button)newgame.find("autoplaceall");
        
        // kind of a hack
        Component startButton = newgame.find("startButton");
        Component cancelButton = newgame.find("cancelButton");
        if (startButton!=null && cancelButton!=null && !cancelButton.isVisible()) {
            ((GridBagConstraints)((Panel)startButton.getParent()).getConstraints().get(startButton)).colSpan = 2;
        }

        for (int c=0;c<compsNames.length;c++) {
            addChangeListener(compsNames[c]);
        }

        if (localgame) {
            RiskUtil.loadPlayers( myrisk ,getClass());
        }
        else {
            setLobbyMap( allowedMaps[0] );
        }
        
        ((Spinner)newgame.find("human")).setMinimum( localgame?1:2 );

        // we need to go onto the UI thread to change the UI as it may be in the middle of a repaint
        DesktopPane.invokeLater( new Runnable() {
            public void run() {

                setTitle(resb.getProperty(localgame?"newgame.title.local":"newgame.title.network"));
                //resetplayers.setVisible(localgame?true:false);

                setContentPane( new ScrollPane( newgame.getRoot() ) );

                // bring this window to front, in case we have a mini lobby window open too
                getDesktopPane().setSelectedFrame(MiniFlashGUI.this);
                
                revalidate();
                repaint();
            }
        } );

    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        
        if (!b && newgame!=null) {
            MapUpdateService.getInstance().deleteObserver( (BadgeButton)newgame.find("MapImg") );
        }
    }
    
    private void addChangeListener(String name) {
        Component comp = newgame.find(name);
        if (comp!=null && comp instanceof Spinner) {
            ((Spinner)comp).addChangeListener(this);
        }
    }
    private void removeChangeListener(String name) {
        Component comp = newgame.find(name);
        if (comp!=null && comp instanceof Spinner) {
            ((Spinner)comp).removeChangeListener(this);
        }
    }


    public void updatePlayers() {

        java.util.List players = myrisk.getGame().getPlayers();

        int[] count = new int[compTypes.length];

        for (int c=0;c<players.size();c++) {
            int type = ((Player)players.get(c)).getType();
            for (int a=0;a<compTypes.length;a++) {
                if (type == compTypes[a]) {
                    count[a]++;
                    break;
                }
            }
        }

        for (int c=0;c<compsNames.length;c++) {
            Component comp = newgame.find(compsNames[c]);
            if (comp!=null) {
                // we want to remove the listener first as this update is not user generated
                removeChangeListener(compsNames[c]);
                comp.setValue( new Integer(count[c]) );
                addChangeListener(compsNames[c]);
            }
        }

    }

    public void changeEvent(Component source, int num) {
        System.out.println("changeEvent changeEvent changeEvent changeEvent changeEvent changeEvent changeEvent changeEvent");

        int type = -1;
        for (int c=0;c<compsNames.length;c++) {
            Component comp = newgame.find(compsNames[c]);
            if (source == comp) {
                type = compTypes[c];
                break;
            }
        }

        if (type==-1) {
            throw new RuntimeException("type for this Component can not be found "+source); // should also never happen
        }
        
        if (localgame) {
            List players = myrisk.getGame().getPlayers();
            int count=0;
            for (int c=0;c<players.size();c++) {
                int ptype = ((Player)players.get(c)).getType();
                if (ptype == type) {
                    count++;
                }
            }
            int newval = ((Integer)((Spinner)source).getValue()).intValue();

            if (newval<count) {
                for (int c=players.size()-1;c>=0;c--) {
                    Player p = (Player)players.get(c);
                    if (p.getType() == type) {
                        myrisk.parser("delplayer " + p.getName());
                        break;
                    }
                }
            }
            else if (newval>count) {
                if (players.size() == RiskGame.MAX_PLAYERS) {
                    for (int c=players.size()-1;c>=0;c--) {
                        Player p = (Player)players.get(c);
                        if (p.getType()!=type) {
                            p.setType( type );
                            updatePlayers();
                            return;
                        }
                    }
                }
                else {
                    String newname=null;
                    String newcolor=null;
                    for (int c=0;c<Risk.names.length;c++) {
                        boolean badname=false;
                        boolean badcolor=false;
                        for (int a=0;a<players.size();a++) {
                            if (Risk.names[c].equals(((Player)players.get(a)).getName())) {
                                badname = true;
                            }
                            if (ColorUtil.getColor(Risk.colors[c])==((Player)players.get(a)).getColor()) {
                                badcolor = true;
                            }
                            if (badname&&badcolor) {
                                break;
                            }
                        }
                        if (newname==null && !badname) {
                            newname = Risk.names[c];
                        }
                        if (newcolor==null && !badcolor) {
                            newcolor = Risk.colors[c];
                        }
                        if (newname!=null && newcolor!=null) {
                            break;
                        }
                    }

                    if (newname!=null&&newcolor!=null) {
                        myrisk.parser("newplayer " + Risk.getType(type)+" "+ newcolor+" "+ newname );
                    }
                    else {
                        throw new RuntimeException("new name and color can not be found"); // this should never happen
                    }
                }
            }
        }
        // else network game not done yet

    }

    public void showMapPic(String mapFile) {
/*
        InputStream in=null;
        
        String prv = p.getPreviewPic();
        if (prv!=null) {
            in = MapChooser.getLocalePreviewImg("preview/"+prv);
        }
        if (in==null) {
            in = MapChooser.getLocalePreviewImg( p.getImagePic() );
        }

        Image img=null;
        if (in!=null) {
            try {
                img = MapChooser.createImage(in);
            }
            catch (Exception ex) {
                Logger.warn(ex);
            }
        }
        
        Label label = (Label)newgame.find("MapImg");
        
        if (img!=null) {
            LazyIcon icon = new LazyIcon( adjustSizeToDensityFromMdpi(150) , adjustSizeToDensityFromMdpi(94) ); // 150x94
            icon.setImage( img );
            label.setIcon( icon );
        }
        else {
            label.setIcon( null );
        }
*/
        // crazy 1 liner
        ((Label)newgame.find("MapImg")).setIcon( MapChooser.getLocalIconForMap( MapChooser.createMap( mapFile ) ) );
        
        revalidate();
        repaint();
    }

    public void showCardsFile(String c, boolean hasMission) {
        //cardsFile.setText(c);
        
        Button mission = (Button)newgame.find("mission");
        Button domination = (Button)newgame.find("domination");
        
        if ( !hasMission && mission.isSelected() ) {
            domination.setSelected(true);
            autoplaceall.setFocusable(true);
        }
        mission.setFocusable(hasMission);
    }
}
