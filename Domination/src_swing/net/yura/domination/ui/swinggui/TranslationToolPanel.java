package net.yura.domination.ui.swinggui;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import net.yura.domination.engine.RiskUIUtil;
import net.yura.translation.MessageTool;
import net.yura.translation.Mtcomm;
import net.yura.translation.plugins.PropertiesComm;

public class TranslationToolPanel extends MessageTool implements SwingGUITab {

    private Mtcomm mycomm1;

    public TranslationToolPanel() {

        // this is the name that is seen at the top of the tab
        setName("Translation");
    }

    public void load() {

        if (!RiskUIUtil.checkForNoSandbox()) {
                RiskUIUtil.showAppletWarning(null);
                return;
        }

        if (mycomm1==null) {
            mycomm1 = new PropertiesComm() {
                public void setupFilter(JFileChooser fc) {
                    super.setupFilter(fc);

                    FileFilter ff = new FileFilter() {
                        public boolean accept(File f) {
                                return (f.isDirectory() || ( f.getName().equals("Risk.properties") || f.getName().equals("DefaultMaps.properties") || f.getName().equals("DefaultCards.properties") ) );
                        }
                        public String getDescription() {
                                return "Game Translation Files (Risk.properties,DefaultMaps...,DefaultCards...)";
                        }
                    };

                    fc.addChoosableFileFilter( ff );

                    fc.addChoosableFileFilter( new FileFilter() {
                        public boolean accept(File f) {
                                return (f.isDirectory() || "game.ini".equals(f.getName()) || "settings.ini".equals(f.getName()) );
                        }
                        public String getDescription() {
                                return "Game Settings File (game.ini,settings.ini)";
                        }
                    } );

                    fc.setFileFilter( ff );

                }
            };
        }
        
        try {
            if (mycomm1.load()) {
                load(mycomm1);
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

