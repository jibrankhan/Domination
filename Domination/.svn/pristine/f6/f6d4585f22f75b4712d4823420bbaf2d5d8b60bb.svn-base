package net.yura.domination.mobile.flashgui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import net.yura.domination.engine.Risk;
import net.yura.domination.engine.RiskUtil;
import net.yura.domination.engine.translation.TranslationBundle;
import net.yura.domination.mapstore.MapChooser;
import net.yura.domination.mapstore.MapUpdateService;
import net.yura.domination.mobile.MiniUtil;
import net.yura.domination.mobile.RiskMiniIO;
import net.yura.grasshopper.SimpleBug;
import net.yura.mobile.gui.DesktopPane;
import net.yura.mobile.gui.Graphics2D;
import net.yura.mobile.gui.Icon;
import net.yura.mobile.gui.Midlet;
import net.yura.mobile.gui.components.Component;
import net.yura.mobile.gui.components.OptionPane;
import net.yura.mobile.gui.plaf.Style;
import net.yura.mobile.gui.plaf.SynthLookAndFeel;
import net.yura.mobile.gui.plaf.nimbus.NimbusLookAndFeel;
import net.yura.swingme.core.CoreUtil;

public class DominationMain extends Midlet {

    public static final String product = "MiniFlashGUI";
    public static final String version;
    static {
        String versionCode = System.getProperty("versionCode");
        version = versionCode!=null ? versionCode : "?me4se?";
    }
    
    public Risk risk;
    public MiniFlashRiskAdapter adapter;
    
    public DominationMain() {

        // IO depends on this, so we need to do this first
        RiskUtil.streamOpener = new RiskMiniIO();

        // get version from AndroidManifest.xml
        //String versionName = System.getProperty("versionName");
        //Risk.RISK_VERSION = versionName!=null ? versionName : "?me4se?";
        
        try {

            SimpleBug.initLogFile( RiskUtil.GAME_NAME , Risk.RISK_VERSION+" "+product+" "+version , TranslationBundle.getBundle().getLocale().toString() );

            CoreUtil.setupLogging();

            if ( "true".equals( System.getProperty("debug") ) ) {

                // MWMWMWMWMWMWMWMWMWMWMWM ONLY DEBUG MWMWMMWMWMWMWMWMWMWMWMWMWM
                
                Logger.getLogger("").addHandler( new Handler() {
                    boolean open;
                    @Override
                    public void publish(LogRecord record) {
                        if (record.getLevel().intValue() >= Level.WARNING.intValue()) {
                            if (!open) {
                                open = true;
                                try {
                                    OptionPane.showMessageDialog(null, record.getMessage()+" "+record.getThrown(), "WARN", OptionPane.WARNING_MESSAGE);
                                }
                                catch(Exception ex) { ex.printStackTrace(); }
                            }
                        }
                    }

                    @Override public void flush() { }
                    @Override public void close() { }
                } );

                // if we want to see DEBUG, default is INFO
                java.util.logging.Logger.getLogger("").setLevel(java.util.logging.Level.ALL);

                // so we do not need to wait for AI while testing
                net.yura.domination.engine.ai.AIPlayer.setWait(5);
                
                // MWMWMWMWMWMWMWMWMWMWM END ONLY DEBUG MWMWMMWMWMWMWMWMWMWMWMWM
            }

        }
        catch (Throwable th) {
            System.out.println("Grasshopper not loaded");
            th.printStackTrace();
        }

    }

/*
    @Override
    protected void destroyApp(boolean arg0) throws javax.microedition.midlet.MIDletStateChangeException {

        if (risk.getGame()!=null) {
            risk.parser("savegame auto.save");
        }
        risk.kill();
        try { risk.join(); } catch (InterruptedException e) { } // wait for game thread to die 
        
        super.destroyApp(arg0);
    }
*/

    @Override
    public void initialize(DesktopPane rootpane) {

        SynthLookAndFeel synth;
        
        try {
            synth = (SynthLookAndFeel)Class.forName("net.yura.android.plaf.AndroidLookAndFeel").newInstance();

            // small hack to center radiobutton icon
            Style radioButtonStyle = synth.getStyle("RadioButton");
            Icon radioButtonIcon = (Icon)radioButtonStyle.getProperty("icon", Style.ALL);
            if (radioButtonIcon!=null) {
                radioButtonStyle.addProperty( new CentreIcon(radioButtonIcon,radioButtonIcon.getIconWidth(),radioButtonIcon.getIconWidth()), "icon", Style.ALL);
            }
            
        }
        catch (Exception ex) {
            if (Midlet.getPlatform()==Midlet.PLATFORM_ANDROID) {
                net.yura.mobile.logging.Logger.warn(ex);
            }
            
            synth = new NimbusLookAndFeel();
        }
        
        try {
            synth.load( Midlet.getResourceAsStream("/dom_synth.xml") );
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        rootpane.setLookAndFeel( synth );

        MapChooser.loadThemeExtension(); // this has theme elements used inside AND outside of the MapChooser




        risk = new Risk();
        adapter = new MiniFlashRiskAdapter(risk);

        adapter.openMainMenu();


        
        new Thread() {
            @Override
            public void run() {
                MapUpdateService.getInstance().init( MiniUtil.getFileList("map"), MapChooser.MAP_PAGE );
            }
        }.start();

        
        //risk.parser("newgame");
        //risk.parser("newplayer ai hard blue bob");
        //risk.parser("newplayer ai hard red fred");
        //risk.parser("newplayer ai hard green greg");
        //risk.parser("startgame domination increasing");

        
        try {
            //File saves = new File( net.yura.android.AndroidMeApp.getIntance().getFilesDir() ,"saves");
            //File sdsaves = new File("/sdcard/Domination-saves");
            //copyFolder(saves, sdsaves);
            //copyFolder(sdsaves, saves);
            //System.out.println("files"+ Arrays.asList( saves.list() ) );
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    
    public static class CentreIcon extends Icon {
        Icon wrappedIcon;
        public CentreIcon(Icon icon,int w,int h) {
            wrappedIcon = icon;
            width = w;
            height = h;
        }
        @Override
        public void paintIcon(Component c, Graphics2D g, int x, int y) {
            // paint real icon in the middle of this icon
            wrappedIcon.paintIcon(c, g, x + (getIconWidth()-wrappedIcon.getIconWidth())/2, y + (getIconHeight()-wrappedIcon.getIconHeight())/2);
        }
    }

}
