package net.yura.domination.engine.translation;

import java.util.Locale;
import java.util.ResourceBundle;
import net.yura.domination.engine.RiskUtil;

/**
 * just a small class keeping the only resource
 * bundle needed in memory
 *
 * @author Christian Weiske <cweiske@cweiske.de>
 */
public class TranslationBundle {

	private static ResourceBundle resBundle = null;
	private static String strLanguage = null;

	/**
	 * returns the resource bundle
	 *
	 * It should be accessed statically as
	 * this causes only one instance of the resource
	 * bundle to load, which can be reused in all
	 * parts of the program
	 */
	static public ResourceBundle getBundle() {

		if (resBundle == null) {
			loadBundle();
		}
		return resBundle;
	}

	/**
	 * loads the translation bundle
	 */
	static private void loadBundle() {

		final Locale loc;

		//load the default or a forced locale
		if (strLanguage == null) {
			loc = Locale.getDefault();
		}
		else {
			loc = new Locale(strLanguage);
		}

		resBundle = RiskUtil.getResourceBundle(TranslationBundle.class,"Risk",loc);

		// this will work MOST of the time, but does not in lobby
		// for some reason it stops the classes from loading, and does not find the files
		//resBundle = ResourceBundle.getBundle("net.yura.domination.engine.translation.Risk", loc );


	}






	/**
	 * sets the language to use
	 *
	 * should be called from a main method after parsing the
	 * command line
	 */
	static private void setLanguage(String strL) {

		strLanguage = strL;

		// need to reload it again!
		loadBundle();
	}

	/**
	 * parses the command line arguments for
	 * language settings
	 *
	 * you need to call the program with a parameter like
	 * "--lang=en" or "--lang=de" to set the english or german
	 * language
	 */
	static public void parseArgs(String[] args) {

		for (int nA = 0; nA < args.length; nA++ ) {
			if (args[nA].length() > 7 && args[nA].substring(0,7).equals( "--lang=")) {
				//set the language to the given string
				TranslationBundle.setLanguage( args[nA].substring(7) );
			}
		}
	}

}
