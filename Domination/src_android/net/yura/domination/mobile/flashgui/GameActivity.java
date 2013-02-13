package net.yura.domination.mobile.flashgui;

import java.io.File;
import java.io.StringWriter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import net.yura.domination.engine.Risk;
import net.yura.domination.engine.RiskUtil;
import net.yura.domination.engine.core.RiskGame;
import net.yura.domination.engine.guishared.MapMouseListener;
import net.yura.domination.engine.translation.TranslationBundle;
import net.yura.domination.mobile.MiniUtil;
import net.yura.domination.mobile.MouseListener;
import net.yura.domination.mobile.PicturePanel;
import net.yura.mobile.gui.ActionListener;
import net.yura.mobile.gui.Graphics2D;
import net.yura.mobile.gui.Icon;
import net.yura.mobile.gui.KeyEvent;
import net.yura.mobile.gui.Midlet;
import net.yura.mobile.gui.border.Border;
import net.yura.mobile.gui.components.Button;
import net.yura.mobile.gui.components.CheckBox;
import net.yura.mobile.gui.components.Component;
import net.yura.mobile.gui.components.Frame;
import net.yura.mobile.gui.components.Menu;
import net.yura.mobile.gui.components.OptionPane;
import net.yura.mobile.gui.components.Panel;
import net.yura.mobile.gui.components.ScrollPane;
import net.yura.mobile.gui.components.TextArea;
import net.yura.mobile.gui.components.TextField;
import net.yura.mobile.gui.components.Window;
import net.yura.mobile.gui.layout.BorderLayout;
import net.yura.mobile.gui.layout.GridBagConstraints;
import net.yura.mobile.gui.layout.GridBagLayout;
import net.yura.mobile.gui.layout.XULLoader;
import net.yura.mobile.io.kdom.Document;
import net.yura.mobile.io.kdom.Element;
import net.yura.mobile.io.kxml2.KXmlSerializer;
import net.yura.mobile.util.Option;
import net.yura.mobile.util.Properties;
import net.yura.mobile.util.Url;
import net.yura.swingme.core.CoreUtil;
import net.yura.swingme.core.ViewChooser;

/**
 * @author Yura
 */
public class GameActivity extends Frame implements ActionListener {
 
    public static final Logger logger = Logger.getLogger( GameActivity.class.getName() );
    
    public static final Properties resb = CoreUtil.wrap(TranslationBundle.getBundle());
    public static final Border marble;
    static {
        marble = new BackgroundBorder( Midlet.createImage("/marble.jpg") );
    }
    
    public static final String SAVE_EXTENSION = ".save";
    
    Risk myrisk;
    PicturePanel pp;
    ViewChooser mapViewControl;
    Button note,gobutton,closebutton,savebutton,undobutton;
    
    String status;
    int gameState;

    private CheckBox AutoEndGo,AutoDefend;
    private Button cardsbutton,missionbutton;

    Menu menu;
    private int gameOptionSize;
    MiniFlashRiskAdapter controller;

    public GameActivity(Risk risk,MiniFlashRiskAdapter controller) {
        myrisk = risk;
        this.controller = controller;
        setMaximum(true);

        setUndecorated(true);
        
        setBorder(marble);
        setBackground( 0x00FFFFFF );
        
        
        
        pp = new PicturePanel(myrisk);

        final MapMouseListener mml = new MapMouseListener(myrisk, pp);
        pp.addMouseListener(
            new MouseListener() {
                public void click(int x,int y) {
                    int[] countries = mml.mouseReleased(x, y, gameState);
                    if (countries!=null) {
                        mapClick(countries);
                    }
                }
            }
        );
        
        // MWMWMWMWMWMWM MENU MWMWMWMWMWMWMW
        
        savebutton = new Button( resb.getProperty("game.menu.save") );
        savebutton.setIcon( new Icon("/save.png") );
        savebutton.addActionListener(this);
        savebutton.setActionCommand("save");
        
        Button graphbutton = new Button( resb.getProperty("game.button.statistics") );
        graphbutton.setIcon( new Icon("/ic_menu_chartsettings.png") );
        graphbutton.addActionListener(this);
        graphbutton.setActionCommand("graph");
        
        undobutton = new Button( resb.getProperty("game.button.undo") );
        undobutton.setIcon( new Icon("/undo.png") );
        undobutton.addActionListener(this);
        undobutton.setActionCommand("undo");

        AutoEndGo = new CheckBox( resb.getProperty("game.menu.autoendgo") );
        AutoEndGo.setActionCommand("autoendgo");
        AutoEndGo.addActionListener(this);

        AutoDefend = new CheckBox( resb.getProperty("game.menu.autodefend") );
        AutoDefend.setActionCommand("autodefend");
        AutoDefend.addActionListener(this);

        Button helpbutton = new Button( resb.getProperty("game.menu.manual") );
        helpbutton.addActionListener(this);
        helpbutton.setActionCommand("help");
        
        
        menu = new Menu();
        menu.setMnemonic(KeyEvent.KEY_SOFTKEY1);
        menu.setActionCommand("menu");
        menu.addActionListener(this);
        menu.setName("ActionbarMenuButton");
        menu.setIcon( new Icon("/menu.png") );
        menu.add( savebutton );
        menu.add( graphbutton );
        menu.add( undobutton );
        menu.add( AutoEndGo );
        menu.add( AutoDefend );
        //menu.add( helpbutton );
        
        gameOptionSize = menu.getItemCount();
        
        // MWMWMWMWMWMWM END MENU MWMWMWMWMWMWMW

        gobutton = new Button(" ");
        gobutton.setName("GoButton");
        gobutton.setPreferredSize(gobutton.getFont().getWidth("WWWWWWWWWWW"), -1);
        gobutton.setActionCommand("go");
        gobutton.addActionListener(this);

        note = new Button(" ");
        note.setName("GoNote");
        note.setHorizontalAlignment(Graphics.HCENTER);
        note.setActionCommand("go");
        note.addActionListener(this);
        
        cardsbutton = new Button();
        cardsbutton.setName("CardsButton");
        //cardsbutton.setIcon( new Icon("/cards_button.png") );
        cardsbutton.setToolTipText(resb.getProperty("game.button.cards"));
        cardsbutton.setActionCommand("cards");
        cardsbutton.addActionListener(this);
        
        missionbutton = new Button();
        missionbutton.setName("HintButton");
        //missionbutton.setIcon( new Icon("/mission_button.png") );
        missionbutton.setToolTipText(resb.getProperty("game.button.mission"));
        missionbutton.setActionCommand("mission");
        missionbutton.addActionListener(this);
        
        Panel gamecontrol = new Panel( new BorderLayout() );
        gamecontrol.setName("TransPanel");
        
        Option[] options = new Option[6];
        options[0] = new Option( String.valueOf( PicturePanel.VIEW_CONTINENTS ) , resb.getProperty("game.tabs.continents") );
        options[1] = new Option( String.valueOf( PicturePanel.VIEW_OWNERSHIP ) , resb.getProperty("game.tabs.ownership") );
        options[2] = new Option( String.valueOf( PicturePanel.VIEW_BORDER_THREAT ) , resb.getProperty("game.tabs.borderthreat") );
        options[3] = new Option( String.valueOf( PicturePanel.VIEW_CARD_OWNERSHIP ) , resb.getProperty("game.tabs.cardownership") );
        options[4] = new Option( String.valueOf( PicturePanel.VIEW_TROOP_STRENGTH ) , resb.getProperty("game.tabs.troopstrength") );
        options[5] = new Option( String.valueOf( PicturePanel.VIEW_CONNECTED_EMPIRE ) , resb.getProperty("game.tabs.connectedempire") );        

        
        mapViewControl = new ViewChooser(options);
        mapViewControl.addActionListener(this);
        mapViewControl.setActionCommand("mapViewChanged");
        
        gamecontrol.add(mapViewControl);
        
        closebutton = new Button();
        closebutton.setMnemonic( KeyEvent.KEY_END );
        closebutton.setActionCommand("close");
        closebutton.addActionListener(this);
        gamecontrol.add(closebutton,Graphics.LEFT);
        
        gamecontrol.add(menu,Graphics.RIGHT);
        

        
        
        
        Panel mainWindow = new Panel( new BorderLayout() );
        scroll = new ScrollPane(pp) {
            // a little hack as we set setClip to false
            @Override
            public void repaint() {
                Window w = getWindow();
                if (w!=null) {
                    w.repaint();
                }
            }
        };


        //sp.setMode( ScrollPane.MODE_FLOATING_SCROLLBARS );
        scroll.setClip(false);
        mainWindow.add( scroll );
        mainWindow.add(gamecontrol,Graphics.TOP);
        mainWindow.add( makeBottomPanel() ,Graphics.BOTTOM);

        Panel contentPane = new Panel( new BorderLayout() );
        contentPane.add( mainWindow );
        setContentPane(contentPane);

    }
    
    ScrollPane scroll;
    
    private Panel makeBottomPanel() {
        
        Panel bottom = new Panel(new BorderLayout());
        
        int g = XULLoader.adjustSizeToDensity(1);
        Panel gamepanel2 = new Panel( new GridBagLayout(3, g, g, g, g, g) );
        gamepanel2.setName("TransPanel");

        GridBagConstraints gc = new GridBagConstraints();
        gc.rowSpan = 2;

        gamepanel2.add( cardsbutton, gc );
        
        GridBagConstraints gc1 = new GridBagConstraints();
        gc1.rowSpan = 2;
        gc1.halign = "left";
        gc1.weightx = 1;
        
        gamepanel2.add( missionbutton, gc1 );

        GridBagConstraints gc2 = new GridBagConstraints();

        gamepanel2.add( note,gc2 );
        gamepanel2.add( gobutton,gc2 );
        
        bottom.add( new PlayersPanel(), Graphics.TOP );
        bottom.add(gamepanel2);
        
        return bottom;
    }
    
    class PlayersPanel extends Component {
        @Override
        protected String getDefaultName() {
            return "PlayersPanel";
        }
        @Override
        public void paintComponent(Graphics2D g) {
            int[] colors = myrisk.getPlayerColors();
            
            int w = XULLoader.adjustSizeToDensity(20);
            
            int x=0;
            for (int c=0; c < colors.length ; c++) {
                    g.setColor( PicturePanel.colorWithAlpha(colors[c],100) );
                    int ww = c==0?width-(w*(colors.length-1)):w;
                    g.fillRect( x , 0 , ww , height);
                    x = x + ww;
            }

        }
        @Override
        protected void workoutMinimumSize() {
            width = 10;
            height = XULLoader.adjustSizeToDensity(2);
        }
    }
    
    boolean localGame;
    /**
     * @see net.yura.domination.ui.flashgui.GameFrame#setup(boolean) 
     */
    public void startGame(boolean localGame) {
        this.localGame = localGame;

        String mapFile = myrisk.getGame().getMapFile();
        logger.log(Level.INFO, "Starting new game: {0}", mapFile);

        closebutton.setText( getLeaveCloseText(localGame) );
        
        // ============================================ setup UI

        boolean retry=false;
        boolean error = pp != scroll.getView();
        
        if (!error) Midlet.openURL("nativeNoResult://net.yura.android.LoadingDialog?message=" + Url.encode( resb.getProperty("mainmenu.loading") ));

        try {
            pp.load();

            // just in case last time we showed a error
            if (error) {
                scroll.removeAll();
                scroll.add(pp);
                scroll.revalidate();
            }
        }
        catch (Throwable ex) { // ALL errors come here
            System.gc();
            String text = ((ex instanceof OutOfMemoryError)?"Not enough memory to load map: ":"Error loading map: ")+mapFile +" "+ex+(ex.getCause()!=null?" "+ex.getCause():"")+(error?" TWO ERRORS!!":"");
            
            TextArea ta = new TextArea(text);
            ta.setLineWrap(true);
            ta.setFocusable(false);

            scroll.removeAll();
            scroll.add(ta);
            scroll.revalidate();

            // this must have been a badly downloaded map, we must remove it
            if (!error && ex instanceof PicturePanel.CountryNotFoundException) {
                // we should del the map file so that we can re-download it
                File file = new File( MiniUtil.getSaveMapDir(), mapFile);
                if (file.exists()) {
                    System.out.println("deleting file: "+file+" date: "+new Date(file.lastModified()) );
                    file.delete();
                    RiskUtil.streamOpener.getMap(mapFile,myrisk,null);
                    retry=true;
                }
            }

            // if there has been NO retry and it is NOT a OutOfMemoryError
            logger.log( (retry || ex instanceof OutOfMemoryError) ?Level.INFO:Level.WARNING , text, ex);
        }
        finally {
            if (!retry) Midlet.openURL("nativeNoResult://net.yura.android.LoadingDialog?command=hide");
        }
        
        mapViewControl.resetMapView();
        
        // ============================================ show

        // disable all buttons at the start of the game
        Button[] buttons = new Button[] {savebutton,AutoEndGo,AutoDefend,cardsbutton,undobutton,gobutton};
        for (int c=0;c<buttons.length;c++) {
            buttons[c].setFocusable(false);
        }
        
        setVisible(true);
    }
    
    public void actionPerformed(String actionCommand) {
        if ("go".equals(actionCommand)) {
            goOn();
        }
        else if ("mapViewChanged".equals(actionCommand)) {
            pp.repaintCountries( getMapView() );
            pp.repaint();
        }
        else if ("menu".equals(actionCommand)) {
            	if (myrisk.getGame().getCurrentPlayer()!=null) {
			AutoEndGo.setSelected( myrisk.getAutoEndGo() );
			AutoDefend.setSelected( myrisk.getAutoDefend() );
		}
                while (menu.getItemCount() > gameOptionSize) {
                    menu.remove( (Component)menu.getComponents().lastElement() );
                }
                controller.addExtraButtons(menu);
        }
        else if ("save".equals(actionCommand)) {
            
            final TextField saveText = new TextField();
            saveText.setText( MiniUtil.getSaveGameName(myrisk.getGame()) );
            
            OptionPane.showOptionDialog(new ActionListener() {
                public void actionPerformed(String actionCommand) {
                    if ("ok".equals(actionCommand)) {
                        String fileName = saveText.getText();
                        fileName = RiskUtil.replaceAll(RiskUtil.replaceAll(fileName, "/", "-"),"\\","-");
                        String name = MiniUtil.getSaveGameDirURL() + fileName + SAVE_EXTENSION;
                        go("savegame " + name );
                    }
                }
            }, saveText, resb.getProperty("game.menu.save") , OptionPane.OK_CANCEL_OPTION, OptionPane.QUESTION_MESSAGE, null, null, null);

        }
        else if ("graph".equals(actionCommand)) {
            
            Midlet.openURL("nativeNoResult://net.yura.domination.android.StatsActivity");

        }
        else if ("undo".equals(actionCommand)) {
            pp.setC1(PicturePanel.NO_COUNTRY);
            pp.setC2(PicturePanel.NO_COUNTRY);
            go("undo");
        }
        else if ("autoendgo".equals(actionCommand)) {
            go("autoendgo "+(AutoEndGo.isSelected()?"on":"off"));
        }
        else if ("autodefend".equals(actionCommand)) {
            go("autodefend "+(AutoDefend.isSelected()?"on":"off"));
        }
        else if ("help".equals(actionCommand)) {
            MiniUtil.openHelp();
        }
        else if ("close".equals(actionCommand)) {

            showClosePrompt(myrisk);

        }
        else if ("mission".equals(actionCommand)) {
            
            String missionTitle = resb.getProperty("core.showmission.mission");
            String mission=myrisk.getCurrentMission();

            //String html = "<html><p>" + status + "</p><p><b>" +missionTitle + "</b><br/>"+ mission + "</p></html>";
            
            Element html = new Element("html",
                    new Element("p",
                            status
                    ),
                    myrisk.showHumanPlayerThereInfo()? 
                        new Element("p",
                                new Element("b",
                                        missionTitle
                                ),
                                new Element("br"),
                                mission
                        ):
                        new Element("p",
                            resb.getString("game.pleasewaitnetwork") // "game.pleasewait"
                        )
            );

            OptionPane.showMessageDialog(null, toString(html) , resb.getProperty("swing.menu.help"), OptionPane.INFORMATION_MESSAGE);
        }
        else if ("cards".equals(actionCommand)) {
            
            CardsDialog cardsDialog = new CardsDialog( myrisk, pp);
            cardsDialog.setup( (gameState==RiskGame.STATE_TRADE_CARDS) );
            cardsDialog.setVisible(true);
        }
        else {
            throw new IllegalArgumentException("unknown command "+actionCommand);
        }
    }

    static String toString(Element element) {
        try {
            Document doc = new Document();
            doc.setEncoding("UTF-8");
            
            doc.addChild(element); // adding root
            
            StringWriter writer = new StringWriter();
            
            KXmlSerializer makeXml = new KXmlSerializer();
            makeXml.setOutput(writer);
            
            doc.write(makeXml);

            makeXml.flush();

            String text = writer.toString();
            return text.substring( text.indexOf("?>")+2 );
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public static void showClosePrompt(final Risk myrisk) {
            OptionPane.showOptionDialog(new ActionListener() {
                public void actionPerformed(String actionCommand) {
                    if ("ok".equals(actionCommand)) {
                        myrisk.parser("closegame");
                    }
                }
            }, resb.getProperty("game.areyousurequit"), getLeaveCloseText( myrisk.getLocalGame() ) , OptionPane.OK_CANCEL_OPTION, OptionPane.QUESTION_MESSAGE, null, null, null);
    }
    
    static String getLeaveCloseText(boolean locagame) {
        return resb.getProperty( locagame ? "game.menu.close" : "game.menu.leave");
    }
    
    void setGameStatus(String state) {
        status = state;
    }

    /**
     * @see net.yura.domination.ui.flashgui.GameFrame#needInput(int)
     */
    public void needInput(int s) {
            gameState=s;
            String goButtonText=null;
            String noteText=null;
            boolean mustTrade=false;
            switch (gameState) {
                    case RiskGame.STATE_TRADE_CARDS: {
                            // after wiping out someone if you go into trade mode
                            pp.setC1(255);
                            pp.setC2(255);
                            noteText = getArmiesLeftText();
                            if (myrisk.getGame().canEndTrade()) {
                                goButtonText = resb.getProperty("game.button.go.endtrade");
                            }
                            else {
                                mustTrade = true;
                            }
                            break;
                    }
                    case RiskGame.STATE_PLACE_ARMIES: {
                            if ( !myrisk.getGame().NoEmptyCountries() ) {
                                    goButtonText = resb.getProperty("game.button.go.autoplace");
                            }
                            noteText = getArmiesLeftText();
                            break;
                    }
                    case RiskGame.STATE_ATTACKING: {
                            pp.setC1(255);
                            pp.setC2(255);
                            noteText = resb.getProperty("game.note.selectattacker");
                            goButtonText = resb.getProperty("game.button.go.endattack");
                            break;
                    }
                    case RiskGame.STATE_FORTIFYING: {
                            noteText = resb.getProperty("game.note.selectsource");
                            goButtonText = resb.getProperty("game.button.go.nomove");
                            break;
                    }
                    case RiskGame.STATE_END_TURN: {
                            goButtonText = resb.getProperty("game.button.go.endgo");
                            break;
                    }
                    case RiskGame.STATE_GAME_OVER: {
                            if (myrisk.getGame().canContinue()) {
                                goButtonText = resb.getProperty("game.button.go.continue");
                            }
                            else {
                                if (localGame) {
                                        goButtonText = resb.getProperty("game.button.go.closegame");
                                }
                                else {
                                        // TODO, not sure if this is needed?
                                        goButtonText = resb.getProperty("game.button.go.leavegame");
                                }
                            }
                            break;
                    }
                    case RiskGame.STATE_SELECT_CAPITAL: {
                            noteText = resb.getProperty("core.help.selectcapital");
                            goButtonText = null;
                            break;
                    }
                    // for gameState 4 look in FlashRiskAdapter.java
                    // for gameState 10 look in FlashRiskAdapter.java
                    default: break;
            }

            setGoButtonText(goButtonText);
            
            note.setText(noteText==null?" ":noteText);
            
            cardsbutton.setName(mustTrade?"MustTradeButton":"CardsButton");

            if (gameState!=RiskGame.STATE_DEFEND_YOURSELF) {
                    cardsbutton.setFocusable(true);

                    if (localGame) {
                        undobutton.setFocusable(true);
                        savebutton.setFocusable(true);
                    }

                    AutoEndGo.setFocusable(true);
                    //AutoEndGo.setBackground( Color.white );
                    AutoEndGo.setSelected( myrisk.getAutoEndGo() );

                    AutoDefend.setFocusable(true);
                    //AutoDefend.setBackground( Color.white );
                    AutoDefend.setSelected( myrisk.getAutoDefend() );
            }
            
            repaint(); // SwingGUI has this here, if here then not needed in set status
            
            if (isFocused()) {
                toast(status);
            }
    }
    
    private void setGoButtonText(String goButtonText) {
        if (gobutton!=null) {
            if (goButtonText!=null) {
                note.setFocusable(true);
                gobutton.setFocusable(true);
                gobutton.setText(goButtonText);
            }
            else {
                note.setFocusable(false);
                gobutton.setFocusable(false);
                gobutton.setText(" ");
            }
        }
    }
    
   /**
    * the armiesLeft method call from the core is not really needed as it is not
    * a event and the same data can be got by using these getters
    * @see MiniFlashRiskAdapter#armiesLeft(int, boolean)
    */
    public String getArmiesLeftText() {
            int l = myrisk.getGame().getCurrentPlayer().getExtraArmies();
            return RiskUtil.replaceAll( resb.getString("game.note.armiesleft"),"{0}", String.valueOf(l));
    }

    private void goOn() {
            if (gameState==RiskGame.STATE_TRADE_CARDS) {
                    go("endtrade");
            }
            else if (gameState==RiskGame.STATE_PLACE_ARMIES) {
                    go("autoplace");
            }
            else if (gameState==RiskGame.STATE_ATTACKING) {
                    pp.setC1(255);
                    go("endattack");
            }
            else if (gameState==RiskGame.STATE_FORTIFYING) {
                    pp.setC1(255);
                    go("nomove");
            }
            else if (gameState==RiskGame.STATE_END_TURN) {
                    go("endgo");
            }
            else if (gameState==RiskGame.STATE_GAME_OVER) {
                    if (myrisk.getGame().canContinue()) {
                        go("continue");
                    }
                    else {
                        go("closegame");
                    }
            }
            else if (gameState == RiskGame.STATE_SELECT_CAPITAL) {
                    int c1Id = pp.getC1();
                    pp.setC1(255);
                    go("capital " + c1Id);
            }
    }//private void goOn()

    private void go(String input) {

        pp.setHighLight(255);

        if (gameState!=RiskGame.STATE_PLACE_ARMIES || !myrisk.getGame().getSetup() ) { noInput(); }

        myrisk.parser(input);

    }

    public void noInput() {

            cardsbutton.setFocusable(false);
            undobutton.setFocusable(false);
            savebutton.setFocusable(false);
            AutoEndGo.setFocusable(false);
            AutoDefend.setFocusable(false);

            setGoButtonText(null);
            
            note.setText( resb.getString("game.pleasewait") );

            gameState=0;

            repaint();
    }

    public void mapClick(int[] countries) {

        if (gameState == RiskGame.STATE_PLACE_ARMIES) {
            if (countries.length==1) {
                //if ( e.getModifiers() == java.awt.event.InputEvent.BUTTON1_MASK ) {
                    go( "placearmies " + countries[0] + " 1" );
                //}
                //else {
                // TODO: make a method for adding 10 armies at a time
                //    go( "placearmies " + countries[0] + " 10" );
                //}
            }
        }
        else if (gameState == RiskGame.STATE_ATTACKING) {

            if (countries.length==0) {
                note.setText( resb.getProperty("game.note.selectattacker") );
            }
            else if (countries.length == 1) {
                note.setText( resb.getProperty("game.note.selectdefender") );
            }
            else {
                go("attack " + countries[0] + " " + countries[1]);
                note.setText(" "); // HACK: go sets the note to "please wait" so now we want to clear it
            }

        }
        else if (gameState == RiskGame.STATE_FORTIFYING) {
            if (countries.length==0) {
                note.setText( resb.getProperty("game.note.selectsource") );
            }
            else if (countries.length==1) {
                note.setText( resb.getProperty("game.note.selectdestination") );
            }
            else {
                note.setText(" ");

                MoveDialog move = new MoveDialog(myrisk) {
                    @Override
                    public void setVisible(boolean b) { // catch closing of the dialog
                        super.setVisible(b);
                        if (!b) {
                            // clean up
                            pp.setC1(255);
                            pp.setC2(255);
                            note.setText( resb.getProperty("game.note.selectsource") );
                        }
                    }
                };

                Image c1img = pp.getCountryImage(countries[0]);
                Image c2img = pp.getCountryImage(countries[1]);

                move.setupMove(1,countries[0] , countries[1],c1img,c2img, true);
                move.setVisible(true);

            }
        }
        else if (gameState == RiskGame.STATE_SELECT_CAPITAL) {
            note.setText( resb.getProperty("game.note.happyok") );
            setGoButtonText( resb.getProperty("game.button.go.ok") );
        }

    }

    public int getMapView() {
        return Integer.parseInt( mapViewControl.getSelectedItem().getKey() );
    }
    
    public void mapRedrawRepaint(boolean redrawNeeded, boolean repaintNeeded) {
        if (pp!=null) {
            if(redrawNeeded) {
                pp.repaintCountries( getMapView() );
            }
            if (repaintNeeded) {
                pp.repaint();
            }
        }
    }

    static void toast(String message) {
        Midlet.openURL("nativeNoResult://net.yura.android.ToastActivity?message="+Url.encode(message)+"&duration=SHORT");
    }

}
