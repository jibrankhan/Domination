package net.yura.domination.engine.translation;

import java.util.ResourceBundle;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.io.IOException;
import net.yura.domination.engine.RiskUtil;

/**
 * Translation services for maps:
 * Countries and continents
 *
 * @author Christian Weiske <cweiske@cweiske.de>
 */
public class MapTranslator
{
	private static ResourceBundle MapResb = null;
	private static ResourceBundle CardsResb = null;


	/**
	 * sets the currently used map
	 * Tries to find a matching properties file + resource bundle
	 *
	 * @param strFile	The map file absolute path
	 */
	public static void setMap(String strFile) {

		//remove the extension
		String strName = strFile.substring( 0, strFile.lastIndexOf( '.'));

		//now get the locale and try to the strName + "_" + 2-letter-code
		strFile = strName + "_" + TranslationBundle.getBundle().getLocale().getLanguage() + ".properties";

		//file exists, use it!
		try {
				MapResb = new PropertyResourceBundle( RiskUtil.openMapStream(strFile) );

		}
		catch( Exception ioe ) {

			try {
				MapResb = ResourceBundle.getBundle( "net.yura.domination.engine.translation.DefaultMaps", TranslationBundle.getBundle().getLocale());
			} catch( MissingResourceException e) {
				//ok, we don't have one
				MapResb = null;
			}

		}

	}//public static void setMap(String strFile)



	/**
	 * returns the translation for the given string if any
	 * If there is no translation, the string is simply returned
	 */
	public static String getTranslatedMapName(String strOriginal)
	{
		if (MapResb == null) {
			return strOriginal;
		}

		String strReturn;
		try {
			strReturn = MapResb.getString( strOriginal);
		} catch(MissingResourceException e) {
			//no translation for the string
			strReturn = strOriginal;
		}
		return strReturn;
	}//public static String getTranslation(String strOriginal)




	/**
	 * sets the currently used map
	 * Tries to find a matching properties file + resource bundle
	 *
	 * @param strFile	The map file absolute path
	 */
	public static void setCards(String INstrFile) {

		//remove the extension
		String strName = INstrFile.substring( 0, INstrFile.lastIndexOf( '.'));

		//now get the locale and try to the strName + "_" + 2-letter-code
		String strFile = strName + "_" + TranslationBundle.getBundle().getLocale().getLanguage() + ".properties";

		//file exists, use it!
		try {

			CardsResb = new PropertyResourceBundle( RiskUtil.openMapStream(strFile) );


		} catch( Exception ioe ) {

		    if ( INstrFile.equals("risk.cards") ) { // only use this with the default cards file
			//load default cards translation bundle
			try {
				CardsResb = ResourceBundle.getBundle( "net.yura.domination.engine.translation.DefaultCards", TranslationBundle.getBundle().getLocale());
			} catch( MissingResourceException e) {

				//ok, we don't have one
				CardsResb = null;
			}
		    }

		}

	}



	/**
	 * returns the translation for the given string if any
	 * If there is no translation, the string is simply returned
	 */
	public static String getTranslatedMissionName(String strOriginal)
	{
		if (CardsResb == null) {
			return null;
		}

		String strReturn;
		try {
			strReturn = CardsResb.getString( strOriginal);
		} catch(MissingResourceException e) {
			//no translation for the string
			strReturn = null;
		}
		return strReturn;
	}


}
