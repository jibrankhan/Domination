package net.yura.swingme.core;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Yura Mamyrin
 */
public class J2SELogger extends net.yura.mobile.logging.Logger {

    static final Logger logger = Logger.getLogger(J2SELogger.class.getName());
    
    protected synchronized void log(String message, int level) {
        logger.log( getLevel(level), message);
    }

    protected synchronized void log(Throwable throwable, int level) {
        logger.log( getLevel(level), null, throwable);
    }
    
    private static Level getLevel(int level) {
        switch (level) {
            case net.yura.mobile.logging.Logger.DEBUG: return Level.FINE;
            case net.yura.mobile.logging.Logger.INFO: return Level.INFO;
            case net.yura.mobile.logging.Logger.WARN: return Level.WARNING;
            case net.yura.mobile.logging.Logger.ERROR: return Level.SEVERE;
            // TODO can not log as this level, it just means OFF or if we can we should make a new Level for it like ASSERT
            case net.yura.mobile.logging.Logger.FATAL: return Level.SEVERE;
            default: throw new IllegalArgumentException("level: "+level);
        }
    }
    
}
