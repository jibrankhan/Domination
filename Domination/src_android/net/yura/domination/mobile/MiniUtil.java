package net.yura.domination.mobile;

import java.io.File;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import net.yura.domination.engine.Risk;
import net.yura.domination.engine.RiskUtil;
import net.yura.domination.engine.core.RiskGame;
import net.yura.domination.engine.translation.TranslationBundle;
import net.yura.domination.mobile.flashgui.DominationMain;
import net.yura.grasshopper.BugUIInfo;
import net.yura.mobile.gui.ActionListener;
import net.yura.mobile.gui.KeyEvent;
import net.yura.mobile.gui.components.Button;
import net.yura.mobile.gui.components.OptionPane;
import net.yura.mobile.io.FileUtil;

public class MiniUtil {

    public static void showAbout() {

        ResourceBundle resb = TranslationBundle.getBundle();
        
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String copyright = resb.getString("about.copyright").replaceAll("\\{0\\}", String.valueOf(year) );
        
        String author = resb.getString("about.author") + " Yura Mamyrin (yura@yura.net)";
        
        String text = "<html>" +
                "<h3>yura.net "+RiskUtil.GAME_NAME+"</h3>"+
                "<p>"+DominationMain.product+" "+resb.getString("about.version")+" "+DominationMain.version +"</p>"+
                "<p>"+"Game Engine: "+" "+Risk.RISK_VERSION +"</p>"+
                "<p>"+author+"</p>"+
                "<p>"+copyright+"</p>"+
               // "<p>"+ resb.getString("about.comments") +"</p>"+
                "<p>DPI: "+System.getProperty("display.dpi")+" Size: "+System.getProperty("display.size")+"</p>"+
                "<p>Locale: "+Locale.getDefault()+" use: "+resb.getLocale()+"</p>"+
                "<p>"+BugUIInfo.getLookAndFeel()+"</p>"+
                "</html>";

        Button credits = new Button(resb.getString("about.tab.credits"));
        credits.setActionCommand("credits");
        Button license = new Button(resb.getString("about.tab.license"));
        license.setActionCommand("license");
        Button changelog = new Button(resb.getString("about.tab.changelog"));
        changelog.setActionCommand("changelog");
        Button ok = new Button(resb.getString("about.okbutton"));
        ok.setMnemonic( KeyEvent.KEY_END );
        
        OptionPane.showOptionDialog(new ActionListener() {
            public void actionPerformed(String actionCommand) {
                try {
                    if ("license".equals(actionCommand)) {
                        RiskUtil.openDocs("gpl.txt");
                    }
                    else if ("changelog".equals(actionCommand)) {
                        RiskUtil.openDocs("ChangeLog.txt");
                    }
                    else if ("credits".equals(actionCommand)) {
                        RiskUtil.openDocs("help/game_credits.htm");
                    }
                }
                catch(Exception e) {
                    OptionPane.showMessageDialog(null,"Unable to open info: "+e.getMessage(),"Error", OptionPane.ERROR_MESSAGE);
                }
            }
        } ,text,resb.getString("about.title"), 0, OptionPane.INFORMATION_MESSAGE,
        null, new Button[] {credits,license,changelog,ok} , ok);
        
    }

    public static List getFileList(String string) {
        List result = new java.util.Vector();
        
        Enumeration en = FileUtil.getDirectoryFiles(RiskMiniIO.mapsdir);
        while (en.hasMoreElements()) {
            String file = (String)en.nextElement();
            if (file.endsWith("."+string)) {
                result.add( file );
            }
        }


        String[] list = getSaveMapDir().list();
        for (int c=0;c<list.length;c++) {
            String file = list[c];
            if (file.endsWith("."+string) && !result.contains(file)) {
                result.add( file );
            }
        }        
        
        return result;
    }
    
    
    private static File mapsDir;
    public static File getSaveMapDir() {

        if (mapsDir!=null) {
            return mapsDir;
        }

        File userHome = new File( System.getProperty("user.home") );
        File userMaps = new File(userHome, "maps");
        if (!userMaps.isDirectory() && !userMaps.mkdirs()) { // if it does not exist and i cant make it
            throw new RuntimeException("can not create dir "+userMaps);
        }

        mapsDir = userMaps;
        return userMaps;
    }
    
    private static File savesDir;
    public static File getSaveGameDir() {

        if (savesDir!=null) {
            return savesDir;
        }

        File userHome = new File( System.getProperty("user.home") );
        File userMaps = new File(userHome, "saves");
        if (!userMaps.isDirectory() && !userMaps.mkdirs()) { // if it does not exist and i cant make it
            throw new RuntimeException("can not create dir "+userMaps);
        }

        savesDir = userMaps;
        return userMaps;
    }

    public static String getSaveGameDirURL() {
        return FileUtil.ROOT_PREX + getSaveGameDir().toString() +"/";
    }

    public static String getSaveGameName(RiskGame game) {
        String file = game.getMapFile();
        if (file.endsWith(".map")) {
            file = file.substring(0, file.length() - 4);
        }
        return file;
    }
    
    public static void openHelp() {
        try {
            RiskUtil.openDocs("help/index.htm");
        }
        catch(Exception e) {
            OptionPane.showMessageDialog(null,"Unable to open manual: "+e.getMessage(),"Error", OptionPane.ERROR_MESSAGE);
        }
    }
    
}
