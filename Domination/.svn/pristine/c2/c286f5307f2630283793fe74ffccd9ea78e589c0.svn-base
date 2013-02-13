// Yura Mamyrin, Group D

package net.yura.domination.engine.guishared;

import java.io.File;
import javax.swing.filechooser.FileFilter;
import net.yura.domination.engine.translation.TranslationBundle;

/**
 * <p> Risk File Filter </p>
 * @author Yura Mamyrin
 */

public class RiskFileFilter extends FileFilter {

	public final static String RISK_MAP_FILES		="map";
	public final static String RISK_CARDS_FILES		="cards";
	public final static String RISK_SAVE_FILES		="save";
	public final static String RISK_SCRIPT_FILES		="risk";
	public final static String RISK_LOG_FILES		="log";
	//public final static String RISK_PROPERTIES_FILES	="properties";

	private String extension = null;

	public RiskFileFilter(String ext) {
		extension = ext;
	}

	public boolean accept(File f) {

		if (f.isDirectory()) {
			return true;
		}

		String ext = getExtension(f);

		if (ext != null) {
			if (ext.equals( extension )) {
				return true;
			}
			else {
				return false;
			}
		}

		return false;

	}

	/**
	 * returns the description for a file extension
	 */
	public String getDescription()
	{
		java.util.ResourceBundle resb = TranslationBundle.getBundle();

		String name;

		if ( extension.equals(RISK_MAP_FILES) ) {

			name = resb.getString("riskfilefilter.map");
		}
		else if ( extension.equals(RISK_CARDS_FILES) ) {

			name = resb.getString("riskfilefilter.cards");
		}
		else if ( extension.equals(RISK_SAVE_FILES) ) {

			name = resb.getString("riskfilefilter.save");
		}
		else if ( extension.equals(RISK_SCRIPT_FILES) ) {

			name = resb.getString("riskfilefilter.script");
		}
		else if ( extension.equals(RISK_LOG_FILES) ) {

			name = resb.getString("riskfilefilter.log") ;
		}
		else {
			name = resb.getString("riskfilefilter.files");
		}

		return name + " (*."+extension+")";

	}//public String getDescription()

	public String getExtension() {

		return extension;
	}

	/**
	* Get the extension of a file.
	*/
	public static String getExtension(File f) {
		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');

		if (i > 0 &&  i < s.length() - 1) {
			ext = s.substring(i+1).toLowerCase();
		}
		return ext;
	}


}
