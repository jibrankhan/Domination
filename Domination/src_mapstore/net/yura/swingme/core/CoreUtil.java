package net.yura.swingme.core;

import java.util.ResourceBundle;
import net.yura.mobile.util.Properties;

/**
 * @author Yura Mamyrin
 */
public class CoreUtil {

    public static Properties wrap(final ResourceBundle res) {
        return new Properties() {
            public String getProperty(String key) {
                try {
                    return res.getString(key);
                }
                catch (Exception ex) {
                    return null;
                }
            }
        };
    }

    public static void setupLogging() {

        net.yura.mobile.logging.Logger.setLogger( new net.yura.swingme.core.J2SELogger() );

    }

}
