// Yura Mamyrin

package net.yura.domination.ui.swinggui;

import javax.swing.JApplet;
import net.yura.domination.engine.Risk;
import net.yura.domination.engine.RiskUIUtil;
import net.yura.domination.engine.translation.TranslationBundle;

/**
 * @author Yura Mamyrin
 */

public class SwingGUIApplet extends JApplet {

	public void init() {

		//Risk.applet=this;
/*
		// set up system Look&Feel
		try {

			String os = System.getProperty("os.name");
			String jv = System.getProperty("java.version");

			if ( jv.startsWith("1.4.2") && os != null && os.startsWith("Linux")) {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
			}
			else {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}
		}
		catch (Exception e) {
			RiskUtil.printStackTrace(e);
		}
*/

                RiskUIUtil.setupMapsDir(this);

		String lang = getParameter("lang");
		if (lang !=null) {
			TranslationBundle.parseArgs( new String[] {"--lang="+lang } );
		}

                SwingGUIPanel sp = new SwingGUIPanel( new Risk() );
                
        	setContentPane( sp );
                
                setJMenuBar( sp.getJMenuBar() );
	}

}
